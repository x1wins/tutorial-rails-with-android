package org.changwoo.rhee.tutorial_post_android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Picasso;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.CommentApi;
import io.swagger.client.auth.ApiKeyAuth;
import io.swagger.client.model.Auth;
import io.swagger.client.model.Comment;
import io.swagger.client.model.CommentParam;
import io.swagger.client.model.Post;

import java.util.List;

public class PostShowActivity extends AppCompatActivity {
    private Auth mAuth;
    private Post mPost;
    private ListView mList;
    private EditText mEditText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_show);
        mAuth = (Auth) getIntent().getSerializableExtra("auth");
        mPost = (Post) getIntent().getSerializableExtra("post");
        mList = (ListView)findViewById(R.id.post_show_list);
        mEditText = (EditText) findViewById(R.id.edittext_chatbox);
        mButton = (Button) findViewById(R.id.button_chatbox_send);
        getSupportActionBar().setTitle(mPost.getTitle());
        buildListView(mPost);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask<Auth, Void, Comment> asyncTask = new AsyncTask<Auth, Void, Comment>() {
                    @Override
                    protected Comment doInBackground(Auth... auth) {
                        String authorization = auth[0].getToken();
                        ApiClient defaultClient = Configuration.getDefaultApiClient();
                        ApiKeyAuth Bearer = (ApiKeyAuth) defaultClient.getAuthentication("Bearer");
                        Bearer.setApiKey(authorization);
                        CommentApi apiInstance = new CommentApi();
                        String message = mEditText.getEditableText().toString();
                        Integer postId = mPost.getId();
                        CommentParam body = new CommentParam(message, postId);
                        try {
                            Comment result = apiInstance.apiV1CommentsPost(body, authorization);
                            Log.d(this.getClass().toString(), result.toString());
                            return result;
                        } catch (ApiException e) {
                            Log.d(this.getClass().toString(),"Exception when calling CategoryApi#apiV1CategoriesGet");
                            e.printStackTrace();
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(Comment commentResponse) {
                        super.onPostExecute(commentResponse);
                        mPost.getComments().add(0, commentResponse);
                        mEditText.setText("");
                        mList.invalidateViews();
                    }
                };
                asyncTask.execute(mAuth);

            }
        });
    }

    private void buildListView(final Post post){
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), 0) {
            @Override
            public int getCount() {
                int count = 0;
                List<Comment> comments = post.getComments();
                if(comments != null){
                    count = comments.size();
                }
                count += 2;
                return count;
            }
            @Override
            public int getViewTypeCount() {
                return 3;
            }
            @Override
            public int getItemViewType(int position) {
                if (position == 0){
                    return 0;
                }else if (position == 1) {
                    return 1;
                }else{
                    return 2;
                }
            }
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (position == 0){
                    convertView = recyclePostItemConvertView(convertView, parent);
                }else if (position == 1) {
                    convertView = recyclePostDetailItemConvertView(convertView, parent);
                }else{
                    convertView = recycleCommentItemConvertView(position, convertView, parent);
                }
                return convertView;
            }

            private View recyclePostItemConvertView(View convertView, ViewGroup parent){
                PostItemViewHolder holder;
                if(convertView == null) {
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.post_item, parent, false);
                    holder = new PostItemViewHolder();
                    holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
                    holder.title = (TextView) convertView.findViewById(R.id.title);
                    holder.name = (TextView) convertView.findViewById(R.id.sub_title);
                    convertView.setTag(holder);
                } else {
                    holder = (PostItemViewHolder) convertView.getTag();
                }
                holder.title.setText(post.getTitle());
                holder.name.setText(post.getUser().getName());
                String url = post.getUser().getAvatar();
                Picasso.get().load(url).placeholder(R.drawable.contact_picture_placeholder)
                        .error(R.drawable.noise).into(holder.avatar);
                return convertView;
            }

            private View recyclePostDetailItemConvertView(View convertView, ViewGroup parent){
                PostDetailItemViewHolder holder;
                if(convertView == null) {
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.post_show_item, parent, false);
                    holder = new PostDetailItemViewHolder();
                    holder.content = (TextView) convertView.findViewById(R.id.post_content);
                    convertView.setTag(holder);
                } else {
                    holder = (PostDetailItemViewHolder) convertView.getTag();
                }
                holder.content.setText(post.getBody());
                return convertView;
            }

            private View recycleCommentItemConvertView(int position, View convertView, ViewGroup parent){
                CommentItemViewHolder holder;
                List<Comment> comments = post.getComments();
                Comment comment = comments.get(position-2);
                if(convertView == null) {
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.comment_item, parent, false);
                    holder = new CommentItemViewHolder();
                    holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
                    holder.title = (TextView) convertView.findViewById(R.id.title);
                    holder.name = (TextView) convertView.findViewById(R.id.sub_title);
                    convertView.setTag(holder);
                } else {
                    holder = (CommentItemViewHolder) convertView.getTag();
                }
                holder.title.setText(comment.getBody());
                holder.name.setText(comment.getUser().getName());
                String url = comment.getUser().getAvatar();
                Picasso.get().load(url).placeholder(R.drawable.contact_picture_placeholder)
                        .error(R.drawable.noise).into(holder.avatar);
                return convertView;
            }
        };
        mList.setAdapter(adapter);
    }

    private class PostItemViewHolder {
        ImageView avatar;
        TextView title;
        TextView name;
    }

    private class PostDetailItemViewHolder {
        TextView content;
    }

    private class CommentItemViewHolder {
        ImageView avatar;
        TextView title;
        TextView name;
    }
}
