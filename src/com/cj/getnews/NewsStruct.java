package com.cj.getnews;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;



public class NewsStruct implements Parcelable{
	final static String LOG = "NewsStruct";
	public String date, time, topic, http;
	public boolean  tosee;
	public Bitmap bitmap;
	NewsStruct(String date, String time,String topic, String http, boolean tosee, Bitmap bitmap)
	{
		this.date = date;
		this.time = time;
		this.topic = topic;
		this.http = http;
		this.tosee = tosee;
		this.bitmap = bitmap;
	}
	
	NewsStruct(){}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flag) {
		parcel.writeString(date);
		parcel.writeString(time);
		parcel.writeString(topic);
		parcel.writeString(http);
		parcel.writeByte((byte)(tosee?1:0));
		if (bitmap != null)
		   bitmap.writeToParcel(parcel, flag);
	}

}
