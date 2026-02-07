export PATH="$HOME/bin:$PATH"
export SUPABASE_URL="https://bkvycodiojbwcomnylqa.supabase.co"
export SUPABASE_ANON_KEY="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImJrdnljb2Rpb2pid2NvbW55bHFhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjYzMTQzMDUsImV4cCI6MjA4MTg5MDMwNX0.0gltwyReAE20AWk558et6kbSmqI43gArNTvWwYOt3ZQ"
export SUPABASE_AI_FUNC_PATH="/functions/v1/audit_ai_judge"



# ===== LINE Messaging API =====

export LINE_PUSH_API_URL="https://api.line.me/v2/bot/message/push"
export LINE_CHANNEL_ACCESS_TOKEN="pLfMETC/xXNt6V+CxjeNiF9MQQs0W7bqp+gyWFT3Le8eUDj0uqBltXXDqorWmQgdVfgVH1ccnPvf0owsj9ROUMFJES8Yk7B+xaxF8zTpcp7njxDdDpKmGx3n1jKUrDvBJvy05MU+mJOqUj3z59SS0AdB04t89/1O/w1cDnyilFU="
export LINE_TO_USER_ID="U5a234ca7d1d32d6e3c7ace90f07bfa26"


# ===== Supabase =====


# ===== Notify Backend (Test Company) =====
export COMPANY_ID="8f3c2e6a-9c1b-4c7a-a5d2-1c8c4b7f9e12"


# ===== Supabase / PostgreSQL =====
# libpq 互換 & 将来互換
  | sed 's/sslmode=[^&]*/sslmode=verify-full/' \
  | sed 's/$/&use-libpq-compat=true/')"


# ===== Supabase Direct DB (Phase6-12 固定) =====



# （DDL用がある場合）
