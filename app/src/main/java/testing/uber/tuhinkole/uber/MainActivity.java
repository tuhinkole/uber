package testing.uber.tuhinkole.uber;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.uber.tuhinkole.uber.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Button btnSignIn,btnRegister;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                          .setDefaultFontPath("fonts/Sofia-Regular.otf")
                                          .setFontAttrId(R.attr.fontPath)
                                            .build());

        setContentView(R.layout.activity_main);

        btnSignIn =(Button)findViewById(R.id.btnSignIn);
        btnRegister =(Button)findViewById(R.id.btnRegister);
    }
}
