package com.zhonghong.cold;

import java.util.ArrayList;
import java.util.List;

import com.tencent.bugly.crashreport.CrashReport;
import com.zhonghong.cold.datebase.ColdDBManager;
import com.zhonghong.cold.utils.ColdAppBean;
import com.zhonghong.cold.utils.T;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.util.Log;

public class ColdHelper implements Runnable {
	
	private static final String COLD_FILE = "coldtimepref";
	private static final String KEY_COLD_TIME = "coldtime";
	private static final long DEFAULT_COLD_TIME = 60*60*24;
	
	//所有应用信息
	private List<ColdAppBean> mAllAppInfo;	
	//被冷藏的App
	private ArrayList<String> mColdList = new ArrayList<String>();
	//被冷藏的白名称（在表中的数据说明不在冷藏范围内）
	private ArrayList<String> mWhiteList = null;
	//过滤的已安装的列表
	private List<String> mInstallApp = null;
	private String[] mDefaultWhiteList = new String[]{
			"com.zhonghong.commonapp",
			"com.zhonghong.cold",
			"com.zhonghong.gesture",
			"com.mobint.hololauncher",
			"com.example.zuiserver",
			"com.android.launcher",
			"com.android.launcher2",
			"com.android.launcher3",
			"com.chenli.launcher2",
			"com.chenli.launcher"
	};
	//冷藏库变化回调
	private ColdListChangedCallBack mCallback = null;
	
	private Context mContext;
	private PackageManager mPm;
	private ActivityManager mAm;
	private ColdDBManager mDBManager = null;
	
	private SharedPreferences mPref = null;
	private Editor mEditor = null;
	
	private Thread mThread;	//检测线程
	private boolean isRunning;	//线程退出
	
	private long mColdTime = DEFAULT_COLD_TIME;	//冷藏时间

	public ColdHelper(Context mContext) {
		super();
		this.mContext = mContext;
		mPm = mContext.getPackageManager();
		mAm = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		T.mColdHelper = this;
		CrashReport.initCrashReport(mContext, "900022119", false);
		mDBManager = new ColdDBManager(mContext);
		
		init();
		startCheck();
	}
	
	//冷藏列表更新回调
	public interface ColdListChangedCallBack
	{
		public void callback();
	}
	
	//冷藏列表变化回调
	public void setColdListChangedCallBack(ColdListChangedCallBack callback)
	{
		mCallback = callback;
	}
	
	// 获取冷藏库数据
	public List<String> getColdList()
	{
		LOG("getColdList");
		return mColdList;
	}
	
	// 设置冷藏时间
	public void setColdTime(long second)
	{
		if (mColdTime != second)
		{
			mColdTime = second;
			savePrefColdTime(mColdTime);
		}
	}
	
	//获取冷藏时间
	public long getColdTime()
	{
		return mColdTime;
	}
	
	//从冷藏库中删除
	public void RemoveFromColdList(String pkgName)
	{
		resetOneItemAllAppInfo(pkgName);
		updateColdList();
	}
	
	private void init() {
		getPrefColdTime();
		updateWhiteList();
		loadDatabase();
		
		checkAllInstallApp(0);
		updateColdList();
	}

	//加载数据库
	private void loadDatabase() {
		mDBManager.openDatebase();
		mAllAppInfo = mDBManager.qurey();
		mDBManager.closeDatebase();
	}
	
	//储存数据库
	private void saveDatabase() {
		mDBManager.openDatebase();
		mDBManager.add(mAllAppInfo);
		mDBManager.closeDatebase();
	}

	private void deleteDatabaseItem(String pkgName)
	{
		mDBManager.openDatebase();
		mDBManager.deleteItem(pkgName);
		mDBManager.closeDatebase();
		removeItemFromAllAppInfo(pkgName);
	}
	
	// 获取冷藏时间
	private void getPrefColdTime() {
		mPref = mContext.getSharedPreferences(COLD_FILE, Context.MODE_PRIVATE);
		mEditor = mPref.edit();
		mColdTime = mPref.getLong(KEY_COLD_TIME, DEFAULT_COLD_TIME);
	}
	
