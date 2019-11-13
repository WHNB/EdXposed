package com.android.system.riru.edxp.sandhook.entry;

import com.android.system.riru.edxp.sandhook.hooker.OnePlusWorkAroundHooker;
import com.android.system.riru.common.KeepMembers;

public class WorkAroundHookInfo implements KeepMembers {
    public static String[] hookItemNames = {
            OnePlusWorkAroundHooker.class.getName()
    };

    public static Class[] hookItems = {
            OnePlusWorkAroundHooker.class
    };
}
