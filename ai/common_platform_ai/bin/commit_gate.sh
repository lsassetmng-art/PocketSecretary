set -e

BASE="$HOME/ai/common_platform_ai"
BIN="$BASE/bin"

sh "$BIN/check_rules.sh" || exit 1
command -v git >/dev/null 2>&1 || exit 1
git rev-parse --is-inside-work-tree >/dev/null 2>&1 || exit 1

git status --porcelain | grep . >/dev/null 2>&1 || exit 0
git diff --check >/dev/null 2>&1 || exit 1

exit 0
