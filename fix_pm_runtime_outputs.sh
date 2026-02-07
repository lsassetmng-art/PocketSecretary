#!/bin/sh
set -eu

REPO="$HOME/erp-foundation"
cd "$REPO"

# .gitignore が無ければ作る
[ -f .gitignore ] || touch .gitignore

# pm runtime outputs を ignore
cat <<'EOG' >> .gitignore

# pm runtime outputs
reports/
exports/
EOG

# 既に追跡されていたら解除
git rm -r --cached reports exports 2>/dev/null || true

git add .gitignore
git commit -m "chore(pm): ignore runtime reports and exports" || true

echo "✅ pm runtime outputs ignored"
exit 0
