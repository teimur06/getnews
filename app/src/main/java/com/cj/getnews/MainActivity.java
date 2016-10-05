package com.cj.getnews;



import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;




public class MainActivity extends Activity {

	private static final String LOG = "MainActivity";
	public ArrayList<NewsStruct> arrayNewsStruct;

	
	public ListView listView;
	public GifImageView gifImageView;
	public Button reconnectButton;
	ThreadLoadXML oMyAsyncTask;
	

	
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
			oMyAsyncTask = new ThreadLoadXML(this);
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
			oMyAsyncTask = new ThreadLoadXML(this);
			oMyAsyncTask.execute();
			break;
		default:
			break;
		}
	}
		
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelableArrayList("arrayNewsStruct", arrayNewsStruct);
		outState.putInt("reconnectButtonVisibility", reconnectButton.getVisibility());
		super.onSaveInstanceState(outState);
	}
	
	public void errorLoadXML(String TextError)
	{
		 Toast.makeText(MainActivity.this, TextError, Toast.LENGTH_LONG).show();
		 reconnectButton.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void onDestroy() {
		if (oMyAsyncTask != null) Log.d(LOG, Boolean.toString(oMyAsyncTask.cancel(true)));
		super.onDestroy();
	}		

	

}
