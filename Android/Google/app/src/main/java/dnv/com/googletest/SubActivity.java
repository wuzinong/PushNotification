package dnv.com.googletest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by GUXH on 5/22/2017.
 */

public class SubActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

        String message = getIntent().getStringExtra("message");
        String open_type = getIntent().getStringExtra("open_type");
        String url = getIntent().getStringExtra("url");
        String type = getIntent().getStringExtra("type");
        String action = getIntent().getStringExtra("action");
        TextView tv = (TextView) findViewById(R.id.textView);



        tv.setText("Parameter---open_type:"+open_type+"---type:"+type+"---url:"+url+"---action:"+action+"---message:"+message);
        //        if(open_type.equals("1")){
        //            // if equals 1 then open url
        //            Intent intent = new Intent();
        //            intent.setAction("android.intent.action.VIEW");
        //            Uri content_url = Uri.parse(url);
        //            intent.setData(content_url);
        //            context.startActivity(intent);
        //        }
    }

    protected void onStart() {
        super.onStart();
    }
}
