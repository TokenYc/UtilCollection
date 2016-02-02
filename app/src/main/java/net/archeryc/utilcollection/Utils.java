package net.archeryc.utilcollection;

import android.Manifest;
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
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by 24706 on 2016/2/2.
 */
public class Utils {

    /**
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
}
