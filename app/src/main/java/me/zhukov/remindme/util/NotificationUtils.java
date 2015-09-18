package me.zhukov.remindme.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;

import me.zhukov.remindme.R;
import me.zhukov.remindme.activity.MainActivity;

public class NotificationUtils {

    private static NotificationUtils sInstance;
    private static Context sContext;

    private NotificationManager mManager;

    private NotificationUtils(Context context) {
        sContext = context;
        mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static NotificationUtils getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NotificationUtils(context);
        } else {
            sContext = context;
        }
        return sInstance;
    }

    public void createNotification(String message, int id) {
        Intent notificationIntent = new Intent(sContext, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(
                sContext,
                id,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Notification notification = new NotificationCompat.Builder(sContext)
                .setLargeIcon(
                        BitmapFactory.decodeResource(sContext.getResources(), R.mipmap.ic_launcher)
                )
                .setSmallIcon(R.drawable.ic_clock_grey)
                .setTicker(message)
                .setContentText(message)
                .setContentIntent(pi)
                .setContentTitle(sContext.getResources().getString(R.string.app_name))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .build();

        mManager.notify(id, notification);
    }
}
