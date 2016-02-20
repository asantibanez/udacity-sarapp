package com.andressantibanez.sarapp.database.common;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;

/**
 * Created by asantibanez on 2/20/16.
 */
public class BaseCursor extends CursorWrapper {

    public static Uri CONTENT_URI = null;

    public BaseCursor(Cursor cursor) {
        super(cursor);
        moveToFirst();
    }

    public int count() {
        return getCount();
    }

    public void discard() {
        if(isClosed())
            return;

        close();
    }

}
