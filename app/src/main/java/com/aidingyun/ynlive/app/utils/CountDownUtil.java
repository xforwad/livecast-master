package com.aidingyun.ynlive.app.utils;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 倒计时工具
 * Created by weijing on 2017-08-22 11:50.
 */
public class CountDownUtil {
    /**
 * 开始倒计时code
 */
private final int MSG_WHAT_START = 10_010; /**
 * 弱引用
 */
private WeakReference<TextView> mWeakReference; /**
 * 倒计时时间
 */
private long mCountDownMillis = 60_000; /**
 * 提示文字
 */
private String mHintText = "重新发送"; /**
 * 剩余倒计时时间
 */
private long mLastMillis; /**
 * 间隔时间差(两次发送handler)
 */
private long mIntervalMillis = 1_000; /**
 * 可用状态下字体颜色Id
 */
private int usableColorId = android.R.color.holo_blue_light; /**
 * 不可用状态下字体颜色Id
 */
private int unusableColorId = android.R.color.darker_gray;
private Handler mHandler = new Handler() {
    @Override public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case MSG_WHAT_START:
                if (mLastMillis > 0) {
                    setUsable(false);
                    mLastMillis -= mIntervalMillis;
                    if (mWeakReference.get() != null) {
                        mHandler.sendEmptyMessageDelayed(MSG_WHAT_START, mIntervalMillis);
                    }
                } else {
                    setUsable(true);
                }
                break;
        }
    }
};
public CountDownUtil(TextView textView) {
    mWeakReference = new WeakReference<>(textView);
} public CountDownUtil(TextView textView, long countDownMillis) {
    mWeakReference = new WeakReference<>(textView);
    this.mCountDownMillis = countDownMillis; }
    public CountDownUtil setCountDownMillis(long countDownMillis) {
    this.mCountDownMillis = countDownMillis;
    return this; }
    /**
 * 设置是否可用
 *
 * @param usable
 */
    private void setUsable(boolean usable) {
        TextView mTextView = mWeakReference.get();
        if (mTextView != null) { if (usable) { //可用
            if (!mTextView.isClickable()) {
                mTextView.setClickable(usable);
                mTextView.setTextColor(mTextView.getResources().getColor(usableColorId));
                mTextView.setText(mHintText); } } else { //不可用
             if (mTextView.isClickable()) {
                 mTextView.setClickable(usable);
                 mTextView.setTextColor(mTextView.getResources().getColor(unusableColorId));
             }
             String content = mLastMillis / 1000 + "秒后" + mHintText;
             mTextView.setText(content);
        }
        }
    }
    /**
     * 设置倒计时颜色
            *
     * @param usableColorId   可用状态下的颜色
     * @param unusableColorId 不可用状态下的颜色
     */
    public CountDownUtil setCountDownColor(@ColorRes int usableColorId, @ColorRes int unusableColorId) {
        this.usableColorId = usableColorId;
        this.unusableColorId = unusableColorId;
        return this; }
        /**
     * 开始倒计时
     */
        public CountDownUtil start() {
            mLastMillis = mCountDownMillis;
            mHandler.sendEmptyMessage(MSG_WHAT_START);
            return this; } public CountDownUtil
    setOnClickListener(@Nullable final View.OnClickListener onClickListener) {
            TextView mTextView = mWeakReference.get();
            if (mTextView != null)
                mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mHandler.removeMessages(MSG_WHAT_START);
                        start();
                        onClickListener.onClick(v);
                    }
                });
            return this;
        }
        /**
     * 重置停止倒计时
     */
        public CountDownUtil reset() {
            mLastMillis = 0;
            mHandler.sendEmptyMessage(MSG_WHAT_START);
            return this;
        }


    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long timeMillis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

}