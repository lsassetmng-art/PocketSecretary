#!/data/data/com.termux/files/usr/bin/bash
set -e
cd "$HOME/foundation-team"

mkdir -p logs
LOG="logs/ai-loop-$(date +%Y%m%d-%H%M%S).log"

{
  echo "=== AI LOOP START $(date) ==="

  python merge_policy_for_generation.py
  python run_all.py
  python cross_review.py
  python generate_policy_next.py
  python auto_policy_and_safestop.py
  python rerun_ng_roles.py
  python report_onepage.py
  python leader_ng_summary.py

  echo "=== AI LOOP END $(date) ==="
} | tee -a "$LOG"
