package com.manaul.highschool.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.manaul.highschool.bean.ItemContent;
import com.manaul.highschool.bean.Navigate;
import com.manaul.highschool.bean.Project;
import com.manaul.highschool.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航栏目dao
 * 
 * @author lovebin
 *
 */
public class NavigateDao {
	private SQLiteDatabase db = null;

	public NavigateDao(SQLiteDatabase db) {
		this.db = db;
	}

	/**
	 * 获取课程
	 * 
	 * @param parentId
	 * @return
	 */
	public List<Project> findProjectSimple() {
		List<Project> projects = new ArrayList<Project>();
		if (db.isOpen()) {
			String sql = "SELECT b.projectId,b.imageUrl,b.status,b.type,intro,b.name,(SELECT COUNT(*) FROM t_item_content a WHERE a.type = b.type ) AS total FROM t_project b";
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				Project project = new Project();

				project.setProjectId(cursor.getInt(cursor.getColumnIndex("projectId")));
				project.setImageUrl(cursor.getString(cursor.getColumnIndex("imageUrl")));
				project.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
				project.setType(cursor.getString(cursor.getColumnIndex("type")));
				project.setIntro(cursor.getString(cursor.getColumnIndex("intro")));
				project.setName(cursor.getString(cursor.getColumnIndex("name")));
				project.setTotal(cursor.getInt(cursor.getColumnIndex("total")));
				projects.add(project);
			}
		}
//		db.close();  // 暂不关闭链接，zai
		return projects;
	}

	/**
	 * 获取首页的大分类导航标签
	 * 
	 * @param parentId
	 * @return
	 */
	public List<Navigate> findNavigateSimple(int parentId , int pageIndex , int pageSize) {
		if(pageIndex <= 0){
			pageIndex = 1;
		}
		if(pageIndex <= 0){
			pageIndex = 6;
		}
		List<Navigate> navigateList = new ArrayList<Navigate>();
		if (db.isOpen()) {
			String sql = "SELECT navigateId,name,parentId,intro,imgUrl,type FROM " + Constant.T_NAVIGATE
					+ " WHERE parentId = " + parentId +" limit "+(pageIndex-1)*pageSize+","+pageSize;
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				Navigate navigate = new Navigate();
				navigate.setName(cursor.getString(cursor.getColumnIndex("name")));
				navigate.setNavigateId(cursor.getInt(cursor.getColumnIndex("navigateId")));
				navigate.setType(cursor.getString(cursor.getColumnIndex("type")));
				navigateList.add(navigate);
			}
		}
		return navigateList;
	}

	// 获取�??有的导航分类标签
	public List<Navigate> findAll(int parentId, String type) {

		List<Navigate> navigateList = new ArrayList<Navigate>();
		// parentId : -1：收藏的
		if (parentId == -1) {
			return findAllFavority(type);
		}
		
		if (db.isOpen()) {
			String sql = "SELECT navigateId,name,parentId,intro,imgUrl FROM " + Constant.T_NAVIGATE
					+ " WHERE parentId = " + parentId;
			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				Navigate navigate = new Navigate();
				navigate.setImgUrl(cursor.getString(cursor.getColumnIndex("imgUrl")));
				navigate.setIntro(cursor.getString(cursor.getColumnIndex("intro")));
				navigate.setName(cursor.getString(cursor.getColumnIndex("name")));
				navigate.setNavigateId(cursor.getInt(cursor.getColumnIndex("navigateId")));
				navigate.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));

				String sql_child = "SELECT itemId," + " parentId,title,context," + " intro,status,price " + " FROM "
						+ Constant.T_ITEM_CONTENT + " WHERE parentId = " + navigate.getNavigateId()
						+ " order by status asc";

				Cursor cursor_child = db.rawQuery(sql_child, null);

				List<ItemContent> itemContents = new ArrayList<ItemContent>();
				while (cursor_child.moveToNext()) {
					ItemContent itemContent = new ItemContent();
					itemContent.setItemId(cursor_child.getInt(cursor_child.getColumnIndex("itemId")));
					itemContent.setParentId(cursor_child.getInt(cursor_child.getColumnIndex("parentId")));
					itemContent.setTitle(cursor_child.getString(cursor_child.getColumnIndex("title")));
					itemContent.setIntro(cursor_child.getString(cursor_child.getColumnIndex("intro")));
					itemContent.setStatus(cursor_child.getInt(cursor_child.getColumnIndex("status")));
					itemContent.setPrice(cursor_child.getInt(cursor_child.getColumnIndex("price")));
					itemContents.add(itemContent);
				}
				navigate.setItemContents(itemContents);
				navigateList.add(navigate);
			}
		}
		db.close();
		return navigateList;
	}

	// 获取�??有的导航分类标签
	public List<ItemContent> search(String type , String keyword) {
		
		List<ItemContent> itemContents = new ArrayList<ItemContent>();
		if (db.isOpen()) {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT itemId,title,context,type,status,created,price,intro FROM " + Constant.T_ITEM_CONTENT+ " WHERE type = '" + type + "' ");
			if(keyword != null && keyword.length() > 0){
				sb.append(" and ( title like '%"+keyword+"%' or context like '%"+keyword+"%' ) ");
			}
			Log.e("22222", sb.toString());
			Cursor cursor = db.rawQuery(sb.toString(), null);
			while (cursor.moveToNext()) {
				ItemContent itemContent = new ItemContent();
				itemContent.setItemId(cursor.getInt(cursor.getColumnIndex("itemId")));
				itemContent.setTitle(cursor.getString(cursor.getColumnIndex("title")));
				itemContent.setIntro(cursor.getString(cursor.getColumnIndex("intro")));
				itemContent.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
				itemContent.setPrice(cursor.getInt(cursor.getColumnIndex("price")));
				itemContent.setCreated(cursor.getLong(cursor.getColumnIndex("created")));
				itemContents.add(itemContent);
			}
		}
		return itemContents;
	}

	// 获取收藏的信�??
	public List<Navigate> findAllFavority(String type) {
		if (type == null || type.length() <= 0) {
			type = "";
		}
		List<Navigate> navigateList = new ArrayList<Navigate>();
		if (db.isOpen()) {
			// 获取收藏的导航列
			String sql = " select " + " navigate.navigateId , " + " navigate.name , " + " navigate.parentId , "
					+ " navigate.intro , " + " navigate.imgUrl , " + " navigate.status , " + " navigate.type , "
					+ " navigate.created , " + " share.parentId , " + " share.itemId " + " from " + Constant.T_NAVIGATE
					+ " navigate " + " inner join " + Constant.T_LIST_ITEM_SHARE + " share " + " on "
					+ " navigate.navigateId = share.parentId " + " and navigate.type = '" + type + "' "
					+ " group by navigate.navigateId order by navigate.navigateId asc ";

			Cursor cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				Navigate navigate = new Navigate();
				navigate.setParentId(cursor.getInt(cursor.getColumnIndex("parentId")));
				navigate.setName(cursor.getString(cursor.getColumnIndex("name")));
				navigate.setNavigateId(cursor.getInt(cursor.getColumnIndex("navigateId")));
				navigate.setIntro(cursor.getString(cursor.getColumnIndex("intro")));
				navigate.setImgUrl(cursor.getString(cursor.getColumnIndex("imgUrl")));

				navigate.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
				navigate.setType(cursor.getString(cursor.getColumnIndex("type")));
				navigate.setCreated(cursor.getLong(cursor.getColumnIndex("created")));

				// 获取收藏的二级标�??
				String sql_child = " select " + " a.itemId , " + " a.parentId, " + " a.title , " + " a.intro , "
						+ " a.type , " + " a.status , " + " a.created , " + " b.itemId , " + " b.parentId ,"
						+ " b.shareId,a.price " + " from " + Constant.T_ITEM_CONTENT + " a " + " inner join "
						+ Constant.T_LIST_ITEM_SHARE + " b " + " on a.itemId = b.itemId " + " and b.parentId = "
						+ navigate.getParentId() + " order by b.shareId desc ";

				List<ItemContent> itemContents = new ArrayList<ItemContent>();
				Cursor cursor_child = db.rawQuery(sql_child, null);
				while (cursor_child.moveToNext()) {
					ItemContent itemContent = new ItemContent();

					itemContent.setItemId(cursor_child.getInt(cursor_child.getColumnIndex("itemId")));
					itemContent.setParentId(cursor_child.getInt(cursor_child.getColumnIndex("parentId")));
					itemContent.setTitle(cursor_child.getString(cursor_child.getColumnIndex("title")));
					itemContent.setIntro(cursor_child.getString(cursor_child.getColumnIndex("intro")));
					itemContent.setType(cursor_child.getString(cursor_child.getColumnIndex("type")));
					itemContent.setStatus(cursor_child.getInt(cursor_child.getColumnIndex("status")));
					itemContent.setCreated(cursor_child.getLong(cursor_child.getColumnIndex("created")));
					itemContent.setPrice(cursor_child.getInt(cursor_child.getColumnIndex("price")));
					
					itemContents.add(itemContent);

				}
				navigate.setItemContents(itemContents);
				navigateList.add(navigate);
			}
		}

		db.close();
		return navigateList;
	}

	public ItemContent getItemContent(int itemId) {
		String sql = " select " + " a.itemId , " + " a.parentId, " + " a.title , " + " a.intro , " + " a.context , "
				+ " a.type , " + " a.status , " + " a.created,a.price from " + Constant.T_ITEM_CONTENT + " a "
				+ " where a.itemId = " + itemId;
		Cursor cursor_child = db.rawQuery(sql, null);
		ItemContent itemContent = new ItemContent();
		while (cursor_child.moveToNext()) {
			itemContent.setItemId(cursor_child.getInt(cursor_child.getColumnIndex("itemId")));
			itemContent.setParentId(cursor_child.getInt(cursor_child.getColumnIndex("parentId")));
			itemContent.setTitle(cursor_child.getString(cursor_child.getColumnIndex("title")));
			itemContent.setIntro(cursor_child.getString(cursor_child.getColumnIndex("intro")));
			itemContent.setContext(cursor_child.getString(cursor_child.getColumnIndex("context")));
			itemContent.setType(cursor_child.getString(cursor_child.getColumnIndex("type")));
			itemContent.setStatus(cursor_child.getInt(cursor_child.getColumnIndex("status")));
			itemContent.setCreated(cursor_child.getLong(cursor_child.getColumnIndex("created")));
			itemContent.setPrice(cursor_child.getInt(cursor_child.getColumnIndex("price")));
		}
		return itemContent;
	}
}
