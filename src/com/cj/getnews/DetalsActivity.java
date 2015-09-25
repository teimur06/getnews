package com.cj.getnews;


import com.cj.getnews.R;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DetalsActivity extends Activity {

	final String LOG = "DetalsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weblayout);
		Log.d(LOG,"onCreate getData: "+getIntent().getDataString());
		WebView webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setJavaScriptEnabled(true);
	    Uri data = getIntent().getData();
	    
	    webView.setWebViewClient(new WebViewClient() 
	    		{
	    	 @Override
	    	    public boolean shouldOverrideUrlLoading(WebView view, String url) 
	    	    {
	    	        view.loadUrl(url);
	    	        setTitle(url);
	    	        return true;
	    	    }
	    	 
	    	 @Override
	    			public void onPageFinished(WebView view, String url) {
	    				// TODO Auto-generated method stub
	    		 	    setTitle(view.getTitle());
	    				super.onPageFinished(view, url);
	    			}
	    	});
	    
	    webView.loadUrl(data.toString());
	    setTitle(data.toString());
	    
	    webView.getSettings().setUseWideViewPort(true);
	    webView.getSettings().setSupportZoom(true);
	    webView.getSettings().setSupportMultipleWindows(true);
	    
    
	}
}
