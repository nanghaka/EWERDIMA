package com.ilicit.ewerdima;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "NOTIFICATION INTENT";
    public static final String UPDATE = "update";
    public static final String ACTION = "action";
    public static final String PK = "pk";
    public static final String TITLE = "title";
    public static final String USER_FROM = "user_from";
    public static final String TEXT = "product";
    private NotificationManager mNotificationManager;
    private int count = 0;

    public GCMIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification(extras);
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification(extras);
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                sendNotification(extras);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(Bundle data) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = null;
        mBuilder = build_update_notification(data);
      /*  if (data != null) {
            Map<String, NotificationCompat.Builder> handlers = new HashMap<String, NotificationCompat.Builder>();
            handlers.put(UPDATE, build_update_notification(data));

            if (data.containsKey(ACTION)) {
                String action = null;
                action = data.getString(ACTION);
                if (action != null) {
                    mBuilder = handlers.get(action);
                    if (mBuilder == null) {
                        Log.e(TAG, "action update");
                    }
                } else {
                    Log.e(TAG, "action is null");
                }

            } else {
                Log.e(TAG, "no action");
            }
        }*/


        if (mBuilder != null) {
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        } else {
            Log.e(TAG, "no builder");
        }

    }




    private NotificationCompat.Builder getBuilder(String title) {
        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(title)
                .setDefaults(-1)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentText(title);

    }






    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private NotificationCompat.Builder build_update_notification(Bundle data) {
        Log.e(TAG, data.toString());
        String title = "Ewerdima";
        String message = String.format("%s ", data.getString(TEXT));
        String id = "1";
        String name = String.format("from %s ", data.getString("name"));
        if(data.containsKey("program_id")) {
             id = String.format("%s", data.getString("program_id")).trim();

        }



        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setDefaults(-1)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentText(message+" "+name);


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(data);
        intent.putExtra(TITLE, message);
        intent.putExtra("gcm", id);
        intent.putExtra("name", name);


        count++;
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself).
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(count, PendingIntent.FLAG_CANCEL_CURRENT);


        mBuilder.setContentIntent(resultPendingIntent);
        return mBuilder;
    }




    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private PendingIntent getContentIntent(String message,int action,String name) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ACTION, action);
        intent.putExtra(TITLE, message);
        intent.putExtra("name", name);


        count++;
// TaskStackBuilder ensures that the back button follows the recommended convention for the back key.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself).
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack.
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(count, PendingIntent.FLAG_CANCEL_CURRENT);



        return resultPendingIntent;


    }






}
