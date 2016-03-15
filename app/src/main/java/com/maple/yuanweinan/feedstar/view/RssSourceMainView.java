package com.maple.yuanweinan.feedstar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.maple.yuanweinan.feedstar.R;
import com.maple.yuanweinan.feedstar.easyadapterhelperlib.BaseViewHolderHelper;
import com.maple.yuanweinan.feedstar.easyadapterhelperlib.EasyAdapter;
import com.maple.yuanweinan.feedstar.lib.RSSFeed;
import com.maple.yuanweinan.feedstar.lib.RSSItem;
import com.maple.yuanweinan.feedstar.manager.FeedStarDataManager;
import com.maple.yuanweinan.feedstar.thread.AdSdkThreadExecutorProxy;
import com.maple.yuanweinan.feedstar.view.inter.BaseView;

import java.util.List;

/**
 * Created by yuanweinan on 16/3/11.
 */
public class RssSourceMainView extends BaseView {

    public RssSourceMainView(Context context) {
        super(context);
        init(context);
    }

    public RssSourceMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private List<RSSFeed> mData;
    private GridView mGridView;
    private Context mContext;
    private EasyAdapter<RSSFeed, BaseViewHolderHelper> mRssSourceAdapter;

    private void init(Context context) {
        inflate(context, R.layout.rss_source_main, this);
        mContext = context;
        mGridView = (GridView) findViewById(R.id.rss_source_gridview_id);
        mData = FeedStarDataManager.getInstance(mContext).getRssSourceInfo();

        mRssSourceAdapter = new EasyAdapter<RSSFeed, BaseViewHolderHelper>(mContext, R.layout.rss_source_grid_item, mData) {

            @Override
            public void convert(BaseViewHolderHelper viewHolderHelper, final RSSFeed data, int position) {

                viewHolderHelper.setTextView(R.id.main_grid_item_title_id, data.getTitle());

                viewHolderHelper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FeedStarDataManager.getInstance(mContext).requestRssFeed(data, new FeedStarDataManager.IRequestAction() {
                            @Override
                            public void onFinish(final Object... objects) {
                                AdSdkThreadExecutorProxy.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        mDetailListData.clear();
//                                        mDetailListData.addAll(data.getItems());
//                                        mDetailListViewAdapter.notifyDataSetChanged();
//                                        showDetailView();
                                        showItemListView(data.getItems());
                                    }
                                });
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                    }
                });
            }
        };

        mGridView.setAdapter(mRssSourceAdapter);
    }

    private RssItemListMainView mRssItemListMainView;
    private void showItemListView(List<RSSItem> items) {
        mRssItemListMainView = new RssItemListMainView(mContext, items);
//        addView(mRssItemListMainView);
        showView(mRssItemListMainView);
    }

    private void removeItemListView() {
        removeView(mRssItemListMainView);
        removeTopView();
        mRssItemListMainView = null;
    }

    @Override
    public CurPage getCurPage() {
        return CurPage.RSS_SOURCE_GRIDVIEW;
    }
}
