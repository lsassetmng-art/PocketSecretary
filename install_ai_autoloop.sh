#!/data/data/com.termux/files/usr/bin/bash
set -e

BASE="$HOME/foundation-team"
LOGDIR="$BASE/logs"
JOB_ID=9001
INTERVAL_MS=1800000   # 30分

echo "=== AI AUTONOMOUS LOOP INSTALLER START ==="

# -------------------------
# 1. 基本チェック
# -------------------------
if [ ! -d "$BASE" ]; then
  echo "❌ foundation-team not found at $BASE"
  exit 1
fi

command -v termux-job-scheduler >/dev/null 2>&1 || {
  echo "❌ termux-job-scheduler not found"
  echo "👉 pkg install termux-api"
  exit 1
}

mkdir -p "$LOGDIR"

# -------------------------
# 2. ループ実行スクリプト生成
# -------------------------
cat << 'EOS' > "$BASE/ai_autoloop.sh"
#!/data/data/com.termux/files/usr/bin/bash
set -e
cd "$HOME/foundation-team"

mkdir -p logs
LOG="logs/ai-loop-$(date +%Y%m%d-%H%M%S).log"

{
  echo "=== AI LOOP START $(date) ==="

  python run_all.py
  python cross_review.py
  python generate_policy_next.py
  python auto_policy_and_safestop.py
  python rerun_ng_roles.py
  python report_onepage.py
  python leader_ng_summary.py

  echo "=== AI LOOP END $(date) ==="
} | tee -a "$LOG"
EOS

chmod +x "$BASE/ai_autoloop.sh"

# -------------------------
# 3. 既存ジョブがあれば削除
# -------------------------
termux-job-scheduler --cancel --job-id $JOB_ID >/dev/null 2>&1 || true

# -------------------------
# 4. ジョブ登録
# -------------------------
termux-job-scheduler \
  --job-id $JOB_ID \
  --period-ms $INTERVAL_MS \
  --script "$BASE/ai_autoloop.sh"

echo ""
echo "✅ AI AUTONOMOUS LOOP INSTALLED"
echo "   Job ID      : $JOB_ID"
echo "   Interval    : $((INTERVAL_MS / 60000)) minutes"
echo "   Script      : $BASE/ai_autoloop.sh"
echo "   Logs        : $BASE/logs/"
echo ""
echo "=== INSTALL COMPLETED ==="
