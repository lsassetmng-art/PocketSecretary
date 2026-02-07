#!/bin/sh
set -eu

# =========================================================
# 1) erp-foundation: pm_loop rule existence check
# =========================================================

FOUNDATION="$HOME/erp-foundation"
PM_LOOP="$FOUNDATION/bin/pm_loop.sh"
RULE_DIR="$FOUNDATION/pm_ai/rules"

if [ ! -d "$FOUNDATION" ]; then
  echo "❌ erp-foundation not found"
  exit 1
fi

if [ ! -f "$PM_LOOP" ]; then
  echo "❌ pm_loop.sh not found"
  exit 1
fi

# --- backup (non-destructive) ---
if [ ! -f "$PM_LOOP.bak_rules_check" ]; then
  cp "$PM_LOOP" "$PM_LOOP.bak_rules_check"
fi

# --- rewrite pm_loop.sh with rule check ---
cat <<'EOT' > "$PM_LOOP"
#!/bin/sh
set -eu

REPO="$(cd "$(dirname "$0")/.." && pwd)"
INBOX="$REPO/pm_ai/inbox"
DONE="$REPO/pm_ai/done"
LOG="$REPO/logs/pm_loop.log"
RULES="$REPO/pm_ai/rules"

echo "▶ pm_loop start"
echo "▶ repo: $REPO"

# --- rules existence check (HARD) ---
if [ ! -d "$RULES" ] || [ -z "$(ls -A "$RULES" 2>/dev/null)" ]; then
  echo "❌ PM rules not found or empty: $RULES"
  exit 3
fi

cd "$REPO"

# --- clean check ---
if [ -n "$(git status --porcelain)" ]; then
  echo "❌ working tree not clean; stop"
  git status --short
  exit 2
fi

mkdir -p "$DONE" "$(dirname "$LOG")"

count=0
for task in "$INBOX"/*.md; do
  [ -e "$task" ] || break
  name="$(basename "$task")"
  echo "▶ task: $name"

  # --- master hint (soft) ---
  if ! grep -Eq 'マスタ|共通マスタ|準マスタ|マスタ判定' "$task"; then
    echo "⚠ WARNING: master judgment not found in $name" | tee -a "$LOG"
  fi

  mv "$task" "$DONE/$name"
  count=$((count+1))
done

echo "✔ processed $count task(s)"
echo "▶ pm_loop end"
exit 0
EOT

chmod +x "$PM_LOOP"

echo "✅ pm_loop updated with rules check"

# =========================================================
# 2) business repos: apply_task.sh rule reference warning
# =========================================================

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

for NAME in $BUSINESSES; do
  REPO="$HOME/$NAME"
  APPLY="$REPO/bin/apply_task.sh"

  if [ ! -f "$APPLY" ]; then
    echo "↪ skip (no apply_task.sh): $NAME"
    continue
  fi

  # backup
  if [ ! -f "$APPLY.bak_rules_ref" ]; then
    cp "$APPLY" "$APPLY.bak_rules_ref"
  fi

cat <<'EOB' > "$APPLY"
#!/bin/sh
set -eu

TASK="$1"
FOUNDATION="$HOME/erp-foundation"
RULES="$FOUNDATION/pm_ai/rules"

echo "▶ apply_task start"
echo "▶ task file: $TASK"

[ -f "$TASK" ] || { echo "❌ task file not found"; exit 1; }

# --- rules reference check (SOFT) ---
if [ ! -d "$RULES" ] || [ -z "$(ls -A "$RULES" 2>/dev/null)" ]; then
  echo "⚠ WARNING: PM rules not found: $RULES"
  echo "  👉 erp-foundation の rules を参照してください"
else
  echo "ℹ PM rules detected"
fi

echo "---------------------------------"
cat "$TASK"
echo "---------------------------------"

# --- master hint ---
if ! grep -Eq 'マスタ|共通マスタ|準マスタ|マスタ判定' "$TASK"; then
  echo "⚠ WARNING: master judgment not found in instruction"
fi

echo "ℹ implementation handled elsewhere"
echo "▶ apply_task end"
exit 0
EOB

  chmod +x "$APPLY"
  echo "✅ updated apply_task.sh: $NAME"
done

echo "🎉 rules checks installed"
exit 0
