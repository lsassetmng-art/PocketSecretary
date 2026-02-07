set -e

BASE="$HOME/ai/common_platform_ai"
BIN="$BASE/bin"

# ---- rules check ----
if ! sh "$BIN/check_rules.sh" ; then
  echo "❌ rules violation detected"
  exit 1
fi

# ---- git existence ----
if ! command -v git >/dev/null 2>&1 ; then
  echo "❌ git command not found"
  exit 1
fi

# ---- git repository ----
if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1 ; then
  echo "❌ not a git repository"
  exit 1
fi

# ---- changes check ----
if ! git status --porcelain | grep . >/dev/null 2>&1 ; then
  echo "ℹ no changes to commit"
  exit 0
fi

# ---- diff check ----
if ! git diff --check >/dev/null 2>&1 ; then
  echo "❌ git diff check failed (whitespace or conflict)"
  exit 1
fi

echo "✅ commit gate passed"
exit 0
