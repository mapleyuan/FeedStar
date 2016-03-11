package com.maple.yuanweinan.feedstar.data;

import android.text.TextUtils;
import android.util.SparseArray;

/**
 * Created by yuanweinan on 16/3/8.
 */
public class GroupInfo {

    public int mID;
    public String mGroupName;
    public SparseArray<String> mSourceIDs = new SparseArray<>();

    public String formatIDsToString() {
        int size = mSourceIDs.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(mSourceIDs.get(i) + ",");
        }
        return sb.toString();
    }

    public void formatStringToIDs(String idsString) {
        if (TextUtils.isEmpty(idsString)) {
            return;
        }
        String[] ids = idsString.split(",");
        int length = ids.length;
        for (int i = 0; i < length; i++) {
            mSourceIDs.put(i, ids[i]);
        }
    }
}
