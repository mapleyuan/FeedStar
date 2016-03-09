package com.maple.yuanweinan.feedstar.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.maple.yuanweinan.feedstar.db.DBHelper;
import com.maple.yuanweinan.feedstar.db.DatabaseException;
import com.maple.yuanweinan.feedstar.lib.RSSItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanweinan
 */
public class RssItemTable {

	private static final String TABLENAME = "RssItemTable";
	private static final String ID = "id";
	private static final String SOURCE_FROM_ID = "source_from_id";
	private static final String THUMBNAIL = "thumbnail";
	private static final String CONTENT = "content";

	public static final String CREATETABLESQL = "create table " + TABLENAME + " ("
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ SOURCE_FROM_ID + " text, "
			+ THUMBNAIL + " text, "
			+ CONTENT + " text"
			+ ")";

	public static ContentValues getContentValue(RSSItem info) {
		if (null == info) {
			return null;
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(SOURCE_FROM_ID, info.mSourceFromID);
		contentValues.put(THUMBNAIL, info.getThumbnail());
		contentValues.put(CONTENT, info.getContent());
		return contentValues;
	}

	/**
	 * 查询消息列表
	 *
	 * @param dbHelper Dbhelper
	 * @return 消息列表
	 */
	public static List<RSSItem> queryAll(DBHelper dbHelper) {
		List<RSSItem> datas = new ArrayList<RSSItem>();

		String[] columns = {
				ID,
				SOURCE_FROM_ID,
				THUMBNAIL,
				CONTENT,
		}; // 查询的列
		Cursor cursor = dbHelper.query(TABLENAME, columns, null, null, null);

		if (null == cursor) {
			return datas;
		}

		try {
			if (cursor.moveToFirst()) {
				do {
					RSSItem info = new RSSItem();
					info.mID = cursor.getInt(0);
					info.mSourceFromID = cursor.getInt(1);
					info.setThumbnail(cursor.getString(2));
					info.setContent(cursor.getString(3));
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


	public static long insert(DBHelper dbHelper, RSSItem info) {
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