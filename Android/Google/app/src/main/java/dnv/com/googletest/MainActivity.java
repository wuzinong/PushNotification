package dnv.com.googletest;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.*;
import com.microsoft.windowsazure.notifications.NotificationsManager;
import android.util.Log;
import android.widget.*;
import android.widget.TextView;
import android.widget.Toast;


import java.util.HashSet;
import java.util.Set;
import android.net.Uri;
public class MainActivity extends AppCompatActivity {

    public static MainActivity mainActivity;
    public static Boolean isVisible = false;
    private GoogleCloudMessaging gcm;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    CheckBox cb1,cb2,cb3,cb4;
    Button subBtn;

    private Notifications notifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mainActivity = this;
        NotificationsManager.handleNotifications(this, NotificationSettings.SenderId, MyHandler.class);

        registerWithNotificationHubs();  //used for all notification



        // subscription tag
        notifications = new Notifications(this, NotificationSettings.SenderId, NotificationSettings.HubName, NotificationSettings.HubListenConnectionString);
        notifications.subscribeToCategories(notifications.retrieveCategories());




        cb1 = (CheckBox) findViewById(R.id.checkBox1);
        cb2 = (CheckBox) findViewById(R.id.checkBox2);
        cb3 = (CheckBox) findViewById(R.id.checkBox3);
        cb4 = (CheckBox) findViewById(R.id.checkBox4);
        subBtn = (Button) findViewById(R.id.button2);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Set<String> cTemp = Notifications.cts;

                if(cTemp.contains("user:7f97b4c9-fc4d-4a30-b27e-03eb35b4f92c")){
                    cb1.setChecked(true);
                }
                if(cTemp.contains("channel:2572ddea-551d-4361-a08b-739256809383")){
                    cb2.setChecked(true);
                }
                if(cTemp.contains("channel:fd3dedb8-0212-4222-8232-6b593af020db")){
                    cb3.setChecked(true);
                }
                if(cTemp.contains("channel:851d2ca8-6f2c-486a-be4e-6d0cee37ff7e")){
                    cb4.setChecked(true);
                }

            }
        });


        //Handle with checkbox




        subBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                HashSet<String> temp = new HashSet<String>();
                if(cb1.isChecked()){
                    temp.add("user:7f97b4c9-fc4d-4a30-b27e-03eb35b4f92c");
                }
                if(cb2.isChecked()){
                    temp.add("channel:2572ddea-551d-4361-a08b-739256809383");
                }
                if(cb3.isChecked()){
                    temp.add("channel:fd3dedb8-0212-4222-8232-6b593af020db");
                }
                if(cb4.isChecked()){
                    temp.add("channel:851d2ca8-6f2c-486a-be4e-6d0cee37ff7e");
                }
                //  // register tags passed from layout
               MainActivity.this.subscribe(temp);
            }
        });

    }



    //mock subscribe tags so that we can receive the tagged news.
    public void subscribe(Set<String> categories){

        notifications.storeCategoriesAndSubscribe(categories);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported by Google Play Services.");
                ToastNotify("This device is not supported by Google Play Services.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void registerWithNotificationHubs()
    {
        Log.i(TAG, " Registering with Notification Hubs");

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);

        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        isVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isVisible = false;
    }

    public void ToastNotify(final String notificationMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, notificationMessage, Toast.LENGTH_LONG).show();
                TextView helloText = (TextView) findViewById(R.id.text_hello);
                helloText.setText(notificationMessage);
            }
        });
    }


}
