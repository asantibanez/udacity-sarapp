package com.andressantibanez.sarapp.database.common;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;

import com.andressantibanez.sarapp.database.SarappProvider;

import java.util.ArrayList;

/**
 * Created by asantibanez on 2/20/16.
 */
public abstract class BaseSelection<T extends BaseSelection> {

    protected static final String EQUALS = "=";

    ArrayList<String> selectionParts;
    ArrayList<String> selectionArgsParts;
    String orderBy;

    public BaseSelection() {
        selectionParts = new ArrayList<>();
        selectionArgsParts = new ArrayList<>();
    }

    protected T addSelection(String column, String operator, String value) {
        selectionParts.add(String.format("%s %s ?", column, operator));
        selectionArgsParts.add(value);

        return (T) this;
    }

    protected T and() {
        selectionParts.add(" AND ");
        return (T) this;
    }

    public String selection() {
        return selectionParts.size() > 0 ?
                TextUtils.join("", selectionParts.toArray(new String[selectionParts.size()])) :
                null;
    }

    public String[] selectionArgs() {
        return selectionArgsParts.size() > 0 ?
                selectionArgsParts.toArray(new String[selectionArgsParts.size()]) :
                null;
    }

    public T orderBy(String orderBy) {
        this.orderBy = orderBy;
        return (T) this;
    }

    public String order() {
        return orderBy;
    }

    public abstract Uri uri();
    public abstract BaseCursor get();

    protected Cursor getCursor() {
        return SarappProvider.context()
                .getContentResolver()
                .query(uri(), null, selection(), selectionArgs(), order());
    }

    public CursorLoader getAsCursorLoader() {
        return new CursorLoader(SarappProvider.context(), uri(), null, selection(), selectionArgs(), order());
    }

}
