#!/bin/sh
set -eu

############################################
# config（cd 非依存）
############################################
ROOT="$HOME/erp-foundation"
BIN="$ROOT/bin"
LOG="$ROOT/logs/pm_loop.cron.log"
CRON_TAG="# erp-foundation pm_loop"

############################################
# guards
############################################
[ -d "$ROOT" ] || { echo "❌ erp-foundation not found"; exit 11; }
[ -x "$BIN/pm_loop.sh" ] || { echo "❌ pm_loop.sh not executable"; exit 12; }
command -v crontab >/dev/null 2>&1 || { echo "❌ crontab not found (install cronie)"; exit 13; }

mkdir -p "$(dirname "$LOG")"

############################################
# cron entry（5分毎）
############################################
ENTRY="*/5 * * * * sh \"$BIN/pm_loop.sh\" >> \"$LOG\" 2>&1"

# 既存 crontab を取得
TMP="$(mktemp)"
crontab -l 2>/dev/null > "$TMP" || true

# 既に登録済みならスキップ
if grep -Fq "$CRON_TAG" "$TMP"; then
  echo "↪ cron already configured; skip"
else
  {
    echo "$CRON_TAG"
    echo "$ENTRY"
  } >> "$TMP"
  crontab "$TMP"
  echo "✅ cron installed (every 5 minutes)"
fi

rm -f "$TMP"

############################################
# status
############################################
echo "▶ cron list:"
crontab -l

exit 0
