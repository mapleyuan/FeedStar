package com.maple.yuanweinan.feedstar.manager;

import android.app.Activity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.maple.yuanweinan.feedstar.DetailWebView;
import com.maple.yuanweinan.feedstar.R;
import com.maple.yuanweinan.feedstar.SearchActivity;
import com.maple.yuanweinan.feedstar.easyadapterhelperlib.BaseViewHolderHelper;
import com.maple.yuanweinan.feedstar.easyadapterhelperlib.EasyAdapter;
import com.maple.yuanweinan.feedstar.image.AsyncImageLoader;
import com.maple.yuanweinan.feedstar.image.AsyncImageManager;
import com.maple.yuanweinan.feedstar.lib.RSSFeed;
import com.maple.yuanweinan.feedstar.lib.RSSItem;
import com.maple.yuanweinan.feedstar.thread.AdSdkThreadExecutorProxy;
import com.maple.yuanweinan.feedstar.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 主view管理类
 *
 * @author yuanweinan
 */
public class FeedStarMainViewManager {

    private GridView mGridView;
    private ListView mDetailListView;
    private BaseAdapter mDetailListViewAdapter;
    private List<RSSItem> mDetailListData = new ArrayList<>();

    /**
     *
     */
    private enum CurPage {
        MAIN_GRIDVIEW, DETAIL_LIST, DETAIL_WEBVIEW
    }

    ;
    private CurPage mCurPage;

    private EasyAdapter<RSSFeed, BaseViewHolderHelper> mMainListAdapter;

    private DetailWebView mDetailWebView;

    private List<RSSFeed> mData;

    public static FeedStarMainViewManager getInstance() {
        if (sInstance == null) {
            synchronized (FeedStarMainViewManager.class) {
                if (sInstance == null) {
                    sInstance = new FeedStarMainViewManager();
                }
            }
        }
        return sInstance;
    }

    public void onCreate(Activity activity) {
        mActivity = activity;
        mGridView = (GridView) activity.findViewById(R.id.grid_view);
        mDetailListView = (ListView) activity.findViewById(R.id.feedstar_detail_list_id);
        mDetailWebView = (DetailWebView) activity.findViewById(R.id.feedstar_detail_webview_id);
        activity.findViewById(R.id.fs_search_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.start(mActivity.getApplicationContext());
            }
        });
        initData();
    }

    private void initData() {

        mData = FeedStarDataManager.getInstance(mActivity.getApplicationContext()).getRssSourceInfo();

        mMainListAdapter = new EasyAdapter<RSSFeed, BaseViewHolderHelper>(mActivity.getApplicationContext(), R.layout.main_grid_item, mData) {

            @Override
            public void convert(BaseViewHolderHelper viewHolderHelper, final RSSFeed data, int position) {

                viewHolderHelper.setTextView(R.id.main_grid_item_go_id, data.getTitle());

                viewHolderHelper.setOnClickListener(R.id.main_grid_item_go_id, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FeedStarDataManager.getInstance(mContext).requestRssFeed(data, new FeedStarDataManager.IRequestAction() {
                            @Override
                            public void onFinish(final Object... objects) {
                                AdSdkThreadExecutorProxy.runOnMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDetailListData.clear();
                                        mDetailListData.addAll(data.getItems());
                                        mDetailListViewAdapter.notifyDataSetChanged();
                                        showDetailView();
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

        mGridView.setAdapter(mMainListAdapter);

        mDetailListViewAdapter = new EasyAdapter<RSSItem, BaseViewHolderHelper>(mActivity.getApplicationContext(), R.layout.detail_list_item, mDetailListData) {
            @Override
            public void convert(BaseViewHolderHelper viewHolderHelper, final RSSItem data, int position) {
                viewHolderHelper.setTextView(R.id.detail_list_item_title, data.getTitle().trim());
                viewHolderHelper.setTextView(R.id.detail_list_item_dec, data.getDescription().trim());

                if (data.getThumbnail() != null) {
                    viewHolderHelper.getImageView(R.id.detail_list_item_thumbnail_id).setVisibility(View.VISIBLE);
                    AsyncImageManager.getInstance(mContext).setImageView(viewHolderHelper.getImageView(R.id.detail_list_item_thumbnail_id), "", data.getThumbnail(), new AsyncImageLoader.ImageScaleConfig(
                            180, 180, false));
                    LogUtil.i("thumbnail不为空,url:" + data.getThumbnail());
                } else {
                    viewHolderHelper.setImageDrawable(R.id.detail_list_item_thumbnail_id, null);
                    viewHolderHelper.getImageView(R.id.detail_list_item_thumbnail_id).setVisibility(View.GONE);
                }
                viewHolderHelper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDetailWebView();
                        mDetailWebView.loadUrl(data.getLink());
                    }
                });

            }
        };

        mDetailListView.setAdapter(mDetailListViewAdapter);

        mCurPage = CurPage.MAIN_GRIDVIEW;


    }

    public void onBackPressed() {
        switch (mCurPage) {
            case MAIN_GRIDVIEW:
                mActivity.finish();
                break;
            case DETAIL_LIST:
                showMainView();
                break;
            case DETAIL_WEBVIEW:
                showDetailView();
                break;
        }
    }

    public void onDestroy() {
        mActivity = null;
    }

    private void showMainView() {
        mCurPage = CurPage.MAIN_GRIDVIEW;
        mGridView.setVisibility(View.VISIBLE);
        mDetailWebView.setVisibility(View.GONE);
        mDetailListView.setVisibility(View.GONE);
    }

    private void showDetailView() {
        mCurPage = CurPage.DETAIL_LIST;
        mGridView.setVisibility(View.GONE);
        mDetailWebView.setVisibility(View.GONE);
        mDetailListView.setVisibility(View.VISIBLE);
    }

    private void showDetailWebView() {
        mCurPage = CurPage.DETAIL_WEBVIEW;
        mGridView.setVisibility(View.GONE);
        mDetailWebView.setVisibility(View.VISIBLE);
        mDetailListView.setVisibility(View.GONE);
    }


    private static volatile FeedStarMainViewManager sInstance;
    private Activity mActivity;

    private FeedStarMainViewManager() {

    }
}
