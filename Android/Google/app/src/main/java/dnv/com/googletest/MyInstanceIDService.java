package dnv.com.googletest;

import android.content.Intent;
import android.util.Log;
import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by guxh on 5/12/2017.
 */

public class MyInstanceIDService extends InstanceIDListenerService {
    private static final String TAG = "MyInstanceIDService";

    public void onTokenRefresh(){
         Log.i(TAG,"Refreshing GCM Registration Token");
        Intent intent = new Intent(this,RegistrationIntentService.class);
        startService(intent);
    }
}
