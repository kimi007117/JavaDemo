-renamesourcefileattribute ProGuard
-printusage unused.txt
-applymapping mapping_previous.txt
-keepattributes SourceFile,LineNumberTable,*Annotation*,Signature,InnerClasses,Exceptions
-optimizationpasses 1
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-dontwarn android.support.v4.**
-dontwarn android.support.v13.**
-dontwarn android.webkit.WebViewClient
-dontwarn com.ganji.android.lib.util.UmengWrapper


-keep class net.sourceforge.pinyin4j.** {*; }
-keep class com.ganji.im.parse.** {*; }

# 热修复
# 保留匿名内部类的$,否则补丁打包会Warn
-keepattributes EnclosingMethod
# 忽略assist到类中的代码
-dontwarn com.ganji.android.DontPreverify

# tencent qq_browser sdk
-keep class com.tencent.smtt.a.** { *;}
-dontwarn com.tencent.smtt.export.external.**
-keep class com.tencent.smtt.export.external.** { *;}
-keep class com.tencent.smtt.export.external.extension.interfaces.** { *;}
-keep class com.tencent.smtt.export.external.extension.proxy.** { *;}
-keep class com.tencent.smtt.export.external.interfaces.** { *;}
-keep class com.tencent.smtt.export.external.proxy.** { *;}
-keep class com.tencent.smtt.sdk.** { *;}
-keep class com.tencent.smtt.sdk.a.** { *;}

-keep class com.tencent.mm.sdk.openapi.WXMediaMessage { *;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
-dontwarn eclipse.local.sdk.Util$ClipboardHelper
-dontwarn eclipse.local.sdk.b

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.view.View


-keep public class com.android.vending.licensing.ILicensingService

-keepattributes JavascriptInterface
-keepattributes *Annotation*

#-keep class com.ganji.android.jsonrpc.GJJsonRpcServer { *; }
#-keep class com.ganji.android.jsonrpc.JsonRpcRouter$GJNativeAPI { *; }
#-keep class com.ganji.android.html5.Storage { *; }

-keep class com.ganji.android.R
-keep class com.ganji.android.R$*{
    <fields>;
    <methods>;
}

-keep class com.ganji.android.comp.html5.jsonrpc.JsonRpcRouter$GJNativeAPI { *; }
-keep class com.ganji.android.comp.html5.LocalStorage { *; }
-keep class * extends com.ganji.android.comp.html5.jsonrpc.BaseJsonRpcServer {
    <fields>;
    <methods>;
}
-keep class com.ganji.android.myinfo.control.PayWebActivity$* {
    <fields>;
    <methods>;
}

-keep class com.ganji.android.control.WebViewActivity$* {
    <fields>;
    <methods>;
}

-keep class com.ganji.android.house.data.AllTrendChartInfo {
    <fields>;
    <methods>;
}
-keep class com.ganji.android.house.control.LargeChartActivity {
    <fields>;
    <methods>;
}
-keep class com.ganji.android.myinfo.control.FeedbackWebActivity$* {
    <fields>;
    <methods>;
}
-keep class com.ganji.android.publish.ui.* {
    <fields>;
    <methods>;
}

-keep class com.autonavi.mapapi.* {
    <fields>;
    <methods>;
}

-keep class * extends android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keep class * extends android.os.Parcelable$Creator {
	*;
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet);
}

-keepclasseswithmembers,allowshrinking class * {
    public <init>(android.content.Context,android.util.AttributeSet,int);
}

-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep names - Native method names. Keep all native class/method names.

-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}

-keep class com.iflytek.**{*;}

-keep class com.umeng.** { *; }
-keep class u.aly.** { *; }

# Keep game center class.
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep public class cn.domob.app.gamecenter.activity.R$*{
    public static final int *;
}

-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.tenddata.** { *; }

-dontwarn dalvik.system.VMRuntime
-keep public class dalvik.system.VMRuntime { public protected *;}

-dontwarn org.apache.thrift.**
-keep public class org.apache.thrift.** { public protected *;}

-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.**

-dontwarn com.google.gson.**
-keep class com.google.gson.** { *; }

##---------------Begin: proguard configuration for Gson ----------
-keep public class com.google.gson.**
-keep public class com.google.gson.** {public private protected *;}

-keep public class com.ganji.android.trade.data.**

##---------------End: proguard configuration for Gson ----------

-dontwarn cn.dm.networktool.util.**
-keep class cn.dm.networktool.util.**

