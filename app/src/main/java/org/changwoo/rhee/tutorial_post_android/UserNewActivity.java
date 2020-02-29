package org.changwoo.rhee.tutorial_post_android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.UserMultipartformDataApi;
import io.swagger.client.model.User;
import io.swagger.client.model.UserMultipartParam;

import java.io.File;

public class UserNewActivity extends UserFormActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void sendUser(UserMultipartParam param){
        AsyncTask asyncTask = new AsyncTask<UserMultipartParam, Void, ApiResponse<User >>() {
            @Override
            protected ApiResponse<User> doInBackground(UserMultipartParam... params) {
                ApiResponse<User> result = null;
                UserMultipartformDataApi apiInstance = new UserMultipartformDataApi();
                UserMultipartParam param = params[0];
                String userName = param.getName();
                String userUsername = param.getUsername();
                String userEmail = param.getEmail();
                String userPassword = param.getPassword();
                String userPasswordConfirmation = param.getPasswordConfirmation();
                File userAvatar = param.getAvatar();
                try {
                    Log.d(this.getClass().toString(), params.toString());
                    result = apiInstance.apiV1UsersPostWithHttpInfo(userName, userUsername, userEmail, userPassword, userPasswordConfirmation, userAvatar);
                    Log.d(this.getClass().toString(), result.toString());
                } catch (ApiException e) {
                    Log.d(this.getClass().toString(), e.toString());
                }
                return result;
            }
            @Override
            protected void onPostExecute(final ApiResponse<User> userResponse) {
                showProgress(false);
                if(userResponse == null){
                    //TODO error
                }

                if(userResponse.getStatusCode() == 201){
                    finish();
                }else if(userResponse.getStatusCode() == 422){
                    //TODO error
                }else{
                    //TODO error
                }
            }
            @Override
            protected void onCancelled() {
                showProgress(false);
            }
        };
        asyncTask.execute(param);
    }
}
