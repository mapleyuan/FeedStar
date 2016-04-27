package com.maple.yuanweinan.feedstar;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maple.yuanweinan.feedstar.data.RssFeedInfoTable;
import com.maple.yuanweinan.feedstar.db.FeedStarDBHelpler;
import com.maple.yuanweinan.feedstar.lib.RSSFeed;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by yuanweinan on 16-4-26.
 */
public class RssSourceAddActivity extends Activity {
    private EditText mTitleEdit;
    private EditText mUrlEdit;
    private Button mButton;
    private long mLastClickTime;


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("RssSourceAddActivity");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("RssSourceAddActivity");
        MobclickAgent.onPause(this);
    }
    /**
     * 快速重复点击
     * @return
     */
    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - mLastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        mLastClickTime = time;
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cp_tokencoin_login_dialog_view);
        mButton = (Button) findViewById(R.id.login_dialog_ok_id);
        mTitleEdit = (EditText) findViewById(R.id.fs_rss_source_title_id);
        mUrlEdit = (EditText) findViewById(R.id.fs_rss_source_url_id);
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isFastDoubleClick()) {
                    return;
                }
                String title = mTitleEdit.getText().toString();
                String url = mUrlEdit.getText().toString();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(url)) {
                    Toast.makeText(getApplicationContext(), "title or url no valid!", Toast.LENGTH_LONG).show();
                    return;
                }
                RSSFeed info = new RSSFeed(title, url, "");
                RssFeedInfoTable.insert(FeedStarDBHelpler.getInstance(getApplicationContext()), info);

                finish();
            }
        });

    }
}
