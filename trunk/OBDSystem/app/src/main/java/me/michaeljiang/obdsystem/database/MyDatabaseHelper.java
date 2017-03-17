package me.michaeljiang.obdsystem.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Michael on 2016/11/14 0014.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    public static final String CREATE_ContentData = "create table ContentData ("
            + "ContentDataID integer primary key autoincrement, "
            + "userName text, "
            + "Content text)";


    @Override
    public void onCreate(SQLiteDatabase db) {
        //默认建表
        db.execSQL(CREATE_ContentData);
        Toast.makeText(mContext, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //删除表
//        db.execSQL("drop table if exists ContentData");
        onCreate(db);
    }

}
