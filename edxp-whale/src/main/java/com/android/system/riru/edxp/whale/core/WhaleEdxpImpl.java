package com.android.system.riru.edxp.whale.core;

import android.os.Build;

import com.android.system.riru.edxp.core.BaseEdxpImpl;
import com.android.system.riru.edxp.core.EdxpImpl;
import com.android.system.riru.edxp.core.Main;
import com.android.system.riru.edxp.core.Yahfa;
import com.android.system.riru.edxp.core.yahfa.HookMethodResolver;
import com.android.system.riru.edxp.config.ConfigManager;
import com.android.system.riru.edxp.config.InstallerChooser;
import com.android.system.riru.edxp.proxy.Router;

public class WhaleEdxpImpl extends BaseEdxpImpl {

    static {
        final EdxpImpl edxpImpl = new WhaleEdxpImpl();
        if (Main.setEdxpImpl(edxpImpl)) {
            edxpImpl.init();
        }
    }

    @Override
    protected Router createRouter() {
        return new WhaleRouter();
    }

    @Override
    public int getVariant() {
        return WHALE;
    }

    @Override
    public void init() {
        Yahfa.init(Build.VERSION.SDK_INT);
        HookMethodResolver.init();
        getRouter().injectConfig();
        InstallerChooser.setInstallerPackageName(ConfigManager.getInstallerPackageName());

        setInitialized();
    }

}
