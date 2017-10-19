package com.manaul.highschool.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.manaul.highschool.utils.Constant;

public class SQLiteHelper extends SQLiteOpenHelper {
	

	public SQLiteHelper(Context context) {
		super(context, Constant.APP_TYPE+".db", null, Constant.DATABASE_VERSION);// 继承父类
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		
//		String list_item = "CREATE TABLE " + Constant.T_LIST_ITEM + "("
//        		+ "id INT,"
//        		+ "title VARCHAR(255),"
//        		+ "type VARCHAR(255),"
//        		+ "state INT"
//        		+ ")";
//        
//		String list_item_child = "CREATE TABLE " + Constant.T_LIST_ITEM_CHILD + "("
//        		+ "id INT,"
//        		+ "pid INT,"
//        		+ "title VARCHAR(255),"
//        		+ "intro VARCHAR(255),"
//        		+ "xmlurl VARCHAR(255),"
//        		+ "price INT,"
//        		+ "state INT"
//        		+ ")";
//        
//		String list_item_share = "CREATE TABLE " + Constant.T_LIST_ITEM_SHARE + "("
//        		+ "id INT,"
//        		+ "pid INT,"
//        		+ "cid INT"
//        		+ ")";
//        
//		db.execSQL(list_item);
//		db.execSQL(list_item_child);
//		db.execSQL(list_item_share);
//        Log.e("create","数据库创建成�? == 数据库工具类可用�?");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		String sql = "DROP TABLE IF EXISTS " + Constant.T_LIST_ITEM;
//		String sql2 = "DROP TABLE IF EXISTS " + Constant.T_LIST_ITEM_CHILD;
//		String sql3 = "DROP TABLE IF EXISTS " + Constant.T_LIST_ITEM_SHARE;
//		db.execSQL(sql);
//		db.execSQL(sql2);
//		db.execSQL(sql3);
    	this.onCreate(db);
	}

}
