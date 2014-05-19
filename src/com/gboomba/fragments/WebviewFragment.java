package com.gboomba.fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.gboomba.R;


public class WebviewFragment extends Fragment {
	private ProgressDialog mProgressDialog;
	ImageView backNavigation;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_webview, container,
				false);
		WebView mWebView = (WebView) view.findViewById(R.id.webview);
		mWebView.setWebViewClient(new mWebClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl("http://beta.gbooomba.com/terms.html");
		backNavigation	=	(ImageView) view.findViewById(R.id.backNavigation);
		backNavigation.setOnClickListener(mOnClickListener);
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage(this.getString(R.string.please_wait));
		return view;
	}

	View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.backNavigation:
				getActivity().finish();
				break;
			}
		}
	};
	public class mWebClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);
			if (mProgressDialog != null && !mProgressDialog.isShowing())
				mProgressDialog.show();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			if (mProgressDialog != null && mProgressDialog.isShowing())
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
