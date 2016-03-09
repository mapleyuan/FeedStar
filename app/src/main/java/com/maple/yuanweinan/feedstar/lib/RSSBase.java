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

/**
 * Common data about RSS feeds and items.
 * 
 * @author Mr Horn
 */
abstract class RSSBase {

  public String title;
  public String link;
  public String description;
  private java.util.List<String> categories;
  public String pubdate;

  public RSSBase() {

  }

  /**
   * Specify initial capacity for the List which contains the category names.
   */
  RSSBase(byte categoryCapacity) {
    categories = categoryCapacity == 0 ? null : new ArrayList<String>(
        categoryCapacity);
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getLink() {
    return link;
  }

  public java.util.List<String> getCategories() {
    if (categories == null) {
      return java.util.Collections.emptyList();
    }

    return java.util.Collections.unmodifiableList(categories);
  }

  public String getPubDate() {
    return pubdate;
  }

  void setTitle(String title) {
    this.title = title;
  }

  void setLink(String link) {
    this.link = link;
  }

  void setDescription(String description) {
    this.description = description;
  }

  void addCategory(String category) {
    if (categories == null) {
      categories = new ArrayList<String>(3);
    }

    this.categories.add(category);
  }

  void setPubDate(String pubDate) {
    this.pubdate = pubdate;
  }

  /**
   * Returns the title.
   */
  public String toString() {
    return title;
  }

  /**
   * Returns the hash code of the link.
   */
  @Override
  public int hashCode() {
    if (link == null) {
      return 0;
    }

    return link.hashCode();
  }

  /**
   * Compares the links for equality.
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object instanceof RSSBase) {
      /* other is never null */
      final RSSBase other = (RSSBase) (object);

      if (link == null) {
        return other.link == null;
      }

      return link.equals(other.link);
    } else {
      return false;
    }
  }

}

