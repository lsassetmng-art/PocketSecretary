package com.lsam.pocketsecretary.consult;

import android.content.Context;
import com.lsam.pocketsecretary.core.time.FreeTimeUtil;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.*;

public class ConsultEngine {

    public static String answer(Context c, QuestionType q){
        try{
            JSONArray arr = SimpleEventStore.list(c);
            List<long[]> today = new ArrayList<>();
            long now = System.currentTimeMillis();

            for (int i=0;i<arr.length();i++){
                JSONObject o = arr.getJSONObject(i);
                long s = o.getLong("startAt");
                long e = o.optLong("endAt", s + 60*60*1000);
                if (isToday(s)) today.add(new long[]{s,e});
            }
            today.sort(Comparator.comparingLong(a->a[0]));

            switch (q){
                case TODAY_BUSY:
                    return today.isEmpty()
                        ? "今日は予定はありません。"
                        : "今日は予定が" + today.size() + "件あります。";
                case NEXT_EVENT:
                    for (long[] e: today){
                        if (e[0] > now){
                            return "次は " + fmt(e[0]) + " から予定があります。";
                        }
                    }
                    return "このあとの予定はありません。";
                case FREE_TIME:
                    return FreeTimeUtil.todaySummary(today);
                case SHOULD_GO_NOW:
                    for (long[] e: today){
                        if (e[0] > now){
                            long min = Math.max(0,(e[0]-now)/60000);
                            return (min>=10)
                                ? "今出る必要はありません。余裕があります。"
                                : "そろそろ出発した方がよさそうです。";
                        }
                    }
                    return "今日は出発の必要はなさそうです。";
            }
        }catch(Exception ignored){}
        return "情報を取得できませんでした。";
    }

    private static boolean isToday(long t){
        Calendar a=Calendar.getInstance(); a.setTimeInMillis(t);
        Calendar b=Calendar.getInstance();
        return a.get(Calendar.YEAR)==b.get(Calendar.YEAR)
            && a.get(Calendar.DAY_OF_YEAR)==b.get(Calendar.DAY_OF_YEAR);
    }
    private static String fmt(long t){
        Calendar c=Calendar.getInstance(); c.setTimeInMillis(t);
        return String.format(Locale.getDefault(),"%02d:%02d",
            c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));
    }
}
