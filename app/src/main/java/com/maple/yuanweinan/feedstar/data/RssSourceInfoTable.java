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
public class RssSourceInfoTable {

	private static final String TABLENAME = "RssSourceInfoTable";
	private static final String ID = "id";
	private static final String NAME = "name";
	private static final String RSS_ADDRESS = "rss_address";
	private static final String RSS_THUMBNAIL = "rss_thumbnail";

	public static final String CREATETABLESQL = "create table " + TABLENAME + " ("
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ NAME + " text, "
			+ RSS_ADDRESS + " text, "
			+ RSS_THUMBNAIL + " text"
			+ ")";

	public static ContentValues getContentValue(RssSourceInfo info) {
		if (null == info) {
			return null;
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(NAME, info.mName);
		contentValues.put(RSS_ADDRESS, info.mRssAddress);
		contentValues.put(RSS_THUMBNAIL, info.mRssThumbnail);
		return contentValues;
	}

	/**
	 * 查询消息列表
	 *
	 * @param dbHelper Dbhelper
	 * @return 消息列表
	 */
	public static List<RssSourceInfo> queryAll(DBHelper dbHelper) {
		List<RssSourceInfo> datas = new ArrayList<RssSourceInfo>();

		String[] columns = {
				ID,
				NAME,
				RSS_ADDRESS,
				RSS_THUMBNAIL,
		}; // 查询的列
		Cursor cursor = dbHelper.query(TABLENAME, columns, null, null, null);

		if (null == cursor) {
			return datas;
		}

		try {
			if (cursor.moveToFirst()) {
				do {
					RssSourceInfo info = new RssSourceInfo();
					info.mID = cursor.getInt(0);
					info.mName = cursor.getString(1);
					info.mRssAddress = cursor.getString(2);
					info.mRssThumbnail = cursor.getString(3);
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


	public static long insert(DBHelper dbHelper, RssSourceInfo info) {
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