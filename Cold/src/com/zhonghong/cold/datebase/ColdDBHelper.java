package com.zhonghong.cold.datebase;

import com.zhonghong.cold.utils.T;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class ColdDBHelper extends SQLiteOpenHelper {

	private Context mContext;
	public ColdDBHelper(Context context)
	{
		super(context, T.DATABASE_FILE_NAME, null, T.DATABASE_VER);
		mContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自动生成的方法存根
//		Toast.makeText(mContext, "创建数据库", Toast.LENGTH_SHORT).show();
		db.execSQL("create table if not exists " + T.COLD_TABLE_NAME +"(_id integer primary key autoincrement, pkgName text , millis integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 自动生成的方法存根
//		Toast.makeText(mContext, "更新数据库", Toast.LENGTH_SHORT).show();
	}

}
