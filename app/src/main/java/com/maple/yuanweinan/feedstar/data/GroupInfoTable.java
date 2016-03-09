package com.maple.yuanweinan.feedstar.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.maple.yuanweinan.feedstar.db.DBHelper;
import com.maple.yuanweinan.feedstar.db.DatabaseException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanweinan
 */
public class GroupInfoTable {

	private static final String TABLENAME = "GroupInfoTable";
	private static final String ID = "id";
	private static final String GROUP_NAME = "group_name";

	public static final String CREATETABLESQL = "create table " + TABLENAME + " ("
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ GROUP_NAME + " text"
			+ ")";

	public static ContentValues getContentValue(GroupInfo info) {
		if (null == info) {
			return null;
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(GROUP_NAME, info.mGroupName);
		return contentValues;
	}

	/**
	 * 查询消息列表
	 *
	 * @param dbHelper Dbhelper
	 * @return 消息列表
	 */
	public static List<GroupInfo> queryAll(DBHelper dbHelper) {
		List<GroupInfo> datas = new ArrayList<GroupInfo>();

		String[] columns = {
				ID,
				GROUP_NAME
		}; // 查询的列
		Cursor cursor = dbHelper.query(TABLENAME, columns, null, null, null);

		if (null == cursor) {
			return datas;
		}

		try {
			if (cursor.moveToFirst()) {
				do {
					GroupInfo info = new GroupInfo();
					info.mID = cursor.getInt(0);
					info.mGroupName = cursor.getString(1);
					datas.add(info);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}

		return datas;
	}


	public static long insert(DBHelper dbHelper, GroupInfo info) {
		if (null == info) {
			return -1;
		}

		try {
			long result = dbHelper.insert(TABLENAME, getContentValue(info));
			return result;
		} catch (DatabaseException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public static void delete(DBHelper dbHelper, long id) {
		try {
			dbHelper.delete(TABLENAME, ID + "=" + id, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}