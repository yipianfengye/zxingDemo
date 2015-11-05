package com.mingzebj.bmpcn.qr.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class Config {
    public static ProgressDialog progress = null;
    private static Dialog loadingDialog = null;


    public static void showToast(Context context, String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showProgressDialog(Context context, boolean canCancel, final ProgressCancelListener listener) {
        dismissProgress();
        progress = new ProgressDialog(context);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("请稍候...");
        progress.setCancelable(canCancel);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
        progress.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                if (listener != null)
                {
                    listener.progressCancel();
                }
            }
        });
    }

    public static void showProgressTextDialog(Context context, String text) {
        dismissProgress();
        progress = new ProgressDialog(context);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage(text);
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();
    }


    public static interface ProgressCancelListener {
        public void progressCancel();
    }

    public static void dismissProgress() {
        try {
            if (progress != null  && progress.isShowing() ) {
                progress.dismiss();
            }
        } catch (Exception e) {
        }

        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }


    private static boolean timeScrolled = false;
    private static boolean timeChanged = false;


    public static interface TimePickResult {


        boolean hook();

        void timePickResult(long time);
    }

//    public static void showDaySecondPickDialog(final Activity context, long currenttime, final long startTime, final long oldTime, final int availableDay, final TimePickResult timePickResult) {
//        if (context == null) {
//            return;
//        }
//        boolean hookBoolean = false;
//        if (timePickResult != null) {
//            hookBoolean = timePickResult.hook();
//        }
//        if (!hookBoolean) {
//            return;
//        }
//        Date oldDate = new Date(oldTime);
//        Date date = new Date(startTime);
//        final Calendar calendar = Calendar.getInstance();
//        final Calendar oldCalendar = Calendar.getInstance();
//        calendar.setTime(date);
//        oldCalendar.setTime(oldDate);
//        int minsInt = calendar.get(Calendar.MINUTE);
//        int yu = minsInt % 15;
//        if (yu != 0) {
//            calendar.add(Calendar.MINUTE, 15 - yu);
//        }
//        calendar.set(Calendar.SECOND, 0);
//        int oldMinsInt = oldCalendar.get(Calendar.MINUTE);
//        int oldYu = oldMinsInt % 15;
//        if (oldYu != 0) {
//            oldCalendar.add(Calendar.MINUTE, 15 - oldYu);
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View view = context.getLayoutInflater().inflate(R.layout.daytime_pick_layout, null);
//        final TextView timeTextView = (TextView) view.findViewById(R.id.date_textview);
////        Calendar calendar = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
//        timeTextView.setText(TimeUtils.formatTimeWithHHMM(calendar));
//        final WheelView days = (WheelView) view.findViewById(R.id.day);
//        final Calendar nowCalendar = Calendar.getInstance();
//        final Calendar dayCalendar = Calendar.getInstance();
//        final Calendar nowCalendarClone = Calendar.getInstance();
//        final Date nowDate = new Date(currenttime);
//        dayCalendar.setTime(nowDate);
//        nowCalendar.setTime(nowDate);
//        nowCalendarClone.setTime(nowDate);
//        int rollDay = 0;
//        while (nowCalendar.before(calendar)) {
//            int nowDay = nowCalendar.get(Calendar.DATE);
//            int day = calendar.get(Calendar.DATE);
//            if (day == nowDay) {
//                int nowMonth = nowCalendar.get(Calendar.MONTH);
//                int month = calendar.get(Calendar.MONTH);
//                if (nowMonth == month) {
//                    break;
//                }
//            }
//            nowCalendar.add(Calendar.DATE, 1);
//            rollDay++;
//        }
//
//        int avialDay = 0;
//        while (nowCalendarClone.before(oldCalendar)) {
//            int nowDay = nowCalendarClone.get(Calendar.DATE);
//            int day = oldCalendar.get(Calendar.DATE);
//            if (day == nowDay) {
//                int nowMonth = nowCalendarClone.get(Calendar.MONTH);
//                int month = oldCalendar.get(Calendar.MONTH);
//                if (nowMonth == month) {
//                    break;
//                }
//            }
//            nowCalendarClone.add(Calendar.DATE, 1);
//            avialDay++;
//        }
//        DayArrayAdapter dayArrayAdapter = new DayArrayAdapter(context, dayCalendar);
//        days.setViewAdapter(dayArrayAdapter);
//        days.setVisibleItems(7);
//        final WheelView hours = (WheelView) view.findViewById(R.id.hour);
//        hours.setViewAdapter(new NumericWheelAdapter(context, 0, 23));
//        hours.setVisibleItems(7);
//        final WheelView mins = (WheelView) view.findViewById(R.id.mins);
//        mins.setViewAdapter(new SpeedAdapter(context, 45, 15));
//        mins.setVisibleItems(7);
//        dayArrayAdapter.setDaysCount(availableDay + avialDay);
//        days.setCurrentItem(rollDay);
//        hours.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
//        mins.setCurrentItem(calendar.get(Calendar.MINUTE) / 15);
//        OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
//            @Override
//            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                if (!timeScrolled) {
//                    timeChanged = true;
//                    int daysRoll = days.getCurrentItem();
//                    int hoursRoll = hours.getCurrentItem();
//                    int minsRoll = mins.getCurrentItem();
//                    Calendar selectCalendar = Calendar.getInstance();
//                    selectCalendar.setTime(nowDate);
//                    selectCalendar.add(Calendar.DATE, daysRoll);
//                    selectCalendar.set(Calendar.HOUR_OF_DAY, hoursRoll);
//                    selectCalendar.set(Calendar.MINUTE, minsRoll * 15);
//                    selectCalendar.set(Calendar.SECOND, 0);
//                    selectCalendar.set(Calendar.MILLISECOND, 0);
//                    timeTextView.setText(TimeUtils.formatTimeWithHHMM(selectCalendar));
//                    timeChanged = false;
//                }
//            }
//        };
//        hours.addChangingListener(wheelListener);
//        mins.addChangingListener(wheelListener);
//        days.addChangingListener(wheelListener);
//
//        OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
//            @Override
//            public void onScrollingStarted(WheelView wheel) {
//                timeScrolled = true;
//            }
//
//            @Override
//            public void onScrollingFinished(WheelView wheel) {
//                timeScrolled = false;
//                timeChanged = true;
//                int daysRoll = days.getCurrentItem();
//                int hoursRoll = hours.getCurrentItem();
//                int minsRoll = mins.getCurrentItem();
//                Calendar selectCalendar = Calendar.getInstance();
//                selectCalendar.setTime(nowDate);
//                selectCalendar.add(Calendar.DATE, daysRoll);
//                selectCalendar.set(Calendar.HOUR_OF_DAY, hoursRoll);
//                selectCalendar.set(Calendar.MINUTE, minsRoll * 15);
//                selectCalendar.set(Calendar.SECOND, 0);
//                selectCalendar.set(Calendar.MILLISECOND, 0);
//                timeTextView.setText(TimeUtils.formatTimeWithHHMM(selectCalendar));
//                timeChanged = false;
//            }
//        };
//        hours.addScrollingListener(scrollListener);
//        mins.addScrollingListener(scrollListener);
//        days.addScrollingListener(scrollListener);
//        final TextView sureTextView = (TextView) view.findViewById(R.id.sure);
//        final TextView cancleTextView = (TextView) view.findViewById(R.id.cancle);
//        builder.setView(view);
//        timePickDialog = builder.create();
//        timePickDialog.setCanceledOnTouchOutside(false);
//        timePickDialog.show();
//        sureTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int daysRoll = days.getCurrentItem();
//                int hoursRoll = hours.getCurrentItem();
//                int minsRoll = mins.getCurrentItem();
//                Calendar selectCalendar = Calendar.getInstance();
//                selectCalendar.setTime(nowDate);
//                selectCalendar.add(Calendar.DATE, daysRoll);
//                selectCalendar.set(Calendar.HOUR_OF_DAY, hoursRoll);
//                selectCalendar.set(Calendar.MINUTE, minsRoll * 15);
//                selectCalendar.set(Calendar.SECOND, 0);
//                selectCalendar.set(Calendar.MILLISECOND, 0);
//                Date selectCalendarTime = selectCalendar.getTime();
//                long selectTime = selectCalendarTime.getTime();
//                timePickDialog.dismiss();
//                if (timePickResult != null) {
//                    timePickResult.timePickResult(selectTime);
//                }
//            }
//        });
//        cancleTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                timePickDialog.dismiss();
//            }
//        });
//    }
//
//
//    public static void showDayFirstPickDialog(final Activity context, long currenttime, final long startTime, final int availableDay, final TimePickResult timePickResult) {
//        if (context == null) {
//            return;
//        }
//        boolean hookBoolean = false;
//        if (timePickResult != null) {
//            hookBoolean = timePickResult.hook();
//        }
//        if (!hookBoolean) {
//            return;
//        }
//        if (timePickDialog != null && timePickDialog.isShowing()) {
//            timePickDialog.dismiss();
//        }
//        Date date = new Date(startTime);
//        final Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        int minsInt = calendar.get(Calendar.MINUTE);
//        int yu = minsInt % 15;
//        if (yu != 0) {
//            calendar.add(Calendar.MINUTE, 15 - yu);
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View view = context.getLayoutInflater().inflate(R.layout.daytime_pick_layout, null);
//        final TextView timeTextView = (TextView) view.findViewById(R.id.date_textview);
////        Calendar calendar = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
//        timeTextView.setText(TimeUtils.formatTimeWithHHMM(calendar));
//        final WheelView days = (WheelView) view.findViewById(R.id.day);
//        final Calendar nowCalendar = Calendar.getInstance();
//        final Date nowDate = new Date(currenttime);
//        final Date dayDate = new Date(currenttime);
//        nowCalendar.setTime(nowDate);
//        final Calendar dayCalendar = Calendar.getInstance();
//        dayCalendar.setTime(dayDate);
//        int rollDay = 0;
//        while (nowCalendar.before(calendar)) {
//            int nowDay = nowCalendar.get(Calendar.DATE);
//            int day = calendar.get(Calendar.DATE);
//            if (day == nowDay) {
//                int nowMonth = nowCalendar.get(Calendar.MONTH);
//                int month = calendar.get(Calendar.MONTH);
//                if (nowMonth == month) {
//                    break;
//                }
//            }
//            nowCalendar.add(Calendar.DATE, 1);
//            rollDay++;
//        }
//        DayArrayAdapter dayArrayAdapter = new DayArrayAdapter(context, dayCalendar);
//        days.setViewAdapter(dayArrayAdapter);
//        days.setVisibleItems(7);
//        final WheelView hours = (WheelView) view.findViewById(R.id.hour);
//        hours.setViewAdapter(new NumericWheelAdapter(context, 0, 23));
//        hours.setVisibleItems(7);
//        final WheelView mins = (WheelView) view.findViewById(R.id.mins);
//        mins.setViewAdapter(new SpeedAdapter(context, 45, 15));
//        mins.setVisibleItems(7);
//        int monthInt = calendar.get(Calendar.MONTH);
//        int dayInt = calendar.get(Calendar.DAY_OF_MONTH);
//
//        dayArrayAdapter.setDaysCount(availableDay);
//        days.setCurrentItem(rollDay);
//        hours.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
//        mins.setCurrentItem(calendar.get(Calendar.MINUTE) / 15);
//        OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
//            @Override
//            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                if (!timeScrolled) {
//                    timeChanged = true;
//                    int daysRoll = days.getCurrentItem();
//                    int hoursRoll = hours.getCurrentItem();
//                    int minsRoll = mins.getCurrentItem();
//                    Calendar selectCalendar = Calendar.getInstance();
//                    selectCalendar.setTime(nowDate);
//                    selectCalendar.add(Calendar.DATE, daysRoll);
//                    selectCalendar.set(Calendar.HOUR_OF_DAY, hoursRoll);
//                    selectCalendar.set(Calendar.MINUTE, minsRoll * 15);
//                    selectCalendar.set(Calendar.SECOND, 0);
//                    selectCalendar.set(Calendar.MILLISECOND, 0);
//                    timeTextView.setText(TimeUtils.formatTimeWithHHMM(selectCalendar));
//                    timeChanged = false;
//                }
//            }
//        };
//        hours.addChangingListener(wheelListener);
//        mins.addChangingListener(wheelListener);
//        days.addChangingListener(wheelListener);
//
//        OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
//            @Override
//            public void onScrollingStarted(WheelView wheel) {
//                timeScrolled = true;
//            }
//
//            @Override
//            public void onScrollingFinished(WheelView wheel) {
//                timeScrolled = false;
//                timeChanged = true;
//                int daysRoll = days.getCurrentItem();
//                int hoursRoll = hours.getCurrentItem();
//                int minsRoll = mins.getCurrentItem();
//                Calendar selectCalendar = Calendar.getInstance();
//                selectCalendar.setTime(nowDate);
//                selectCalendar.add(Calendar.DATE, daysRoll);
//                selectCalendar.set(Calendar.HOUR_OF_DAY, hoursRoll);
//                selectCalendar.set(Calendar.MINUTE, minsRoll * 15);
//                selectCalendar.set(Calendar.SECOND, 0);
//                timeTextView.setText(TimeUtils.formatTimeWithHHMM(selectCalendar));
//                timeChanged = false;
//            }
//        };
//
//        hours.addScrollingListener(scrollListener);
//        mins.addScrollingListener(scrollListener);
//        days.addScrollingListener(scrollListener);
//
//        final TextView sureTextView = (TextView) view.findViewById(R.id.sure);
//        final TextView cancleTextView = (TextView) view.findViewById(R.id.cancle);
//
//        builder.setView(view);
//        timePickDialog = builder.create();
//        timePickDialog.setCanceledOnTouchOutside(false);
//        timePickDialog.show();
//        sureTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int daysRoll = days.getCurrentItem();
//                int hoursRoll = hours.getCurrentItem();
//                int minsRoll = mins.getCurrentItem();
//                Calendar selectCalendar = Calendar.getInstance();
//                selectCalendar.setTime(nowDate);
//                selectCalendar.add(Calendar.DATE, daysRoll);
//                selectCalendar.set(Calendar.HOUR_OF_DAY, hoursRoll);
//                selectCalendar.set(Calendar.MINUTE, minsRoll * 15);
//                selectCalendar.set(Calendar.SECOND, 0);
//                selectCalendar.set(Calendar.MILLISECOND, 0);
//                Date selectCalendarTime = selectCalendar.getTime();
//                long selectTime = selectCalendarTime.getTime();
//                timePickDialog.dismiss();
//                if (timePickResult != null) {
//                    timePickResult.timePickResult(selectTime);
//                }
//            }
//        });
//        cancleTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                timePickDialog.dismiss();
//            }
//        });
//    }

    public static Dialog timePickDialog;

