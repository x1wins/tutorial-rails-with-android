TODO
====
- [ ] http client
    - [ ] retrofit https://github.com/square/retrofit
- [ ] swagger model
- [ ] authentication
    - [ ] save token into local from json response
    - [ ] login
        - [ ] form
        - [ ] http post
    - [ ] join
        - [ ] form
        - [ ] http post
    - [ ] edit
        - [ ] form
        - [ ] http put 
    - [ ] logout
- [ ] category
    - [ ] index
- [ ] post
    - [ ] index
    - [ ] show
        - [ ] comment
            - [ ] index
            - [ ] form
                - [ ] new
                - [ ] edit
            - [ ] create
            - [ ] update
            - [ ] destroy
    - [ ] form
        - [ ] new
        - [ ] edit
    - [ ] create
    - [ ] update
    - [ ] destroy
    
    
docker-compose run --no-deps web bundle exec rake rswag
swagger-codegen generate -i http://localhost:3000/api-docs/v1/swagger.yaml \
  -l java --library=okhttp-gson \
  -D hideGenerationTimestamp=true \
  -o /var/tmp/java/okhttp-gson/ 

https://github.com/swagger-api/swagger-codegen/wiki/FAQ#how-can-i-generate-an-android-sdk


> https://github.com/swagger-api/swagger-codegen/wiki/FAQ#how-can-i-generate-an-android-sdk    
```bash
    brew install swagger-codegen
    mkdir -p /var/tmp/java/okhttp-gson/
    swagger-codegen generate -i http://localhost:3000/api-docs/v1/swagger.yaml \
      -l java --library=okhttp-gson \
      -D hideGenerationTimestamp=true \
      -o /var/tmp/java/okhttp-gson/  
```   

### Troubleshooting
#### Android studio does not recognize the project as android
1. close the project and delete the .idea folder.
https://github.com/firebase/FirebaseUI-Android/issues/1349#issuecomment-396329774

2.
welcome to intellij IDEA -> import project -> 
import project from external model -> Gradle -> 
check - Use Auto-import
check - Create directories for empty content roots automatically

![step1](/screenshot/step1.png)
![step2](/screenshot/step2.png)  
![step3](/screenshot/step3.png)  

#### Caused by: android.os.NetworkOnMainThreadException
https://www.toptal.com/android/android-threading-all-you-need-to-know

#### AsyncTask
https://i.stack.imgur.com/ytin1.png
https://gist.github.com/just-kip/1376527af60c74b07bef7bd7f136ff56
```java
        AsyncTask<Post, Void, Post> asyncTask = new AsyncTask<Post, Void, Post>() {
            @Override
            protected Post doInBackground(Post... params) {
                try {
                    ApiClient defaultClient = Configuration.getDefaultApiClient();
                    String authorization = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJleHAiOjE1ODIwOTg3NzF9.JGPR2oOOeGcjSocU4Ohvw1bg49ZjTQ9tQ3FtxmqmPDM"; // String | JWT token for Authorization
                    ApiKeyAuth Bearer = (ApiKeyAuth) defaultClient.getAuthentication("Bearer");
                    Bearer.setApiKey(authorization);
                    PostApi apiInstance = new PostApi();
                    String id = "1"; // String | id
                    Integer commentPage = 1; // Integer | Page number for Comment
                    Integer commentPer = 10; // Integer | Per page number For Comment
                    Post result;
                    try {
                        result = apiInstance.apiV1PostsIdGet(id, authorization, commentPage, commentPer);
//                        System.out.println(result);
                    } catch (ApiException e) {
                        System.err.println("Exception when calling PostApi#apiV1PostsIdGet");
                        e.printStackTrace();
                        result = new Post();
                    }
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Post();
                }
            }

            @Override
            protected void onPostExecute(Post post) {
                super.onPostExecute(post);
                if (post != null) {
                    mEmailView.setText(post.getBody());
                    System.out.print(post);
                }
            }
        };

        asyncTask.execute();
```

### passing object from activity to another activity 
```java
//To pass:
intent.putExtra("MyClass", obj);
// To retrieve object in second Activity
getIntent().getSerializableExtra("MyClass");

class MainClass implements Serializable {

    public MainClass() {}

    public static class ChildClass implements Serializable {

        public ChildClass() {}
    }
}
```

### Menu
https://stackoverflow.com/questions/31722566/dynamic-adding-item-to-navigationview-in-android
```bash
final Menu menu = navigationView.getMenu();
for (int i = 1; i <= 3; i++) {
   menu.add("Runtime item "+ i);
}

// adding a section and items into it
final SubMenu subMenu = menu.addSubMenu("SubMenu Title");
for (int i = 1; i <= 2; i++) {
   subMenu.add("SubMenu Item " + i);
}

// EDIT: If you want to interact with the menu, use
for (int i = 1; i <= 2; i++) {
   menu.add(0, itemId, 0, title); and then
}

public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
}
```

### ArrayAdaptor
#### recycle
https://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
```java
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
                holder.tv2.setText(post.getBody());
                String url = post.getUser().getAvatar();
                Picasso.get().load(url).into(holder.image);

                return convertView;
            }
        };
        mList.setAdapter(adapter);
    }

    class ViewHolder
    {
        ImageView image;
        TextView tv1;
        TextView tv2;
    }
```


