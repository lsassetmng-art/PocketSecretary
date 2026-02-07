set -e

BASE="$HOME/ai/common_platform_ai"
BIN="$BASE/bin"

sh "$BIN/commit_gate.sh" || exit 1

git add .
git commit -m "common platform auto commit" >/dev/null 2>&1 || exit 1
git push >/dev/null 2>&1 || exit 1

exit 0
