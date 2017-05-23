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
        TextView tv = (TextView) findViewById(R.id.textView);



        tv.setText("Random open_type:"+open_type+" and you may do something according this type; message:"+message);
    }

    protected void onStart() {
        super.onStart();
    }
}
