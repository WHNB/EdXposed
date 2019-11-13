package com.android.system.riru.edxp.whale.core;

import com.android.system.riru.edxp.whale.config.WhaleEdxpConfig;
import com.android.system.riru.edxp.whale.config.WhaleHookProvider;
import com.android.system.riru.edxp.config.EdXpConfigGlobal;
import com.android.system.riru.edxp.framework.Zygote;
import com.android.system.riru.edxp.proxy.BaseRouter;

public class WhaleRouter extends BaseRouter {

    public void onEnterChildProcess() {

    }

    public void injectConfig() {
        BaseRouter.useXposedApi = true;
        EdXpConfigGlobal.sConfig = new WhaleEdxpConfig();
        EdXpConfigGlobal.sHookProvider = new WhaleHookProvider();
        Zygote.allowFileAcrossFork("/system/lib/libwhale.edxp.so");
        Zygote.allowFileAcrossFork("/system/lib64/libwhale.edxp.so");
        Zygote.allowFileAcrossFork("/system/lib/libart.so");
        Zygote.allowFileAcrossFork("/system/lib64/libart.so");
    }

}
