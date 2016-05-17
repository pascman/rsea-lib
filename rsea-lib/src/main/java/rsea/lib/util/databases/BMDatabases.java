package rsea.lib.util.databases;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import rsea.lib.util.databases.anontation.ColumName;
import rsea.lib.util.databases.anontation.PrimaryKey;
import rsea.lib.util.databases.anontation.SQLiteTable;
import rsea.lib.util.databases.anontation.UniqueIndex;


public class BMDatabases {
	
	private static final Class<?>[] DAO_CLASS = {
	};
	
	private static final String DAO_PACKAGE = "kr.wish.kollshop.entity";
	
	public static final String DATABASE_NAME = "bm_database.db";
	public static final int DB_VERSION = 3;

	public static class RedDBOpenHelper extends SQLiteOpenHelper {
		public RedDBOpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
//			super(context, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + name, factory, version);
		}
		private String getReflectionQueryString(ColumName colum, PrimaryKey primaryKey){
			String not_null = colum.isAllowNull() ? "" : " not null";
			Log.d("BMDatabases", "getReflectionQueryString " + not_null);
			if(primaryKey != null){
				if(primaryKey.autoincrease()){
					return colum.name() + " " + colum.type() + " primary key autoincrement";
				}else{
					return colum.name() + " " + colum.type() + " primary key";
				}
			}else{
				return colum.name() + " " + colum.type() + not_null;
			} 
		}
		
		private ColumName getColumName(Field f){
			return f.getAnnotation(ColumName.class);
		} 
		private PrimaryKey getPrimaryKey(Field f){
			return f.getAnnotation(PrimaryKey.class);
		} 
		private UniqueIndex getUniqueIndex(Field f){
			try{
				ColumName colum =  f.getAnnotation(ColumName.class);
				if(colum != null){
					return f.getAnnotation(UniqueIndex.class);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;  
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			for(Class<?> cls: DAO_CLASS){
				SQLiteTable table =  cls.getAnnotation(SQLiteTable.class);
				if(table == null) continue;
				StringBuilder builder = new StringBuilder("CREATE TABLE " + table.tableName() + " (");
				Field[] fields = cls.getDeclaredFields();
				HashMap<String,ArrayList<ColumName>> uniqueFileds = new HashMap<String, ArrayList<ColumName>>();
				for(Field f : fields){
					ColumName cName = getColumName(f);
					PrimaryKey pKey = getPrimaryKey(f);
					if(cName == null) continue;
					UniqueIndex index = getUniqueIndex(f);
					if(index != null){
						ArrayList<ColumName> datas = uniqueFileds.get(index.indexName());
						datas = datas == null ? new ArrayList<ColumName>() : datas;
						datas.add(cName);
						uniqueFileds.put(index.indexName(), datas);
					}
					String fieldQuery = getReflectionQueryString(cName,pKey);
					builder.append(fieldQuery);
					builder.append(",");
				}

				
				Iterator<String> iterator = uniqueFileds.keySet().iterator();
				if(iterator.hasNext()){
					StringBuilder uniqueBuilder = new StringBuilder("unique (");
					while (iterator.hasNext()) {
						String indexName = iterator.next();
						ArrayList<ColumName> u_index = uniqueFileds.get(indexName);
						for(ColumName index : u_index){
							uniqueBuilder.append(index.name()+",");
						}
						if(uniqueBuilder.toString().endsWith(",")){
							uniqueBuilder.deleteCharAt(uniqueBuilder.length() -1);
						}
						uniqueBuilder.append("),");
					}
					builder.append(uniqueBuilder.toString());
				}
				
				if(builder.toString().endsWith(",")){
					builder.deleteCharAt(builder.length() -1);
				}
				builder.append(");");
				db.execSQL(builder.toString());
				
				for(String query : table.initQuery()){
					db.execSQL(query);
				}
			} 
			
		}
 
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			for(Class<?> cls: DAO_CLASS){
				SQLiteTable table =  cls.getAnnotation(SQLiteTable.class);
				db.execSQL("DROP TABLE IF EXISTS " + table.tableName());
			}
			onCreate(db);
		}
	}
	
}
