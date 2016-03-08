package com.maple.yuanweinan.feedstar.data;

/**
 * Created by yuanweinan on 16-3-8.
 */
public class RssSourceInfo {

    public int mID;
    public String mName;
    public String mRssAddress;
    public String mRssThumbnail;

    public RssSourceInfo() {

    }
    public RssSourceInfo(String name, String rssAddress, String rssThunmbnail) {
        mName = name;
        mRssThumbnail = rssThunmbnail;
        mRssAddress = rssAddress;
    }
}
