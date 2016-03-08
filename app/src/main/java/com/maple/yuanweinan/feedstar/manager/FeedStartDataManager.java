package com.maple.yuanweinan.feedstar.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.maple.yuanweinan.feedstar.data.RssSourceInfo;
import com.maple.yuanweinan.feedstar.data.RssSourceInfoTable;
import com.maple.yuanweinan.feedstar.db.FeedStarDBHelpler;

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

    public List<RssSourceInfo> getRssSourceInfo() {
        return mRssSourceInfo;
    }

    private Context mContext;
    private static volatile FeedStartDataManager sInstance;
    private FeedStarDBHelpler mFeedStarDBHelper;
    private List<RssSourceInfo> mRssSourceInfo;
    private static final String FS = "fs";
    private static final String IS_INITED = "is_inited";

    private FeedStartDataManager(Context context) {
        mContext = context;
        mRssSourceInfo = new ArrayList<>();
        mFeedStarDBHelper = FeedStarDBHelpler.getInstance(context);
        testData();
        mRssSourceInfo.addAll(RssSourceInfoTable.queryAll(mFeedStarDBHelper));
    }

    /**
     * test
     */
    private void testData() {
        if (getInited()) {
            return;
        }
        saveIsInited(true);
        RssSourceInfo info = new RssSourceInfo("sina", "http://rss.sina.com.cn/tech/rollnews.xml", "");
        RssSourceInfoTable.insert(mFeedStarDBHelper, info);
        info = new RssSourceInfo("zhihu", "http://www.zhihu.com/rss", "");
        RssSourceInfoTable.insert(mFeedStarDBHelper, info);
        info = new RssSourceInfo("nhzy资讯", "http://www.nhzy.org/feed", "");
        RssSourceInfoTable.insert(mFeedStarDBHelper, info);
        info = new RssSourceInfo("科学松鼠会", "http://songshuhui.net/feed", "");
        RssSourceInfoTable.insert(mFeedStarDBHelper, info);
        info = new RssSourceInfo("爱范儿", "http://www.ifanr.com/feed", "");
        RssSourceInfoTable.insert(mFeedStarDBHelper, info);
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
