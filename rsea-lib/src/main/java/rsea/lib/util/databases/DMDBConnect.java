package rsea.lib.util.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class DMDBConnect {
	
	private static DMDBConnect g_connect;
	private Context mContext;
	private BMDatabases.RedDBOpenHelper mHelper;
	private SQLiteDatabase mDatabase;
	public static DMDBConnect getInstance(){
		if(g_connect == null){
			synchronized (DMDBConnect.class) {
				g_connect = new DMDBConnect();
			}
		} 
		return g_connect;
	}
	
	public void init(Context context){
		mContext = context;
		mHelper = new BMDatabases.RedDBOpenHelper(mContext, BMDatabases.DATABASE_NAME, null, BMDatabases.DB_VERSION);
		mDatabase = mHelper.getWritableDatabase();
	}
	public SQLiteDatabase geDatabase(){
		return mDatabase;
	}

}
