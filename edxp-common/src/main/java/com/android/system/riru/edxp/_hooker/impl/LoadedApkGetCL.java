package com.android.system.riru.edxp._hooker.impl;

import android.app.LoadedApk;

import com.android.system.riru.edxp.util.Hookers;
import com.android.system.riru.edxp.hooker.SliceProviderFix;
import com.android.system.riru.edxp.hooker.XposedBlackListHooker;
import com.android.system.riru.edxp.hooker.XposedInstallerHooker;

import com.android.system.xposed.XC_MethodHook;
import com.android.system.xposed.XposedBridge;
import com.android.system.xposed.XposedHelpers;
import com.android.system.xposed.callbacks.XC_LoadPackage;

import static com.android.system.riru.edxp.config.InstallerChooser.INSTALLER_PACKAGE_NAME;
import static com.android.system.riru.edxp.hooker.SliceProviderFix.SYSTEMUI_PACKAGE_NAME;
import static com.android.system.riru.edxp.hooker.XposedBlackListHooker.BLACK_LIST_PACKAGE_NAME;


public class LoadedApkGetCL extends XC_MethodHook {

    private final LoadedApk loadedApk;
    private final String packageName;
    private final String processName;
    private final boolean isFirstApplication;
    private Unhook unhook;

    public LoadedApkGetCL(LoadedApk loadedApk, String packageName, String processName,
                          boolean isFirstApplication) {
        this.loadedApk = loadedApk;
        this.packageName = packageName;
        this.processName = processName;
        this.isFirstApplication = isFirstApplication;
    }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {

        try {

            if (XposedBlackListHooker.shouldDisableHooks("")) {
                return;
            }

            Hookers.logD("LoadedApk#getClassLoader starts");

            LoadedApk loadedApk = (LoadedApk) param.thisObject;

            if (loadedApk != this.loadedApk) {
                return;
            }

            Object mAppDir = XposedHelpers.getObjectField(loadedApk, "mAppDir");
            ClassLoader classLoader = (ClassLoader) param.getResult();
            Hookers.logD("LoadedApk#getClassLoader ends: " + mAppDir + " -> " + classLoader);

            if (classLoader == null) {
                return;
            }

            XC_LoadPackage.LoadPackageParam lpparam = new XC_LoadPackage.LoadPackageParam(
                    XposedBridge.sLoadedPackageCallbacks);
            lpparam.packageName = this.packageName;
            lpparam.processName = this.processName;
            lpparam.classLoader = classLoader;
            lpparam.appInfo = loadedApk.getApplicationInfo();
            lpparam.isFirstApplication = this.isFirstApplication;
            XC_LoadPackage.callAll(lpparam);

            if (this.packageName.equals(INSTALLER_PACKAGE_NAME)) {
                XposedInstallerHooker.hookXposedInstaller(lpparam.classLoader);
            }
            if (this.packageName.equals(BLACK_LIST_PACKAGE_NAME)) {
                XposedBlackListHooker.hook(lpparam.classLoader);
            }
            if (this.packageName.equals(SYSTEMUI_PACKAGE_NAME)) {
                SliceProviderFix.hook();
            }

        } catch (Throwable t) {
            Hookers.logE("error when hooking LoadedApk#getClassLoader", t);
        } finally {
            if (unhook != null) {
                unhook.unhook();
            }
        }
    }

    public void setUnhook(Unhook unhook) {
        this.unhook = unhook;
    }

    public Unhook getUnhook() {
        return unhook;
    }
}
