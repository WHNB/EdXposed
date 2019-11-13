package com.android.system.riru.edxp.yahfa.core;

import android.os.Build;

import com.android.system.riru.edxp.core.BaseEdxpImpl;
import com.android.system.riru.edxp.core.EdxpImpl;
import com.android.system.riru.edxp.core.Main;
import com.android.system.riru.edxp.core.Proxy;
import com.android.system.riru.edxp.core.Yahfa;
import com.android.system.riru.edxp.core.yahfa.HookMethodResolver;
import com.android.system.riru.edxp.config.ConfigManager;
import com.android.system.riru.edxp.config.InstallerChooser;
import com.android.system.riru.edxp.proxy.BlackWhiteListProxy;
import com.android.system.riru.edxp.proxy.NormalProxy;
import com.android.system.riru.edxp.proxy.Router;

public class YahfaEdxpImpl extends BaseEdxpImpl {

    static {
        final EdxpImpl edxpImpl = new YahfaEdxpImpl();
        if (Main.setEdxpImpl(edxpImpl)) {
            edxpImpl.init();
        }
    }

    @Variant
    @Override
    public int getVariant() {
        return YAHFA;
    }

    @Override
    public void init() {
        Yahfa.init(Build.VERSION.SDK_INT);
        HookMethodResolver.init();
        getRouter().injectConfig();
        InstallerChooser.setInstallerPackageName(ConfigManager.getInstallerPackageName());

        setInitialized();
    }

    @Override
    protected Proxy createBlackWhiteListProxy() {
        return new BlackWhiteListProxy(getRouter());
    }

    @Override
    protected Proxy createNormalProxy() {
        return new NormalProxy(getRouter());
    }

    @Override
    protected Router createRouter() {
        return new YahfaRouter();
    }
}
