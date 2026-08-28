#!/bin/bash
set -e

# 检查参数个数
if [ $# -ne 2 ]; then
    echo "用法: $0 <版本号> <分支名>"
    echo "示例: $0 1.1.7 3.9.7"
    exit 1
fi

VERSION=$1
BRANCH=$2
TAG="v${VERSION}_robot${BRANCH}"

echo "========== 开始发布流程 =========="
echo "分支: $BRANCH"
echo "标签: $TAG"

# 1. 切换到发布分支，拉取最新（若远程存在则拉取，否则忽略）
git checkout $BRANCH
git pull origin $BRANCH 2>/dev/null || echo "远程无分支 $BRANCH，跳过拉取"

# 2. 添加所有更改并提交（若没有更改则跳过提交）
git status
git add .
if git diff --staged --quiet; then
    echo "没有需要提交的更改，跳过 commit"
else
    git commit -m "$BRANCH"
fi
git push origin $BRANCH

# 3. 切回主分支并合并
git checkout main
git pull origin main
git merge $BRANCH -m "Merge branch '$BRANCH'"
git push origin main

# 4. 打附注标签并推送
git tag -a $TAG -m "release version $TAG"
git push origin $TAG

echo "========== 发布完成 =========="
echo "标签 $TAG 已创建并推送到远程仓库"