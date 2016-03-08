package com.maple.yuanweinan.feedstar.manager;

import android.content.Context;

/**
 * Created by yuanweinan on 16-3-8.
 */
public class FeedStarManager {

    public static FeedStarManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (FeedStarManager.class) {
                if (sInstance == null) {
                    sInstance = new FeedStarManager(context);
                }
            }
        }
        return sInstance;
    }

    private Context mContext;
    private static volatile FeedStarManager sInstance;

    private FeedStarManager(Context context) {
        mContext = context;

    }
}
