package com.android.system.riru.edxp.hooker;

import android.os.StrictMode;

import com.android.system.xposed.XC_MethodHook;
import com.android.system.xposed.XposedHelpers;

public class SliceProviderFix {

    public static final String SYSTEMUI_PACKAGE_NAME = "com.android.systemui";

    public static void hook() {
        XposedHelpers.findAndHookMethod(StrictMode.ThreadPolicy.Builder.class, "build", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                XposedHelpers.callMethod(param.thisObject, "permitAll");
            }
        });
    }

}
