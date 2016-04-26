package com.maple.yuanweinan.feedstar.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.maple.yuanweinan.feedstar.R;
import com.maple.yuanweinan.feedstar.RssSourceAddActivity;
import com.maple.yuanweinan.feedstar.easyadapterhelperlib.BaseViewHolderHelper;
import com.maple.yuanweinan.feedstar.easyadapterhelperlib.EasyAdapter;
import com.maple.yuanweinan.feedstar.lib.RSSFeed;
import com.maple.yuanweinan.feedstar.lib.RSSItem;
import com.maple.yuanweinan.feedstar.manager.FeedStarDataManager;
import com.maple.yuanweinan.feedstar.thread.AdSdkThreadExecutorProxy;
import com.maple.yuanweinan.feedstar.utils.AndroidUtils;
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
    private EditText mSearchEdit;

    private void init(final Context context) {
        inflate(context, R.layout.rss_source_main, this);
        mContext = context;
        mGridView = (GridView) findViewById(R.id.rss_source_gridview_id);
        mSearchEdit = (EditText) findViewById(R.id.rss_source_search_id);
        findViewById(R.id.rss_source_search_Ok_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = mSearchEdit.getText().toString();
                if (TextUtils.isEmpty(word)) {
                    return;
                }

                AndroidUtils.openOnDefaultBrowser(AndroidUtils.BAIDU + word, context);
            }
        });
        mData = FeedStarDataManager.getInstance(mContext).getRssSourceInfo();


        mRssSourceAdapter = new EasyAdapter<RSSFeed, BaseViewHolderHelper>(mContext, R.layout.rss_source_grid_item, mData) {

            @Override
            public void convert(BaseViewHolderHelper viewHolderHelper, final RSSFeed data, int position) {

                if (position == mData.size() - 1 ) {
                    viewHolderHelper.setImageResource(R.id.main_grid_item_bg_id, R.drawable.fs_add_more);
                    viewHolderHelper.getView(R.id.main_grid_item_title_id).setVisibility(View.INVISIBLE);
                    viewHolderHelper.getView(R.id.main_grid_item_bg_id).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            RssSourceSelectDialog loginDialog = new RssSourceSelectDialog(mContext);
//                            loginDialog.show();
                            Intent intent = new Intent();
                            intent.setClass(mContext, RssSourceAddActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        }
                    });
                    return;
                }

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
