package com.zhonghong.commonapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.zhonghong.commonapp.utils.T;

public class CommonDBHelper extends SQLiteOpenHelper {

	private Context mContext;
	public CommonDBHelper(Context context)
	{
		super(context, T.DATABASE_FILE_NAME, null, T.DATABASE_VER);
		mContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �Զ����ɵķ������
//		Toast.makeText(mContext, "�������ݿ�", Toast.LENGTH_SHORT).show();
//		db.execSQL("create table if not exists " + T.COMMON_APP_TABLE_NAME +"(_id integer primary key autoincrement, pkgName text , millis integer)");
		db.execSQL("create table if not exists " + T.COMMON_APP_TABLE_NAME +"(_id integer primary key autoincrement, subtable text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �Զ����ɵķ������
//		Toast.makeText(mContext, "�������ݿ�", Toast.LENGTH_SHORT).show();
		
		Cursor cursor = db.rawQuery("select * from " + T.COMMON_APP_TABLE_NAME, null);
		while(cursor.moveToNext()){	//�����ܱ�
			String table = cursor.getString(cursor.getColumnIndex("subtable"));
			db.execSQL("DROP TABLE IF EXISTS " + table);
		}
		db.execSQL("DROP TABLE IF EXISTS " + T.COMMON_APP_TABLE_NAME);
		
		db.execSQL("create table if not exists " + T.COMMON_APP_TABLE_NAME +"(_id integer primary key autoincrement, subtable text)");

	}

}
