package com.hwang.sy_knu.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper {
	
	public static int num1=0;
	
	private static final String DATABASE_NAME = "Saved_Sy.db";
	private static final int DATABASE_VERSION = 2; // ver. 20
	public static SQLiteDatabase mDB;
	private DatabaseHelper mDBHelper;
	private Context mCtx;

	private class DatabaseHelper extends SQLiteOpenHelper{

		// 생성자
		public DatabaseHelper(Context context, String name,CursorFactory factory, int version) {
			super(context, name, factory, version);
			//	context, 'DB name', null, 'DB version'
		}

		// 최초 DB를 만들때 한번만 호출된다.
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			db.execSQL(DataBases.CreateDB_Table.CREATE_DB);

		}

		// 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
			db.execSQL("DROP TABLE IF EXISTS "+DataBases.CreateDB_Table.TABLE_SAVED);
			onCreate(db);
		}
	}

	public DbOpenHelper(){
		
	}
	
	public DbOpenHelper(Context context){
		this.mCtx = context;
	}


	public DbOpenHelper open() throws SQLException{
		mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
		mDB = mDBHelper.getWritableDatabase();
		return this;
	}

	public void close(){
		mDB.close();
	}

	// Insert DB
	public long insertColumnSaved(String subNum, String subTerm){
		ContentValues values = new ContentValues();
		values.put(DataBases.CreateDB_Table.SUB_NUM, subNum);
		values.put(DataBases.CreateDB_Table.SUB_TERM, subTerm);

		return mDB.insert(DataBases.CreateDB_Table.TABLE_SAVED, null, values);
	}
	
	

		
	
	// Update DB // 사용 안함
	public boolean updateColumnSaved(int D_key , byte[] Image, int num){
		ContentValues values = new ContentValues();
		if(num==0){
			//values.put(DB_DataBases.CreateDB_Table.D_Image1, Image);
		}
		
		return mDB.update(DataBases.CreateDB_Table.TABLE_SAVED, values, "D_key="+Integer.toString(D_key), null) > 0;
	}


	// Delete ID
	public boolean deleteColumn(String num){
		return mDB.delete(DataBases.CreateDB_Table.TABLE_SAVED, "Sub_Num='"+num+"'", null) > 0;
	}
	

	// Select Table All
	public Cursor getAllColumns(){
		return mDB.query(DataBases.CreateDB_Table.TABLE_SAVED, null, null, null, null, null, null);
	}

	

	// ID 컬럼 얻어 오기 // 사용 안함
	public Cursor getColumn(long id){
		Cursor c = mDB.query(DataBases.CreateDB_Table.TABLE_SAVED, null, "U_id="+id, null, null, null, null);
		if(c != null && c.getCount() != 0)
			c.moveToFirst();
		return c;
	}

	// 이름 검색 하기 (rawQuery) // 안쓰는 듯?
	public Cursor getMatchName(String name){
		Cursor c = mDB.rawQuery( "select * from Saved where name=" + "'" + name + "'" , null);
		return c;
	}


}