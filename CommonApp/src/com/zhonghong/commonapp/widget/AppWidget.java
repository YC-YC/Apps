package com.zhonghong.commonapp.widget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.zhonghong.commonapp.CommonAppHelper;
import com.zhonghong.commonapp.CommonAppHelper.CommonAppListChangedCallBack;
import com.zhonghong.commonapp.R;
import com.zhonghong.commonapp.setup.SetupActivity;
import com.zhonghong.commonapp.utils.AppBean;
import com.zhonghong.commonapp.utils.T;

public class AppWidget extends AppWidgetProvider implements CommonAppListChangedCallBack{

	private static final String TAG = "AppWidget";
	
	private Context mContext;
	private PackageManager mPm;
	private boolean bInit;
	private static Set<Integer> idSets = new HashSet<Integer>();	//存储所有组件
	
	private List<AppBean> appList/* = new ArrayList<String>()*/;
	
	private static int[] app_id = new int[]{
		R.id.app1,
		R.id.app2,
		R.id.app3,
		R.id.app4};
	private static int[] app_pic_id = new int[]{
		R.id.app1_app_pic,
		R.id.app2_app_pic,
		R.id.app3_app_pic,
		R.id.app4_app_pic};
	private static int[] app_name_id = new int[]{
		R.id.app1_app_name,
		R.id.app2_app_name,
		R.id.app3_app_name,
		R.id.app4_app_name};
	private static int[] cmd = new int[]{
		T.Cmd.OPEN_APP_1,
		T.Cmd.OPEN_APP_2,
		T.Cmd.OPEN_APP_3,
		T.Cmd.OPEN_APP_4};
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		String action = intent.getAction();
		init(context);
		LOG("Receive " + action);
		if (T.Action.UPDATE_APP.equals(action) )
		{
			updateAppAppWidget(context);
		}
		//按键广播
		else if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE))
		{
			Uri data = intent.getData();
			int key = Integer.parseInt(data.getSchemeSpecificPart());
			switch (key) {
			case T.Cmd.OPEN_APP_1:
				openOtherActivity(appList.get(0));
				break;
			case T.Cmd.OPEN_APP_2:
				openOtherActivity(appList.get(1));
				break;
			case T.Cmd.OPEN_APP_3:
				openOtherActivity(appList.get(2));
				break;
			case T.Cmd.OPEN_APP_4:
				openOtherActivity(appList.get(3));
				break;
			case T.Cmd.OPEN_SET_TIME:
				openSetTime();
				
				break;
			}
		}
	}

	private void openSetTime() {
		// TODO 自动生成的方法存根
		Intent it = new Intent(mContext, SetupActivity.class);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		mContext.startActivity(it);
//		Intent it = new Intent(Intent.ACTION_MAIN);
//		ComponentName name = new ComponentName("com.zhonghong.commonapp", "com.zhonghong.commonapp.setup.SetupActivity");
//		it.setComponent(name);
//		mContext.startActivity(it);
	}
	
	
	private void openOtherActivity(AppBean bean)
	{
		Intent it = new Intent(Intent.ACTION_MAIN);
		ComponentName name = new ComponentName(bean.pkgName, bean.className);
		it.setComponent(name);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		mContext.startActivity(it);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		LOG("onUpdate");
		init(context);
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		for (int appWidgetId : appWidgetIds) {
			idSets.add(Integer.valueOf(appWidgetId));
		}
		updateAppAppWidget(context);
	}

	private void init(Context context) {
		if (!bInit)
		{
			bInit = true;
			mContext = context;
			mPm = mContext.getPackageManager();
			if (T.mCommonAppHelper == null)
			{
				new CommonAppHelper(context);
			}
			T.mCommonAppHelper.setCommonAppListChangedCallBack(this);
			appList = T.mCommonAppHelper.getCommonAppList();
			LOG("getCommonAppList appList size = " + appList.size());
		}
	}
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		LOG("onDeleted");
		for(int appWidget:appWidgetIds)
		{
			idSets.remove(appWidget);
		}
	}

	@Override
	public void onEnabled(Context context) {
		// TODO 自动生成的方法存根
		super.onEnabled(context);
		LOG("onEnabled");
		init(context);
	}

	@Override
	public void onDisabled(Context context) {
		// TODO 自动生成的方法存根
		super.onDisabled(context);
		LOG("onDisabled");
	}

	private void updateAppAppWidget(Context context) {
		Iterator<Integer> iterator = idSets.iterator();
		int appWidgetId;
		RemoteViews remoteView;
		LOG("updateAppAppWidget");
		while(iterator.hasNext())
		{
			appWidgetId = iterator.next();
			remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_app);
			LOG("updateAppAppWidget222 AppList size = " + appList.size());
			for (int i =0; i < 4; i++)
			{
				remoteView.setInt(app_id[i], "setVisibility", View.INVISIBLE);
			}
			if (appList.size() > 0)
			{
				for (int i = 0; i < appList.size(); i++)
				{
					remoteView.setImageViewBitmap(app_pic_id[i], getPackageIcon(appList.get(i).pkgName));
					remoteView.setTextViewText(app_name_id[i], getPackageLabel(appList.get(i).pkgName));
					//设置监听事件
					remoteView.setOnClickPendingIntent(app_id[i], 
							getBtPendingIntent(context, cmd[i]));
					remoteView.setInt(app_id[i], "setVisibility", View.VISIBLE);
				}
			}
			remoteView.setOnClickPendingIntent(R.id.settime, 
					getBtPendingIntent(context, T.Cmd.OPEN_SET_TIME));
			
			AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, remoteView);
		}
	}
	
	private String getPackageLabel(String pkgName)
	{
//		LOG("pkgName = " + pkgName);
		if (mPm == null)
		{
			LOG("mPm = null");
		}
		try {
			PackageInfo info = mPm.getPackageInfo(pkgName, 0);
			ApplicationInfo applicationInfo = info.applicationInfo;
			return mPm.getApplicationLabel(applicationInfo).toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Bitmap getPackageIcon(String pkgName)
	{
		try {
			PackageInfo info = mPm.getPackageInfo(pkgName, 0);
			ApplicationInfo applicationInfo = info.applicationInfo;
			BitmapDrawable bitmapDrawable = (BitmapDrawable) mPm.getApplicationIcon(applicationInfo);
			return bitmapDrawable.getBitmap();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	private PendingIntent getBtPendingIntent(Context context, int key) {
		Intent intent = new Intent();
		intent.setClass(context, AppWidget.class);
		intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
		intent.setData(Uri.parse("custom:" + key));
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		return pi;
	}
	
	private void LOG(String string)
	{
		Log.e(TAG, string);
	}

	@Override
	public void callback() {
		appList = T.mCommonAppHelper.getCommonAppList();
		LOG("get AppList size = " + appList.size());
//		updateAppAppWidget(mContext);
		mContext.sendBroadcast(new Intent(T.Action.UPDATE_APP));
	}
	
}
