package net.archeryc.utilcollection;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类
 * Created by yc on 2016/2/2.
 */
public class SharedPreferencesUtil {
    private final static String NAME = "saveInfo";
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferencesUtil mSharedPreferencesUtil;
    private static SharedPreferences.Editor mEditor;


    private SharedPreferencesUtil(Context context) {
        mSharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    /**
     * 单利模式
     *
     * @param mContext
     * @return
     */
    public static SharedPreferencesUtil getInstance(Context mContext) {
        if (mSharedPreferencesUtil == null) {
            synchronized (SharedPreferencesUtil.class) {
                if (mSharedPreferencesUtil == null) {
                    mSharedPreferencesUtil = new SharedPreferencesUtil(mContext);
                }
            }
        }
        return mSharedPreferencesUtil;
    }

    public void putString(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public void putInt(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public int getInt(String key, int defValue) {
        return mSharedPreferences.getInt(key, defValue);
    }

    public void putBoolean(String key, Boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public Boolean getBoolean(String key, boolean defValue) {
        return mSharedPreferences.getBoolean(key, defValue);
    }

    public void putFloat(String key, Float value) {
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    public float getFloat(String key, Float defValue) {
        return mSharedPreferences.getFloat(key, defValue);
    }

}
