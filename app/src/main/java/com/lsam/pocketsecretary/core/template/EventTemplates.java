package com.lsam.pocketsecretary.core.template;

import android.content.Context;
import com.lsam.pocketsecretary.R;
import java.util.Arrays;
import java.util.List;

public class EventTemplates {

    public static class T {
        public final String title;
        public final int hour;
        public T(String t,int h){ title=t; hour=h; }
    }

    public static List<T> list(Context context){
        return Arrays.asList(
            new T(context.getString(R.string.template_morning_meeting),10),
            new T(context.getString(R.string.template_meeting),14),
            new T(context.getString(R.string.template_standup),9),
            new T(context.getString(R.string.template_gym),18),
            new T(context.getString(R.string.template_dinner),20)
        );
    }
}
