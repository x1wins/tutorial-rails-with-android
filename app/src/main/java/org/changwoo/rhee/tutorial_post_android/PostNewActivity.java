package org.changwoo.rhee.tutorial_post_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.PostApi;
import io.swagger.client.auth.ApiKeyAuth;
import io.swagger.client.model.Auth;
import io.swagger.client.model.Post;
import io.swagger.client.model.PostParam;

public class PostNewActivity extends PostFormActivity {
    private Integer mCategoryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryId = getIntent().getIntExtra("categoryId", 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    void sendPost(){
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
