package com.mingzebj.bmpcn.qr.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.mingzebj.bmpcn.qr.ErrorActivity;
import com.mingzebj.bmpcn.qr.R;

/**
 * Created by liuchao on 2015/10/8.
 */
public class NotificationUtil {

    /**
     * 通知栏发送通知消息
     *
     * @param context
     */
    public static void showNotification(Context context, String errorMsg) {
        if (context == null)
            return;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, ErrorActivity.class);
        // 当前通知显示时的时间，由于通知会根据失效与否进行不同的跳转逻辑，所以需要结合当前显示时间showTime和validTime来判断如何跳转
        intent.putExtra("error", errorMsg);
        // requestCode 设置不同的值，防止后面的notification对前面的notification影响
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String contentTitle = "程序异常";
        String contentText = errorMsg;
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon);

        NotificationCompat.BigTextStyle bigTextStyle = new android.support.v4.app.NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(contentText);
        bigTextStyle.setSummaryText("查看更多");
        bigTextStyle.setBigContentTitle(contentTitle);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(contentTitle)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .setLargeIcon(icon)
                .setContentText(contentText)
                .setStyle(bigTextStyle)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.icon)
                .setContentIntent(pendingIntent);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notification);
    }

}
