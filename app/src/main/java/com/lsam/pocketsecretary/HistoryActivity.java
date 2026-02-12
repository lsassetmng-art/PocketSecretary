package com.lsam.pocketsecretary;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lsam.pocketsecretary.history.NotificationHistoryEntity;
import com.lsam.pocketsecretary.history.NotificationHistoryStore;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        RecyclerView recyclerView = findViewById(R.id.historyRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<NotificationHistoryEntity> list =
                NotificationHistoryStore.get(this).latestBlocking(100);

        recyclerView.setAdapter(new HistoryAdapter(list));
    }

    static class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

        private final List<NotificationHistoryEntity> items;

        HistoryAdapter(List<NotificationHistoryEntity> items) {
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_history, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            NotificationHistoryEntity e = items.get(position);

            holder.personaName.setText(
                    e.source != null ? e.source : "遘俶嶌"
            );

            holder.historyText.setText(e.text);

            long time = e.id; // 譌｢蟄倥ヵ繧｣繝ｼ繝ｫ繝峨ｒ螳牙・縺ｫ蛻ｩ逕ｨ

            String date = DateFormat.format(
                    "yyyy/MM/dd HH:mm",
                    time
            ).toString();

            holder.historyDate.setText(date);

            // 繧ｿ繝・・縺ｧ繧ｳ繝斐・
            holder.itemView.setOnClickListener(v -> {

                ClipboardManager clipboard =
                        (ClipboardManager) v.getContext()
                                .getSystemService(Context.CLIPBOARD_SERVICE);

                String copyText =
                        holder.personaName.getText().toString()
                        + "\n"
                        + holder.historyText.getText().toString()
                        + "\n"
                        + holder.historyDate.getText().toString();

                ClipData clip = ClipData.newPlainText("history", copyText);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(
                        v.getContext(),
                        "繧ｳ繝斐・縺励∪縺励◆",
                        Toast.LENGTH_SHORT
                ).show();
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            android.widget.TextView personaName;
            android.widget.TextView historyText;
            android.widget.TextView historyDate;

            ViewHolder(View itemView) {
                super(itemView);
                personaName = itemView.findViewById(R.id.personaName);
                historyText = itemView.findViewById(R.id.historyText);
                historyDate = itemView.findViewById(R.id.historyDate);
            }
        }
    }
}