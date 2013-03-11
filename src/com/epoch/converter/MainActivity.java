package com.epoch.converter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button convertTimeButton;
	EditText epochTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		convertTimeButton = (Button) findViewById(R.id.bConvertTime);
		epochTime = (EditText) findViewById(R.id.etEpochSeconds);
		final TextView tvConvertedDateResult = (TextView) findViewById(R.id.tvConvertedDateResult);						
		final TextView tvCurrentEpochResult = (TextView) findViewById(R.id.tvCurrentEpochResult);
		
			convertTimeButton.setOnClickListener(new OnClickListener() {
				@SuppressWarnings("unused")
				public void onClick(View view) {
					String epoch = epochTime.getText().toString();
					
					// Checking the length of epoch allows the use of the button to get and display
					// the current number of epoch seconds so that it can be copied and pasted.  If you 
					// don't do this, then the application fails because epoch is empty - nothing was entered.
					if (epoch.length() < 1) {
						long seconds = System.currentTimeMillis();
						epoch = "" + seconds;
					}
					boolean itWorkedDebug = true;

					try {
						String [] dataArray = new String [2];
						SQLiteDBMaster entry = new SQLiteDBMaster(MainActivity.this);
						entry.open();
						entry.createEntry(epoch);
						entry.close();
						
//						TextView tvConvertedDateResult = (TextView) findViewById(R.id.tvConvertedDateResult);						
//						TextView tvCurrentEpochResult = (TextView) findViewById(R.id.tvCurrentEpochResult);
//						tvCurrentEpochResult.setKeyListener(null);
						
						SQLiteDBMaster info = new SQLiteDBMaster(MainActivity.this);
						info.open();
//						String data = info.getData();
						dataArray = info.convertTime(epoch);
						info.removeTable();
						info.close();
						tvConvertedDateResult.setText(dataArray[0]);
						tvCurrentEpochResult.setText(dataArray[1]);

						
					} catch (Exception e) {
						itWorkedDebug = false;
						e.printStackTrace();
						String error = e.toString();
						Dialog d = new Dialog(MainActivity.this);
						d.setTitle("No good...");
						TextView tv = new TextView(MainActivity.this);
						tv.setText(error);
						d.setContentView(tv);
						d.show();

					}//finally {
//						if (itWorked) {
//							Dialog d = new Dialog(MainActivity.this);
//							d.setTitle("Heck Yea!");
//							TextView tv = new TextView(MainActivity.this);
//							tv.setText("Success");
//							d.setContentView(tv);
//							d.show();
//						}
//					}
				}
			}); // end setOnClickListener
						
//			Intent i = new Intent("android.intent.action.MAIN");
//			startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
