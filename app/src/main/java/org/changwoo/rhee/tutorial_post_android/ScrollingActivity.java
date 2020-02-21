package org.changwoo.rhee.tutorial_post_android;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import io.swagger.client.model.Auth;
import io.swagger.client.model.Comment;
import io.swagger.client.model.Post;

import java.util.List;

public class ScrollingActivity extends AppCompatActivity {
    private Post mPost;
    private ListView mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mList = (ListView)findViewById(R.id.post_list);
        mPost = (Post) getIntent().getSerializableExtra("post");
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setTitle(mPost.getTitle());
        buildListView(mPost);
    }

    private void buildListView(final Post post){
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), 0) {
            @Override
            public int getCount() {
                int count = 0;
                List <Comment> comments = post.getComments();
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
                ScrollingActivity.PostItemViewHolder holder;
                if(convertView == null) {
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.post_item, parent, false);
                    holder = new ScrollingActivity.PostItemViewHolder();
                    holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
                    holder.title = (TextView) convertView.findViewById(R.id.title);
                    holder.name = (TextView) convertView.findViewById(R.id.sub_title);
                    convertView.setTag(holder);
                } else {
                    holder = (ScrollingActivity.PostItemViewHolder) convertView.getTag();
                }
                holder.title.setText(post.getBody());
                holder.name.setText(post.getUser().getName());
                String url = post.getUser().getAvatar();
                Picasso.get().load(url).placeholder(R.drawable.contact_picture_placeholder)
                        .error(R.drawable.noise).into(holder.avatar);
                return convertView;
            }

            private View recyclePostDetailItemConvertView(View convertView, ViewGroup parent){
                ScrollingActivity.PostDetailItemViewHolder holder;
                if(convertView == null) {
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.post_detail_item, parent, false);
                    holder = new ScrollingActivity.PostDetailItemViewHolder();
                    holder.content = (TextView) convertView.findViewById(R.id.post_content);
                    convertView.setTag(holder);
                } else {
                    holder = (ScrollingActivity.PostDetailItemViewHolder) convertView.getTag();
                }
                holder.content.setText(post.getBody());
                return convertView;
            }

            private View recycleCommentItemConvertView(int position, View convertView, ViewGroup parent){
                ScrollingActivity.CommentItemViewHolder holder;
                List<Comment> comments = post.getComments();
                Comment comment = comments.get(position-2);
                if(convertView == null) {
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.comment_item, parent, false);
                    holder = new ScrollingActivity.CommentItemViewHolder();
                    holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
                    holder.title = (TextView) convertView.findViewById(R.id.title);
                    holder.name = (TextView) convertView.findViewById(R.id.sub_title);
                    convertView.setTag(holder);
                } else {
                    holder = (ScrollingActivity.CommentItemViewHolder) convertView.getTag();
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

    private class PostItemViewHolder
    {
        ImageView avatar;
        TextView title;
        TextView name;
    }

    private class PostDetailItemViewHolder
    {
        TextView content;
    }

    private class CommentItemViewHolder
    {
        ImageView avatar;
        TextView title;
        TextView name;
    }
}
