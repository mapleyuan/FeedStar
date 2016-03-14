package com.maple.yuanweinan.feedstar.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.maple.yuanweinan.feedstar.R;
import com.maple.yuanweinan.feedstar.easyadapterhelperlib.BaseViewHolderHelper;
import com.maple.yuanweinan.feedstar.easyadapterhelperlib.EasyAdapter;
import com.maple.yuanweinan.feedstar.image.AsyncImageLoader;
import com.maple.yuanweinan.feedstar.image.AsyncImageManager;
import com.maple.yuanweinan.feedstar.lib.RSSItem;
import com.maple.yuanweinan.feedstar.utils.LogUtil;
import com.maple.yuanweinan.feedstar.view.inter.BaseView;

import java.util.List;

/**
 * Created by yuanweinan on 16/3/11.
 */
public class RssItemListMainView extends BaseView {
    public RssItemListMainView(Context context, List<RSSItem> rssItems) {
        super(context);
        init(context, rssItems);
    }

    private Context mContext;
    private ListView mDetailListView;
    private BaseAdapter mDetailListViewAdapter;
    private List<RSSItem> mDetailListData;

    private void init(Context context, List<RSSItem> rssItems) {
        mContext = context;
        inflate(context, R.layout.fs_rss_listview, this);
        mDetailListView = (ListView) findViewById(R.id.feedstar_detail_list_id);
        mDetailListData = rssItems;
        mDetailListViewAdapter = new EasyAdapter<RSSItem, BaseViewHolderHelper>(mContext, R.layout.detail_list_item, mDetailListData) {
            @Override
            public void convert(BaseViewHolderHelper viewHolderHelper, final RSSItem data, int position) {
                if (data.getTitle() == null) {
                    return;
                }
                viewHolderHelper.setTextView(R.id.detail_list_item_title, data.getTitle().trim());
                viewHolderHelper.setTextView(R.id.detail_list_item_dec, data.getDescription().trim());

                if (!TextUtils.isEmpty(data.getThumbnail())) {
                    viewHolderHelper.getImageView(R.id.detail_list_item_thumbnail_id).setVisibility(View.VISIBLE);
                    AsyncImageManager.getInstance(mContext).setImageView(viewHolderHelper.getImageView(R.id.detail_list_item_thumbnail_id), "", data.getThumbnail(), new AsyncImageLoader.ImageScaleConfig(
                            180, 180, false));
                    LogUtil.i("thumbnail不为空,url:" + data.getThumbnail());
                } else {
                    viewHolderHelper.setImageDrawable(R.id.detail_list_item_thumbnail_id, null);
                    viewHolderHelper.getImageView(R.id.detail_list_item_thumbnail_id).setVisibility(View.GONE);
                }
                viewHolderHelper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        showDetailWebView();
//                        mDetailWebView.loadUrl(data.getLink());
                    }
                });

            }
        };

        mDetailListView.setAdapter(mDetailListViewAdapter);
    }

    @Override
    public CurPage getCurPage() {
        return CurPage.RSS_ITEM_LIST_VIEW;
    }
}
