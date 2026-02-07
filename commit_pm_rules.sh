#!/bin/sh
set -eu

REPO="$HOME/erp-foundation"
cd "$REPO"

# rules が存在しなければ異常
if [ ! -d pm_ai/rules ]; then
  echo "❌ pm_ai/rules not found"
  exit 2
fi

# rules を正式管理
git add pm_ai/rules

git commit -m "feat(pm): add business rules for PM decision making" || true

echo "✅ pm rules committed"
exit 0
