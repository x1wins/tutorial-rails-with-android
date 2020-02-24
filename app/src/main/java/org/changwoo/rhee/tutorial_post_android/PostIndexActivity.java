package org.changwoo.rhee.tutorial_post_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.squareup.picasso.Picasso;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.Configuration;
import io.swagger.client.api.CategoryApi;
import io.swagger.client.auth.ApiKeyAuth;
import io.swagger.client.model.Auth;
import io.swagger.client.model.Categories;
import io.swagger.client.model.Category;
import io.swagger.client.model.Post;

import java.util.List;

public class PostIndexActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private Auth mAuth;
    private MenuItem mPreviousMenuItem;
    private ListView mList;
    private List<Category> mCategories;
    private Category mSelectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_index);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        mList = (ListView)findViewById(R.id.post_list);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Post> posts = mSelectedCategory.getPosts();
                Post post = posts.get(position);
                Intent intent = new Intent(getApplicationContext(), PostShowActivity.class);
                intent.putExtra("auth", mAuth);
                intent.putExtra("postId", post.getId());
                startActivity(intent);
            }
        });

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mAuth = (Auth) getIntent().getSerializableExtra("auth");
        final Menu menu = mNavigationView.getMenu();
        menu.clear();

        AsyncTask<Auth, Void, Categories> asyncTask = new AsyncTask<Auth, Void, Categories>() {
            @Override
            protected Categories doInBackground(Auth... auth) {
                String authorization = auth[0].getToken();
                ApiClient defaultClient = Configuration.getDefaultApiClient();
                ApiKeyAuth Bearer = (ApiKeyAuth) defaultClient.getAuthentication("Bearer");
                Bearer.setApiKey(authorization);
                CategoryApi apiInstance = new CategoryApi();
                Integer page = 1;
                Integer per = 10; // Integer | Per page number
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
                final Menu menu = mNavigationView.getMenu();
                mCategories = categoriesResponse.getCategories();
                for (int i = 0; i < mCategories.size(); i++) {
                    Category category = mCategories.get(i);
                    String title = category.getId() + " " + category.getTitle();
                    Integer id = category.getId();
                    menu.add(R.id.group_category, id, i, title);
                }
                selectMenu(0);
            }
        };
        asyncTask.execute(mAuth);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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

    private void selectMenu(int position){
        mSelectedCategory = mCategories.get(position);
        mToolbar.setTitle(mSelectedCategory.getTitle());
        List<Post> posts = mSelectedCategory.getPosts();
        buildListView(posts);
    }

    private void buildListView(final List<Post> posts){
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
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                Post post = posts.get(position);
                holder.tv1.setText(post.getTitle());
                holder.tv2.setText(post.getUser().getName());
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
    }
}