	//保存冷藏时间
	private void savePrefColdTime(long coldtime)
	{
		mEditor.putLong(KEY_COLD_TIME, coldtime);
		mEditor.commit();
	}
	
	//更新白名单
	private void updateWhiteList() {
		mWhiteList = new ArrayList<String>();
		for (int i = 0; i < mDefaultWhiteList.length; i++)
		{
			mWhiteList.add(mDefaultWhiteList[i]);
//			LOG("Add whiteList : " + i + "、 " + mWhiteListStrs[i]);
		}
		
	}

	/**
	 * 更新列表
	 * @param millisOffset
	 */
	private void checkAllInstallApp(long millisOffset) {
		mInstallApp = getAllInstallApp();
		if (mInstallApp.size() > 0)
		{
			for (int i = 0; i < mInstallApp.size(); i++)
			{
				addItemIntoAllAppInfo(mInstallApp.get(i), millisOffset);
			}
		}
//		printAllAppInfo();
	}

	private void printAllAppInfo()
	{
		if ( mAllAppInfo.size() > 0)
		{
			for (int i = 0; i < mAllAppInfo.size(); i++)
			{
				LOG("AllAppInfo : " + i + "、" + mAllAppInfo.get(i).toString());
			}
		}
	}
	
	// 开始检测
	private void startCheck()
	{
		if (!isRunning)
		{
			isRunning = true;
			mThread = new Thread(this);
			mThread.start();
		}
	}
	
	//停止检测
	private void stopCheck()
	{
		isRunning = false;
		//saveFile
	}
	
	//获取顶层包名
	private String getTopAppPackage() {
//		List<RunningTaskInfo> tasks = mAm.getRunningTasks(1);
		List<RunningAppProcessInfo> processes = mAm.getRunningAppProcesses();
//		LOG("getTasks size = " + tasks.size());
//		ArrayList<String> listTask = new ArrayList<String>();
		/*int i = 1;
		for (RunningTaskInfo task: tasks)
		{
			listTask.add(task.baseActivity.getPackageName());
			listTask.add("" + i + "." + task.topActivity.getPackageName() + ", "  +
		task.topActivity.getClassName() + ";" + 
		task.id + ";" + 
		task.baseActivity.getPackageName() + task.baseActivity.getClassName());
			i++;
		}*/
		return processes.get(0).processName;
//		return tasks.get(0).baseActivity.getPackageName();
	}
	
	
	private List<String> getAllTheLauncher()
    {
        List<String> names = null;
        Intent it = new Intent(Intent.ACTION_MAIN);
        it.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> ra = mPm.queryIntentActivities(it, 0);
        if (ra.size() != 0)
        {
            names = new ArrayList<String>();
        }
        for (int i = 0; i < ra.size(); i++)
        {
            String packageName = ra.get(i).activityInfo.packageName;
            LOG(packageName);
            names.add(packageName);
        }
        return names;
    }
	
