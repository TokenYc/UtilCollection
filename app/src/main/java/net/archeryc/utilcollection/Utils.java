package net.archeryc.utilcollection;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;

/**
 * Created by 24706 on 2016/2/2.
 */
public class Utils {

    /**检查权限并请求权限
     * 身体传感器
     * 日历
     * 摄像头
     * 通讯录
     * 地理位置
     * 麦克风
     * 电话
     * 短信
     * 存储空间
     * 这些权限在6.0以上系统，不仅要在manifest中配置，还要动态申请。
     *
     * @param activity
     * @param permission 如:Manifest.permission.WRITE_EXTERNAL_STORAGE
     * @param code       小于8位
     *                   在activity中要写onRequestPermissionsResult方法处理结果
     */
    public static void checkPermission(Activity activity, String permission, int code) {
        if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED) {
            //申请对应权限
            ActivityCompat.requestPermissions(activity, new String[]{permission},
                    code);
        }
    }


    /**
     * 拨打电话
     * <uses-permission android:name="android.permission.CALL_PHONE" />
     *
     * @param context
     * @param phoneNumber
     */
    public static void call(Context context, String phoneNumber) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)));
    }


    /**
     * 跳转到拨号界面
     *
     * @param context
     * @param phoneNumber
     */
    public static void callDial(Context context, String phoneNumber) {

        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));

    }


    /**
     * <uses-permission android:name="android.permission.SEND_SMS"/>
     * 发送短信
     *
     * @param context
     * @param phoneNumber
     * @param content
     */
    public static void sendSms(Context context, String phoneNumber,

                               String content) {

        Uri uri = Uri.parse("smsto:"

                + (TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber));

        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);

        intent.putExtra("sms_body", TextUtils.isEmpty(content) ? "" : content);

        context.startActivity(intent);

    }


    /**
     * 唤醒并解锁
     *
     * @param context 需要权限
     *                <uses-permission android:name="android.permission.WAKE_LOCK" />
     *                <uses-permission android:name="android.permission.DISABLE_KEYGUARD" />
     */
    public static void wakeUpAndUnlock(Context context) {

        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }


    /**
     * 判断应用在前台还是后台
     *
     * @param context
     * @return
     */
    public static boolean isApplicationBackground(final Context context) {

        ActivityManager am = (ActivityManager) context

                .getSystemService(Context.ACTIVITY_SERVICE);

        @SuppressWarnings("deprecation")

        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);

        if (!tasks.isEmpty()) {

            ComponentName topActivity = tasks.get(0).topActivity;

            if (!topActivity.getPackageName().equals(context.getPackageName())) {

                return true;

            }

        }
        return false;
    }


    /**
     * 判断手机是否处于锁屏（睡眠）状态
     *
     * @param context
     * @return
     */
    public static boolean isSleeping(Context context) {

        KeyguardManager kgMgr = (KeyguardManager) context

                .getSystemService(Context.KEYGUARD_SERVICE);

        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();

        return isSleeping;
    }


    /**
     * 判断当前是否联网
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context

                .getSystemService(Activity.CONNECTIVITY_SERVICE);

        NetworkInfo info = manager.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {

            return true;

        }

        return false;

    }


    /**
     * 判断当前是否为wifi连接
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context

                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetworkInfo = connectivityManager

                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifiNetworkInfo.isConnected()) {

            return true;

        }

        return false;

    }

    /**
     * 判断当前设备是否为手机
     *
     * @param context
     * @return
     */
    public static boolean isPhone(Context context) {

        TelephonyManager telephony = (TelephonyManager) context

                .getSystemService(Context.TELEPHONY_SERVICE);

        if (telephony.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE) {

            return false;

        } else {

            return true;

        }
    }


    /**
     * 获取手机IMEI码，需要动态获取权限。
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)

    public static String getDeviceIMEI(Context context) {

        String deviceId;

        if (isPhone(context)) {

            TelephonyManager telephony = (TelephonyManager) context

                    .getSystemService(Context.TELEPHONY_SERVICE);

            deviceId = telephony.getDeviceId();

        } else {

            deviceId = Settings.Secure.getString(context.getContentResolver(),

                    Settings.Secure.ANDROID_ID);


        }

        return deviceId;

    }

    /**
     * 获取Mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {

        String macAddress;

        WifiManager wifi = (WifiManager) context

                .getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifi.getConnectionInfo();

        macAddress = info.getMacAddress();

        if (null == macAddress) {

            return "";

        }

        macAddress = macAddress.replace(":", "");

        return macAddress;

    }


    /**
     * 获取应用当前版本号
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {

        String version = "0";

        try {

            version = context.getPackageManager().getPackageInfo(

                    context.getPackageName(), 0).versionName;

        } catch (PackageManager.NameNotFoundException e) {

            e.printStackTrace();

        }

        return version;

    }


    //判断是否有SD卡
    public static boolean haveSDCard() {

        return android.os.Environment.getExternalStorageState().equals(

                android.os.Environment.MEDIA_MOUNTED);

    }


    /**
     * 动态隐藏软键盘
     *
     * @param activity
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)

    public static void hideSoftInput(Activity activity) {

        View view = activity.getWindow().peekDecorView();

        if (view != null) {

            InputMethodManager inputmanger = (InputMethodManager) activity

                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }

    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)

    public static void hideSoftInput(Context context, EditText edit) {

        edit.clearFocus();

        InputMethodManager inputmanger = (InputMethodManager) context

                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputmanger.hideSoftInputFromWindow(edit.getWindowToken(), 0);

    }


    /**
     * 动态显示软键盘
     *
     * @param context
     * @param edit
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)

    public static void showSoftInput(Context context, EditText edit) {

        edit.setFocusable(true);

        edit.setFocusableInTouchMode(true);

        edit.requestFocus();

        InputMethodManager inputManager = (InputMethodManager) context

                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.showSoftInput(edit, 0);

    }

    /**
     * 主动回到home
     *
     * @param context
     */
    public static void goHome(Context context) {

        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);

        mHomeIntent.addCategory(Intent.CATEGORY_HOME);

        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK

                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        context.startActivity(mHomeIntent);

    }


    /**
     * 判断手机连接的网络类型(2G,3G,4G)
     * 联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
     */
    public class Constants {

        /**
         * Unknown network class
         */

        public static final int NETWORK_CLASS_UNKNOWN = 0;


        /**
         * wifi net work
         */

        public static final int NETWORK_WIFI = 1;


        /**
         * "2G" networks
         */

        public static final int NETWORK_CLASS_2_G = 2;


        /**
         * "3G" networks
         */

        public static final int NETWORK_CLASS_3_G = 3;


        /**
         * "4G" networks
         */

        public static final int NETWORK_CLASS_4_G = 4;


    }


    public static int getNetWorkClass(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context

                .getSystemService(Context.TELEPHONY_SERVICE);


        switch (telephonyManager.getNetworkType()) {

            case TelephonyManager.NETWORK_TYPE_GPRS:

            case TelephonyManager.NETWORK_TYPE_EDGE:

            case TelephonyManager.NETWORK_TYPE_CDMA:

            case TelephonyManager.NETWORK_TYPE_1xRTT:

            case TelephonyManager.NETWORK_TYPE_IDEN:

                return Constants.NETWORK_CLASS_2_G;


            case TelephonyManager.NETWORK_TYPE_UMTS:

            case TelephonyManager.NETWORK_TYPE_EVDO_0:

            case TelephonyManager.NETWORK_TYPE_EVDO_A:

            case TelephonyManager.NETWORK_TYPE_HSDPA:

            case TelephonyManager.NETWORK_TYPE_HSUPA:

            case TelephonyManager.NETWORK_TYPE_HSPA:

            case TelephonyManager.NETWORK_TYPE_EVDO_B:

            case TelephonyManager.NETWORK_TYPE_EHRPD:

            case TelephonyManager.NETWORK_TYPE_HSPAP:

                return Constants.NETWORK_CLASS_3_G;


            case TelephonyManager.NETWORK_TYPE_LTE:

                return Constants.NETWORK_CLASS_4_G;


            default:

                return Constants.NETWORK_CLASS_UNKNOWN;

        }
    }

    /**
     * 判断当前手机的网络类型(WIFI还是2,3,4G)
     *
     * @param context
     * @return
     */
    public static int getNetWorkStatus(Context context) {

        int netWorkType = Constants.NETWORK_CLASS_UNKNOWN;


        ConnectivityManager connectivityManager = (ConnectivityManager) context

                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {

            int type = networkInfo.getType();


            if (type == ConnectivityManager.TYPE_WIFI) {

                netWorkType = Constants.NETWORK_WIFI;

            } else if (type == ConnectivityManager.TYPE_MOBILE) {

                netWorkType = getNetWorkClass(context);

            }

        }


        return netWorkType;

    }

    /**
     * px-dp转换
     *
     * @param context
     * @param dpValue
     * @return
     */


    public static int dip2px(Context context, float dpValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (dpValue * scale + 0.5f);

    }


    public static int px2dip(Context context, float pxValue) {

        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (pxValue / scale + 0.5f);

    }


    /**
     * px-sp转换
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {

        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

        return (int) (pxValue / fontScale + 0.5f);

    }


    public static int sp2px(Context context, float spValue) {

        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

        return (int) (spValue * fontScale + 0.5f);

    }


    /**
     * 把一个毫秒数转化成时间字符串
     *
     * @param millis   要转化的毫秒数。
     * @param isWhole  是否强制全部显示小时/分/秒/毫秒。
     * @param isFormat 时间数字是否要格式化，如果true：少位数前面补全；如果false：少位数前面不补全。
     * @return 返回时间字符串：小时/分/秒/毫秒的格式（如：24903600 --> 06小时55分03秒600毫秒）。
     */

    public static String millisToString(long millis, boolean isWhole,

                                        boolean isFormat) {

        String h = "";

        String m = "";

        String s = "";

        String mi = "";

        if (isWhole) {

            h = isFormat ? "00小时" : "0小时";

            m = isFormat ? "00分" : "0分";

            s = isFormat ? "00秒" : "0秒";

            mi = isFormat ? "00毫秒" : "0毫秒";

        }


        long temp = millis;


        long hper = 60 * 60 * 1000;

        long mper = 60 * 1000;

        long sper = 1000;


        if (temp / hper > 0) {

            if (isFormat) {

                h = temp / hper < 10 ? "0" + temp / hper : temp / hper + "";

            } else {

                h = temp / hper + "";

            }

            h += "小时";

        }

        temp = temp % hper;


        if (temp / mper > 0) {

            if (isFormat) {

                m = temp / mper < 10 ? "0" + temp / mper : temp / mper + "";

            } else {

                m = temp / mper + "";

            }

            m += "分";

        }

        temp = temp % mper;


        if (temp / sper > 0) {

            if (isFormat) {

                s = temp / sper < 10 ? "0" + temp / sper : temp / sper + "";

            } else {

                s = temp / sper + "";

            }

            s += "秒";

        }

        temp = temp % sper;

        mi = temp + "";


        if (isFormat) {

            if (temp < 100 && temp >= 10) {

                mi = "0" + temp;

            }

            if (temp < 10) {

                mi = "00" + temp;

            }

        }


        mi += "毫秒";

        return h + m + s + mi;

    }
}
