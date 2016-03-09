package com.maple.yuanweinan.feedstar.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.maple.yuanweinan.feedstar.db.DBHelper;
import com.maple.yuanweinan.feedstar.db.DatabaseException;
import com.maple.yuanweinan.feedstar.lib.Dates;
import com.maple.yuanweinan.feedstar.lib.RSSFeed;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanweinan
 */
public class RssFeedInfoTable {

	private static final String TABLENAME = "RssFeedInfoTable";
	private static final String ID = "id";
	private static final String TITLE = "title";
	private static final String DERCRIPSTION = "description";
	private static final String THUMBNAIL = "thumbnail";
	private static final String ADDRESS_LINK = "address_link";
	private static final String DATE = "date";

	public static final String CREATETABLESQL = "create table " + TABLENAME + " ("
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ TITLE + " text, "
			+ DERCRIPSTION + " text, "
			+ THUMBNAIL + " text, "
			+ ADDRESS_LINK + " text, "
			+ DATE + " text"
			+ ")";

	public static ContentValues getContentValue(RSSFeed info) {
		if (null == info) {
			return null;
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(TITLE, info.getTitle());
		contentValues.put(DERCRIPSTION, info.getDescription());
		contentValues.put(THUMBNAIL, info.mThumbnail);
		contentValues.put(ADDRESS_LINK, info.getLink());
		contentValues.put(DATE, info.getPubDate());
		return contentValues;
	}

	/**
	 * 查询消息列表
	 *
	 * @param dbHelper Dbhelper
	 * @return 消息列表
	 */
	public static List<RSSFeed> queryAll(DBHelper dbHelper) {
		List<RSSFeed> datas = new ArrayList<RSSFeed>();

		String[] columns = {
				ID,
				TITLE,
				DERCRIPSTION,
				THUMBNAIL,
				ADDRESS_LINK,
				DATE
		}; // 查询的列
		Cursor cursor = dbHelper.query(TABLENAME, columns, null, null, null);

		if (null == cursor) {
			return datas;
		}

		try {
			if (cursor.moveToFirst()) {
				do {
					RSSFeed info = new RSSFeed();
					info.mID = cursor.getInt(0);
					info.title = cursor.getString(1);
					info.description = cursor.getString(2);
					info.mThumbnail = cursor.getString(3);
					info.link = cursor.getString(4);
					info.pubdate = cursor.getString(5);
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


	public static long insert(DBHelper dbHelper, RSSFeed info) {
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