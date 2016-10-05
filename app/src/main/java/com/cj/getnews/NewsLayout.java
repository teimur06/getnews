package com.cj.getnews;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;





import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewsLayout extends LinearLayout{
    private static final String LOG = "NewsLayout";
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
		TextView topicText = (TextView) findViewById(R.id.theme);

        SimpleDateFormat inFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z", Locale.ENGLISH);
        SimpleDateFormat outFormat = new SimpleDateFormat("dd.M.yyyy HH:mm:ss");
        try {
            Date date = inFormat.parse(data.date);
            dateText.setText(outFormat.format(date));

        } catch (ParseException e) {
            Log.e(LOG,e.getMessage() );
        } finally {

        }


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
