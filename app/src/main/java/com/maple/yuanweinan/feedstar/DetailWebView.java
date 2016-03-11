package com.maple.yuanweinan.feedstar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


/**
 * 消息中心webview
 * 
 * @author yuanweinan
 *
 */
public class DetailWebView extends RelativeLayout {

	private WebView mWebView;

	private final static float DENSITY_H = 1.5f;
	private final static float DENSITY_L = 2.0f;

	public DetailWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		inflate(context, R.layout.rss_web_detail, this);
	}

	public DetailWebView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		inflate(context, R.layout.rss_web_detail, this);
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		mWebView = (WebView) findViewById(R.id.webview_id);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setDomStorageEnabled(true);
		webSettings.setDefaultTextEncodingName("utf-8");
		webSettings.setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url);
				return true;
			}
		});
	}

	public void loadUrl(String url) {
		if (mWebView != null) {
			mWebView.loadUrl(url);
		}
	}

	/**
	 * 返回webveiw历史记录
	 * 
	 * @return
	 */
	public boolean goBackHistory() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return false;
	}

	/**
	 * <br>
	 * 功能简述:可以让不同的density的情况下，可以让页面进行适配 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	private void setWebViewDensity() {
		DisplayMetrics dm = new DisplayMetrics();
		dm = this.getResources().getDisplayMetrics();
		float density = dm.density;
		if (density == DENSITY_H) {
			mWebView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		} else if (density == DENSITY_L) {
			mWebView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		} else {
			mWebView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		}
	}

}
