package com.example.getnews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;


// Класс загружающий XML
class ThreadLoadXML extends AsyncTask<Void, Void, Void>
{
	private static final String LOG = "ThreadLoadXML";
	boolean connectError = false;
	String localError = "";
	String host = "http://informburo.kz/xml/rss.xml";
	String XMLString= "";
	String line = "";
	String TEG_XML = "";
	MainActivity activity;
	

	
	ThreadLoadXML(MainActivity activity)
	{
		this.activity = activity;
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		try {
			URL url = new URL(host);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();	
	        connection.setDoInput(true);
	        connection.setReadTimeout(2500);		
	        connection.connect();
	        StringBuilder builder = new StringBuilder();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        while((line = reader.readLine()) != null)
	        {
	            builder.append(line);
	        }
	        XMLString = builder.toString();
	        loadXML(XMLString);
		}  catch (IOException e) {return null;}
		
		return null;
	}

	// Загрузка BitMap по URL
	private Bitmap getBitmapFromURL(String src) {
	    try {
	    	Bitmap myBitmap;
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.setReadTimeout(2500);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	        
	    } catch (IOException e) {return null;}
	}

	private void loadXML(String XML)
	{
		activity.arrayNewsStruct = new ArrayList<NewsStruct>();
		NewsStruct newsStruct = null;
		try {
			
			XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
			xmlPullParserFactory.setNamespaceAware(true);
			XmlPullParser xml = xmlPullParserFactory.newPullParser();
			xml.setInput(new StringReader(XML));
			int eventType = xml.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				switch (eventType) {
				case XmlPullParser.START_TAG:
					if (xml.getName().contains("item"))	
						{
							newsStruct = new NewsStruct();
							TEG_XML = "";
						} else TEG_XML = xml.getName();
					break;
					
					
				case XmlPullParser.TEXT:
					if (newsStruct != null)
					{
						if (TEG_XML.contains("title"))
						{
							newsStruct.topic = xml.getText();
							TEG_XML = "";
						}
						if (TEG_XML.contains("link")) 
							{
								newsStruct.http = xml.getText();
								TEG_XML = "";
							}
						if (TEG_XML.contains("pubDate")) 
							{
								newsStruct.date = xml.getText();
								TEG_XML = "";
							}
						if (TEG_XML.contains("url")) 
							{
								newsStruct.bitmap = getBitmapFromURL(xml.getText());
								TEG_XML = "";
							}
					}
					break;
					
				case XmlPullParser.END_TAG:
					if (xml.getName().contains("item"))	
					{
						newsStruct.tosee = false;
						activity.arrayNewsStruct.add(newsStruct);
						newsStruct = null;
					}
					break;
				default:
					break;
				}
				eventType = xml.next();
			}
		} catch (XmlPullParserException e) {
			Log.e(LOG,"XmlPullParserException ="+ e.getMessage());
			localError = activity.getResources().getString(R.string.ErrorXMLParcer)+" '"+e.getMessage()+"'";
			connectError = true;
		} catch (IOException e) {
			Log.e(LOG,"IOException = " +e.getMessage());
			localError = activity.getResources().getString(R.string.ErrorXMLParcer)+" '"+e.getMessage()+"'";
			connectError = true;
			return;
		}
	}		
	
	@Override
	protected void onPostExecute(Void result) {
		Log.i(LOG, "onPostExecute");
		
		if (connectError) activity.errorLoadXML(localError);
		else
		  if (!XMLString.isEmpty()) 
		  {
				NewsAdapter adapter = new NewsAdapter(activity,activity.arrayNewsStruct);
				activity.listView.setAdapter(adapter);	
				activity.gifImageView.setVisibility(View.INVISIBLE);
				activity.listView.setVisibility(View.VISIBLE);
		  }
		  else 
			  activity.errorLoadXML(activity.getResources().getString(R.string.noXML));
		
		super.onPostExecute(result);
	}		
}
