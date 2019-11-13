package com.android.system.riru.edxp._hooker.impl;

import com.android.system.riru.edxp.core.Main;
import com.android.system.riru.edxp.deopt.PrebuiltMethodsDeopter;
import com.android.system.riru.edxp.util.Hookers;

import com.android.system.xposed.XC_MethodHook;
import com.android.system.xposed.XposedBridge;

// system_server initialization
// ed: only support sdk >= 21 for now
public class SystemMain extends XC_MethodHook {

    public static volatile ClassLoader systemServerCL;

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        if (XposedBridge.disableHooks) {
            return;
        }
        Hookers.logD("ActivityThread#systemMain() starts");
        try {
            // get system_server classLoader
            systemServerCL = Thread.currentThread().getContextClassLoader();
            // deopt methods in SYSTEMSERVERCLASSPATH
            PrebuiltMethodsDeopter.deoptSystemServerMethods(systemServerCL);
            Main.getEdxpImpl().getRouter().startSystemServerHook();
        } catch (Throwable t) {
            Hookers.logE("error when hooking systemMain", t);
        }
    }
}
