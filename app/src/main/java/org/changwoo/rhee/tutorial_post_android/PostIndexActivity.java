package org.changwoo.rhee.tutorial_post_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.CategoryApi;
import io.swagger.client.api.PostApi;
import io.swagger.client.auth.ApiKeyAuth;
import io.swagger.client.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.changwoo.rhee.tutorial_post_android.RequestCode.POST_NEW_REQUEST;

public class PostIndexActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private NavigationView mNavigationView;
    private Auth mAuth;
    private MenuItem mPreviousMenuItem;
    private ListView mList;
    private Integer mNextPage;
    private Integer mCurrentPage;
    private Integer mTotalPage;
    private List<Category> mCategories;
    private Category mSelectedCategory;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView mAvatar;
    private TextView mUsername;
    private TextView mEmail;
    private KProgressHUD mKProgressHUD;
    private LoadMore mLoadMore;
    public static final int POST_FORM_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_index);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.i(this.getClass().toString(), "onRefresh called from SwipeRefreshLayout");
                    Integer categoryId = mSelectedCategory.getId();
                    resetAdapter();
                    executePostsAsync(categoryId);
                }
            }
        );
        setSupportActionBar(mToolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostNewActivity.class);
                intent.putExtra("auth", mAuth);
                intent.putExtra("categoryId", mSelectedCategory.getId());
                startActivityForResult(intent, POST_NEW_REQUEST);
            }
        });
        mFab.hide();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mList = (ListView)findViewById(R.id.post_list);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter adapter = (ArrayAdapter)mList.getAdapter();
                Post post = (Post)adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), PostShowActivity.class);
                intent.putExtra("auth", mAuth);
                intent.putExtra("postId", post.getId());
                startActivity(intent);
            }
        });

        mAuth = (Auth) getIntent().getSerializableExtra("auth");
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        buildAvatar(mAuth);
        final Menu menu = mNavigationView.getMenu();
        menu.clear();

        mKProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        initLoadMore();
        initAdapter(new ArrayList<Post>());
        executeCategoryAsync();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_signout) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        menuItem.setCheckable(true);
        menuItem.setChecked(true);
        if (mPreviousMenuItem != null) {
            mPreviousMenuItem.setChecked(false);
        }
        mPreviousMenuItem = menuItem;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        int position = menuItem.getOrder();
        selectMenu(position);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == POST_NEW_REQUEST) {
            if (data.hasExtra("post")) {
                Post post = (Post) data.getSerializableExtra("post");
                insertToAdapter(post);
            }
        }
    }

    private void buildAvatar(Auth auth){
        View headerLayout = mNavigationView.getHeaderView(0);
        mAvatar = (ImageView) headerLayout.findViewById(R.id.avatar);
        mUsername = (TextView) headerLayout.findViewById(R.id.username);
        mEmail = (TextView) headerLayout.findViewById(R.id.email);
        Picasso.get().load(auth.getAvatar()).placeholder(R.drawable.contact_picture_placeholder).error(R.drawable.noise).into(mAvatar);
        mUsername.setText(auth.getUsername());
        mEmail.setText(auth.getEmail());
    }

    private void selectMenu(int position){
        mSelectedCategory = mCategories.get(position);
        mToolbar.setTitle(mSelectedCategory.getTitle());
        Integer categoryId = mSelectedCategory.getId();
        resetAdapter();
        executePostsAsync(categoryId);
    }

    private void executeCategoryAsync(){
        AsyncTask<Auth, Void, Categories> asyncTask = new AsyncTask<Auth, Void, Categories>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mKProgressHUD.show();
            }
            @Override
            protected Categories doInBackground(Auth... auth) {
                String authorization = auth[0].getToken();
                ApiClient defaultClient = Configuration.getDefaultApiClient();
                ApiKeyAuth Bearer = (ApiKeyAuth) defaultClient.getAuthentication("Bearer");
                Bearer.setApiKey(authorization);
                CategoryApi apiInstance = new CategoryApi();
                Integer page = 1;
                Integer per = 20; // Integer | Per page number
                Integer postPage = 1; // Integer | Page number for Post
                Integer postPer = 40; // Integer | Per page number For Post
                try {
                    Categories result = apiInstance.apiV1CategoriesGet(authorization, page, per, postPage, postPer);
                    Log.d(this.getClass().toString(), result.toString());
                    return result;
                } catch (ApiException e) {
                    Log.d(this.getClass().toString(),"Exception when calling CategoryApi#apiV1CategoriesGet");
                    e.printStackTrace();
                    return null;
                }
            }
            @Override
            protected void onPostExecute(Categories categoriesResponse) {
                super.onPostExecute(categoriesResponse);
                mKProgressHUD.dismiss();
                final Menu menu = mNavigationView.getMenu();
                mCategories = categoriesResponse.getCategories();
                for (int i = 0; i < mCategories.size(); i++) {
                    Category category = mCategories.get(i);
                    String title = category.getId() + " " + category.getTitle();
                    Integer id = category.getId();
                    menu.add(R.id.group_category, id, i, title);
                }
                selectMenu(0);
                mFab.show();
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                mKProgressHUD.dismiss();
            }
        };
        asyncTask.execute(mAuth);
    }

    private void executePostsAsync(final Integer categoryId){
        AsyncTask<Auth, Void, Posts> asyncTask = new AsyncTask<Auth, Void, Posts>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mKProgressHUD.show();
            }
            @Override
            protected Posts doInBackground(Auth... auth) {
                String authorization = auth[0].getToken();
                ApiClient defaultClient = Configuration.getDefaultApiClient();
                ApiKeyAuth Bearer = (ApiKeyAuth) defaultClient.getAuthentication("Bearer");
                Bearer.setApiKey(authorization);
                PostApi apiInstance = new PostApi();
                Integer page = 1;
                if(mNextPage != null){
                    page = mNextPage;
                }
                if(mTotalPage != null && mTotalPage == mCurrentPage){
                    return null;
                }
                Integer per = 20;
                Integer commentPage = 1;
                Integer commentPer = 0;
                String search = null;
                try {
                    Posts result = apiInstance.apiV1PostsGet(authorization, categoryId, page, per, commentPage, commentPer, search);
                    Log.d(this.getClass().toString(), result.toString());
                    return result;
                } catch (ApiException e) {
                    Log.d(this.getClass().toString(),"Exception when calling CategoryApi#apiV1CategoriesGet");
                    e.printStackTrace();
                    return null;
                }
            }
            @Override
            protected void onPostExecute(Posts postsResponse) {
                super.onPostExecute(postsResponse);
                mKProgressHUD.dismiss();
                mSwipeRefreshLayout.setRefreshing(false);
                if(postsResponse == null){
                    return;
                }
                getSupportActionBar().setTitle(mSelectedCategory.getTitle());
                List<Post> posts = postsResponse.getPosts();
                if(posts != null){
                    Pagination postsPagination = postsResponse.getPostsPagination();
                    if(postsPagination != null){
                        mNextPage = postsPagination.getNextPage();
                        mCurrentPage = postsPagination.getCurrentPage();
                        mTotalPage = postsPagination.getTotalPages();
                    }
                }
                addArrayToAdapter(posts);
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
                mKProgressHUD.dismiss();
            }
        };
        asyncTask.execute(mAuth);
    }

    private void resetAdapter(){
        initPagination();
        ArrayAdapter adapter = (ArrayAdapter)mList.getAdapter();
        adapter.clear();
        adapter.setNotifyOnChange(true);
    }

    private void insertToAdapter(Post post){
        ArrayAdapter adapter = (ArrayAdapter)mList.getAdapter();
        adapter.insert(post, 0);
        adapter.setNotifyOnChange(true);
    }

    private void addArrayToAdapter(final List<Post> posts){
        ArrayAdapter adapter = (ArrayAdapter)mList.getAdapter();
        adapter.addAll(posts);
        adapter.setNotifyOnChange(true);
    }

    private void initPagination(){
        mCurrentPage = 1;
        mNextPage = null;
        mTotalPage = null;
    }

    private void initLoadMore(){
        mLoadMore = new LoadMore(mList, new LoadMore.OnScrollListener() {
            @Override
            public void onLastFocus(Integer currentPage) {
                Integer categoryId = mSelectedCategory.getId();
                executePostsAsync(categoryId);
            }
        });
    }

    private void initAdapter(final List<Post> posts){
        initPagination();
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), 0, posts) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;
                if(convertView == null) {
                    convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.post_item, parent, false);
                    holder = new ViewHolder();
                    holder.image = (ImageView) convertView.findViewById(R.id.avatar);
                    holder.tv1 = (TextView) convertView.findViewById(R.id.title);
                    holder.tv2 = (TextView) convertView.findViewById(R.id.sub_title);
                    holder.tv3 = (TextView) convertView.findViewById(R.id.created_at);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                Post post = posts.get(position);
                holder.tv1.setText(post.getId() + ". " + post.getTitle());
                holder.tv2.setText(post.getUser().getName());
                holder.tv3.setText(Ago.build(post.getCreatedAt()));
                String url = post.getUser().getAvatar();
                Picasso.get().load(url).placeholder(R.drawable.contact_picture_placeholder)
                        .error(R.drawable.noise).into(holder.image);
                return convertView;
            }
        };
        mList.setAdapter(adapter);
    }

    private class ViewHolder {
        ImageView image;
        TextView tv1;
        TextView tv2;
        TextView tv3;
    }
}
