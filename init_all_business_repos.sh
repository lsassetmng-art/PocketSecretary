#!/bin/sh
set -eu

# ============================
# Business repositories
# ============================
BUSINESSES="
erp-sales
erp-shipping
erp-billing
erp-purchase
erp-inventory
erp-accounting
erp-asset
erp-hr
erp-audit
"

BASE="$HOME"

for NAME in $BUSINESSES; do
  REPO="$BASE/$NAME"
  echo "▶ init git repo: $NAME"

  # ディレクトリが無ければ作る（既存は壊さない）
  mkdir -p "$REPO"
  cd "$REPO"

  # git init（未初期化のみ）
  if [ ! -d ".git" ]; then
    git init >/dev/null
  fi

  # 初期 commit が無ければ作る
  if ! git rev-parse --verify HEAD >/dev/null 2>&1; then
    # 空でも commit できるように .gitkeep
    [ -e .gitkeep ] || : > .gitkeep
    git add .gitkeep
    git commit -m "init: $NAME business repository" >/dev/null
  fi
done

echo "✅ all business repositories initialized"
exit 0
