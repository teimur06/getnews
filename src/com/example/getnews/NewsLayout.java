package com.example.getnews;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewsLayout extends LinearLayout{
	LinearLayout linearLayout;
	Button button;
	Resources res;
	ImageView themeImage;
	RelativeLayout relativeLayout;
	public NewsLayout(Context context,final String s) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.newslayout, this);
		linearLayout = (LinearLayout) findViewById(R.id.NewsLayout);
		button = (Button) findViewById(R.id.start);
		themeImage = (ImageView) findViewById(R.id.themeImage);
		relativeLayout = (RelativeLayout)  findViewById(R.id.relativeLayout);
		res = getContext().getResources();
	}

	
	public void setData(NewsStruct data)
	{
		TextView dateText = (TextView) findViewById(R.id.date);
		TextView timeText = (TextView) findViewById(R.id.time);
		TextView topicText = (TextView) findViewById(R.id.theme);
		dateText.setText(data.date);
		timeText.setText(data.time);
		topicText.setText(data.topic);
		if (data.bitmap == null)
		{
			android.view.ViewGroup.LayoutParams lp = relativeLayout.getLayoutParams();
			lp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
			relativeLayout.setLayoutParams(lp);
		}
		themeImage.setImageBitmap(data.bitmap);
		setToSee(data.tosee);
	}	
	
	
	public Button getButton()
	{
		return button;
	}
	
    public void setToSee(boolean ToSee)
    {
		if (ToSee) linearLayout.setBackgroundColor(res.getColor(R.color.See));	
		else linearLayout.setBackgroundColor(res.getColor(R.color.NotSee));	   	
    }
}
