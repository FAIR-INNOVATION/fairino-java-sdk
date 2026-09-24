#!/bin/bash
set -euo pipefail

# ========== 出错时打印当前步骤 ==========
CURRENT_STEP="初始化"
on_error() {
    local exit_code=$?
    echo ""
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    echo "发布失败！"
    echo "失败步骤: $CURRENT_STEP"
    echo "退出码: $exit_code"
    echo "仓库可能处于中间状态，请手动检查（如 git merge --abort / git status）"
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    exit "$exit_code"
}
trap on_error ERR

# ========== 参数检查 ==========
if [ $# -ne 2 ]; then
    echo "用法: $0 <版本号> <分支名>"
    echo "示例: $0 1.1.7 3.9.7"
    exit 1
fi

VERSION="$1"
BRANCH="$2"
TAG="v${VERSION}_robot${BRANCH}"
MAIN_BRANCH="main"

echo "========== 开始发布流程 =========="
echo "分支: $BRANCH"
echo "标签: $TAG"

# ========== 预检查：tag 是否已存在 ==========
CURRENT_STEP="预检查 tag"
if git rev-parse "$TAG" >/dev/null 2>&1; then
    echo "错误: 标签 $TAG 已存在，请更换版本号或删除旧标签"
    exit 1
fi

# ========== 同步远程分支信息 ==========
CURRENT_STEP="同步远程分支信息"
git fetch --all --prune

# ========== 1. 切换到发布分支（不存在则新建） ==========
CURRENT_STEP="切换到发布分支（必要时新建）"

if git show-ref --verify --quiet "refs/heads/$BRANCH"; then
    # 本地已有该分支
    echo "本地已存在分支 $BRANCH，切换并拉取"
    git checkout "$BRANCH"
    if git ls-remote --exit-code --heads origin "$BRANCH" >/dev/null 2>&1; then
        git pull origin "$BRANCH"
    else
        echo "远程无分支 $BRANCH，跳过拉取"
    fi
elif git show-ref --verify --quiet "refs/remotes/origin/$BRANCH"; then
    # 远程有、本地没有 → 基于远程创建 tracking 分支
    echo "远程存在分支 $BRANCH，本地新建并跟踪"
    git checkout -b "$BRANCH" "origin/$BRANCH"
else
    # 本地远程都没有 → 基于 main 新建
    echo "本地和远程都不存在分支 $BRANCH，基于 $MAIN_BRANCH 新建"
    git checkout "$MAIN_BRANCH"
    git pull origin "$MAIN_BRANCH"
    git checkout -b "$BRANCH"
    git push -u origin "$BRANCH"
fi

# ========== 2. 提交 ==========
CURRENT_STEP="提交并推送发布分支"
git add .
if git diff --staged --quiet; then
    echo "没有需要提交的更改，跳过 commit"
else
    git commit -m "chore: release $TAG"
fi
git push origin "$BRANCH"

# ========== 3. 合并到 main ==========
CURRENT_STEP="切换到 $MAIN_BRANCH 并合并"
git checkout "$MAIN_BRANCH"
git pull origin "$MAIN_BRANCH"
git merge --no-ff "$BRANCH" -m "Merge branch '$BRANCH'"
git push origin "$MAIN_BRANCH"

# ========== 4. 打 tag 并推送 ==========
CURRENT_STEP="打标签并推送"
git tag -a "$TAG" -m "release version $TAG"
git push origin "$TAG"

CURRENT_STEP="完成"
echo "========== 发布完成 =========="
echo "标签 $TAG 已创建并推送到远程仓库"