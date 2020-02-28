package org.changwoo.rhee.tutorial_post_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import com.kaopiz.kprogresshud.KProgressHUD;
import io.swagger.client.model.Auth;

public abstract class PostFormActivity extends AppCompatActivity {
    protected Auth mAuth;
    protected EditText mTitle;
    protected EditText mBody;
    protected KProgressHUD mKProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_form);
        getSupportActionBar().setTitle("Form");
        mTitle = (EditText) findViewById(R.id.title);
        mBody = (EditText) findViewById(R.id.body);
        mAuth = (Auth) getIntent().getSerializableExtra("auth");
        mKProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send) {
            sendPost();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    abstract void sendPost();
}
