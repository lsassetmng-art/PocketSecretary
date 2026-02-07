set -e

BASE="$HOME/ai/common_platform_ai"
BIN="$BASE/bin"

# ---- commit gate（verbose 版を優先） ----
if [ -f "$BIN/commit_gate_verbose.sh" ]; then
  sh "$BIN/commit_gate_verbose.sh" || exit 1
else
  sh "$BIN/commit_gate.sh" || exit 1
fi

# ---- git add ----
if ! git add . ; then
  echo "❌ git add failed"
  exit 1
fi

# ---- git commit ----
if ! git commit -m "common platform auto commit" >/dev/null 2>&1 ; then
  echo "❌ git commit failed (nothing to commit or config issue)"
  exit 1
fi

# ---- git push ----
if ! git push >/dev/null 2>&1 ; then
  echo "❌ git push failed (remote / auth / network)"
  exit 1
fi

echo "✅ git commit & push completed successfully"
exit 0
