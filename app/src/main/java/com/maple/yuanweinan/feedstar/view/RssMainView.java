package com.maple.yuanweinan.feedstar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.maple.yuanweinan.feedstar.DetailWebView;
import com.maple.yuanweinan.feedstar.R;
import com.maple.yuanweinan.feedstar.lib.RSSItem;
import com.maple.yuanweinan.feedstar.manager.FeedStarDataManager;
import com.maple.yuanweinan.feedstar.view.inter.BaseView;

import java.util.List;

/**
 * Created by yuanweinan on 16/3/11.
 */
public class RssMainView extends BaseView {

    public RssMainView(Context context) {
        super(context);
        init(context);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.equals(KeyEvent.KEYCODE_BACK)) {
            return onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    public RssMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private RssSourceMainView mRssSourceMainView;
    private RssItemListMainView mRssItemListMainView;
    private Context mContext;

    private void init(Context context) {
        mContext = context;
        showMainRssItemView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViewById(R.id.fs_search_id).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showRssSourceMainView();
            }
        });
        findViewById(R.id.fs_back_id).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



    public void showMainRssItemView() {
        mRssItemListMainView = new RssItemListMainView(mContext, FeedStarDataManager.getInstance(mContext).getAllRssItems());
//        addView(mRssItemListMainView);
        showView(mRssItemListMainView);
    }

    public void removeMainRssItemView() {
//        removeView(mRssItemListMainView);
        removeTopView();
        mRssItemListMainView = null;
    }

    public void showRssSourceMainView() {
        mRssSourceMainView = new RssSourceMainView(mContext);
//        addView(mRssSourceMainView);
        showView(mRssSourceMainView);
    }

    public void removeRssSourceMainView() {
//        removeView(mRssSourceMainView);
        removeTopView();
        mRssSourceMainView = null;
    }




    @Override
    public CurPage getCurPage() {
        return CurPage.MAINVIEW;
    }
}
