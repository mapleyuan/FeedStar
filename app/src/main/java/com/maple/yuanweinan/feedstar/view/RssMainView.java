package com.maple.yuanweinan.feedstar.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;

import com.maple.yuanweinan.feedstar.DetailWebView;
import com.maple.yuanweinan.feedstar.R;
import com.maple.yuanweinan.feedstar.easyadapterhelperlib.BaseViewHolderHelper;
import com.maple.yuanweinan.feedstar.easyadapterhelperlib.EasyAdapter;
import com.maple.yuanweinan.feedstar.image.AsyncImageLoader;
import com.maple.yuanweinan.feedstar.image.AsyncImageManager;
import com.maple.yuanweinan.feedstar.lib.RSSItem;
import com.maple.yuanweinan.feedstar.manager.FeedStarDataManager;
import com.maple.yuanweinan.feedstar.thread.AdSdkThreadExecutorProxy;
import com.maple.yuanweinan.feedstar.utils.AndroidUtils;
import com.maple.yuanweinan.feedstar.utils.LogUtil;
import com.maple.yuanweinan.feedstar.view.inter.BaseView;
import com.maple.yuanweinan.feedstar.view.listview.widget.SimpleFooter;
import com.maple.yuanweinan.feedstar.view.listview.widget.SimpleHeader;
import com.maple.yuanweinan.feedstar.view.listview.widget.ZrcListView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by yuanweinan on 16/3/11.
 */
public class RssMainView extends BaseView {

    public RssMainView(Context context) {
        super(context);
        init(context);
    }

    public RssMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private Context mContext;
    private ZrcListView mDetailListView;
    private BaseAdapter mDetailListViewAdapter;
    private List<RSSItem> mDetailListData;
    private View mHasContentView;
    private View mNoContentView;

    private void init(Context context) {
        mContext = context;
        inflate(context, R.layout.fs_rss_home, this);

        mHasContentView = findViewById(R.id.fs_has_content_id);
        mNoContentView = findViewById(R.id.fs_no_content_id);
        mDetailListView = (ZrcListView) findViewById(R.id.feedstar_detail_list_id);
        // 设置下拉刷新的样式
        SimpleHeader header = new SimpleHeader(context);
        header.setTextColor(0xff0066aa);
        header.setCircleColor(0xff33bbee);
        mDetailListView.setHeadable(header);

// 设置加载更多的样式
        SimpleFooter footer = new SimpleFooter(context);
        footer.setCircleColor(0xff33bbee);
        mDetailListView.setFootable(footer);

// 设置列表项出现动画
//        listView.setItemAnimForTopIn(R.anim.topitem_in);
//        listView.setItemAnimForBottomIn(R.anim.bottomitem_in);

// 下拉刷新事件回调
        mDetailListView.setOnRefreshStartListener(new ZrcListView.OnStartListener() {
            @Override
            public void onStart() {
                LogUtil.i("refresh()");
                FeedStarDataManager.getInstance(mContext).isNeedToUpdate(new FeedStarDataManager.IFeedStarResultListener() {
                    @Override
                    public void onFinish() {
                        mDetailListView.setRefreshSuccess();
                    }
                });
            }
        });

// 加载更多事件回调
//        mDetailListView.setOnLoadMoreStartListener(new ZrcListView.OnStartListener() {
//            @Override
//            public void onStart() {
//                LogUtil.i("loadMore()");
//
//            }
//        });

        mDetailListData = FeedStarDataManager.getInstance(mContext).getAllRssItems();
        FeedStarDataManager.getInstance(mContext).addFeedDataChangeListener(new FeedStarDataManager.IFeedStarDataChangeListener() {
            @Override
            public void onListDataChange(List<RSSItem> items) {
                AdSdkThreadExecutorProxy.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        showContentView(mDetailListData.size() > 0 ? VISIBLE : GONE);
                    mDetailListViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        mDetailListViewAdapter = new EasyAdapter<RSSItem, BaseViewHolderHelper>(mContext, R.layout.detail_list_item, mDetailListData) {
            @Override
            public void convert(BaseViewHolderHelper viewHolderHelper, final RSSItem data, int position) {
                if (data.getTitle() == null) {
                    return;
                }
                viewHolderHelper.setTextView(R.id.detail_list_item_title, data.getTitle().trim());
                viewHolderHelper.setTextView(R.id.detail_list_item_dec, data.getDescription().trim());

                if (!TextUtils.isEmpty(data.getThumbnail())) {
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
//                        showDetailView();
//                        mDetailWebView.loadUrl(data.getLink());
                        MobclickAgent.onEvent(mContext, "homeitemclickurl", data.getLink());
                        AndroidUtils.openOnDefaultBrowser(data.getLink(), mContext);
                    }
                });

            }
        };

        mDetailListView.setAdapter(mDetailListViewAdapter);
        showContentView(mDetailListData.size() > 0 ? VISIBLE : GONE);


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void onRightClick() {
        showRssSourceMainView();
    }

    private void showContentView(int visibility) {
        mHasContentView.setVisibility(visibility);
        mNoContentView.setVisibility(visibility == VISIBLE ? GONE : VISIBLE);
    }


    private DetailWebView mDetailWebView;


    private void showDetailView() {
        mDetailWebView = new DetailWebView(mContext);
        showView(mDetailWebView);
    }

    public void showRssSourceMainView() {
        showView(new RssSourceMainView(mContext));
    }

    @Override
    public CurPage getCurPage() {
        return CurPage.MAINVIEW;
    }
}
