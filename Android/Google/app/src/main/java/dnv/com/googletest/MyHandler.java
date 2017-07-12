package dnv.com.googletest;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import com.microsoft.windowsazure.notifications.NotificationsHandler;


/**
 * Created by guxh on 5/12/2017.
 */
public class MyHandler extends NotificationsHandler{
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;

    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;
        String nhMessage = bundle.getString("message");
        String nhTitle = bundle.getString("title");

        String url = bundle.getString("url");
        String type = bundle.getString("type");
        String action = bundle.getString("action");

        String open_type = bundle.getString("open_type");


        sendNotification(nhMessage,open_type,url,type,action);
        if (MainActivity.isVisible) {
            MainActivity.mainActivity.ToastNotify("Title: "+nhTitle+" message: "+nhMessage);
        }


    }



    private void sendNotification(String message,String open_type,String url,String type,String action) {

        Intent intent = new Intent(ctx, SubActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message",message);
        intent.putExtra("open_type",open_type);
        intent.putExtra("url",url);
        intent.putExtra("type",type);
        intent.putExtra("action",action);

        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Notification Hub Demo")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message))
                        .setSound(defaultSoundUri)
                        .setContentText(message);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }
}