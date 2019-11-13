package com.android.system.riru.edxp.proxy;

import com.android.system.riru.edxp.core.Proxy;

import com.android.system.xposed.XposedBridge;

public abstract class BaseProxy implements Proxy {

    protected Router mRouter;

    public BaseProxy(Router router) {
        mRouter = router;
    }

    @Override
    public boolean init() {
        return true;
    }

    public static void onBlackListed() {
        XposedBridge.clearAllCallbacks();
    }
}
