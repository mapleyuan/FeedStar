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
	private static final String TITLE = "title";
	private static final String DESCRIPTION = "description";
	private static final String SOURCE_FROM_ID = "source_from_id";
	private static final String THUMBNAIL = "thumbnail";
	private static final String CONTENT = "content";
	private static final String ADDRESS = "address";
	private static final String PUBLISH_TIME = "publish_time";

	public static final String CREATETABLESQL = "create table " + TABLENAME + " ("
			+ ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ SOURCE_FROM_ID + " text, "
			+ THUMBNAIL + " text, "
			+ CONTENT + " text,"
			+ PUBLISH_TIME + " text,"
			+ TITLE + " text,"
			+ ADDRESS + " text,"
			+ DESCRIPTION + " text"
			+ ")";

	public static ContentValues getContentValue(RSSItem info) {
		if (null == info) {
			return null;
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(SOURCE_FROM_ID, info.mSourceFromID);
		contentValues.put(THUMBNAIL, info.getThumbnail());
		contentValues.put(CONTENT, info.getContent());
		contentValues.put(PUBLISH_TIME, info.getPubDate());
		contentValues.put(TITLE, info.getTitle());
		contentValues.put(ADDRESS, info.getLink());
		contentValues.put(DESCRIPTION, info.getDescription());
		return contentValues;
	}

	/**
	 * 查询消息列表
	 *
	 * @param dbHelper Dbhelper
	 * @return 消息列表
	 */
	public static List<RSSItem> query(int sourceID, DBHelper dbHelper) {
		List<RSSItem> datas = new ArrayList<RSSItem>();

		String[] columns = {
				ID,
				SOURCE_FROM_ID,
				THUMBNAIL,
				CONTENT,
				PUBLISH_TIME,
				TITLE,
				ADDRESS,
				DESCRIPTION
		}; // 查询的列
		Cursor cursor = dbHelper.query(TABLENAME, columns, SOURCE_FROM_ID + "=" + sourceID, null, null);

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
					info.pubdate = cursor.getString(4);
					info.title = cursor.getString(5);
					info.link = cursor.getString(6);
					info.description = cursor.getString(7);
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
				PUBLISH_TIME,
				TITLE,
				ADDRESS,
				DESCRIPTION
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
					info.pubdate = cursor.getString(4);
					info.title = cursor.getString(5);
					info.link = cursor.getString(6);
					info.description = cursor.getString(7);
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

	public static void delete(DBHelper dbHelper, long sourceFromId) {
		try {
			dbHelper.delete(TABLENAME, SOURCE_FROM_ID + "=" + sourceFromId, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}