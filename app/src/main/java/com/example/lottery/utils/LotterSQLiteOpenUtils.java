package com.example.lottery.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LotterSQLiteOpenUtils extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lottery.db";
    private static final int DATABASE_VERSION = 1;

    public LotterSQLiteOpenUtils(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建数据表的 SQL 语句
        String createCookieTable = "CREATE TABLE IF NOT EXISTS lottery_cookie (id INTEGER PRIMARY KEY AUTOINCREMENT, cookie TEXT)";
        String createBuyLotteryTable = "CREATE TABLE IF NOT EXISTS buy_lottery (id INTEGER PRIMARY KEY AUTOINCREMENT, index_no INTEGER NOT NULL,number TEXT NOT NULL,color_type TEXT NOT NULL,group_id INTEGER NOT NULL)";
        String createClientInfo = "CREATE TABLE IF NOT EXISTS client_info (id INTEGER PRIMARY KEY AUTOINCREMENT, base_url TEXT NOT NULL,url TEXT NOT NULL,client_id TEXT NOT NULL,grant_type TEXT NOT NULL,scope TEXT NOT NULL,client_secret TEXT NOT NULL,user_name TEXT NOT NULL,user_password TEXT NOT NULL)";

        // 执行 SQL 语句创建数据表
        db.execSQL(createCookieTable);
        db.execSQL(createBuyLotteryTable);
        db.execSQL(createClientInfo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 数据库升级逻辑
        String upgradeTableQuery = "DROP TABLE IF EXISTS  lottery_cookie";

        db.execSQL(upgradeTableQuery);
        onCreate(db);
    }
}
