package com.android.system.riru.edxp.entry.yahfa;

import com.android.system.riru.common.KeepMembers;
import com.android.system.riru.edxp._hooker.yahfa.StartBootstrapServicesHooker;

public class SysInnerHookInfo implements KeepMembers {
    public static String[] hookItemNames = {
            StartBootstrapServicesHooker.class.getName()
    };
}
