package com.android.system.riru.edxp.sandhook.core;

import android.os.Build;

import com.android.system.riru.edxp.core.BaseEdxpImpl;
import com.android.system.riru.edxp.core.EdxpImpl;
import com.android.system.riru.edxp.core.Main;
import com.android.system.riru.edxp.core.Yahfa;
import com.android.system.riru.edxp.core.yahfa.HookMethodResolver;
import com.android.system.riru.edxp.config.ConfigManager;
import com.android.system.riru.edxp.config.InstallerChooser;
import com.android.system.riru.edxp.proxy.Router;
import com.swift.sandhook.xposedcompat.methodgen.SandHookXposedBridge;

public class SandHookEdxpImpl extends BaseEdxpImpl {

    static {
        final EdxpImpl edxpImpl = new SandHookEdxpImpl();
        if (Main.setEdxpImpl(edxpImpl)) {
            edxpImpl.init();
        }
    }

    @Override
    protected Router createRouter() {
        return new SandHookRouter();
    }

    @Variant
    @Override
    public int getVariant() {
        return SANDHOOK;
    }

    @Override
    public void init() {
        Yahfa.init(Build.VERSION.SDK_INT);
        HookMethodResolver.init();
        getRouter().injectConfig();
        InstallerChooser.setInstallerPackageName(ConfigManager.getInstallerPackageName());
        SandHookXposedBridge.init();

        setInitialized();
    }
}
