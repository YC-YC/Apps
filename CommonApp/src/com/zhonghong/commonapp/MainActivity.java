package com.zhonghong.commonapp;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zhonghong.commonapp.CommonAppHelper.CommonAppListChangedCallBack;
import com.zhonghong.commonapp.database.CommonDBManager;
import com.zhonghong.commonapp.setup.SetupActivity;
import com.zhonghong.commonapp.utils.SubTableBean;
import com.zhonghong.commonapp.utils.T;

public class MainActivity extends Activity implements CommonAppListChangedCallBack{

	private CommonDBManager mDbManager = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
	}

	private void init() {
//		mDbManager = new CommonDBManager(this);	
		if (T.mCommonAppHelper == null)
		{
			new CommonAppHelper(this);
			T.mCommonAppHelper.setCommonAppListChangedCallBack(this);
		}
	}

	
	public void doClick(View view)
	{
		switch (view.getId()) {
		case R.id.button1:
//			saveCurrentInfo();
			startActivity(new Intent(this, SetupActivity.class));
			break;
		case R.id.button2:
//			qureyInfo();
			break;
		case R.id.button3:
//			deleteSubTable();
			break;
		case R.id.button4:
//			deleteAll();
			break;
		}
	}
	
	
	
	
	
	private void deleteAll() {
		mDbManager.openDatebase();
		mDbManager.clear();
		mDbManager.closeDatebase();
	}

/*	private void deleteSubTable() {
		mDbManager.openDatebase();
		mDbManager.deleteSubTable(getTopAppPackage());
		mDbManager.closeDatebase();
	}
*/
	private void qureyInfo() {
		mDbManager.openDatebase();
		List<SubTableBean> qurey = mDbManager.qurey();
		mDbManager.closeDatebase();
		if (qurey != null)
		{
			for (SubTableBean bean:qurey)
			{
				LOG(bean.toString());
			}
		}
	}

	private void saveCurrentInfo() {
		mDbManager.openDatebase();
//		mDbManager.addSubTableItem(getTopAppPackage(), System.currentTimeMillis());
		mDbManager.closeDatebase();
	}

	//获取顶层包名
		private String getTopAppPackage() {
			List<RunningAppProcessInfo> processes = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
			return processes.get(0).processName;
		}
		
		private void LOG(String string)
		{
			Log.i("CommonApp", string);
		}

		@Override
		public void callback() {
			/*List<String> appList = T.mCommonAppHelper.getCommonAppList();	
			for (String app: appList)
			{
				LOG(app);
			}*/
		}
		
	
}
