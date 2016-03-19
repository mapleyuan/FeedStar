package com.maple.yuanweinan.feedstar.manager;

import android.app.Activity;
import android.util.SparseArray;
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
import com.maple.yuanweinan.feedstar.view.RssMainView;

import java.util.ArrayList;
import java.util.List;

/**
 * 主view管理类
 *
 * @author yuanweinan
 */
public class FeedStarMainViewManager {




    private DetailWebView mDetailWebView;
    private RssMainView mRssMainView;



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
        mRssMainView = (RssMainView) activity.findViewById(R.id.fs_rssmainview_id);
        activity.findViewById(R.id.fs_back_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        mDetailWebView = (DetailWebView) activity.findViewById(R.id.feedstar_detail_webview_id);
//        activity.findViewById(R.id.fs_search_id).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SearchActivity.start(mActivity.getApplicationContext());
//            }
//        });
        initData();
    }

    public boolean onBackPressed() {
        return mRssMainView.onBackPressed();
    }

    private void initData() {
    }



    public void onDestroy() {
        mActivity = null;
    }




    private static volatile FeedStarMainViewManager sInstance;
    private Activity mActivity;


    private FeedStarMainViewManager() {

    }
}
