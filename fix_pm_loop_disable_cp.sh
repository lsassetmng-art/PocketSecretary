#!/bin/sh
set -eu

FILE="$HOME/erp-foundation/bin/pm_loop.sh"

# cp 行を無効化（完全に安全）
sed -i '
/cp .*pm_ai\/inbox/ {
  s/^/# DISABLED: same-file cp guard: /
}
' "$FILE"

chmod +x "$FILE"

echo "✅ pm_loop: same-file cp DISABLED"
exit 0
