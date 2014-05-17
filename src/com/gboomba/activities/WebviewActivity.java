package com.gboomba.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gboomba.R;

public class WebviewActivity extends FragmentActivity {
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.enter, R.anim.exit);
		WebView mWebView	=	new WebView(this);
		mWebView.setWebViewClient(new mWebClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl("http://beta.gbooomba.com/terms.html");
		setContentView(mWebView);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(this.getString(R.string.please_wait));
	
	}

	protected void onPause() {
		super.onPause();
		this.overridePendingTransition(R.anim.pop_enter, R.anim.pop_exit);

	}
	
	 public class mWebClient extends WebViewClient
	    {
	        @Override
	        public void onPageStarted(WebView view, String url, Bitmap favicon) {
	            // TODO Auto-generated method stub
	            super.onPageStarted(view, url, favicon);
	            if(mProgressDialog!=null&&!mProgressDialog.isShowing())
	            mProgressDialog.show();
	        }
	 
	        @Override
	        public void onPageFinished(WebView view, String url) {
	            // TODO Auto-generated method stub
	            super.onPageFinished(view, url);
	            if(mProgressDialog!=null&&mProgressDialog.isShowing())
		            mProgressDialog.dismiss();
	        }
	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            // TODO Auto-generated method stub
	 
	            view.loadUrl(url);
	            return true;
	 
	        }
	    }

}
