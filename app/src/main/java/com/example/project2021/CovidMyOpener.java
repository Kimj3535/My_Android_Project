package com.example.project2021;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CovidMyOpener extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "SavedCovidEntries";
    protected final static int VERSION_NUM = 1;

    public final static String TABLE_NAME = "SAVEDENTRIES";
    public final static String COL_PROVINCE = "PROVINCE";
    public final static String COL_DATE = "DATE";
    public final static String COL_CASES = "NUMCASES";
    public final static String COL_ID = "_id";


    //Constructor
    public CovidMyOpener(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override //onCreate method
    public void onCreate(SQLiteDatabase db) {

        //**If you change anything in this method you must increment the VERSION_NUM to force rebuild**
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_PROVINCE + " text,"
                + COL_DATE + " text,"
                + COL_CASES + " text);");

    }

    @Override //onUpgrade method
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop the old table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table
        onCreate(db);

    }

    @Override //OnDowngrade method
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop the old table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table
        onCreate(db);

    }

    //Add data to table
    public boolean insertEntry(String province, String date, String numCases) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_PROVINCE, province);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_CASES, numCases);

        long entryID = db.insert("SAVEDENTRIES", null, contentValues);

        return entryID != -1;
    }


    //Remove row of information
    public void removeRow(int position) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("SAVEDENTRIES", "_id = ?", new String[]{Long.toString(position)});

    }

    //View data
    public Cursor viewData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    //Get the data
    public Cursor getData() {

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);

        return data;
    }


    //(used for debug purposes)
    public void printCursor() {

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);

        int count = cursor.getCount();

        int colIndex = cursor.getColumnIndex("COL_PROVINCE");

        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            String message = cursor.getString(colIndex);
            Log.d("Col names", cursor.getColumnName(colIndex));
            Log.d("Province: ", message);
            cursor.moveToNext();
        }

        Log.d("version Number", Integer.toString(db.getVersion()));
        Log.d("num Cols", Integer.toString(cursor.getColumnCount()));
        Log.d("getCount Num of rows ", Integer.toString(count));
    }

} //End of MyOpener class

