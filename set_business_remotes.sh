#!/bin/sh
set -eu

OWNER="lsassetmng-art"

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
  echo "▶ set remote: $NAME"

  if [ ! -d "$REPO/.git" ]; then
    echo "❌ not a git repo: $REPO"
    exit 1
  fi

  cd "$REPO"

  if git remote get-url origin >/dev/null 2>&1; then
    echo "  ↪ origin already set; skip"
    continue
  fi

  git remote add origin "https://github.com/$OWNER/$NAME.git"
  echo "  ✅ origin added"
done

echo "✅ all business remotes configured"
exit 0
