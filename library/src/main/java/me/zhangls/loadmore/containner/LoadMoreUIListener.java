package me.zhangls.loadmore.containner;

public interface LoadMoreUIListener {

    void onLoading(LoadMoreContainerBase container);

    void onLoadFinish(LoadMoreContainerBase container);

    void onWaitToLoadMore(LoadMoreContainerBase container);

    void onLoadError(LoadMoreContainerBase container, int errorCode, String errorMessage);
}
