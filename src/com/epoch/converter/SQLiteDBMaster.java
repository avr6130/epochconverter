package com.epoch.converter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// To use Toast, the class needs to extend activity.  You have to use getActivity() since you are not 
// calling this inside an Activity(or something that extends Activity). 
// Activity is a context(it extends context), fragment does not.  But, I don't understand this yet so...
//public class SQLiteDBMaster extends Activity{
public class SQLiteDBMaster {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_EPOCH = "epochTime";

	private static final String DATABASE_NAME = "TimeDB";
	private static final String DATABASE_TABLE = "TimeTable";
	private static final int DATABASE_VERSION = 1;

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_EPOCH
					+ " TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	public SQLiteDBMaster(Context context) {
		ourContext = context;
	}

	public SQLiteDBMaster open() {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();

		return (this);
	}

	public void close() {
		ourHelper.close();
	}

	public long createEntry(String epoch) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_EPOCH, epoch);
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}

	public String getData() {
		String[] columns = new String[] { KEY_ROWID, KEY_EPOCH };
		Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null, null,
				null, null, null);
		String result = "";

		int iRow = c.getColumnIndex(KEY_ROWID);
		int iEpoch = c.getColumnIndex(KEY_EPOCH);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			result = result + c.getString(iRow) + " " + c.getString(iEpoch)
					+ "\n";
		}
		return result;

	}

	public String[] convertTime(String epoch) {
		// ourDatabase.execSQL("SELECT datetime (" + epoch + ", 'unixepoch');");
		String[] result = new String[2];
		Cursor c = ourDatabase.rawQuery("SELECT datetime (" + epoch
				+ ", 'unixepoch')", null);
		if (c != null) {
			if (c.moveToFirst()) {
				result[0] = c.getString(0);
			}
		}

		c = ourDatabase.rawQuery(
				"SELECT strftime ('%s','now')", null);
		if (c != null) {
			if (c.moveToFirst()) {
				result[1] = c.getString(0);
			}
		}

		// SELECT datetime(1362438750, 'unixepoch');
		return result;
	}

	public void removeTable() {

		if (ourDatabase != null) {
			ourDatabase.execSQL("DELETE FROM " + DATABASE_TABLE);
		}

	}
}