//    public static void showHourPickDialog(final Activity context, long time, final TimePickResult timePickResult, String tips) {
//        if (context == null) {
//            return;
//        }
//        if (timePickDialog != null && timePickDialog.isShowing()) {
//            timePickDialog.dismiss();
//        }
//        boolean hookBoolean = false;
//        if (timePickResult != null) {
//            hookBoolean = timePickResult.hook();
//        }
//        if (!hookBoolean) {
//            return;
//        }
//        Date date = new Date(time);
//        final Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        int minsInt = calendar.get(Calendar.MINUTE);
//        int yu = minsInt % 15;
//        if (yu != 0) {
//            calendar.add(Calendar.MINUTE, 15 - yu);
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View view = context.getLayoutInflater().inflate(R.layout.hourtime_pick_layout, null);
//        TextView tip = (TextView) view.findViewById(R.id.tips);
//        if (tips != null && !tips.equals("")) {
//            tip.setVisibility(View.VISIBLE);
//            tip.setText(tips);
//        } else {
//            tip.setVisibility(View.GONE);
//        }
//        final TextView timeTextView = (TextView) view.findViewById(R.id.date_textview);
//        timeTextView.setText(TimeUtils.formatTimeWithHHMM(calendar));
//        final WheelView hours = (WheelView) view.findViewById(R.id.hour);
//        hours.setViewAdapter(new NumericWheelAdapter(context, 0, 23));
//        hours.setVisibleItems(7);
//        final WheelView mins = (WheelView) view.findViewById(R.id.mins);
//        mins.setViewAdapter(new SpeedAdapter(context, 45, 15));
//        mins.setVisibleItems(7);
////        final Calendar nowCalendar = Calendar.getInstance();
////        final Date nowDate = new Date();
////        nowCalendar.setTime(nowDate);
//        hours.setCurrentItem(calendar.get(Calendar.HOUR_OF_DAY));
//        mins.setCurrentItem(calendar.get(Calendar.MINUTE) / 15);
//        OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
//            @Override
//            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                if (!timeScrolled) {
//                    timeChanged = true;
//                    int hoursRoll = hours.getCurrentItem();
//                    int minsRoll = mins.getCurrentItem();
////                    Calendar selectCalendar = Calendar.getInstance();
////                    selectCalendar.setTime(nowDate);
//                    calendar.set(Calendar.HOUR_OF_DAY, hoursRoll);
//                    calendar.set(Calendar.MINUTE, minsRoll * 15);
//                    calendar.set(Calendar.SECOND, 0);
//                    calendar.set(Calendar.MILLISECOND, 0);
//                    timeTextView.setText(TimeUtils.formatTimeWithHHMM(calendar));
//                    timeChanged = false;
//                }
//            }
//        };
//        hours.addChangingListener(wheelListener);
//        mins.addChangingListener(wheelListener);
//        OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
//            @Override
//            public void onScrollingStarted(WheelView wheel) {
//                timeScrolled = true;
//            }
//
//            @Override
//            public void onScrollingFinished(WheelView wheel) {
//                timeScrolled = false;
//                timeChanged = true;
//                int hoursRoll = hours.getCurrentItem();
//                int minsRoll = mins.getCurrentItem();
////                Calendar selectCalendar = Calendar.getInstance();
////                selectCalendar.setTime(nowDate);
//                calendar.set(Calendar.HOUR_OF_DAY, hoursRoll);
//                calendar.set(Calendar.MINUTE, minsRoll * 15);
//                calendar.set(Calendar.SECOND, 0);
//                calendar.set(Calendar.MILLISECOND, 0);
//                timeTextView.setText(TimeUtils.formatTimeWithHHMM(calendar));
//                timeChanged = false;
//            }
//        };
//        hours.addScrollingListener(scrollListener);
//        mins.addScrollingListener(scrollListener);
//        final TextView sureTextView = (TextView) view.findViewById(R.id.sure);
//        final TextView cancleTextView = (TextView) view.findViewById(R.id.cancle);
//        builder.setView(view);
//        timePickDialog = builder.create();
//        timePickDialog.setCanceledOnTouchOutside(false);
//        timePickDialog.show();
//        sureTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int hoursRoll = hours.getCurrentItem();
//                int minsRoll = mins.getCurrentItem();
////                Calendar selectCalendar = Calendar.getInstance();
////                selectCalendar.setTime(nowDate);
//                calendar.set(Calendar.HOUR_OF_DAY, hoursRoll);
//                calendar.set(Calendar.MINUTE, minsRoll * 15);
//                calendar.set(Calendar.SECOND, 0);
//                calendar.set(Calendar.MILLISECOND, 0);
//                Date selectCalendarTime = calendar.getTime();
//                long selectTime = selectCalendarTime.getTime();
//                timePickDialog.dismiss();
//                if (timePickResult != null) {
//                    timePickResult.timePickResult(selectTime);
//                }
//            }
//        });
//        cancleTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                timePickDialog.dismiss();
//            }
//        });
//    }

}
