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
                        ? "莉頑律縺ｯ莠亥ｮ壹・縺ゅｊ縺ｾ縺帙ｓ縲・
                        : "莉頑律縺ｯ莠亥ｮ壹′" + today.size() + "莉ｶ縺ゅｊ縺ｾ縺吶・;
                case NEXT_EVENT:
                    for (long[] e: today){
                        if (e[0] > now){
                            return "谺｡縺ｯ " + fmt(e[0]) + " 縺九ｉ莠亥ｮ壹′縺ゅｊ縺ｾ縺吶・;
                        }
                    }
                    return "縺薙・縺ゅ→縺ｮ莠亥ｮ壹・縺ゅｊ縺ｾ縺帙ｓ縲・;
                case FREE_TIME:
                    return FreeTimeUtil.todaySummary(today);
                case SHOULD_GO_NOW:
                    for (long[] e: today){
                        if (e[0] > now){
                            long min = Math.max(0,(e[0]-now)/60000);
                            return (min>=10)
                                ? "莉雁・繧句ｿ・ｦ√・縺ゅｊ縺ｾ縺帙ｓ縲ゆｽ呵｣輔′縺ゅｊ縺ｾ縺吶・
                                : "縺昴ｍ縺昴ｍ蜃ｺ逋ｺ縺励◆譁ｹ縺後ｈ縺輔◎縺・〒縺吶・;
                        }
                    }
                    return "莉頑律縺ｯ蜃ｺ逋ｺ縺ｮ蠢・ｦ√・縺ｪ縺輔◎縺・〒縺吶・;
            }
        }catch(Exception ignored){}
        return "諠・ｱ繧貞叙蠕励〒縺阪∪縺帙ｓ縺ｧ縺励◆縲・;
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
