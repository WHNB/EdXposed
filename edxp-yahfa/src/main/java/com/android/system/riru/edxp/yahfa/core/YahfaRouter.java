package com.android.system.riru.edxp.yahfa.core;

import com.android.system.riru.edxp.config.EdXpConfigGlobal;
import com.android.system.riru.edxp.proxy.BaseRouter;
import com.android.system.riru.edxp.yahfa.config.YahfaEdxpConfig;
import com.android.system.riru.edxp.yahfa.config.YahfaHookProvider;
import com.android.system.riru.edxp.yahfa.dexmaker.DynamicBridge;

public class YahfaRouter extends BaseRouter {

    public void onEnterChildProcess() {
        DynamicBridge.onForkPost();
    }

    public void injectConfig() {
        EdXpConfigGlobal.sConfig = new YahfaEdxpConfig();
        EdXpConfigGlobal.sHookProvider = new YahfaHookProvider();
    }

}
