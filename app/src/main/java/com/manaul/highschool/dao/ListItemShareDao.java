package com.manaul.highschool.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.manaul.highschool.bean.ListItemShare;
import com.manaul.highschool.utils.Constant;

/**
 * @author lovebin
 *
 */
public class ListItemShareDao {
	
	private SQLiteDatabase db = null;
	public ListItemShareDao(SQLiteDatabase db) {
		this.db = db;
	}
	
	// 插入
	public boolean insert(ContentValues values) {
		boolean flag = false;
		if (db.isOpen()) {
			long l = db.insert(Constant.T_LIST_ITEM_SHARE, null, values);
			if (l > 0) {
				flag = true;
			}
		}
		return flag;
	}

	// 删除
	public boolean delete(Integer id) {
		boolean flag = false;
		if (db.isOpen()) {
			long l = db.delete(Constant.T_LIST_ITEM_SHARE, "shareId=?", new String[] { id + "" });
			if (l > 0) {
				flag = true;
			}
		}
		return flag;
	}
	/**
	 * 查询是否收藏
	 * @param parentId
	 * @param itemId
	 * @return
	 */
	public ListItemShare findByCidPid(Integer parentId , Integer itemId) {
		ListItemShare itemShare = null;
		String sql = "select shareId,parentId,itemId from "+Constant.T_LIST_ITEM_SHARE
				+" where parentId = "+parentId+" and itemId = "+itemId;
		if(db.isOpen()){
			Cursor cursor = db.rawQuery(sql, null);
			if(cursor.moveToFirst()){
				itemShare = new ListItemShare();
				itemShare.setShareId(cursor.getInt(cursor.getColumnIndex("shareId")));
				itemShare.setItemId(cursor.getInt(cursor.getColumnIndex("parentId")));
				itemShare.setParentId(cursor.getInt(cursor.getColumnIndex("itemId")));
			}
		}
		return itemShare;
	}

}
