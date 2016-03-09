/*
 * Copyright (C) 2010 A. Horn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maple.yuanweinan.feedstar.lib;

import java.util.ArrayList;
import java.util.List;

/**
 * Data about an RSS feed and its RSS items.
 * 
 * @author Mr Horn
 */
public class RSSFeed extends RSSBase {

	public int mID;
	public String mThumbnail;
    private  java.util.List<RSSItem> items;
	private java.util.Date lastBuildDate;
	private Integer ttl;


	public RSSFeed(String title, String address, String description) {
		this.title = title;
		this.link = address;
		this.description = description;
		items = new ArrayList<>();
	}

  public RSSFeed() {
    super(/* initial capacity for category names */ (byte) 3);
    items = new ArrayList<>();
  }

  /**
   * Returns an unmodifiable list of RSS items.
   */
  public java.util.List<RSSItem> getItems() {
    return items;
  }
	public void addAllItem(List<RSSItem> items) {
		this.items.addAll(items);
	}

	public void clearAllItem() {
		this.items.clear();
	}

  public void addItem(RSSItem item) {
    items.add(item);
  }

	void setLastBuildDate(java.util.Date date) {
		lastBuildDate = date;
	}

	public java.util.Date getLastBuildDate() {
		return lastBuildDate;
	}

	void setTTL(Integer value) {
		ttl = value;
	}

	public Integer getTTL() {
		return ttl;
	}

}

