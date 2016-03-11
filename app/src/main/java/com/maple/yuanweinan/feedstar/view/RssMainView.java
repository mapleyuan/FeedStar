package com.maple.yuanweinan.feedstar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.maple.yuanweinan.feedstar.R;
import com.maple.yuanweinan.feedstar.manager.FeedStarDataManager;

/**
 * Created by yuanweinan on 16/3/11.
 */
public class RssMainView extends RelativeLayout {

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
    private CurPage mCurPage;
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
    }

    public boolean onBackPressed() {
        switch (mCurPage) {
            case MAINVIEW:
                return false;
            case RSS_SOURCE_GRIDVIEW:
                removeRssSourceMainView();
                return true;
            default:
                return false;
        }
    }

    public void showMainRssItemView() {
        mRssItemListMainView = new RssItemListMainView(mContext, FeedStarDataManager.getInstance(mContext).getAllRssItems());
        addView(mRssItemListMainView);
        mCurPage = CurPage.MAINVIEW;
    }

    public void removeMainRssItemView() {
        removeView(mRssItemListMainView);
        mRssItemListMainView = null;
        mCurPage = CurPage.MAINVIEW;
    }

    public void showRssSourceMainView() {
        mRssSourceMainView = new RssSourceMainView(mContext);
        addView(mRssSourceMainView);
        mCurPage = CurPage.RSS_SOURCE_GRIDVIEW;
    }

    public void removeRssSourceMainView() {
        removeView(mRssSourceMainView);
        mRssSourceMainView = null;
        mCurPage = CurPage.MAINVIEW;
    }


        /**
         *
         */
    private enum CurPage {
        MAINVIEW, RSS_SOURCE_GRIDVIEW
    }

}
