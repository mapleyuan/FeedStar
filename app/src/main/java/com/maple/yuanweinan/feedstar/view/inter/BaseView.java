package com.maple.yuanweinan.feedstar.view.inter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.maple.yuanweinan.feedstar.R;


/**
 * Created by yuanweinan on 16/3/14.
 */

public abstract class BaseView extends RelativeLayout implements IBaseView {


    private static SparseArray<BaseView> mViewStack = new SparseArray<>();

    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.equals(KeyEvent.KEYCODE_BACK)) {
            return onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showView(BaseView view) {
        mViewStack.append(mViewStack.size(), view);
        addView(view);
    }
    @Override
    public boolean removeTopView() {
        if (mViewStack.size() <= 0) {
            return false;
        }
        View view = mViewStack.get(mViewStack.size() - 1);
        if(mViewStack.size() > 1) {
            mViewStack.get(mViewStack.size() - 2).removeView(view);
        } else {
            removeView(view);
        }
        mViewStack.remove(mViewStack.size() - 1);
        return true;
    }

    public BaseView getTopView() {
       return mViewStack.get(mViewStack.size() - 1);
    }

    public boolean onBackPressed() {
        if (mViewStack.size() <= 0) {
            return false;
        }
        CurPage curPage = getTopView().getCurPage();
        switch (curPage) {
            case MAINVIEW:
                return false;
            case RSS_SOURCE_GRIDVIEW:
            case RSS_ITEM_LIST_VIEW:
            case RSS_WEBVIEW:
                    removeTopView();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Deprecated
    @Override
    public void removeView(View view) {
        super.removeView(view);
    }
@Deprecated
    @Override
    public void addView(View child) {
        super.addView(child);
    }
}

/**
 * @author yuanweinan
 */
 interface IBaseView {

    /**
     *
     */
     enum CurPage {
        MAINVIEW, RSS_SOURCE_GRIDVIEW, RSS_ITEM_LIST_VIEW, RSS_WEBVIEW
    }

    CurPage getCurPage();

    void showView(BaseView view);

     boolean removeTopView();
}
