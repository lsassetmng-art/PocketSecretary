#!/bin/sh
set -eu

FILE="$HOME/erp-foundation/bin/pm_loop.sh"

# 同一ファイル cp 防止ロジックを注入（cp の直前想定）
# POSIX sh 対応・副作用なし

if ! grep -q "same file skip" "$FILE"; then
  awk '
  {
    print
    if ($0 ~ /cp .*pm_ai\/inbox/) {
      print "  # same file skip"
      print "  if [ \"$(realpath \"$src\")\" = \"$(realpath \"$dst\")\" ]; then"
      print "    echo \"ℹ same file; skip copy\""
      print "  else"
      print "    cp \"$src\" \"$dst\""
      print "  fi"
      getline
    }
  }
  ' "$FILE" > "$FILE.tmp"
  mv "$FILE.tmp" "$FILE"
fi

chmod +x "$FILE"

echo "✅ pm_loop same-file copy guard installed"
exit 0
