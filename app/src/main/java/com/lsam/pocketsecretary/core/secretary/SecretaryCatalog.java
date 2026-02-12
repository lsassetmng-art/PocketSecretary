package com.lsam.pocketsecretary.core.secretary;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import com.lsam.pocketsecretary.R;

public final class SecretaryCatalog {

    public static List<Secretary> list(Context context) {
        List<Secretary> list = new ArrayList<>();

        list.add(new Secretary("hiyori",
                context.getString(R.string.sec_hiyori_name)));

        list.add(new Secretary("aoi",
                context.getString(R.string.sec_aoi_name)));

        list.add(new Secretary("ren",
                context.getString(R.string.sec_ren_name)));

        return list;
    }
}
