package com.android.system.riru.edxp.proxy;

import android.app.ActivityThread;
import android.content.pm.ApplicationInfo;
import android.content.res.CompatibilityInfo;
import android.text.TextUtils;

import com.android.system.riru.edxp._hooker.impl.HandleBindApp;
import com.android.system.riru.edxp._hooker.impl.LoadedApkCstr;
import com.android.system.riru.edxp._hooker.impl.OneplusWorkaround;
import com.android.system.riru.edxp._hooker.impl.SystemMain;
import com.android.system.riru.edxp._hooker.yahfa.HandleBindAppHooker;
import com.android.system.riru.edxp._hooker.yahfa.LoadedApkConstructorHooker;
import com.android.system.riru.edxp._hooker.yahfa.OnePlusWorkAroundHooker;
import com.android.system.riru.edxp._hooker.yahfa.StartBootstrapServicesHooker;
import com.android.system.riru.edxp._hooker.yahfa.SystemMainHooker;
import com.android.system.riru.edxp.core.yahfa.HookMain;
import com.android.system.riru.edxp.entry.yahfa.AppBootstrapHookInfo;
import com.android.system.riru.edxp.entry.yahfa.SysBootstrapHookInfo;
import com.android.system.riru.edxp.entry.yahfa.WorkAroundHookInfo;
import com.android.system.riru.edxp.util.Utils;
import com.android.system.riru.edxp._hooker.impl.StartBootstrapServices;
import com.android.system.riru.edxp.entry.yahfa.SysInnerHookInfo;

import java.util.concurrent.atomic.AtomicBoolean;

import com.android.system.xposed.XposedBridge;
import com.android.system.xposed.XposedHelpers;
import com.android.system.xposed.XposedInit;

public abstract class BaseRouter implements Router {

    protected volatile boolean forkCompleted = false;

    protected volatile AtomicBoolean bootstrapHooked = new AtomicBoolean(false);

    protected static boolean useXposedApi = false;

    public void initResourcesHook() {
        startWorkAroundHook(); // for OnePlus devices
        XposedBridge.initXResources();
    }

    public void prepare(boolean isSystem) {
        // this flag is needed when loadModules
        XposedInit.startsSystemServer = isSystem;
    }

    public void onForkStart() {
        forkCompleted = false;
    }

    public void onForkFinish() {
        forkCompleted = true;
    }

    public boolean isForkCompleted() {
        return forkCompleted;
    }

    public void installBootstrapHooks(boolean isSystem) {
        // Initialize the Xposed framework
        try {
            if (!bootstrapHooked.compareAndSet(false, true)) {
                return;
            }
            startBootstrapHook(isSystem);
            XposedInit.initForZygote(isSystem);
        } catch (Throwable t) {
            Utils.logE("error during Xposed initialization", t);
            XposedBridge.disableHooks = true;
        }
    }

    public void loadModulesSafely(boolean isInZygote, boolean callInitZygote) {
        try {
            // FIXME some coredomain app can't reading modules.list
            XposedInit.loadModules(isInZygote, callInitZygote);
        } catch (Exception exception) {
            Utils.logE("error loading module list", exception);
        }
    }

    public String parsePackageName(String appDataDir) {
        if (TextUtils.isEmpty(appDataDir)) {
            return "";
        }
        int lastIndex = appDataDir.lastIndexOf("/");
        if (lastIndex < 1) {
            return "";
        }
        return appDataDir.substring(lastIndex + 1);
    }


    public void startBootstrapHook(boolean isSystem) {
        Utils.logD("startBootstrapHook starts: isSystem = " + isSystem);
        ClassLoader classLoader = BaseRouter.class.getClassLoader();
        if (useXposedApi) {
            if (isSystem) {
                XposedHelpers.findAndHookMethod(SystemMainHooker.className, classLoader,
                        SystemMainHooker.methodName, new SystemMain());
            }
            XposedHelpers.findAndHookMethod(HandleBindAppHooker.className, classLoader,
                    HandleBindAppHooker.methodName,
                    "android.app.ActivityThread$AppBindData",
                    new HandleBindApp());
            XposedHelpers.findAndHookConstructor(LoadedApkConstructorHooker.className, classLoader,
                    ActivityThread.class, ApplicationInfo.class, CompatibilityInfo.class,
                    ClassLoader.class, boolean.class, boolean.class, boolean.class,
                    new LoadedApkCstr());
        } else {
            if (isSystem) {
                HookMain.doHookDefault(
                        BaseRouter.class.getClassLoader(),
                        classLoader,
                        SysBootstrapHookInfo.class.getName());
            } else {
                HookMain.doHookDefault(
                        BaseRouter.class.getClassLoader(),
                        classLoader,
                        AppBootstrapHookInfo.class.getName());
            }
        }
    }

    public void startSystemServerHook() {
        ClassLoader classLoader = BaseRouter.class.getClassLoader();
        if (useXposedApi) {
            XposedHelpers.findAndHookMethod(StartBootstrapServicesHooker.className,
                    SystemMain.systemServerCL,
                    StartBootstrapServicesHooker.methodName, new StartBootstrapServices());
        } else {
            HookMain.doHookDefault(
                    classLoader,
                    SystemMain.systemServerCL,
                    SysInnerHookInfo.class.getName());
        }
    }

    public void startWorkAroundHook() {
        ClassLoader classLoader = BaseRouter.class.getClassLoader();
        if (useXposedApi) {
            try {
                XposedHelpers.findAndHookMethod(OnePlusWorkAroundHooker.className,
                        classLoader, OnePlusWorkAroundHooker.methodName,
                        int.class, String.class, new OneplusWorkaround());
            } catch (Throwable throwable) {
            }
        } else {
            HookMain.doHookDefault(
                    BaseRouter.class.getClassLoader(),
                    classLoader,
                    WorkAroundHookInfo.class.getName());
        }
    }
}
