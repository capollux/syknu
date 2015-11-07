package com.hwang.sy_knu.DB;

import android.provider.BaseColumns;

//DataBase Table
public class DataBases {
	
	public static final class CreateDB_Table implements BaseColumns{
		
		// Table Name
		public static final String TABLE_SAVED = "Saved";
		
		
		// Context
		public static final String SUB_NUM = "Sub_Num";
		public static final String SUB_TERM = "Sub_Term";


		// Create Table Query
		public static final String CREATE_DB = 
			"create table "+TABLE_SAVED+"("
					+_ID+" integer primary key autoincrement, " 	
					+SUB_NUM+" text not null , " 
					+SUB_TERM+" text not null);";

		
	}
}
