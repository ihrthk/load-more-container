package me.zhangls.loadmore.containner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadMoreDefaultFooterView extends RelativeLayout implements LoadMoreUIListener {


    private TextView mTextView;


    public LoadMoreDefaultFooterView(Context context) {
        this(context, null);
    }


    public LoadMoreDefaultFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public LoadMoreDefaultFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupViews();
    }


    private void setupViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.cube_views_load_more_default_footer, this);
        mTextView = (TextView) findViewById(R.id.cube_views_load_more_default_footer_text_view);
    }


    @Override
    public void onLoading(LoadMoreContainerBase container) {
        setVisibility(VISIBLE);
        mTextView.setText("加载中...");
    }


    @Override
    public void onLoadFinish(LoadMoreContainerBase container) {
        setVisibility(VISIBLE);
        mTextView.setText("全部数据加载完毕");
    }


    @Override
    public void onWaitToLoadMore(LoadMoreContainerBase container) {
        setVisibility(VISIBLE);
        mTextView.setText("点击加载更多");
    }


    @Override
    public void onLoadError(LoadMoreContainerBase container, int errorCode, String errorMessage) {
        mTextView.setText("加载失败,点击重试");
    }
}
