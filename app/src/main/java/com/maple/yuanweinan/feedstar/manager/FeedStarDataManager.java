package com.maple.yuanweinan.feedstar.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseArray;

import com.maple.yuanweinan.feedstar.data.GroupInfo;
import com.maple.yuanweinan.feedstar.data.GroupInfoTable;
import com.maple.yuanweinan.feedstar.data.RssFeedInfoTable;
import com.maple.yuanweinan.feedstar.data.RssItemTable;
import com.maple.yuanweinan.feedstar.db.FeedStarDBHelpler;
import com.maple.yuanweinan.feedstar.lib.RSSFeed;
import com.maple.yuanweinan.feedstar.lib.RSSItem;
import com.maple.yuanweinan.feedstar.lib.RSSReader;
import com.maple.yuanweinan.feedstar.lib.RSSReaderException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanweinan on 16-3-8.
 */
public class FeedStarDataManager {

    /**
     *
     */
    public static interface IRequestAction {
        void onFinish(Object... objects);
        void onFail();
    }

    /**
     *
     */
    public interface IFeedStarDataChangeListener {
        void onListDataChange(List<RSSItem> items);
    }

    public void addFeedDataChangeListener(IFeedStarDataChangeListener feedStarDataChangeListener) {
        if (feedStarDataChangeListener == null) {
            return;
        }

        synchronized (mFeedDataChangeListenerList) {
            if (!mFeedDataChangeListenerList.contains(feedStarDataChangeListener)) {
                mFeedDataChangeListenerList.add(feedStarDataChangeListener);
            }
        }
    }

    public void removeFeedDataChangeListener(IFeedStarDataChangeListener feedStarDataChangeListener) {
        if (feedStarDataChangeListener != null) {
            return;
        }

        synchronized (mFeedDataChangeListenerList) {
            mFeedDataChangeListenerList.remove(feedStarDataChangeListener);
        }
    }

    private List<IFeedStarDataChangeListener> getFeedDataChangeListenerCopy() {
        synchronized (mFeedDataChangeListenerList) {
            return (List<IFeedStarDataChangeListener>) mFeedDataChangeListenerList.clone();
        }
    }

    private void notifyFeedDataChanged(List<RSSItem> newFeedDatas) {
        List<IFeedStarDataChangeListener> listeners = getFeedDataChangeListenerCopy();
        for (IFeedStarDataChangeListener listener : listeners) {
            listener.onListDataChange(newFeedDatas);
        }
    }

    public static FeedStarDataManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (FeedStarManager.class) {
                if (sInstance == null) {
                    sInstance = new FeedStarDataManager(context);
                }
            }
        }
        return sInstance;
    }

    public List<RSSFeed> getRssSourceInfo() {
        return mRssFeedList;
    }

    public List<RSSItem> getAllRssItems() {
        return mAllRssItems;
    }

    public void requestRssFeed(final RSSFeed rssFeed, final IRequestAction iRequestAction) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<RSSItem> items = RssItemTable.query(rssFeed.mID, mFeedStarDBHelper);
                if (items.size() > 0) {
                    rssFeed.clearAllItem();
                    rssFeed.addAllItem(items);
                    if (iRequestAction != null) {
                        iRequestAction.onFinish(rssFeed);
                    }
                    return;
                } else {
                    RSSReader reader = new RSSReader();
//                                String uri = "http://rss.sina.com.cn/tech/rollnews.xml";
                    String uri = rssFeed.getLink();
                    try {
                        RSSFeed feed = reader.load(uri);
                        rssFeed.clearAllItem();
                        rssFeed.addAllItem(feed.getItems());
                        if (iRequestAction != null) {
                            iRequestAction.onFinish(rssFeed);
                        }
                        List<RSSItem> itemss = feed.getItems();
                        for (int i = 0; i < itemss.size(); i++) {
                            RssItemTable.insert(mFeedStarDBHelper, itemss.get(i));
                        }

                        notifyFeedDataChanged(itemss);

                    } catch (RSSReaderException e) {
                        if (iRequestAction != null) {
                            iRequestAction.onFail();
                        }
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void isNeedToUpdate() {
        int size = mRssFeedList.size();
        for (int i = 0; i < size; i++) {
            RSSFeed rssFeed = mRssFeedList.get(i);
            requestRssFeed(rssFeed, new IRequestAction() {
                @Override
                public void onFinish(Object... objects) {

                }

                @Override
                public void onFail() {

                }
            });
        }
    }

    private Context mContext;
    private static volatile FeedStarDataManager sInstance;
    private FeedStarDBHelpler mFeedStarDBHelper;
    private List<RSSFeed> mRssFeedList;
    private List<GroupInfo> mGroupInfoList;
    private List<RSSItem> mAllRssItems;
    private static final String FS = "fs";
    private static final String IS_INITED = "is_inited";
    private ArrayList<IFeedStarDataChangeListener> mFeedDataChangeListenerList = new ArrayList<>();

    private FeedStarDataManager(Context context) {
        mContext = context;
        mFeedStarDBHelper = FeedStarDBHelpler.getInstance(context);
        testData();
        mGroupInfoList = GroupInfoTable.queryAll(mFeedStarDBHelper);
        mRssFeedList = RssFeedInfoTable.queryAll(mFeedStarDBHelper);
        mAllRssItems = RssItemTable.queryAll(mFeedStarDBHelper);
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
