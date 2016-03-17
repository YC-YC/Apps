package com.zhonghong.commonapp.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zhonghong.commonapp.utils.SubTableBean;
import com.zhonghong.commonapp.utils.T;

public class CommonDBManager {

	private static final String TAG = "CommonDBManager";
	
	private CommonDBHelper mDbHelper;
	private SQLiteDatabase mDatabase;
	private Context mContext;

	public CommonDBManager(Context context) {
		mContext = context;
		mDbHelper = new CommonDBHelper(context);
	}

	public void openDatebase()
	{
		mDatabase = mDbHelper.getWritableDatabase();
	}
	
	//�ر�
	public void closeDatebase()
	{
		mDatabase.close();
	}
	
	//���
	public void addSubTableItem(String subTable, String className, long time)
	{
		mDatabase.execSQL("create table if not exists " + T.COMMON_APP_TABLE_NAME +"(_id integer primary key autoincrement, subtable text)");
		String newSubTable = genNewSubTable(subTable);
//		LOG(newSubTable);
		if (!qureyIfSubTableExit(newSubTable))
		{
//			LOG("qureyIfSubTableExit true");
			mDatabase.execSQL("insert into "+ T.COMMON_APP_TABLE_NAME + " values(null, ?)", new Object[]{newSubTable});
		}
		mDatabase.execSQL("create table if not exists " + newSubTable +"(_id integer primary key autoincrement, classname text, millis integer)");
		mDatabase.execSQL("insert into "+ newSubTable + " values(null, ?, ?)", new Object[]{className, time});
	}
	
	
	//��������.ת��aaa,��Ϊ�����������������
	private String genNewSubTable(String pkgName)
	{
		return pkgName.replace(".", T.REPLACE_STRING);
	}
	
	//��ԭ
	private String getOldSubTable(String pkgName)
	{
		return pkgName.replace(T.REPLACE_STRING, ".");
	}
	
	//ɾ���ӱ�
	private void deleteSubTable(String subTable)
	{
		String newSubTable = genNewSubTable(subTable);
		mDatabase.execSQL("DROP TABLE IF EXISTS " + newSubTable);
		mDatabase.delete(T.COMMON_APP_TABLE_NAME, "subtable = ?", new String[]{String.valueOf(newSubTable)});
	}
	
	//ȫ��ɾ��
	public void clear() 
	{
		Cursor cursor = qureyCursor(T.COMMON_APP_TABLE_NAME);
		while(cursor.moveToNext()){	//�����ܱ�
			String table = cursor.getString(cursor.getColumnIndex("subtable"));
			mDatabase.execSQL("DROP TABLE IF EXISTS " + table);
		}
		mDatabase.execSQL("DROP TABLE IF EXISTS " + T.COMMON_APP_TABLE_NAME);
	}
	
	//��ѯ
	public List<SubTableBean> qurey() {
		mDatabase.execSQL("create table if not exists " + T.COMMON_APP_TABLE_NAME +"(_id integer primary key autoincrement, subtable text)");
		List<String> tables = new ArrayList<String>();
		Cursor cursor = qureyCursor(T.COMMON_APP_TABLE_NAME);
		while(cursor.moveToNext()){	//�����ܱ�
			String table = cursor.getString(cursor.getColumnIndex("subtable"));
			tables.add(table);
		}
		List<SubTableBean> subtables = new ArrayList<SubTableBean>();
		for (int i = 0; i < tables.size(); i++)	//��������С��
		{
			cursor = qureyCursor(tables.get(i));
			SubTableBean bean = new SubTableBean();
//			bean.subTable = tables.get(i);
			bean.subTable = getOldSubTable(tables.get(i));
			while (cursor.moveToNext())
			{
				bean.className = cursor.getString(cursor.getColumnIndex("classname"));
				bean.runtime.add(cursor.getLong(cursor.getColumnIndex("millis")));
			}
			subtables.add(bean);
		}
		return subtables;
	}
	
	
	
	private Cursor qureyCursor(String table)
	{
		Cursor c = mDatabase.rawQuery("select * from " + table, null);
		return c;
	}
	
	//���Ƿ����
	private boolean qureyIfSubTableExit(String subTable)
	{
		Cursor cursor = mDatabase.rawQuery("select * from " + T.COMMON_APP_TABLE_NAME + " where subtable=?", new String[]{subTable});
		if (cursor.moveToNext())
		{
			return true;
		}
		return false;
	}
	
	private void LOG(String string)
	{
		Log.i(TAG, string);
	}
	
}
