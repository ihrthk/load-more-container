package me.zhangls.loadmore.containner;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

/**
 * @author zhangls
 */
public abstract class LoadMoreContainerBase extends LinearLayout {

    private AbsListView.OnScrollListener mOnScrollListener;
    private LoadMoreUIListener mLoadMoreUIListener;
    private LoadMoreListener mLoadMoreListener;

    private boolean mIsLoading;
    private boolean mHasMore = true;
    private boolean mAutoLoadMore = true;
    private boolean mLoadError = false;

    private View mFooterView;

    private AbsListView mAbsListView;

    private int preCount = 0;

    public LoadMoreContainerBase(Context context) {
        super(context);
    }

    public LoadMoreContainerBase(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mAbsListView = retrieveAbsListView();
        init();
    }

    public void useDefaultFooter() {
        LoadMoreDefaultFooterView footerView = new LoadMoreDefaultFooterView(getContext());
        footerView.setVisibility(GONE);
        setLoadMoreView(footerView);
    }

    private void init() {
        if (mFooterView != null) {
            addFooterView(mFooterView);
        }
        mAbsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private boolean mIsEnd = false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (null != mOnScrollListener) {
                    mOnScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (null != mOnScrollListener) {
                    mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
                Log.d("LoadMoreContainerBase", "firstVisibleItem:" + firstVisibleItem + "," +
                        "firstVisibleItem:" + visibleItemCount + ",totalItemCount:" + totalItemCount);
                mIsEnd = firstVisibleItem > 0 && totalItemCount > 0 && firstVisibleItem + visibleItemCount + preCount >= totalItemCount - 1;
                if (mIsEnd) {
                    onReachBottom();
                }
            }
        });
    }


    private void tryToPerformLoadMore() {
        if (mIsLoading) {
            return;
        }
        // no more content and also not load for first page
        if (!mHasMore) {
            return;
        }
        mIsLoading = true;
        if (mLoadMoreUIListener != null) {
            mLoadMoreUIListener.onLoading(this);
        }
        if (null != mLoadMoreListener) {
            mLoadMoreListener.onLoadMore();
        }
    }

    private void onReachBottom() {
        // if has error, just leave what it should be
        if (mLoadError) {
            return;
        }
        if (mAutoLoadMore) {
            tryToPerformLoadMore();
        } else {
            if (mHasMore) {
                mLoadMoreUIListener.onWaitToLoadMore(this);
            }
        }
    }

    public void setAutoLoadMore(boolean autoLoadMore) {
        mAutoLoadMore = autoLoadMore;
    }

    public void setHasMore(boolean mHasMore) {
        this.mHasMore = mHasMore;
    }

    public void setPreCount(int preCount) {
        this.preCount = preCount;
    }

    public void setOnScrollListener(AbsListView.OnScrollListener l) {
        mOnScrollListener = l;
    }

    public void setLoadMoreView(View view) {
        if (view instanceof LoadMoreUIListener) {
            mLoadMoreUIListener = (LoadMoreUIListener) view;
        } else {
            mLoadMoreUIListener = null;
        }

        // has not been initialized
        if (mAbsListView == null) {
            mFooterView = view;
            return;
        }
        // remove previous
        if (mFooterView != null && mFooterView != view) {
            removeFooterView(view);
        }
        // add current
        mFooterView = view;
        mFooterView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                tryToPerformLoadMore();
            }
        });
        addFooterView(view);
    }

    public void setLoadMoreListener(LoadMoreListener handler) {
        mLoadMoreListener = handler;
    }

    /**
     * page has loaded
     */
    public void loadMoreFinish() {
        mLoadError = false;
        mIsLoading = false;
        if (mLoadMoreUIListener != null) {
            if (mHasMore) {
                mLoadMoreUIListener.onLoading(this);
            } else {
                mLoadMoreUIListener.onLoadFinish(this);
            }
        }
    }

    public void loadMoreError(int errorCode, String errorMessage) {
        mIsLoading = false;
        mLoadError = true;
        if (mLoadMoreUIListener != null) {
            mLoadMoreUIListener.onLoadError(this, errorCode, errorMessage);
        }
    }

    protected abstract void addFooterView(View view);

    protected abstract void removeFooterView(View view);

    protected abstract AbsListView retrieveAbsListView();
}
