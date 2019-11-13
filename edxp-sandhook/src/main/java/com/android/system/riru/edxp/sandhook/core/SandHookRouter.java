package com.android.system.riru.edxp.sandhook.core;

import com.android.system.riru.edxp.sandhook.config.SandHookEdxpConfig;
import com.android.system.riru.edxp.sandhook.config.SandHookProvider;
import com.android.system.riru.edxp.sandhook.dexmaker.DynamicBridge;
import com.android.system.riru.edxp.sandhook.entry.AppBootstrapHookInfo;
import com.android.system.riru.edxp.sandhook.entry.SysBootstrapHookInfo;
import com.android.system.riru.edxp.sandhook.entry.SysInnerHookInfo;
import com.android.system.riru.edxp.sandhook.entry.WorkAroundHookInfo;
import com.android.system.riru.edxp.sandhook.hooker.SystemMainHooker;
import com.android.system.riru.edxp.util.Utils;
import com.android.system.riru.edxp.config.EdXpConfigGlobal;
import com.android.system.riru.edxp.proxy.BaseRouter;
import com.swift.sandhook.xposedcompat.XposedCompat;

import com.android.system.xposed.XposedBridge;

public class SandHookRouter extends BaseRouter {

    public SandHookRouter() {
        useXposedApi = true;
    }

    private static boolean useSandHook = false;

    public void startBootstrapHook(boolean isSystem) {
        if (useSandHook) {
            Utils.logD("startBootstrapHook starts: isSystem = " + isSystem);
            ClassLoader classLoader = XposedBridge.BOOTCLASSLOADER;
            if (isSystem) {
                XposedCompat.addHookers(classLoader, SysBootstrapHookInfo.hookItems);
            } else {
                XposedCompat.addHookers(classLoader, AppBootstrapHookInfo.hookItems);
            }
        } else {
            super.startBootstrapHook(isSystem);
        }
    }

    public void startSystemServerHook() {
        if (useSandHook) {
            XposedCompat.addHookers(SystemMainHooker.systemServerCL, SysInnerHookInfo.hookItems);
        } else {
            super.startSystemServerHook();
        }
    }

    public void startWorkAroundHook() {
        if (useSandHook) {
            XposedCompat.addHookers(XposedBridge.BOOTCLASSLOADER, WorkAroundHookInfo.hookItems);
        } else {
            super.startWorkAroundHook();
        }
    }

    public void onEnterChildProcess() {
        DynamicBridge.onForkPost();
        //enable compile in child process
        //SandHook.enableCompiler(!XposedInit.startsSystemServer);
    }

    public void injectConfig() {
        EdXpConfigGlobal.sConfig = new SandHookEdxpConfig();
        EdXpConfigGlobal.sHookProvider = new SandHookProvider();
    }

}
