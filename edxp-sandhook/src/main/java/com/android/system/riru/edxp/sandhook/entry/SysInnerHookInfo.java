package com.android.system.riru.edxp.sandhook.entry;

import com.android.system.riru.common.KeepMembers;
import com.android.system.riru.edxp.sandhook.hooker.StartBootstrapServicesHooker;

public class SysInnerHookInfo implements KeepMembers {
    public static String[] hookItemNames = {
            StartBootstrapServicesHooker.class.getName()
    };

    public static Class[] hookItems = {
            StartBootstrapServicesHooker.class
    };
}
