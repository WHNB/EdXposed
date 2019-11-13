package com.android.system.riru.edxp.sandhook.entry;

import com.android.system.riru.edxp.sandhook.hooker.HandleBindAppHooker;
import com.android.system.riru.edxp.sandhook.hooker.OnePlusWorkAroundHooker;
import com.android.system.riru.common.KeepMembers;
import com.android.system.riru.edxp.sandhook.hooker.LoadedApkConstructorHooker;
import com.android.system.riru.edxp.sandhook.hooker.SystemMainHooker;

public class SysBootstrapHookInfo implements KeepMembers {
    public static String[] hookItemNames = {
            OnePlusWorkAroundHooker.class.getName(),
            HandleBindAppHooker.class.getName(),
            SystemMainHooker.class.getName(),
            LoadedApkConstructorHooker.class.getName()
    };

    public static Class[] hookItems = {
            HandleBindAppHooker.class,
            SystemMainHooker.class,
            LoadedApkConstructorHooker.class,
            OnePlusWorkAroundHooker.class
    };
}
