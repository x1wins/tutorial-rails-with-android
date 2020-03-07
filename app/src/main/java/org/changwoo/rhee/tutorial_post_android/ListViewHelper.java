package org.changwoo.rhee.tutorial_post_android;

import android.os.AsyncTask;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import io.swagger.client.model.Auth;
import io.swagger.client.model.Post;

import java.util.List;

public class ListViewHelper {
    private boolean lastItemVisibleFlag;
    private ListViewHelper(){ }
    private Integer currentPage;
    private AsyncTask mAsyncTask;
    private Auth mAuth;
    protected Integer mCurrentPage;
    protected Integer mNextPage;
    protected Integer mTotalPage;
    private ListView mList;

    public ListViewHelper(ListView listView, AsyncTask asyncTask, Auth auth, final OnScrollListener onScrollListener){
        currentPage = 1;
        mList = listView;
        mAsyncTask = asyncTask;
        mAuth = auth;
        mList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    onScrollListener.onLastFocus(currentPage);
                    mAsyncTask.execute(mAuth);
                }
            }
        });
    }

    public interface OnScrollListener {
        void onLastFocus(Integer currentPage);
    }

    public void resetAdapter(){
        initPagination();
        ArrayAdapter adapter = (ArrayAdapter)mList.getAdapter();
        adapter.clear();
        adapter.setNotifyOnChange(true);
    }

    public void addArrayToAdapter(final List<Post> posts){
        ArrayAdapter adapter = (ArrayAdapter)mList.getAdapter();
        adapter.addAll(posts);
        adapter.setNotifyOnChange(true);
    }

    public void initPagination(){
        mCurrentPage = 1;
        mNextPage = null;
        mTotalPage = null;
    }

    public boolean isScrollPoisionAtLast(){
        return mTotalPage != null && mTotalPage == mCurrentPage;
    }

    public boolean hasNext(){
        return mNextPage != null;
    }

    public int getNextPage(){
        int page = 1;
        if(hasNext()){
            page = mNextPage;
        }
        return page;
    }

    public void setPagination(Integer nextPage, Integer currentPage, Integer totalPage){
        mNextPage = nextPage;
        mCurrentPage = currentPage;
        mTotalPage = totalPage;
    }
}
