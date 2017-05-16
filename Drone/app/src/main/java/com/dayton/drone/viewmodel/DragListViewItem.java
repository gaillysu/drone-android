package com.dayton.drone.viewmodel;

/**
 * Created by Jason on 2017/5/12.
 */

public class DragListViewItem {
    private WorldClockCityItemModel item;
    private WorldClockTitleModel title;

    public DragListViewItem(WorldClockCityItemModel item, WorldClockTitleModel title) {
        this.item = item;
        this.title = title;
    }

    public WorldClockCityItemModel getItem() {
        return item;
    }

    public void setItem(WorldClockCityItemModel item) {
        this.item = item;
    }

    public WorldClockTitleModel getTitle() {
        return title;
    }

    public void setTitle(WorldClockTitleModel title) {
        this.title = title;
    }
}
