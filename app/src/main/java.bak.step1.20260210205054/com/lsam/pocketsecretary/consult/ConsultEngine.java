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
                        ? "髣碑崟・ｰ螟ｧ・ｾ迢暦ｽｸ・ｺ繝ｻ・ｯ髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ郢晢ｽｻ驍ｵ・ｺ郢ｧ繝ｻ・ｽ鬘費ｽｸ・ｺ繝ｻ・ｾ驍ｵ・ｺ陝ｶ蜻ｻ・ｽ骰具ｽｸ・ｲ郢晢ｽｻ
                        : "髣碑崟・ｰ螟ｧ・ｾ迢暦ｽｸ・ｺ繝ｻ・ｯ髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ遯ｶ・ｲ" + today.size() + "髣比ｼ夲ｽｽ・ｶ驍ｵ・ｺ郢ｧ繝ｻ・ｽ鬘費ｽｸ・ｺ繝ｻ・ｾ驍ｵ・ｺ陷ｷ・ｶ・つ郢晢ｽｻ;
                case NEXT_EVENT:
                    for (long[] e: today){
                        if (e[0] > now){
                            return "髫ｹ・ｺ繝ｻ・｡驍ｵ・ｺ繝ｻ・ｯ " + fmt(e[0]) + " 驍ｵ・ｺ闕ｵ譎｢・ｽ闃ｽ貊玖滋・･繝ｻ・ｮ陞｢・ｹ遯ｶ・ｲ驍ｵ・ｺ郢ｧ繝ｻ・ｽ鬘費ｽｸ・ｺ繝ｻ・ｾ驍ｵ・ｺ陷ｷ・ｶ・つ郢晢ｽｻ;
                        }
                    }
                    return "驍ｵ・ｺ髦ｮ蜷ｶ繝ｻ驍ｵ・ｺ郢ｧ繝ｻ繝ｻ驍ｵ・ｺ繝ｻ・ｮ髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ郢晢ｽｻ驍ｵ・ｺ郢ｧ繝ｻ・ｽ鬘費ｽｸ・ｺ繝ｻ・ｾ驍ｵ・ｺ陝ｶ蜻ｻ・ｽ骰具ｽｸ・ｲ郢晢ｽｻ;
                case FREE_TIME:
                    return FreeTimeUtil.todaySummary(today);
                case SHOULD_GO_NOW:
                    for (long[] e: today){
                        if (e[0] > now){
                            long min = Math.max(0,(e[0]-now)/60000);
                            return (min>=10)
                                ? "髣碑崟螳ｦ郢晢ｽｻ驛｢・ｧ陷ｿ・･繝ｻ・ｿ郢晢ｽｻ繝ｻ・ｦ遶丞｣ｹ繝ｻ驍ｵ・ｺ郢ｧ繝ｻ・ｽ鬘費ｽｸ・ｺ繝ｻ・ｾ驍ｵ・ｺ陝ｶ蜻ｻ・ｽ骰具ｽｸ・ｲ郢ｧ繝ｻ・ｽ・ｽ陷ｻ・ｵ繝ｻ・｣髴域喚ﾂ・ｲ驍ｵ・ｺ郢ｧ繝ｻ・ｽ鬘費ｽｸ・ｺ繝ｻ・ｾ驍ｵ・ｺ陷ｷ・ｶ・つ郢晢ｽｻ
                                : "驍ｵ・ｺ隴擾ｽｴ繝ｻ蜥ｲ・ｸ・ｺ隴擾ｽｴ繝ｻ蟠取弱・・ｺ鬨ｾ蜈ｷ・ｽ・ｺ驍ｵ・ｺ陷会ｽｱ隨ｳ繝ｻ・ｭ繝ｻ・ｽ・ｹ驍ｵ・ｺ陟暮ｯ会ｽｽ閧ｲ・ｸ・ｺ髴域喚關ｽ驍ｵ・ｺ郢晢ｽｻ邵ｲ蝣､・ｸ・ｺ陷ｷ・ｶ・つ郢晢ｽｻ;
                        }
                    }
                    return "髣碑崟・ｰ螟ｧ・ｾ迢暦ｽｸ・ｺ繝ｻ・ｯ髯ｷ繝ｻ・ｽ・ｺ鬨ｾ蜈ｷ・ｽ・ｺ驍ｵ・ｺ繝ｻ・ｮ髯滂ｽ｢郢晢ｽｻ繝ｻ・ｦ遶丞｣ｹ繝ｻ驍ｵ・ｺ繝ｻ・ｪ驍ｵ・ｺ髴域喚關ｽ驍ｵ・ｺ郢晢ｽｻ邵ｲ蝣､・ｸ・ｺ陷ｷ・ｶ・つ郢晢ｽｻ;
            }
        }catch(Exception ignored){}
        return "髫ｲ・ｰ郢晢ｽｻ繝ｻ・ｰ繝ｻ・ｱ驛｢・ｧ髮区ｧｫ蠕宣辧蜍溷ｹｲ邵ｲ蝣､・ｸ・ｺ鬮ｦ・ｪ遶擾ｽｪ驍ｵ・ｺ陝ｶ蜻ｻ・ｽ骰具ｽｸ・ｺ繝ｻ・ｧ驍ｵ・ｺ陷会ｽｱ隨ｳ繝ｻ・ｸ・ｲ郢晢ｽｻ;
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
