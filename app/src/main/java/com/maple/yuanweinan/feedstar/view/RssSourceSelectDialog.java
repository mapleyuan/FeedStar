package com.maple.yuanweinan.feedstar.view;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.maple.yuanweinan.feedstar.R;
import com.maple.yuanweinan.feedstar.data.RssFeedInfoTable;
import com.maple.yuanweinan.feedstar.db.FeedStarDBHelpler;
import com.maple.yuanweinan.feedstar.lib.RSSFeed;

/**
 * 登录选择框
 * @author yuanweinan
 *
 */
public class RssSourceSelectDialog {
	private Context mContext;
	private Dialog mDialog;
	private Button mButton;
	private long mLastClickTime;
	private ILoginListener mLoginListener;
	private boolean mIsLoadTokenCoinInfo;
	private EditText mTitleEdit;
	private EditText mUrlEdit;


	/**
	 * 登陆监听
	 *
	 * @author matt
	 * @date: 2015年11月9日
	 *
	 */
	public static interface ILoginListener {
		/**
		 * 登录成功
		 */
		void onLoginSuccess();
		/**
		 * 登录失败
		 */
		void onLoginFail();
	}


	/**
	 */
	@SuppressLint("NewApi")
	public RssSourceSelectDialog(Context activity) {
		mContext = activity.getApplicationContext();
		mDialog = new Dialog(activity, R.style.tokencoin_dialog);
		View view = LayoutInflater.from(activity.getApplicationContext()).inflate(R.layout.cp_tokencoin_login_dialog_view, null);
		view.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		mDialog.setContentView(view);
		mButton = (Button) view.findViewById(R.id.login_dialog_ok_id);
		mTitleEdit = (EditText) view.findViewById(R.id.fs_rss_source_title_id);
		mUrlEdit = (EditText) view.findViewById(R.id.fs_rss_source_url_id);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isFastDoubleClick()) {
					return;
				}
				String title = mTitleEdit.getText().toString();
				String url = mUrlEdit.getText().toString();
				if (TextUtils.isEmpty(title) || TextUtils.isEmpty(url)) {
					Toast.makeText(mContext, "title or url no valid!", Toast.LENGTH_LONG).show();
					return;
				}
				RSSFeed info = new RSSFeed(title, url, "");
				RssFeedInfoTable.insert(FeedStarDBHelpler.getInstance(mContext), info);

				mDialog.dismiss();
			}
		});
	}

	public void show() {
		if (mDialog != null) {
			mDialog.show();
		}
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
}
