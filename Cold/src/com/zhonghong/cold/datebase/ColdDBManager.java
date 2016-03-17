package com.zhonghong.cold.datebase;

import java.util.ArrayList;
import java.util.List;

import com.zhonghong.cold.utils.ColdAppBean;
import com.zhonghong.cold.utils.T;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ColdDBManager {

	
	private ColdDBHelper mDbHelper;
	private SQLiteDatabase mDatabase;

	public ColdDBManager(Context context) {

		mDbHelper = new ColdDBHelper(context);
	}

	public void openDatebase()
	{
		mDatabase = mDbHelper.getWritableDatabase();
	}
	
	// 添加
	public void add(List<ColdAppBean> beans) {
		mDatabase.beginTransaction();
		try {
			for (ColdAppBean bean : beans) {
				if (qureyIfExit(bean.pkgName))
				{
					updateItem(bean);
				}
				else
				{
					mDatabase.execSQL("insert into "+ T.COLD_TABLE_NAME + " values(null, ?, ?)", new Object[]{bean.pkgName, bean.millis});
				}
			}
			mDatabase.setTransactionSuccessful();
		} finally{
			mDatabase.endTransaction();
		}
	}

	// 删除
	public void deleteItem(String pkgName)
	{
		mDatabase.delete(T.COLD_TABLE_NAME, "pkgName = ?", new String[]{String.valueOf(pkgName)});
	}

	// 删除
	public void clear() {
//		mDatabase.execSQL("delete from " + TABLE_NAME + " if exists ");
		mDatabase.execSQL("DROP TABLE IF EXISTS " + T.COLD_TABLE_NAME);
	}
	
	// 更新
	public void updateItem(ColdAppBean item) {
		ContentValues values = new ContentValues();
		values.put("millis", item.millis);
		mDatabase.update(T.COLD_TABLE_NAME, values, "pkgName = ?", new String[]{item.pkgName});
	}

	//查询
	public List<ColdAppBean> qurey() {
		List<ColdAppBean> coldList = new ArrayList<ColdAppBean>();
		Cursor cursor = qureyCursor();
		while(cursor.moveToNext()){
			ColdAppBean bean = new ColdAppBean();
			bean.pkgName = cursor.getString(cursor.getColumnIndex("pkgName"));
			bean.millis = cursor.getLong(cursor.getColumnIndex("millis"));
			coldList.add(bean);
		}
		return coldList;
	}
	
	private Cursor qureyCursor(){
		Cursor c = mDatabase.rawQuery("select * from " + T.COLD_TABLE_NAME, null);
		return c;
	}
	
	private boolean qureyIfExit(String pkgName)
	{
		Cursor cursor = mDatabase.rawQuery("select * from " + T.COLD_TABLE_NAME + " where pkgName=?", new String[]{pkgName});
		if (cursor.moveToNext())
		{
			return true;
		}
		return false;
	}
	
	//关闭
	public void closeDatebase(){
		mDatabase.close();
	}
}
