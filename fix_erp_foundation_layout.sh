#!/data/data/com.termux/files/usr/bin/bash
set -e

echo "=== FIX ERP FOUNDATION LAYOUT START ==="

HOME_DIR="$HOME"
PROJECT_DIR="$HOME_DIR/erp-foundation"
BACKUP_DIR="$HOME_DIR/_backup_misplaced_$(date +%Y%m%d_%H%M%S)"

# ---------- 前提 ----------
if [ ! -d "$PROJECT_DIR" ]; then
  echo "ERROR: $PROJECT_DIR not found"
  exit 1
fi

mkdir -p "$BACKUP_DIR"

# ---------- 対象ディレクトリ ----------
DIRS="spec tools scripts reports logs storage docs"

# ---------- home 側に誤ってあるものを退避 ----------
for d in $DIRS; do
  if [ -d "$HOME_DIR/$d" ]; then
    echo "Backup misplaced ~/$d -> $BACKUP_DIR/$d"
    mv "$HOME_DIR/$d" "$BACKUP_DIR/"
  fi
done

# ---------- project 側に統合 ----------
for d in $DIRS; do
  mkdir -p "$PROJECT_DIR/$d"

  if [ -d "$BACKUP_DIR/$d" ]; then
    echo "Merge $BACKUP_DIR/$d -> $PROJECT_DIR/$d"
    cp -r "$BACKUP_DIR/$d/"* "$PROJECT_DIR/$d/" 2>/dev/null || true
  fi
done

# ---------- 権限調整 ----------
chmod +x "$PROJECT_DIR/scripts/"*.sh 2>/dev/null || true

# ---------- 完了 ----------
echo ""
echo "=== FIX COMPLETE ==="
echo "Project root: $PROJECT_DIR"
echo "Backup kept at: $BACKUP_DIR"
echo ""
echo "Recommended next command:"
echo "  cd ~/erp-foundation && tree -L 2"