	//获取所有的APP
	private List<String> getAllInstallApp()
	{
		List<String> names = new ArrayList<String>();
		List<PackageInfo> packages = mPm.getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++)
		{
			PackageInfo info = packages.get(i);
			
			 //判断是否为非系统预装的应用程序    
	        if ((info.applicationInfo.flags & info.applicationInfo.FLAG_SYSTEM) <= 0) {    
	            // customs applications    
//	        	LOG("customs App: " + info.packageName);
	        	names.add(info.packageName);
	        } 
	        else
	        {
//	        	LOG("system App: " + info.packageName);
	        }
		}
		return names;
	}
	
	private void getPackageInfo(String pkgName)
	{
		try {
			PackageInfo info = mPm.getPackageInfo(pkgName, 0);
			ApplicationInfo applicationInfo = info.applicationInfo;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	// 添加
	private void addItemIntoAllAppInfo(String pkgName, long millisOffset)
	{
		if (isInWhiteList(pkgName))
		{
			return;
		}
		
		if (mAllAppInfo.size() > 0)
		{
			int count = 0;
			for (count = 0; count < mAllAppInfo.size(); count++)
			{
				if (mAllAppInfo.get(count).pkgName.equals(pkgName))
				{
					mAllAppInfo.get(count).millis += millisOffset;
					return;
				}
			}
			if (count == mAllAppInfo.size())
			{
				ColdAppBean bean = new ColdAppBean(pkgName, millisOffset);
				mAllAppInfo.add(bean);
			}
		}
		else
		{
			ColdAppBean bean = new ColdAppBean(pkgName, millisOffset);
			mAllAppInfo.add(bean);
		}
	}
	
	private void removeItemFromAllAppInfo(String pkgName)
	{
		if (mAllAppInfo.size() > 0)
		{
			int count = 0;
			for (count = 0; count < mAllAppInfo.size(); count++)
			{
				if (mAllAppInfo.get(count).pkgName.equals(pkgName))
				{
					mAllAppInfo.remove(count);
					return;
				}
			}
		}
	}
	
	/**
	 * 重置mAllAppInfo指定包名的时间
	 * @param pkgName
	 */
	private void resetOneItemAllAppInfo(String pkgName)
	{
		if (mAllAppInfo.size() > 0)
		{
			for (int i = 0; i < mAllAppInfo.size(); i++)
			{
				if (mAllAppInfo.get(i).pkgName.equals(pkgName))
				{
					mAllAppInfo.get(i).millis = 0;
					return;
				}
			}
		}
	}
	
	private boolean isInWhiteList(String pkgName) {
		if (mWhiteList.size() > 0)
		{
			for (int i = 0; i < mWhiteList.size(); i++)
			{
				if (mWhiteList.get(i).equals(pkgName))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	//在已安装应用中
	private boolean isInInstallList(String pkgName)
	{
		if (mInstallApp.size() > 0)
		{
			for (int i = 0; i < mInstallApp.size(); i++)
			{
				if (mInstallApp.get(i).equals(pkgName))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private void LOG(String string)
	{
		Log.i(T.TAG.COLDHELPER, string);
	}

	@Override
	public void run() {
		//TODO 检测线程
		long lastCheckTime = SystemClock.elapsedRealtime();
		long saveTime = lastCheckTime;
		while (isRunning)
		{
			
			long nowTime = SystemClock.elapsedRealtime();
			if (nowTime - saveTime > T.THREAD_SAVE_DATABASE_TIME)
			{
				saveTime = nowTime;
				saveDatabase();
			}
			long timeInterVal = nowTime - lastCheckTime;
			if (timeInterVal >  T.THREAD_CHECH_TIME)
			{
				lastCheckTime = SystemClock.elapsedRealtime();
				checkAllInstallApp(timeInterVal);
				
				List<RunningTaskInfo> tasks = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(Integer.MAX_VALUE);
//				LOG("getTasks size = " + tasks.size());
				for (RunningTaskInfo task: tasks)
				{
//					LOG(task.baseActivity.getPackageName());
					resetOneItemAllAppInfo(task.baseActivity.getPackageName());
				}
				
				updateColdList();
			}
			else
			{
				try {
					Thread.sleep(T.THREAD_CHECH_TIME - timeInterVal);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	/**
	 * 更新冷藏列表
	 */
	private void updateColdList() {
		boolean hasNewColdItem = false;
		if (mAllAppInfo.size() > 0)
		{
			ArrayList<String> tmpColdList = new ArrayList<String>();
			for (int i = 0; i < mAllAppInfo.size(); i++)
			{
				if (!isInInstallList(mAllAppInfo.get(i).pkgName))
				{
					continue;
				}
				ColdAppBean bean = mAllAppInfo.get(i);
				if (bean.millis > mColdTime * 1000)//新加
				{
					tmpColdList.add(bean.pkgName);
				}
			}
			if (!tmpColdList.equals(mColdList))
			{
				hasNewColdItem = true;
			}
			mColdList = tmpColdList;
		}
		if (hasNewColdItem)
		{
			if (mCallback != null)
			{
				mCallback.callback();
				saveDatabase();
			}
			
		}
	}
	
}
