package com.andressantibanez.sarapp.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.andressantibanez.sarapp.database.invoices.InvoicesContract;

import java.util.Arrays;

/**
 * Created by asantibanez on 2/20/16.
 */
public class SarappProvider extends ContentProvider {

    private static final String LOG_TAG = SarappProvider.class.getSimpleName();

    public static final String AUTHORITY = "com.andressantibanez.sarapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /**
     * Context
     */
    private static Context mContext;

    public static Context context() {
        return mContext;
    }


    /**
     * Matcher and routes
     */
    //Routes
    private static final int INVOICES = 1;
    //Matcher
    private static UriMatcher mUriMatcher;
    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, InvoicesContract.TABLE_NAME, INVOICES);
    }


    /**
     * Database
     */
    DatabaseSQLiteOpenHelper mSQLiteOpenHelper;

    public SQLiteDatabase writableDatabase() {
        return mSQLiteOpenHelper.getWritableDatabase();
    }

    public SQLiteDatabase readableDatabase() {
        return mSQLiteOpenHelper.getReadableDatabase();
    }


    /**
     * Initializes content provider
     * @return
     */
    @Override
    public boolean onCreate() {
        mSQLiteOpenHelper = new DatabaseSQLiteOpenHelper(getContext());
        mContext = getContext();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        Log.d(LOG_TAG, String.format("query | uri=%s, projection=%s, selection=%s, selectionArgs=%s, orderBy=%s", uri, Arrays.toString(projection), selection, Arrays.toString(selectionArgs), orderBy));

        String table = uri.getLastPathSegment();
        Cursor cursor = readableDatabase().query(table, projection, selection, selectionArgs, null, null, orderBy);

        if(cursor != null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Log.d(LOG_TAG, String.format("insert | uri=%s, values=%s", uri, values));

        String tableName = uri.getLastPathSegment();
        long rowId = writableDatabase().insert(tableName, null, values);

        if(rowId == -1)
            return null;

        notifyUriChange(uri);
        return uri.buildUpon().appendPath(String.valueOf(rowId)).build();
    }

    private void notifyUriChange(@NonNull Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, String.format("update | uri=%s, values=%s, selection=%s, selectionArgs=%s", uri, values, selection, Arrays.toString(selectionArgs)));

        String tableName = uri.getLastPathSegment();
        int updatedRows = writableDatabase().update(tableName, values, selection, selectionArgs);
        if(updatedRows > 0)
            notifyUriChange(uri);

        return updatedRows;
    }


    /**
     * Static helpers
     */
    public static Uri insertValues(Uri uri, ContentValues values) {
        Context context = SarappProvider.context();
        return context.getContentResolver().insert(uri, values);
    }

    public static int updateValues(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Context context = SarappProvider.context();
        return context.getContentResolver().update(uri, values, selection, selectionArgs);
    }

}
