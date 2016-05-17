package rsea.lib.util.databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;

import rsea.lib.util.databases.anontation.ColumName;
import rsea.lib.util.databases.anontation.PrimaryKey;
import rsea.lib.util.databases.anontation.SQLiteTable;


public class BaseDAO {
	
	protected SQLiteDatabase mDatabase;
	
	
	public BaseDAO() {
		mDatabase = DMDBConnect.getInstance().geDatabase();
	}
	
	
	public String tableName(){
		SQLiteTable table =  getClass().getAnnotation(SQLiteTable.class);
		return table.tableName();
	}
	
	public long insert(ContentValues b){
		return mDatabase.insert(tableName(), null, b);
	}
	
	public long replace(ContentValues b){
		return mDatabase.replace(tableName(), null, b);
	}
	public void delete(long seq){
		Field[] fields =  getClass().getDeclaredFields();
		for(Field f : fields){
			ColumName colum =  f.getAnnotation(ColumName.class);
			if(colum !=null && f.getAnnotation(PrimaryKey.class) != null){
				mDatabase.execSQL("DELETE FROM " + tableName() + " WHERE " + colum.name() +"=" + seq);
				return;
			}
		}
	}
	
	public void update(long seq,ContentValues b){
		Field[] fields =  getClass().getDeclaredFields();
		String pk = "";
		for(Field f : fields){
			ColumName colum =  f.getAnnotation(ColumName.class);
			if(f.getAnnotation(PrimaryKey.class) != null){
				pk = colum.name();
				break;
			}
		}
		mDatabase.update(tableName(), b, pk+"=?", new String[]{seq+""});
	}
	
	
	public Bundle toBundleOne(Cursor cursor){
		Bundle retVal = new Bundle();
		for(int i=0;i<cursor.getColumnCount();i++){
			switch (cursor.getType(i)) {
			case Cursor.FIELD_TYPE_STRING:
				retVal.putString(cursor.getColumnName(i), cursor.getString(i) == null ? "" : cursor.getString(i));
				break;
			case Cursor.FIELD_TYPE_FLOAT:
				retVal.putDouble(cursor.getColumnName(i), cursor.getDouble(i));
				break;
			case Cursor.FIELD_TYPE_INTEGER:
				retVal.putLong(cursor.getColumnName(i), cursor.getLong(i));
				break;
			case Cursor.FIELD_TYPE_NULL:
				retVal.putString(cursor.getColumnName(i), "");
				break;
			default:
				break;
			}
		}
		return retVal;
	} 
	
	public ArrayList<Bundle> toBundleList(Cursor cursor){
		cursor.moveToFirst();
		ArrayList<Bundle> retVal = new ArrayList<Bundle>();
		for(int i=0;i<cursor.getCount();i++){
			retVal.add(toBundleOne(cursor));
			cursor.moveToNext();
		}
		return retVal;
	}  

	
	public void view(){
		Log.d("BaseDAO", "view START=======================================");
		Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + tableName(), null);
		while (cursor.moveToNext()) {
			for(int i=0;i<cursor.getColumnCount();i++){
				Log.d("BaseDAO", "view " + cursor.getColumnName(i) + " , " + cursor.getString(i));
			}
		}
		cursor.close();
		Log.d("BaseDAO", "view END=======================================");
	}
	  
}
