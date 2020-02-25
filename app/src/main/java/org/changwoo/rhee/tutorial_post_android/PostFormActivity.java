package org.changwoo.rhee.tutorial_post_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.PostApi;
import io.swagger.client.auth.ApiKeyAuth;
import io.swagger.client.model.Auth;
import io.swagger.client.model.Post;
import io.swagger.client.model.PostParam;

public class PostFormActivity extends AppCompatActivity {
    private Auth mAuth;
    private Integer mCategoryId;
    private EditText mTitle;
    private EditText mBody;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_form);
        getSupportActionBar().setTitle("Form");
        mTitle = (EditText) findViewById(R.id.title);
        mBody = (EditText) findViewById(R.id.body);
        mAuth = (Auth) getIntent().getSerializableExtra("auth");
        mCategoryId = getIntent().getIntExtra("categoryId", 0);
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

    private void sendPost(){
        final String title = mTitle.getEditableText().toString();
        final String message = mBody.getEditableText().toString();

        mTitle.setError(null);
        mBody.setError(null);

        if(title.trim().equalsIgnoreCase("")){
            mTitle.setError("This field can not be blank");
            return;
        }

        if(message.trim().equalsIgnoreCase("")){
            mBody.setError("This field can not be blank");
            return;
        }

        AsyncTask<Auth, Void, Post> asyncTask = new AsyncTask<Auth, Void, Post>() {
            @Override
            protected Post doInBackground(Auth... auth) {
                String authorization = auth[0].getToken();
                ApiClient defaultClient = Configuration.getDefaultApiClient();
                ApiKeyAuth Bearer = (ApiKeyAuth) defaultClient.getAuthentication("Bearer");
                Bearer.setApiKey(authorization);
                PostApi apiInstance = new PostApi();
                PostParam body = new PostParam(title, message, mCategoryId);
                try {
                    Post result = apiInstance.apiV1PostsPost(body, authorization);
                    Log.d(this.getClass().toString(), result.toString());
                    return result;
                } catch (ApiException e) {
                    Log.d(this.getClass().toString(),"Exception when calling CategoryApi#apiV1CategoriesGet");
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Post postResponse) {
                super.onPostExecute(postResponse);
                Intent data = new Intent();
                data.putExtra("post", postResponse);
                setResult(RESULT_OK, data);
                finish();
            }
        };
        asyncTask.execute(mAuth);
    }
}
