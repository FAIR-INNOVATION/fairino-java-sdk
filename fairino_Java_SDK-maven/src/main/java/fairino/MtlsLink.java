package fairino;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPrivateCrtKeySpec;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.crypto.util.PrivateKeyInfoFactory;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.tls.AlertDescription;
import org.bouncycastle.tls.Certificate;
import org.bouncycastle.tls.CertificateRequest;
import org.bouncycastle.tls.CipherSuite;
import org.bouncycastle.tls.DTLSClientProtocol;
import org.bouncycastle.tls.DTLSTransport;
import org.bouncycastle.tls.DatagramTransport;
import org.bouncycastle.tls.DefaultTlsClient;
import org.bouncycastle.tls.ProtocolVersion;
import org.bouncycastle.tls.SignatureAlgorithm;
import org.bouncycastle.tls.SignatureAndHashAlgorithm;
import org.bouncycastle.tls.TlsAuthentication;
import org.bouncycastle.tls.TlsClientProtocol;
import org.bouncycastle.tls.TlsContext;
import org.bouncycastle.tls.TlsCredentials;
import org.bouncycastle.tls.crypto.TlsCryptoParameters;
import org.bouncycastle.tls.TlsFatalAlert;
import org.bouncycastle.tls.TlsServerCertificate;
import org.bouncycastle.tls.crypto.TlsCertificate;
import org.bouncycastle.tls.crypto.TlsCrypto;
import org.bouncycastle.tls.crypto.impl.bc.BcDefaultTlsCredentialedSigner;
import org.bouncycastle.tls.crypto.impl.bc.BcTlsCertificate;
import org.bouncycastle.tls.crypto.impl.bc.BcTlsCrypto;

/**
 * mTLS/DTLS 通道模块（与 C# MtlsLink.cs、机器人端 mtls_link.c 配套）。
 *   TCP 8080 : SSLSocket 双向认证（客户端证书 + 私有 CA 签名校验，证书不绑 IP/主机名）
 *   UDP 20007: DTLS 1.2（BouncyCastle，双向证书认证，仅校验 CA 签名）
 * 开关 = 证书文件：运行目录 certs/ 下存在 client.crt / client.key / ca.crt 则启用；
 * 否则 enabled=false，SDK 全部走原有明文路径（与旧版本一致）。
 */
