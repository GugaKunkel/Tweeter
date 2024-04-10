package edu.byu.cs.tweeter.client.presenter.view;

import android.content.Intent;

import java.util.List;

public interface PagedView<T> extends BaseView{
    void setLoadingFooter(boolean value);

    void addMoreItems(List<T> items);

    void returnUser(Intent intent);
}
