package com.epoch.converter;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button convertTimeButton;
	EditText epochSecondsToConvert;
	static final int EPOCH_SECONDS_WITHOUT_MILLIS = 10;
	static final int EPOCH_SECONDS_WITH_MILLIS = 13;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// This allows for formatting of numbers, so that you don't get
		// scientific notation.
		final NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(0);
		nf.setMaximumFractionDigits(0);

		convertTimeButton = (Button) findViewById(R.id.bConvertTime);
		epochSecondsToConvert = (EditText) findViewById(R.id.etEpochSecondsToConvert);
		final TextView tvConvertedUTCDateResult = (TextView) findViewById(R.id.tvConvertedUTCDateResult);
		final TextView tvConvertedLocalDateResult = (TextView) findViewById(R.id.tvConvertedLocalDateResult);
		final TextView tvCurrentEpochSecondsResult = (TextView) findViewById(R.id.tvCurrentEpochSecondsResult);
		final TextView tvCurrentEpochUTCDateResult = (TextView) findViewById(R.id.tvCurrentEpochUTCDateResult);
		final TextView tvCurrentEpochLocalDateResult = (TextView) findViewById(R.id.tvCurrentEpochLocalDateResult);

		convertTimeButton.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("unused")
			public void onClick(View view) {

				boolean itWorkedDebug = true;

				try {

					Date now = new Date();
					long epoch = 0;
					long currentEpochSecondsMilliSecs = System.currentTimeMillis();

					int epochSecondsTextLength = (epochSecondsToConvert.getText().toString()).length();
					// Checking the length of epoch allows the use of the button
					// to get and display
					// the current number of epoch seconds so that it can be
					// copied and pasted. 
					// if ( (epochTime.getText().toString()).length() < 1) {
					if (epochSecondsTextLength < 1) {
						
						// When the app is first launched, fill the epoch seconds time to be converted if not already
						// done so.  This makes entering a time easier.  
						epoch = currentEpochSecondsMilliSecs;

						// Fill the text field with the epoch seconds.  However, even though epoch needs
						// the 13 digits for milliseconds, don't output those extra zeros in the text field.
						epochSecondsToConvert.setText(currentEpochSecondsMilliSecs /1000L + "");

					} else {
						switch (epochSecondsTextLength) {
						
						case EPOCH_SECONDS_WITHOUT_MILLIS:
							
							// Read the entry and convert to a number
							epoch = Long.parseLong(epochSecondsToConvert.getText().toString());
							
							// Now fill the text field with the epoch seconds.  However, even though epoch needs
							// the 13 digits for milliseconds, don't output those extra zeros in the text field.
							epochSecondsToConvert.setText(epoch + "");

							// This is required because the Date class constructor requires milliseconds
							// to create a date object from epoch time.  This is simply adding 3 zeros
							// (zero milliseconds) and keeps the number of whole seconds unchanged. 
							epoch *= 1000;

							break;
							
						case EPOCH_SECONDS_WITH_MILLIS:
							
							// Read the entry and convert to a number
							epoch = Long.parseLong(epochSecondsToConvert.getText().toString());
							
							// Now fill the text field with the epoch seconds.  
							epochSecondsToConvert.setText(epoch + "");

							break;
							
						default:
							// The number entered is invalid so pop up a dialog and return.
							Dialog d = new Dialog(MainActivity.this);
							d.setTitle("Input Entry Error");
							TextView tv = new TextView(MainActivity.this);
							tv.setText("Epoch seconds is out of range.  Enter either a 10 digit number for seconds, " +
									"or a 13 digit number for seconds including milliseconds, but do not enter a decimal point.");
							d.setContentView(tv);
							d.show();
							return;
						} // end switch
						
					} // end else

					// Fill in the Current Epoch seconds
					tvCurrentEpochSecondsResult.setText(nf.format(currentEpochSecondsMilliSecs / 1000L));

					// Create a date formatter object
					SimpleDateFormat UTCFormat = new SimpleDateFormat("MMM/dd/yyyy HH:mm:ss zzz", Locale.US);
					
					// Set time zone to local time as set on the device
					UTCFormat.setTimeZone(TimeZone.getDefault());
					
					// Fill in the fields using the local time zone
					tvConvertedLocalDateResult.setText(UTCFormat.format(new Date(epoch)));
					tvCurrentEpochLocalDateResult.setText(UTCFormat.format(now));

					// Set time zone to UTC
					UTCFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

					// Fill in the fields using UTC
					tvCurrentEpochUTCDateResult.setText(UTCFormat.format(now));
					tvConvertedUTCDateResult.setText(UTCFormat.format(new Date(epoch)));

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

				}// finally {
					// if (itWorked) {
				// Dialog d = new Dialog(MainActivity.this);
				// d.setTitle("Heck Yea!");
				// TextView tv = new TextView(MainActivity.this);
				// tv.setText("Success");
				// d.setContentView(tv);
				// d.show();
				// }
				// }
			}
		}); // end setOnClickListener

		// Intent i = new Intent("android.intent.action.MAIN");
		// startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
