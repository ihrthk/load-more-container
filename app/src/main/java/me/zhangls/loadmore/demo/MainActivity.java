package me.zhangls.loadmore.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.zhangls.adapter.helper.BaseAdapterHelper;
import me.zhangls.adapter.helper.QuickAdapter;
import me.zhangls.loadmore.containner.LoadMoreContainer;
import me.zhangls.loadmore.containner.LoadMoreHandler;
import me.zhangls.loadmore.containner.LoadMoreListViewContainer;

public class MainActivity extends AppCompatActivity {

    private QuickAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadMoreListViewContainer loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more);
        loadMoreListViewContainer.useDefaultFooter();
        loadMoreListViewContainer.setShowLoadingForFirstPage(true);

        final ListView listView = (ListView) findViewById(R.id.listview);
        adapter = new QuickAdapter<String>(this, R.layout.item_list) {
            @Override
            protected void convert(BaseAdapterHelper baseAdapterHelper, String s) {
                baseAdapterHelper.setText(R.id.textview, s);
            }
        };
        adapter.addAll(makeDatas(20, 0));

        listView.setAdapter(adapter);
        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                adapter.addAll(makeDatas(20, 20));
            }
        });


    }

    public List<String> makeDatas(int size, int start) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add("item" + (i + start));
        }
        return list;
    }

}
