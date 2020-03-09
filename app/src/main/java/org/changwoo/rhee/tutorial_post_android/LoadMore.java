package org.changwoo.rhee.tutorial_post_android;

import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class LoadMore {
    private boolean lastItemVisibleFlag;
    private LoadMore(){ }
    private Integer mCurrentPage;
    private Integer mNextPage;
    private Integer mTotalPage;
    public LoadMore(ListView listView, final OnScrollListener onScrollListener){
        mCurrentPage = 1;
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    onScrollListener.onLastFocus();
                }
            }
        });
    }

    public void add(ListView listView, List<?> datas){
        ArrayAdapter adapter = (ArrayAdapter)listView.getAdapter();
        adapter.addAll(datas);
        adapter.setNotifyOnChange(true);
    }

    public void resetPagination(){
        setPagination(1, null, null);
    }

    public void setPagination(Integer currentPage, Integer nextPage, Integer totalPage){
        mCurrentPage = currentPage;
        mNextPage = nextPage;
        mTotalPage = totalPage;
    }

    public Integer getNextPage(){
        Integer page = 1;
        if(mNextPage != null){
            page = mNextPage;
        }
        return page;
    }

    public boolean hasNotNextPage(){
        return mTotalPage != null && mTotalPage == mCurrentPage;
    }

    public interface OnScrollListener {
        void onLastFocus();
    }
}