-dontwarn cn.dm.download.**
-keep class cn.dm.download.** { *; }

-dontwarn cn.dm.common.gamecenter.**
-keep class cn.dm.common.gamecenter.** { *; }

-dontwarn com.dxhome.dm.gamecenter.**
-keep class com.dxhome.dm.gamecenter.** { *; }

#baidu tieba
-keep class android.content.** {*;}
-keep class android.support.** {*;}
-keep class com.baidu.** {*;}
-keep class com.slidingmenu.lib.** {*;}
-dontwarn android.content.**
-dontwarn android.support.**
-dontwarn com.baidu.**
-dontwarn com.slidingmenu.lib.**

-keepclassmembers class * extends java.io.Serializable {
    private static final long serialVersionUID;
}

# gatsdk
-keep public class com.ganji.gatsdk.**
-keepclassmembers public class com.ganji.gatsdk.** { *; }


# begin alipay

-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.android.app.pay.IAlixPay{*;}
-keep class com.alipay.android.app.pay.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.pay.IAlixPayCallback{*;}
-keep class com.alipay.android.app.pay.IAlixPayCallback$Stub{*;}
-keep class com.alipay.android.app.script.**{*;}
-keep class com.alipay.android.app.pay.PayTask{*;}
-keep class com.alipay.android.app.pay.PayTask$OnPayListener{*;}
-keep class com.alipay.android.app.pay.CheckAccountTask{*;}
-keep class com.alipay.android.app.pay.CheckAccountTask$OnCheckListener{*;}
-keep class com.alipay.android.app.encrypt.**{*;}

-keep class com.alipay.mobile.command.*
-keep class android.webkit.*
-keep class com.alipay.mobilesecuritysdk.*
-keep class com.alipay.android.app.*
-keep class com.alipay.android.lib.*
-keep class com.alipay.android.mini.*
-keep class com.alipay.html.*
-keep class org.ccil.cowan.tagsoup.*
-keep class com.squareup.picasso.*
-keep class com.ut.*
-keep class com.alipay.test.ui.core.*
-keep class com.alipay.trobot.external.*
-keep class org.rome.android.ipp.*

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


#-ignorewarnings
-dontwarn com.squareup.okhttp.**
# end alipay


# weixin_pay
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage { *;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
# end weixin_pay

# 连连混淆keep规则
-keep class com.yintong.secure.activityproxy.PayIntro$LLJavascriptInterface{*;}
# 连连混淆keep规则

# 数盟混淆规则
-keep class cn.shuzilm.core.Main {
 public *;
}
-keepclasseswithmembernames class cn.shuzilm.core.Main {
 native <methods>;
}
# 数盟混淆规则

-keep public class com.yintong.** {
    <fields>;
    <methods>;
}

-dontwarn com.xiaomi.**
-keepclasseswithmembernames class com.xiaomi.**{*;}
-keep public class * extends com.xiaomi.mipush.sdk.PushMessageReceiver
-keep public class * extends android.text.method.BaseMovementMethod


#兑吧混淆规则
-keep class com.ganji.android.control.CreditActivity$* {
    <fields>;
    <methods>;
}
-dontwarn okio.**
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-keepnames class com.levelup.http.okhttp.** { *; }
-keepnames interface com.levelup.http.okhttp.** { *; }
-keepnames class com.squareup.okhttp.** { *; }
-keepnames interface com.squareup.okhttp.** { *; }
-keep public class android.wbsupport.**{*;}
-keep public class com.wuba.**{*;}

-keep public class com.ganji.android.dexannotation.**

-keep public class com.xxganji.gmacs.**{*;}
-keep public class com.android.gmacs.**{*;}

#平台化
-keep public class * extends android.app.Instrumentation {public *;}#必须
-keep class com.ganji.android.platform.plugin.** {*; }
-keep class com.ganji.android.impl.downloader.** {*; }
#-keep public class com.ganji.android.platform.plugin.annotation.** {*;}#可选
#-keep public class com.ganji.android.platform.plugin.PluginThemeHelper {*;}#可选
#-keep public class com.ganji.android.platform.plugin.localservice.LocalServiceManager {*;}#可选
#-keep public class com.ganji.android.platform.plugin.util.NotificationHelper {*;}#可选
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

#极光混淆规则
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

#==================gson==========================
-dontwarn com.google.**
-keep class com.google.gson.** {*;}

#==================protobuf======================
-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}

-keep class com.baidu.** {*;}
-keep class mapsdkvi.com.** {*;}
-dontwarn com.baidu.**
