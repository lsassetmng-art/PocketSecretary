#!/bin/sh
set -eu

# --- repo root detection (cd 非依存) ---
REPO_ROOT="$(git rev-parse --show-toplevel 2>/dev/null || true)"
[ -n "$REPO_ROOT" ] || { echo "❌ not a git repository"; exit 11; }

BIN_DIR="$REPO_ROOT/bin"
APPLY="$BIN_DIR/apply_task.sh"
RULES_DIR="$HOME/erp-foundation/pm_ai/rules"

mkdir -p "$BIN_DIR"

# --- create / overwrite apply_task.sh safely ---
cat <<'EOS' > "$APPLY"
#!/bin/sh
set -eu

echo "▶ apply_task start"

# repo root
REPO_ROOT="$(git rev-parse --show-toplevel 2>/dev/null || true)"
[ -n "$REPO_ROOT" ] || { echo "❌ not a git repo"; exit 21; }

RULES_DIR="$HOME/erp-foundation/pm_ai/rules"

# --- rules existence check (warning only) ---
if [ ! -d "$RULES_DIR" ]; then
  echo "⚠ WARNING: pm rules directory not found"
  echo "  expected: $RULES_DIR"
elif ! ls "$RULES_DIR"/*.md >/dev/null 2>&1; then
  echo "⚠ WARNING: pm rules exist but no rule files (*.md)"
else
  echo "✔ pm rules detected"
fi

# --- placeholder for real apply logic ---
echo "▶ applying task (business logic placeholder)"

echo "▶ apply_task end"
exit 0
EOS

chmod +x "$APPLY"

echo "✅ apply_task.sh installed with rules check"
echo "   path: $APPLY"

exit 0
