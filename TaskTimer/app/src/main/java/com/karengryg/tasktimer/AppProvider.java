package com.karengryg.tasktimer;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by karen on 12/21/17.
 */

public class AppProvider extends ContentProvider {

    private static final String TAG = "AppProvider";

    private AppDatabase mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final String CONTENT_AUTHORITY = "com.karengryg.tasktimer.provider";
    public static final Uri CONTENT_AUTHORITY_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private static final int TASKS = 100;
    private static final int TASKS_ID = 101;

    private static final int TIMINGS = 200;
    private static final int TIMINGS_ID = 201;

    private static final int TASK_TIMINGS = 300;
    private static final int TASK_TIMINGS_ID = 301;

    private static final int TASK_DURATIONS = 400;
    private static final int TASK_DURATIONS_ID = 401;


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CONTENT_AUTHORITY, TasksContract.TABLE_NAME, TASKS);
        matcher.addURI(CONTENT_AUTHORITY, TasksContract.TABLE_NAME + "/#", TASKS_ID);

//        matcher.addURI(CONTENT_AUTHORITY, TimingsContract.TABLE_NAME, TIMINGS);
//        matcher.addURI(CONTENT_AUTHORITY, TimingsContract.TABLE_NAME + "/#", TIMINGS_ID);
//
//        matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME, TASK_DURATIONS);
//        matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME + "/#", TASK_DURATIONS_ID);

        return matcher;
    }



    @Override
    public boolean onCreate() {
        mOpenHelper = AppDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int matcher = sUriMatcher.match(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        switch (matcher) {
            case TASKS:
                queryBuilder.setTables(TasksContract.TABLE_NAME);
                break;
            case TASKS_ID:
                queryBuilder.setTables(TasksContract.TABLE_NAME);
                long taskId = TasksContract.getTaskId(uri);
                queryBuilder.appendWhere(TasksContract.Columns._ID + " = " + taskId);
                break;
//            case TIMINGS:
//                queryBuilder.setTables(TimingsContract.TABLE_NAME);
//                break;
//            case TIMINGS_ID:
//                queryBuilder.setTables(TimingsContract.TABLE_NAME);
//                long timingId = TimingsContract.getTimingId(uri);
//                queryBuilder.appendWhere(TimingsContract.Columns._ID + " = " + timingId);
//                break;
//            case TASK_DURATIONS:
//                queryBuilder.setTables(DurationsContract.TABLE_NAME);
//                break;
//            case TASK_DURATIONS_ID:
//                queryBuilder.setTables(DurationsContract.TABLE_NAME);
//                long durationId = DurationsContract.getDurationId(uri);
//                queryBuilder.appendWhere(DurationsContract.Columns._ID + " = " + durationId);
//                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case TASKS:
                return TasksContract.CONTENT_TYPE;
            case TASKS_ID:
                return TasksContract.CONTENT_ITEM_TYPE;

//            case TIMINGS:
//                return Timingscontract.Timings.CONTENT_TYPE;
//            case TIMINGS_ID:
//                return Timingscontract.Timings.CONTENT_ITEM_TYPE;
//
//            case TASK_DURATIONS:
//                return DurationsContract.TaskDurations.CONTENT_TYPE;
//            case TASK_DURATIONS_ID:
//                return DurationsContract.TaskDurations.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("unknown uri: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);

        final SQLiteDatabase db;

        Uri returnUri;
        long recordId;

        switch (match) {
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(TasksContract.TABLE_NAME, null, values);
                if (recordId >= 0) {
                    returnUri = TasksContract.buildTaskUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;
            case TIMINGS:
//                db = mOpenHelper.getWritableDatabase();
//                recordId = db.insert(TimingsContract.Timings.buildTimingUri(recordId));
//                if (recordId >= 0) {
//                    returnUri = TimingsContract.Timings.buildTimingUri(recordId);
//                } else {
//                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
//                }
//                break;

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }


        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db;

        int count;

        String selectionCriteria;

        switch (match) {
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(TasksContract.TABLE_NAME, selection, selectionArgs);
                break;
            case TASKS_ID:
                db = mOpenHelper.getWritableDatabase();
                long taskId = TasksContract.getTaskId(uri);
                selectionCriteria = TasksContract.Columns._ID + " = " + taskId;
                if (selection != null && selection.length() > 0) {
                    selectionCriteria += " AND (" + selection + ");";
                }
                count = db.delete(TasksContract.TABLE_NAME, selectionCriteria, selectionArgs);
                break;

//            case TIMINGS:
//                db = mOpenHelper.getWritableDatabase();
//                count = db.delete(TimingsContract.TABLE_NAME, selection, selectionArgs);
//                break;
//            case TIMINGS_ID:
//                db = mOpenHelper.getWritableDatabase();
//                long timingsId = TimingsContract.getTaskId(uri);
//                selectionCriteria = TimingsContract.Columns._ID + " = " + timingsId;
//                if (selection != null && selection.length() > 0) {
//                    selectionCriteria += " AND (" + selection + ");";
//                }
//                count = db.delete(TimingsContract.TABLE_NAME, selectionCriteria, selectionArgs);
//                break;

            default:
                throw new IllegalArgumentException("unknown uri: " + uri.toString());
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db;

        int count;

        String selectionCriteria;

        switch (match) {
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(TasksContract.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TASKS_ID:
                db = mOpenHelper.getWritableDatabase();
                long taskId = TasksContract.getTaskId(uri);
                selectionCriteria = TasksContract.Columns._ID + " = " + taskId;
                if (selection != null && selection.length() > 0) {
                    selectionCriteria += " AND (" + selection + ");";
                }
                count = db.update(TasksContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
                break;

//            case TIMINGS:
//                db = mOpenHelper.getWritableDatabase();
//                count = db.update(TimingsContract.TABLE_NAME, values, selection, selectionArgs);
//                break;
//            case TIMINGS_ID:
//                db = mOpenHelper.getWritableDatabase();
//                long timingsId = TimingsContract.getTaskId(uri);
//                selectionCriteria = TimingsContract.Columns._ID + " = " + timingsId;
//                if (selection != null && selection.length() > 0) {
//                    selectionCriteria += " AND (" + selection + ");";
//                }
//                count = db.update(TimingsContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
//                break;

            default:
                throw new IllegalArgumentException("unknown uri: " + uri.toString());
        }
        return count;
    }
}
