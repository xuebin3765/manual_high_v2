package com.manaul.highschool.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.manaul.highschool.utils.Constant;
import com.manaul.highschool.utils.SharedPreferenceUtil;
import com.manaul.highschool.utils.SystemUtil;

public class SQLiteHelper extends SQLiteOpenHelper {
	

	public SQLiteHelper(Context context) {
		super(context, SystemUtil.APP_DATABASE_NAME+".db", null, Constant.DATABASE_VERSION);// 继承父类
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	this.onCreate(db);
	}

}
