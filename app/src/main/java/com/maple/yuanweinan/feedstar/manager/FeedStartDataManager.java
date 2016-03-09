package com.maple.yuanweinan.feedstar.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.maple.yuanweinan.feedstar.data.RssFeedInfoTable;
import com.maple.yuanweinan.feedstar.db.FeedStarDBHelpler;
import com.maple.yuanweinan.feedstar.lib.RSSFeed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanweinan on 16-3-8.
 */
public class FeedStartDataManager {

    public static FeedStartDataManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (FeedStarManager.class) {
                if (sInstance == null) {
                    sInstance = new FeedStartDataManager(context);
                }
            }
        }
        return sInstance;
    }

    public List<RSSFeed> getRssSourceInfo() {
        return mRssSourceInfo;
    }

    private Context mContext;
    private static volatile FeedStartDataManager sInstance;
    private FeedStarDBHelpler mFeedStarDBHelper;
    private List<RSSFeed> mRssSourceInfo;
    private static final String FS = "fs";
    private static final String IS_INITED = "is_inited";

    private FeedStartDataManager(Context context) {
        mContext = context;
        mRssSourceInfo = new ArrayList<>();
        mFeedStarDBHelper = FeedStarDBHelpler.getInstance(context);
        testData();
        mRssSourceInfo.addAll(RssFeedInfoTable.queryAll(mFeedStarDBHelper));
    }

    /**
     * test
     */
    private void testData() {
        if (getInited()) {
            return;
        }
        saveIsInited(true);
        RSSFeed info = new RSSFeed("sina", "http://rss.sina.com.cn/tech/rollnews.xml", "");
        RssFeedInfoTable.insert(mFeedStarDBHelper, info);
        info = new RSSFeed("zhihu", "http://www.zhihu.com/rss", "");
        RssFeedInfoTable.insert(mFeedStarDBHelper, info);
        info = new RSSFeed("nhzy资讯", "http://www.nhzy.org/feed", "");
        RssFeedInfoTable.insert(mFeedStarDBHelper, info);
        info = new RSSFeed("科学松鼠会", "http://songshuhui.net/feed", "");
        RssFeedInfoTable.insert(mFeedStarDBHelper, info);
        info = new RSSFeed("爱范儿", "http://www.ifanr.com/feed", "");
        RssFeedInfoTable.insert(mFeedStarDBHelper, info);
    }

    private boolean getInited() {
        SharedPreferences sp = mContext.getSharedPreferences(FS, Context.MODE_PRIVATE);
        return sp.getBoolean(IS_INITED, false);
    }

    private void saveIsInited(boolean isInited) {
        SharedPreferences sp = mContext.getSharedPreferences(FS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_INITED, isInited);
        editor.commit();
    }

}
