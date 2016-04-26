package com.maple.yuanweinan.feedstar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.maple.yuanweinan.feedstar.view.RssMainView;

import static android.view.View.INVISIBLE;

/**
 * @author yuanweinan
 */
public class MainActivity extends Activity {

    private RssMainView mRssMainView;
    private View mRightView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRssMainView = (RssMainView) findViewById(R.id.fs_rssmainview_id);
        mRightView = findViewById(R.id.fs_top_right_id);
        mRightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRightView.setVisibility(INVISIBLE);
                mRssMainView.onRightClick();
            }
        });

        findViewById(R.id.fs_top_left_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRightView.setVisibility(View.VISIBLE);
                onBack();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        mRightView.setVisibility(View.VISIBLE);
        if (!mRssMainView.onBackPressed()) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
