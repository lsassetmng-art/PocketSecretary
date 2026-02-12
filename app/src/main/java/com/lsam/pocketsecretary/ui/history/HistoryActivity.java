package com.lsam.pocketsecretary.ui.history;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.history.NotificationHistoryEntity;
import com.lsam.pocketsecretary.history.NotificationHistoryStore;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryActivity extends BaseActivity {

    private ExecutorService executor;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ BaseActivity方式
        setBaseContent(R.layout.activity_history);

        RecyclerView recyclerView = findViewById(R.id.historyRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        executor = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        loadHistory(recyclerView);
    }

    @Override
    protected String getHeaderTitle() {
        return "History";
    }

    private void loadHistory(RecyclerView recyclerView) {

        executor.execute(() -> {

            List<NotificationHistoryEntity> list =
                    NotificationHistoryStore
                            .get(getApplicationContext())
                            .latestBlocking(100);

            mainHandler.post(() -> {
                if (!isFinishing() && list != null) {
                    recyclerView.setAdapter(
                            new HistoryAdapter(this, list)
                    );
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
        super.onDestroy();
    }

    // ==========================================================
    // Adapter
    // ==========================================================
    static class HistoryAdapter
            extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

        private final List<NotificationHistoryEntity> items;
        private final Context context;

        HistoryAdapter(Context context,
                       List<NotificationHistoryEntity> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(
                ViewGroup parent,
                int viewType
        ) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_history, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(
                ViewHolder holder,
                int position
        ) {
            NotificationHistoryEntity e = items.get(position);

            holder.personaName.setText(
                    e.source != null
                            ? e.source
                            : context.getString(
                                    R.string.history_unknown_source)
            );

            holder.historyText.setText(e.text);

            long time = e.id;

            String date = DateFormat.format(
                    "yyyy/MM/dd HH:mm",
                    time
            ).toString();

            holder.historyDate.setText(date);

            holder.itemView.setOnClickListener(v -> {

                ClipboardManager clipboard =
                        (ClipboardManager) context
                                .getSystemService(
                                        Context.CLIPBOARD_SERVICE);

                String copyText =
                        holder.personaName.getText().toString()
                                + "\n"
                                + holder.historyText.getText().toString()
                                + "\n"
                                + holder.historyDate.getText().toString();

                ClipData clip =
                        ClipData.newPlainText("history", copyText);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(
                        context,
                        context.getString(
                                R.string.history_saved_toast),
                        Toast.LENGTH_SHORT
                ).show();
            });
        }

        @Override
        public int getItemCount() {
            return items != null ? items.size() : 0;
        }

        static class ViewHolder
                extends RecyclerView.ViewHolder {

            android.widget.TextView personaName;
            android.widget.TextView historyText;
            android.widget.TextView historyDate;

            ViewHolder(View itemView) {
                super(itemView);
                personaName =
                        itemView.findViewById(R.id.personaName);
                historyText =
                        itemView.findViewById(R.id.historyText);
                historyDate =
                        itemView.findViewById(R.id.historyDate);
            }
        }
    }
}
