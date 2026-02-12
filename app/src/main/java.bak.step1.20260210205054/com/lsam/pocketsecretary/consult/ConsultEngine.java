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
                        ? "闔蛾大ｾ狗ｸｺ・ｯ闔莠･・ｮ螢ｹ繝ｻ邵ｺ繧・ｽ顔ｸｺ・ｾ邵ｺ蟶呻ｽ鍋ｸｲ繝ｻ
                        : "闔蛾大ｾ狗ｸｺ・ｯ闔莠･・ｮ螢ｹ窶ｲ" + today.size() + "闔会ｽｶ邵ｺ繧・ｽ顔ｸｺ・ｾ邵ｺ蜷ｶﾂ繝ｻ;
                case NEXT_EVENT:
                    for (long[] e: today){
                        if (e[0] > now){
                            return "隹ｺ・｡邵ｺ・ｯ " + fmt(e[0]) + " 邵ｺ荵晢ｽ芽滋莠･・ｮ螢ｹ窶ｲ邵ｺ繧・ｽ顔ｸｺ・ｾ邵ｺ蜷ｶﾂ繝ｻ;
                        }
                    }
                    return "邵ｺ阮吶・邵ｺ繧・・邵ｺ・ｮ闔莠･・ｮ螢ｹ繝ｻ邵ｺ繧・ｽ顔ｸｺ・ｾ邵ｺ蟶呻ｽ鍋ｸｲ繝ｻ;
                case FREE_TIME:
                    return FreeTimeUtil.todaySummary(today);
                case SHOULD_GO_NOW:
                    for (long[] e: today){
                        if (e[0] > now){
                            long min = Math.max(0,(e[0]-now)/60000);
                            return (min>=10)
                                ? "闔蛾宦繝ｻ郢ｧ蜿･・ｿ繝ｻ・ｦ竏壹・邵ｺ繧・ｽ顔ｸｺ・ｾ邵ｺ蟶呻ｽ鍋ｸｲ繧・ｽｽ蜻ｵ・｣霈披ｲ邵ｺ繧・ｽ顔ｸｺ・ｾ邵ｺ蜷ｶﾂ繝ｻ
                                : "邵ｺ譏ｴ・咲ｸｺ譏ｴ・崎怎・ｺ騾具ｽｺ邵ｺ蜉ｱ笳・ｭ・ｽｹ邵ｺ蠕鯉ｽ育ｸｺ霈披落邵ｺ繝ｻ縲堤ｸｺ蜷ｶﾂ繝ｻ;
                        }
                    }
                    return "闔蛾大ｾ狗ｸｺ・ｯ陷・ｽｺ騾具ｽｺ邵ｺ・ｮ陟｢繝ｻ・ｦ竏壹・邵ｺ・ｪ邵ｺ霈披落邵ｺ繝ｻ縲堤ｸｺ蜷ｶﾂ繝ｻ;
            }
        }catch(Exception ignored){}
        return "隲繝ｻ・ｰ・ｱ郢ｧ雋槫徐陟募干縲堤ｸｺ髦ｪ竏ｪ邵ｺ蟶呻ｽ鍋ｸｺ・ｧ邵ｺ蜉ｱ笳・ｸｲ繝ｻ;
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
