package org.changwoo.rhee.tutorial_post_android;

import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import io.swagger.client.model.Post;

import java.util.List;

public class LoadMore {
    private boolean lastItemVisibleFlag;
    private LoadMore(){ }
    private Integer currentPage;
    public LoadMore(ListView listView, final OnScrollListener onScrollListener){
        currentPage = 1;
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    onScrollListener.onLastFocus(currentPage);
                }
            }
        });
    }

    public void add(ListView listView, List<Post> posts){
        ArrayAdapter adapter = (ArrayAdapter)listView.getAdapter();
        adapter.addAll(posts);
        adapter.setNotifyOnChange(true);
        currentPage++;
    }

    public interface OnScrollListener {
        void onLastFocus(Integer currentPage);
    }
}
