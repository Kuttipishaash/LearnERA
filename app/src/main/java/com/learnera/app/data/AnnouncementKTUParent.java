package com.learnera.app.data;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by Shankar on 06-08-2017.
 */

public class AnnouncementKTUParent implements ParentListItem {

    private String title;
    private List<AnnouncementKTUChild> children;

    public AnnouncementKTUParent() {

    }

    public String getmAnnouncementTitle() {
        return title;
    }

    public void setmAnnouncementTitle(String title) {
        this.title = title;
    }

    @Override
    public List<?> getChildItemList() {
        return children;
    }

    public void setChildList(List<AnnouncementKTUChild> childItemList) {
        children = childItemList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
