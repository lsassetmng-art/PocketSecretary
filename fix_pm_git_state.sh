#!/bin/sh
set -eu

REPO="$HOME/erp-foundation"
cd "$REPO"

# ---- .gitignore に PM runtime / backup を追加 ----
cat <<'EOT' >> .gitignore

# pm runtime
logs/*.log

# pm loop backups
bin/pm_loop.sh.bak_*

# pm rules runtime cache (not rules themselves)
pm_ai/rules/.cache
EOT

# ---- rules ディレクトリを正として git 管理 ----
mkdir -p pm_ai/rules
touch pm_ai/rules/.keep

git add .gitignore pm_ai/rules/.keep

# ---- 既に修正済み pm_loop.sh を確定 ----
git add bin/pm_loop.sh || true

git commit -m "chore: stabilize pm_loop git state (rules + ignore)" || true

echo "✅ git state fixed for pm_loop"
exit 0
