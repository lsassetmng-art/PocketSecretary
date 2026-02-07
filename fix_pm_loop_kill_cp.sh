#!/bin/sh
set -eu

FILE="$HOME/erp-foundation/bin/pm_loop.sh"

# cp 行をすべて無効化（完全停止）
sed -i '
/^[[:space:]]*cp[[:space:]]/ {
  s/^/# DISABLED_BY_PM_LOOP_FIX: /
}
' "$FILE"

chmod +x "$FILE"

echo "✅ pm_loop: ALL cp statements disabled"
exit 0
