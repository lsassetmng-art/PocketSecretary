#!/bin/bash
set -e

STOP_FLAG="scripts/safe_stop.flag"

# 既にSTOP中なら何もしない
[ -f "$STOP_FLAG" ] && exit 0

# spec差分チェック
./scripts/check_spec_diff.sh || exit 0

# 正常時のみ生成
./scripts/full_generate.sh
