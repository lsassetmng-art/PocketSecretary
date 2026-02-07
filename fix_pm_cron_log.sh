#!/bin/sh
set -eu

ROOT="$HOME/erp-foundation"
LOG_DIR="$ROOT/logs"
LOG_FILE="$LOG_DIR/pm_loop.cron.log"

# guard
[ -d "$ROOT" ] || { echo "❌ erp-foundation not found"; exit 11; }

# ensure log dir
if [ ! -d "$LOG_DIR" ]; then
  mkdir -p "$LOG_DIR"
  echo "✔ created logs directory"
fi

# ensure log file
if [ ! -f "$LOG_FILE" ]; then
  : > "$LOG_FILE"
  echo "✔ created cron log file"
fi

echo "▶ cron log ready:"
ls -l "$LOG_FILE"

exit 0
