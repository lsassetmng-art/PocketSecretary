#!/usr/bin/env bash
set -e

echo "=== REVIEW USECASES ==="

if [ ! -f spec/usecases.schema.yaml ]; then
  echo "ERROR: usecases.schema.yaml not found"
  exit 1
fi

if grep -R "company_id" spec/usecases.schema.yaml >/dev/null; then
  echo "ERROR: company_id found in usecases schema"
  exit 1
fi

echo "OK: usecases schema looks clean"
