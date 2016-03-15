package com.maple.yuanweinan.feedstar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.maple.yuanweinan.feedstar.R;
import com.maple.yuanweinan.feedstar.manager.FeedStarDataManager;
import com.maple.yuanweinan.feedstar.view.inter.BaseView;

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
        addView(new RssItemListMainView(mContext, FeedStarDataManager.getInstance(mContext).getAllRssItems()));
    }

    public void showRssSourceMainView() {
        showView(new RssSourceMainView(mContext));
    }

    @Override
    public CurPage getCurPage() {
        return CurPage.MAINVIEW;
    }
}
