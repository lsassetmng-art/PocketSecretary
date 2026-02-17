package com.lsam.pocketsecretary.ui.hub;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.ui.calculator.CalculatorActivity;
import com.lsam.pocketsecretary.ui.event.EventListActivity;
import com.lsam.pocketsecretary.ui.library.LibraryActivity;
import com.lsam.pocketsecretary.ui.memo.MemoListActivity;
import com.lsam.pocketsecretary.ui.todo.TodoListActivity;
import com.lsam.pocketsecretary.ui.transit.TransitFareActivity;

import com.lsam.pocketsecretary.ui.business.BusinessModeActivity;
import com.lsam.pocketsecretary.ui.businesscard.BusinessCardLinkActivity;
import com.lsam.pocketsecretary.ui.mbo.MboLinkActivity;
import com.lsam.pocketsecretary.ui.request.RequestHubActivity;

import com.lsam.pocketsecretary.ui.consult.ConsultStubActivity;
import com.lsam.pocketsecretary.ui.summary.SummaryStubActivity;

public class AllFeaturesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        TextView hint = new TextView(this);
        hint.setText(getString(R.string.ps_hint_stub));
        root.addView(hint, lp());

        root.addView(navButton(R.string.ps_screen_events, EventListActivity.class), lp());
        root.addView(navButton(R.string.ps_screen_todo, TodoListActivity.class), lp());
        root.addView(navButton(R.string.ps_screen_calc, CalculatorActivity.class), lp());
        root.addView(navButton(R.string.ps_screen_memo, MemoListActivity.class), lp());
        root.addView(navButton(R.string.ps_screen_library, LibraryActivity.class), lp());
        root.addView(navButton(R.string.ps_screen_transit, TransitFareActivity.class), lp());

        root.addView(navButton(R.string.ps_screen_business_mode, BusinessModeActivity.class), lp());
        root.addView(navButton(R.string.ps_screen_business_card, BusinessCardLinkActivity.class), lp());
        root.addView(navButton(R.string.ps_screen_mbo, MboLinkActivity.class), lp());
        root.addView(navButton(R.string.ps_screen_request, RequestHubActivity.class), lp());

        root.addView(navButton(R.string.ps_screen_consult, ConsultStubActivity.class), lp());
        root.addView(navButton(R.string.ps_screen_summary, SummaryStubActivity.class), lp());

        setContentView(root);
    }

    private Button navButton(int titleRes, Class<?> to) {
        Button b = new Button(this);
        b.setText(getString(titleRes));
        b.setOnClickListener(v -> startActivity(new Intent(this, to)));
        return b;
    }

    private static LinearLayout.LayoutParams lp() {
        return new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    @Override
    protected String getHeaderTitle() {
        return getString(R.string.ps_feature_hub_title);
    }

    @Override
    protected boolean showSettingsButton() {
        return false;
    }
}