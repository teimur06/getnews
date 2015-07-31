package com.example.getnews;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

public class NewsAdapter extends BaseAdapter {

	final String LOG = "NewsAdapter";
	Context context;
	ArrayList<NewsStruct> items;
	NewsAdapter(Context context,ArrayList<NewsStruct> items)
	{
		this.context = context;
		this.items = items;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view = arg1;
		if (view == null)
		{
			view = new NewsLayout(context, Integer.toString(arg0));
		}
		//Log.d(LOG, Integer.toString(arg0));
		((NewsLayout)view).setData((NewsStruct)getItem(arg0) );
		Button button = ((NewsLayout)view).getButton();
		button.setTag(R.string.KEY1,arg0);
		button.setTag(R.string.KEY2,view);
		button.setOnClickListener(click);
		
		return view;
	}
	
	public void add(NewsStruct newsStruct)
	{
		Log.d(LOG, newsStruct.topic);
		items.add(newsStruct);
	}
	
	OnClickListener click = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			Integer tag1 = (Integer) view.getTag(R.string.KEY1);
			NewsStruct newsStruct = items.get(tag1);
			NewsLayout newsLayout = (NewsLayout)view.getTag(R.string.KEY2);
			
			newsStruct.tosee = true;
			newsLayout.setToSee(true);
			Intent intent = new Intent("com.example.getnews.DetalsActivity",Uri.parse(newsStruct.http));
			context.startActivity(intent);
		}
	};

}
