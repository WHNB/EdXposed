package com.android.system.riru.edxp._hooker.yahfa;

import com.android.system.riru.edxp.core.yahfa.HookMain;
import com.android.system.riru.common.KeepMembers;
import com.android.system.riru.edxp._hooker.impl.OneplusWorkaround;

import com.android.system.xposed.XC_MethodHook;

public class OnePlusWorkAroundHooker implements KeepMembers {

    static {
        HookMain.addHookItemWhiteList(OnePlusWorkAroundHooker.class.getName());
    }

    public static String className = "dalvik.system.BaseDexClassLoader";
    public static String methodName = "inCompatConfigList";
    public static String methodSig = "(ILjava/lang/String;)Z";

    public static boolean hook(int type, String packageName) throws Throwable {
        final XC_MethodHook methodHook = new OneplusWorkaround();
        final XC_MethodHook.MethodHookParam param = new XC_MethodHook.MethodHookParam();
        param.thisObject = null;
        param.args = new Object[]{type, packageName};
        methodHook.callBeforeHookedMethod(param);
        if (!param.returnEarly) {
            param.setResult(backup(type, packageName));
        }
        methodHook.callAfterHookedMethod(param);
        return (boolean) param.getResult();
    }

    public static boolean backup(int type, String packageName) {
        return false;
    }
}