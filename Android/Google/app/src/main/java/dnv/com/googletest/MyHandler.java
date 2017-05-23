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

        String open_type = bundle.getString("open_type");


        sendNotification(nhMessage,open_type);
        if (MainActivity.isVisible) {
            MainActivity.mainActivity.ToastNotify("Title: "+nhTitle+" message: "+nhMessage+" url:"+url);
        }

//        if(open_type.equals("1")){
//            // if equals 1 then open url
//            Intent intent = new Intent();
//            intent.setAction("android.intent.action.VIEW");
//            Uri content_url = Uri.parse(url);
//            intent.setData(content_url);
//            context.startActivity(intent);
//        }

    }



    private void sendNotification(String message,String open_type) {

//        Intent intent = new Intent(ctx, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        mNotificationManager = (NotificationManager)
//                ctx.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
//                intent, PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(ctx)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle("Notification Hub Demo")
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText(msg))
//                        .setSound(defaultSoundUri)
//                        .setContentText(msg);
//
//        mBuilder.setContentIntent(contentIntent);
//        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());


//        Intent intent = new Intent(ctx, SubActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//       // intent.setAction(Long.toString(System.currentTimeMillis()));
//
//        mNotificationManager = (NotificationManager)
//                ctx.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//         NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(ctx)
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle("Notification Hub Demo")
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText(msg))
//                        .setSound(defaultSoundUri)
//                        .setContentText(msg)
//                        .setContentIntent(contentIntent);
//
//        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        Intent intent = new Intent(ctx, SubActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message",message);
        intent.putExtra("open_type",open_type);

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