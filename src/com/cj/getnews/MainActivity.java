package com.cj.getnews;


import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.cj.getnews.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String LOG = "MainActivity";
	ArrayList<NewsStruct> arrayNewsStruct;

	
	ListView listView;
	GifImageView gifImageView;
	Button reconnectButton;
	MyAsyncTask oMyAsyncTask;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.lvMain);
		gifImageView = (GifImageView) findViewById(R.id.gifImageView);
		reconnectButton = (Button) findViewById(R.id.reconnectButton);
		gifImageView.loadResurce(R.drawable.loading);
	
	
		if (savedInstanceState == null)
		{
			oMyAsyncTask = new MyAsyncTask();
			oMyAsyncTask.execute();
		} else
		{
			arrayNewsStruct = savedInstanceState.getParcelableArrayList("arrayNewsStruct");
			if(arrayNewsStruct != null)
			{
				NewsAdapter adapter = new NewsAdapter(this,arrayNewsStruct);
				listView.setAdapter(adapter);
				gifImageView.setVisibility(View.INVISIBLE);
				listView.setVisibility(View.VISIBLE);
			}
			reconnectButton.setVisibility(savedInstanceState.getInt("reconnectButtonVisibility", View.INVISIBLE));
		}
		
	}

	public void click(View view)
	{
		switch (view.getId()) {
		case R.id.reconnectButton:
			view.setVisibility(View.INVISIBLE);
			oMyAsyncTask = new MyAsyncTask();
			oMyAsyncTask.execute();
			break;
		default:
			break;
		}
	}
		
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putParcelableArrayList("arrayNewsStruct", arrayNewsStruct);
		outState.putInt("reconnectButtonVisibility", reconnectButton.getVisibility());
		super.onSaveInstanceState(outState);
	}
	
	private void errorLoadXML(String TextError)
	{
		 Toast.makeText(MainActivity.this, TextError, Toast.LENGTH_LONG).show();
		 reconnectButton.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onDestroy() {
		if (oMyAsyncTask != null) Log.d(LOG, Boolean.toString(oMyAsyncTask.cancel(true)));
		super.onDestroy();
	}		

	// Класс загружающий XML
	class MyAsyncTask extends AsyncTask<Void, Void, Void>
	{
		boolean connectError = false;
		
		String localError = "";
		String host = "205.234.139.209";
		int port = 3423;
		String XML="";
		Socket socket;
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				socket = new Socket();
				socket.connect(new InetSocketAddress(host, port),2500);
				socket.setSoTimeout(2500);
				DataInputStream in = new DataInputStream(socket.getInputStream());
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				out.write("GetXML()".getBytes());
				out.flush();
				
	            int countByteRead = 0;
	            do
	            {
	            	byte[] buffer = new byte[2048];
	            	countByteRead = in.read(buffer,0,2048);
	            	if (countByteRead == -1) break;
	            	if (buffer[countByteRead-1] == 0)
	            	{
	            		baos.write(buffer, 0, countByteRead-1);
	            		XML = baos.toString("cp1251");
	            		if (!XML.isEmpty())
	            			loadXML(XML);
	            		baos.reset();
	            		break;
	            	}
	            	baos.write(buffer, 0, countByteRead);
	            } while (true);	
			}
			catch (UnknownHostException e) {
				localError = getResources().getString(R.string.errorHost);
				connectError  =true;
			} catch (IOException e) {
				if (e.getMessage() == null)
    				localError = getResources().getString(R.string.timeOutReadServer);
    			else
    				localError = getResources().getString(R.string.ErrorConnect)+host+":"+Integer.toString(port);
				connectError  =true;
			} 
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
			arrayNewsStruct = new ArrayList<NewsStruct>();
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
						NewsStruct newsStruct = new NewsStruct();
						if (xml.getName().contains("news"))
						{
							for (int i=0; i< xml.getAttributeCount(); i++)
							{
								
								if (xml.getAttributeName(i).contains("date"))
									newsStruct.date = xml.getAttributeValue(i);
								if (xml.getAttributeName(i).contains("time"))
									newsStruct.time = xml.getAttributeValue(i);
								if (xml.getAttributeName(i).contains("topic"))
									newsStruct.topic= xml.getAttributeValue(i);
								if (xml.getAttributeName(i).contains("http"))
									newsStruct.http = xml.getAttributeValue(i);	
								if (xml.getAttributeName(i).contains("bitmap"))
								{
    								newsStruct.bitmap = getBitmapFromURL(xml.getAttributeValue(i));
								}
							}
							newsStruct.tosee = false;
							arrayNewsStruct.add(newsStruct);
						}
						break;
					default:
						break;
					}
					eventType = xml.next();
				}
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				Log.e(LOG,"XmlPullParserException ="+ e.getMessage());
				localError = getResources().getString(R.string.ErrorXMLParcer)+" '"+e.getMessage()+"'";
				connectError = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(LOG,"IOException = " +e.getMessage());
				localError = getResources().getString(R.string.ErrorXMLParcer)+" '"+e.getMessage()+"'";
				connectError = true;
				return;
			}
		}		
		
		@Override
		protected void onPostExecute(Void result) {
			Log.i(LOG, "onPostExecute");
			super.onPostExecute(result);
			try {
				socket.shutdownInput();
				socket.close();		
			} catch (IOException e) {}
				
			if (connectError) errorLoadXML(localError);
			else
			  if (!XML.isEmpty()) 
			  {
					NewsAdapter adapter = new NewsAdapter(MainActivity.this,arrayNewsStruct);
					listView.setAdapter(adapter);	
					gifImageView.setVisibility(View.INVISIBLE);
					listView.setVisibility(View.VISIBLE);
			  }
			  else 
			    errorLoadXML(getResources().getString(R.string.noXML));
		}		
	}
	

}