public class MtlsLink
{
    static
    {
        /* 注册 BouncyCastle 提供者：PEM 证书/私钥解析、DTLS 握手均依赖 BC */
        if (Security.getProvider("BC") == null)
        {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    /** 证书目录（相对工作目录，默认 certs/） */
    public final String certDir;

    /** 是否启用（certs 目录证书三件套齐全时自动为 true） */
    public final boolean enabled;

    /** 缺失的证书文件名列表（enabled=false 时有值，用于报错提示） */
    public final String missingCerts;

    private DTLSTransport dtlsTransport;

    public MtlsLink(String certDir)
    {
        if (certDir == null || certDir.isEmpty()) {
            /* 默认证书目录 = SDK jar 所在目录下的 certs/：
            * 与 jar 一起部署，不依赖程序工作目录，也不受
            * 编译输出目录 Clean 清理影响 */
            try {
                certDir = Paths.get(
                        MtlsLink.class.getProtectionDomain().getCodeSource().getLocation().toURI()
                ).getParent().resolve("certs").toString();
            } catch (Exception e) {
                certDir = "certs";
            }
        }
        this.certDir = certDir;
        StringBuilder missing = new StringBuilder();
        if (!new File(certDir, "client.crt").exists()) { missing.append("client.crt "); }
        if (!new File(certDir, "client.key").exists()) { missing.append("client.key "); }
        if (!new File(certDir, "ca.crt").exists())     { missing.append("ca.crt "); }
        this.missingCerts = missing.toString().trim();
        this.enabled = missingCerts.isEmpty();
        if (enabled)
        {
            printClientCertInfo();
        }
    }

    /** 打印当前 client 证书信息：序列号 + 到期时间 + 名称（CN） */
    private void printClientCertInfo()
    {
        try
        {
            X509Certificate cert = loadX509(new File(certDir, "client.crt"));
            long serial = cert.getSerialNumber().longValue();
            long daysLeft = (cert.getNotAfter().getTime() - System.currentTimeMillis()) / 86400000L;
            String cn = cert.getSubjectX500Principal().getName();
            System.out.println("[MtlsLink] client cert: CN=" + cn
                    + ", serial=" + serial
                    + ", expires=" + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cert.getNotAfter())
                    + " (" + daysLeft + " days left)");
        }
        catch (Exception ex)
        {
            System.out.println("[MtlsLink] client cert info read failed: " + ex.getMessage());
        }
    }

    /* ================= TCP 8080：SSLSocket 双向认证 ================= */

    /**
     * 对已连接的 TCP socket 做 mTLS 握手，返回可收发的 SSLSocket。
     * @param socket   已连到机器人 8080 的明文 socket（握手完成后其 streams 由 SSLSocket 接管）
     * @param targetIp 机器人 IP（仅用于日志；SSLSocket 默认不校验主机名，满足"证书不绑 IP"）
     */
    public SSLSocket wrapTcp(Socket socket, String targetIp) throws Exception
    {
        X509Certificate caCert = loadX509(new File(certDir, "ca.crt"));
        X509Certificate clientCert = loadX509(new File(certDir, "client.crt"));
        PrivateKey clientKey = loadPrivateKey(new File(certDir, "client.key"));

        // 客户端证书 + 私钥
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(null, null);
        ks.setKeyEntry("client", clientKey, new char[0], new X509Certificate[]{clientCert});
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, new char[0]);

        // 私有 CA 信任链校验：server 证书必须由本地 ca.crt 签名（等价 C# 根指纹比对）。
        // 不查 CRL/OCSP，不校验主机名/IP（证书不绑 IP，机器人换 IP SDK 零改动）。
        final X509Certificate ca = caCert;
        X509TrustManager trustManager = new X509TrustManager()
        {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
            {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws java.security.cert.CertificateException
            {
                if (chain == null || chain.length == 0)
                {
                    throw new java.security.cert.CertificateException("empty server certificate chain");
                }
                try
                {
                    PublicKey caKey = ca.getPublicKey();
                    for (X509Certificate c : chain)
                    {
                        c.checkValidity();
                        c.verify(caKey);
                    }
                }
                catch (Exception e)
                {
                    throw new java.security.cert.CertificateException(
                            "server certificate not signed by local CA: " + e.getMessage(), e);
                }
            }

            @Override
            public X509Certificate[] getAcceptedIssuers()
            {
                return new X509Certificate[]{ca};
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(kmf.getKeyManagers(), new TrustManager[]{trustManager}, new SecureRandom());

        SSLSocket sslSocket = (SSLSocket) sslContext.getSocketFactory().createSocket(
                socket, socket.getInetAddress().getHostAddress(), socket.getPort(), true);
        sslSocket.setUseClientMode(true);
        sslSocket.setEnabledProtocols(new String[]{"TLSv1.2"});

        /* 握手超时 2s：机器人端未开启加密时不会回 ServerHello，
         * 无超时会无限等待（卡在建联）；超时后抛异常由上层报错 */
        int oldTimeout = socket.getSoTimeout();
        socket.setSoTimeout(2000);
        try
        {
            sslSocket.startHandshake();
        }
        finally
        {
            socket.setSoTimeout(oldTimeout);
        }
        return sslSocket;
    }

    /* ================= UDP 20007：BouncyCastle DTLS 1.2 ================= */

    private static class UdpDatagramTransport implements DatagramTransport
    {
        private final DatagramSocket sock;

        UdpDatagramTransport(DatagramSocket s)
        {
            this.sock = s;
        }

        @Override
        public int getReceiveLimit()
        {
            return 1500;
        }

        @Override
        public int getSendLimit()
        {
            return 1500;
        }

        @Override
        public void send(byte[] buf, int off, int len) throws IOException
        {
            sock.send(new DatagramPacket(buf, off, len));
        }

        @Override
        public int receive(byte[] buf, int off, int len, int waitMillis) throws IOException
        {
            sock.setSoTimeout(waitMillis);
            DatagramPacket packet = new DatagramPacket(buf, off, len);
            try
            {
                sock.receive(packet);
                return packet.getLength();
            }
            catch (java.net.SocketTimeoutException e)
            {
                /* 超时是 DTLS 的正常等待：返回 -1 而不是抛异常，
                 * 否则空闲期每 2 秒抛一次异常，调试时刷屏 */
                return -1;
            }
            catch (java.net.PortUnreachableException e)
            {
                /* ICMP 不可达（对端拒绝/未监听）：转为 -1，由上层累计判定会话失效 */
                return -1;
            }
        }

        @Override
        public void close()
        {
            /* 注意：不关闭 socket——socket 归 UDPClient 所有，
             * 握手失败回退明文后还要继续用。BC 在握手失败时会调用本方法。 */
        }
    }

    private static class MtlsAuthentication implements TlsAuthentication
    {
        private final TlsContext ctx;
        private final String certDir;

        MtlsAuthentication(TlsContext context, String dir)
        {
            this.ctx = context;
            this.certDir = dir;
        }

        @Override
        public void notifyServerCertificate(TlsServerCertificate serverCertificate) throws IOException
        {
            TlsCertificate[] chain = serverCertificate.getCertificate().getCertificateList();
            if (chain == null || chain.length == 0)
            {
                throw new TlsFatalAlert(AlertDescription.bad_certificate);
            }
            try
            {
                BcTlsCertificate cert = (BcTlsCertificate) chain[0];
                java.security.cert.X509Certificate x509 = convertToJcaX509(cert);
                if (x509.getNotAfter().getTime() < System.currentTimeMillis())
                {
                    throw new TlsFatalAlert(AlertDescription.certificate_expired);
                }
                java.security.cert.X509Certificate ca = loadX509(new File(certDir, "ca.crt"));
                x509.verify(ca.getPublicKey());
            }
            catch (TlsFatalAlert e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new TlsFatalAlert(AlertDescription.bad_certificate, e);
            }
        }

        @Override
        public TlsCredentials getClientCredentials(CertificateRequest certificateRequest) throws IOException
        {
            try
            {
                java.security.cert.X509Certificate clientCert = loadX509(new File(certDir, "client.crt"));
                PrivateKey clientKey = loadPrivateKey(new File(certDir, "client.key"));
                TlsCrypto crypto = ctx.getCrypto();
                TlsCertificate[] chain = new TlsCertificate[]{
                        crypto.createCertificate(clientCert.getEncoded())};
                SignatureAndHashAlgorithm sigAlg = new SignatureAndHashAlgorithm(
                        org.bouncycastle.tls.HashAlgorithm.sha256, SignatureAlgorithm.rsa);
                return new BcDefaultTlsCredentialedSigner(
                        new TlsCryptoParameters(ctx),
                        (BcTlsCrypto) crypto,
                        toBcKeyParameter(clientKey),
                        new Certificate(chain),
                        sigAlg);
            }
            catch (IOException e)
            {
                throw e;
            }
            catch (Exception e)
            {
                throw new TlsFatalAlert(AlertDescription.internal_error, e);
            }
        }
    }

    private static class MtlsDtlsClient extends DefaultTlsClient
    {
        private final String certDir;

        MtlsDtlsClient(BcTlsCrypto crypto, String dir)
        {
            super(crypto);
            this.certDir = dir;
        }

        @Override
        public ProtocolVersion[] getProtocolVersions()
        {
            return new ProtocolVersion[]{ProtocolVersion.DTLSv12};
        }

        @Override
        public int[] getCipherSuites()
        {
            return new int[]{CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256};
        }

        @Override
        public TlsAuthentication getAuthentication() throws IOException
        {
            return new MtlsAuthentication(context, certDir);
        }
    }

    /** 对已 connect 到对端的 UDP socket 发起 DTLS 握手。 */
    public void startDtls(DatagramSocket udpSocket) throws IOException
    {
        BcTlsCrypto crypto = new BcTlsCrypto(new SecureRandom());
        DTLSClientProtocol protocol = new DTLSClientProtocol();
        dtlsTransport = protocol.connect(new MtlsDtlsClient(crypto, certDir),
                new UdpDatagramTransport(udpSocket));
    }

    /** DTLS 发送（线程安全）。 */
    public synchronized void dtlsSend(byte[] buf) throws IOException
    {
        if (dtlsTransport != null)
        {
            dtlsTransport.send(buf, 0, buf.length);
        }
    }

    /** DTLS 接收（仅接收线程调用）。返回实际长度；<=0 表示超时/失败。 */
    public int dtlsReceive(byte[] buf, int off, int len, int timeoutMs) throws IOException
    {
        if (dtlsTransport == null)
        {
            return -1;
        }
        return dtlsTransport.receive(buf, off, len, timeoutMs);
    }

    public boolean isDtlsActive()
    {
        return dtlsTransport != null;
    }

    public void dispose()
    {
        if (dtlsTransport != null)
        {
            try
            {
                dtlsTransport.close();
            }
            catch (Exception ignored)
            {
            }
            dtlsTransport = null;
        }
    }

    /* ================= 证书/私钥加载 ================= */

    private static java.security.cert.X509Certificate loadX509(File file) throws Exception
    {
        FileReader reader = new FileReader(file);
        try
        {
            PEMParser parser = new PEMParser(reader);
            Object obj = parser.readObject();
            if (obj instanceof java.security.cert.X509Certificate)
            {
                return (java.security.cert.X509Certificate) obj;
            }
            if (obj instanceof org.bouncycastle.cert.X509CertificateHolder)
            {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                return (java.security.cert.X509Certificate) cf.generateCertificate(
                        new java.io.ByteArrayInputStream(
                                ((org.bouncycastle.cert.X509CertificateHolder) obj).getEncoded()));
            }
            throw new IOException("unrecognized certificate format in " + file);
        }
        finally
        {
            reader.close();
        }
    }

    private static PrivateKey loadPrivateKey(File file) throws Exception
    {
        FileReader reader = new FileReader(file);
        try
        {
            PEMParser parser = new PEMParser(reader);
            Object obj = parser.readObject();
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            if (obj instanceof PrivateKeyInfo)
            {
                return converter.getPrivateKey((PrivateKeyInfo) obj);
            }
            if (obj instanceof PEMKeyPair)
            {
                return converter.getKeyPair((PEMKeyPair) obj).getPrivate();
            }
            if (obj instanceof AsymmetricKeyParameter)
            {
                return toJavaPrivateKey((AsymmetricKeyParameter) obj);
            }
            throw new IOException("unrecognized private key format in " + file);
        }
        finally
        {
            reader.close();
        }
    }

    private static AsymmetricKeyParameter toBcKeyParameter(PrivateKey key) throws Exception
    {
        if (key instanceof java.security.interfaces.RSAPrivateCrtKey)
        {
            java.security.interfaces.RSAPrivateCrtKey rsa = (java.security.interfaces.RSAPrivateCrtKey) key;
            return new RSAPrivateCrtKeyParameters(
                    rsa.getModulus(), rsa.getPublicExponent(), rsa.getPrivateExponent(),
                    rsa.getPrimeP(), rsa.getPrimeQ(), rsa.getPrimeExponentP(),
                    rsa.getPrimeExponentQ(), rsa.getCrtCoefficient());
        }
        // 非 RSA 私钥（如 EC）：通过 PKCS#8 编码走 BC 工厂
        return PrivateKeyFactory.createKey(key.getEncoded());
    }

    private static PrivateKey toJavaPrivateKey(AsymmetricKeyParameter keyParam) throws Exception
    {
        if (keyParam instanceof RSAPrivateCrtKeyParameters)
        {
            RSAPrivateCrtKeyParameters rsa = (RSAPrivateCrtKeyParameters) keyParam;
            RSAPrivateCrtKeySpec spec = new RSAPrivateCrtKeySpec(
                    rsa.getModulus(), rsa.getPublicExponent(), rsa.getExponent(),
                    rsa.getP(), rsa.getQ(), rsa.getDP(), rsa.getDQ(), rsa.getQInv());
            return KeyFactory.getInstance("RSA").generatePrivate(spec);
        }
        PrivateKeyInfo info = PrivateKeyInfoFactory.createPrivateKeyInfo(keyParam);
        return new JcaPEMKeyConverter().setProvider("BC").getPrivateKey(info);
    }

    private static java.security.cert.X509Certificate convertToJcaX509(BcTlsCertificate cert) throws Exception
    {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        return (java.security.cert.X509Certificate) cf.generateCertificate(
                new java.io.ByteArrayInputStream(cert.getEncoded()));
    }
}
