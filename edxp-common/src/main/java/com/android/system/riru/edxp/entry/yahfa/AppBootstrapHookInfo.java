package com.android.system.riru.edxp.entry.yahfa;

import com.android.system.riru.common.KeepMembers;
import com.android.system.riru.edxp._hooker.yahfa.HandleBindAppHooker;
import com.android.system.riru.edxp._hooker.yahfa.LoadedApkConstructorHooker;
import com.android.system.riru.edxp._hooker.yahfa.OnePlusWorkAroundHooker;

public class AppBootstrapHookInfo implements KeepMembers {
    public static String[] hookItemNames = {
            HandleBindAppHooker.class.getName(),
            LoadedApkConstructorHooker.class.getName(),
            OnePlusWorkAroundHooker.class.getName()
    };
}
