package com.maple.yuanweinan.feedstar.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by yuanweinan on 16-4-26.
 */
public class AndroidUtils {

    public static final String BAIDU = "https://www.baidu.com/s?wd=";

    public static void openOnDefaultBrowser(String url, Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        intent.setClassName("com.android.browser","com.android.browser.BrowserActivity");
        context.startActivity(intent);
    }
}
