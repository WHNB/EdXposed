package com.android.system.riru.edxp._hooker.impl;

import android.os.Build;

import com.android.system.riru.edxp.util.Hookers;
import com.android.system.riru.edxp.util.Utils;

import com.android.system.xposed.XC_MethodHook;
import com.android.system.xposed.XC_MethodReplacement;
import com.android.system.xposed.XposedBridge;
import com.android.system.xposed.XposedHelpers;
import com.android.system.xposed.XposedInit;
import com.android.system.xposed.callbacks.XC_LoadPackage;

import static com.android.system.riru.edxp.util.Utils.logD;
import static com.android.system.xposed.XposedHelpers.findAndHookMethod;

public class StartBootstrapServices extends XC_MethodHook {

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        if (XposedBridge.disableHooks) {
            return;
        }

        Utils.logD("SystemServer#startBootstrapServices() starts");

        try {
            XposedInit.loadedPackagesInProcess.add("android");

            XC_LoadPackage.LoadPackageParam lpparam = new XC_LoadPackage.LoadPackageParam(XposedBridge.sLoadedPackageCallbacks);
            lpparam.packageName = "android";
            lpparam.processName = "android"; // it's actually system_server, but other functions return this as well
            lpparam.classLoader = SystemMain.systemServerCL;
            lpparam.appInfo = null;
            lpparam.isFirstApplication = true;
            XC_LoadPackage.callAll(lpparam);

            // Huawei
            try {
                findAndHookMethod("com.android.server.pm.HwPackageManagerService",
                        SystemMain.systemServerCL, "isOdexMode",
                        XC_MethodReplacement.returnConstant(false));
            } catch (XposedHelpers.ClassNotFoundError | NoSuchMethodError ignored) {
            }

            try {
                String className = "com.android.server.pm." + (Build.VERSION.SDK_INT >= 23 ? "PackageDexOptimizer" : "PackageManagerService");
                findAndHookMethod(className, SystemMain.systemServerCL,
                        "dexEntryExists", String.class,
                        XC_MethodReplacement.returnConstant(true));
            } catch (XposedHelpers.ClassNotFoundError | NoSuchMethodError ignored) {
            }
        } catch (Throwable t) {
            Hookers.logE("error when hooking startBootstrapServices", t);
        }
    }
}
