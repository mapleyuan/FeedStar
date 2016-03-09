package com.maple.yuanweinan.feedstar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.maple.yuanweinan.feedstar.data.GroupInfoTable;
import com.maple.yuanweinan.feedstar.data.RssFeedInfoTable;

import java.util.ArrayList;

/**
 * @author yuanweinan
 *
 */
public class FeedStarDBHelpler extends DBHelper {
	private static final int DB_VERSION_MAX = 1;
	private static final String DATABASE_NAME = "feedstar.db";
	public static FeedStarDBHelpler getInstance(Context context) {
		if (sInstance == null) {
			synchronized (FeedStarDBHelpler.class) {
				if (sInstance == null) {
					sInstance = new FeedStarDBHelpler(context);
				}
			}
		}

		return sInstance;
	}

	private static FeedStarDBHelpler sInstance;
	private FeedStarDBHelpler(Context context) {
		super(context, DATABASE_NAME, DB_VERSION_MAX);
	}
	@Override
	public int getDbCurrentVersion() {
		return DB_VERSION_MAX;
	}
	@Override
	public String getDbName() {
		return DATABASE_NAME;
	}
	@Override
	public void onCreateTables(SQLiteDatabase db) {
		db.execSQL(RssFeedInfoTable.CREATETABLESQL);
		db.execSQL(GroupInfoTable.CREATETABLESQL);
	}
	@Override
	public void onAddUpgrades(ArrayList<UpgradeDB> upgrades) {
	}

}
