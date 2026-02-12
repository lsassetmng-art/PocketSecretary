package com.lsam.pocketsecretary.core.notify;

import android.content.Context;
import com.lsam.pocketsecretary.R;
import java.util.*;

public class NotificationTextProvider {

    private static final Random RANDOM = new Random();

    private static String pick(String[] a){
        return a[RANDOM.nextInt(a.length)];
    }

    public static String hint(Context context, boolean tight, boolean cont){

        if (tight && cont)
            return context.getString(R.string.hint_tight_cont);

        if (tight)
            return context.getString(R.string.hint_tight);

        if (cont)
            return context.getString(R.string.hint_cont);

        return "";
    }

    public static String consultFollowUp(Context context){
        return context.getString(R.string.consult_followup);
    }
}
