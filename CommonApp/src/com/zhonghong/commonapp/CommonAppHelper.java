package com.zhonghong.commonapp;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.os.SystemClock;
import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;
import com.zhonghong.commonapp.database.CommonDBManager;
import com.zhonghong.commonapp.utils.AppBean;
import com.zhonghong.commonapp.utils.AppInfoBean;
import com.zhonghong.commonapp.utils.SubTableBean;
import com.zhonghong.commonapp.utils.T;

public class CommonAppHelper implements Runnable{

	
	
	private Context mContext;
	private Thread mThread;	//����߳�
	private boolean isRunning;	//�߳��˳�
	
	private ActivityManager mAm;
	private SharedPreferences mPref = null;
	private Editor mEditor = null;
	
	private CommonDBManager mDbManager = null;
	private List<AppInfoBean> mAppInfos = null;	//Ӧ����Ϣ
	private List<SubTableBean> mSubTableInfos = null;	//������Ϣ
	private List<AppBean> mCommonAppList = new ArrayList<AppBean>();
	private long mCommonAppTime = 0;
	
	private CommonAppListChangedCallBack mCallback = null;
	
	private String[] mDefaultWhiteList = new String[]{
			"com.zhonghong.commonapp",
			"com.zhonghong.cold",
			"com.zhonghong.gesture",
			"com.aliyun.ushell",
			"com.mobint.hololauncher",
			"com.mobint.hololauncher.hd",
			"com.example.zuiserver",
			"com.android.launcher",
			"com.android.launcher2",
			"com.chenli.launcher",
			"com.chenli.launcher2",
			"com.android.systemui",
			"com.android.packageinstaller"
	};
	
	
	public CommonAppHelper(Context context) {
		T.mCommonAppHelper = this;
		mContext = context;
		CrashReport.initCrashReport(context, "900022768", false);
		init();
		startThread();
	}
	
	public List<AppBean> getCommonAppList()
	{
		return mCommonAppList;
	}
	
	public long getCommonAppTime()
	{
		return mCommonAppTime;
	}
	
	public void setCommonAppTime(long time)
	{
		if (mCommonAppTime != time)
		{
			mCommonAppTime = time;
			savePrefCommonTime(time);
		}
		
	}
	
	//�ӳ���App�б���ɾ��һ��
	public void removeFromCommonAppList(String pkgName)
	{
		for (int i = 0; i < mCommonAppList.size(); i++)
		{
			if (pkgName.equals(mCommonAppList.get(i).pkgName))
			{
				mAppInfos = getAppInfosFromSubTableInfos();
				updateCommonAppList();
			}
		}
	}
	
	//����б���»ص�
 	public interface CommonAppListChangedCallBack
	{
		public void callback();
	}
	
	//�б�仯�ص�
	public void setCommonAppListChangedCallBack(CommonAppListChangedCallBack callback)
	{
		mCallback = callback;
	}
	
	private void init() {
		mAm = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		mDbManager = new CommonDBManager(mContext);		
		getPrefCommonTime();
		mSubTableInfos = getSubTableInfos();
//		printSubtableInfo();
		mAppInfos = getAppInfosFromSubTableInfos();
		updateCommonAppList();
	}
	
	private void getPrefCommonTime() {
		mPref = mContext.getSharedPreferences(T.COMMONTIME_FILE, Context.MODE_PRIVATE);
		mEditor = mPref.edit();
		mCommonAppTime = mPref.getLong(T.KEY_COMMONTIME, T.DEFAULT_COMMONAPP_TIME);
	}
	
	private void savePrefCommonTime(long time)
	{
		mEditor.putLong(T.KEY_COMMONTIME, time);
		mEditor.commit();
	}
	
	private void printSubtableInfo()
	{
		for (SubTableBean bean: mSubTableInfos)
		{
			LOG(bean.toString());
		}
	}
	//�����ݿ��ȡ�ӱ���Ϣ
	private List<SubTableBean> getSubTableInfos()
	{
		mDbManager.openDatebase();
		List<SubTableBean> qurey = mDbManager.qurey();
		mDbManager.closeDatebase();
		return qurey;
	}
	
	
	/**
	 * ����Ϣ���浽���ݿ��
	 */
	private void saveSubTableInfos()
	{
		mDbManager.openDatebase();
		mDbManager.clear();
		for (SubTableBean tableBean : mSubTableInfos)
		{
			for (Long time:tableBean.runtime)
			{
				mDbManager.addSubTableItem(tableBean.subTable, tableBean.className, time);
			}
		}
		mDbManager.closeDatebase();
	}
	
	
	//���ӱ���Ϣ��ȡӦ����Ϣ
	private List<AppInfoBean> getAppInfosFromSubTableInfos()
	{
		List<AppInfoBean> appInfos = new ArrayList<AppInfoBean>();
		long currtenTime = System.currentTimeMillis();
//		for (SubTableBean tableBean : mSubTableInfos) {
		for (int j = 0; j < mSubTableInfos.size(); j ++){
			SubTableBean tableBean = mSubTableInfos.get(j);
			int count = 0;
			long lastTime = 0;
			String pkgName = tableBean.subTable;
			String className = tableBean.className;
			if (!isAppExit(pkgName) || isInWhiteList(pkgName))	//���ڰ�װ�б���ɾ��
			{
				mSubTableInfos.remove(j);
				continue;
			}
			for (int i = 0; i < tableBean.runtime.size(); i++)
			{
				long tmpTime = tableBean.runtime.get(i);
//				LOG("currtenTime - tmpTime = " + (currtenTime - tmpTime) + " mCommonAppTime = " + mCommonAppTime);
				if (currtenTime - tmpTime < mCommonAppTime)
				{
//					LOG("currtenTime - tmpTime < mCommonAppTime");
					count++;
					if (tmpTime > lastTime)
					{
						lastTime = tmpTime;
					}
				}
			}
//			LOG("count = " + count);
			if (count > 0)
			{
				AppInfoBean appInfo = new AppInfoBean(count, lastTime, pkgName, className);
				appInfos.add(appInfo);
			}
		}
		return appInfos;
	}
	
	private boolean isAppExit(String pkgName)
	{
		List<String> installApp = getAllInstallApp();
		for (String app: installApp)
		{
			if (app.equals(pkgName))
			{
//				LOG("isAppExit true");
				return true;
			}
		}
//		LOG("isAppExit false");
		return false;
	}
	
	//��ȡ���е�APP
	private List<String> getAllInstallApp()
	{
		List<String> names = new ArrayList<String>();
		List<PackageInfo> packages = mContext.getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++)
		{
			PackageInfo info = packages.get(i);
			names.add(info.packageName);
			 //�ж��Ƿ�Ϊ��ϵͳԤװ��Ӧ�ó���    
	        if ((info.applicationInfo.flags & info.applicationInfo.FLAG_SYSTEM) <= 0) {    
	            // customs applications    
//	        	LOG("customs App: " + info.packageName);
//	        	names.add(info.packageName);
	        } 
	        else
	        {
//	        	LOG("system App: " + info.packageName);
	        }
		}
		return names;
	}
	
	/**
	 * ���һ���¼��SubTableInfos
	 * @param pkgName
	 * @param className
	 * @param time
	 */
	private void pushInSubTableInfos(String pkgName, String className, long time)
	{
//		String newPkgName = genNewSubTable(pkgName);
		for (int i = 0; i < mSubTableInfos.size(); i++)
		{
			SubTableBean tableBean = mSubTableInfos.get(i);
			if (pkgName.equals(tableBean.subTable))//����ӱ��һ��
			{
				mSubTableInfos.get(i).className = className;
				mSubTableInfos.get(i).runtime.add(time);
//				LOG("pushInSubTableInfos111 " + mSubTableInfos.get(i).toString());
				updateSubTable(i);
				saveSubTableInfos();
				return;
			}	
		}
		//���һ�����ӱ�
		SubTableBean tableBean = new SubTableBean();
		tableBean.subTable = pkgName;
		tableBean.className = className;
		tableBean.runtime.add(time);
//		LOG("pushInSubTableInfos222 " + tableBean.toString());
		mSubTableInfos.add(tableBean);
		saveSubTableInfos();
	}
	
	/**
	 * ����SubTableInfos����һ����¼��ʱ��
	 * @param pkgName
	 * @param time
	 */
	private void updateSubTableInfoTime(String pkgName, long time)
	{
		for (int i = 0; i < mSubTableInfos.size(); i++)
		{
			SubTableBean tableBean = mSubTableInfos.get(i);
			if (pkgName.equals(tableBean.subTable))
			{
				int maxItem = 0;
				long maxTime = mSubTableInfos.get(i).runtime.get(0);
				for (int j = 1; j < mSubTableInfos.get(i).runtime.size(); j++)//��������һ��ʱ��
				{
					if (mSubTableInfos.get(i).runtime.get(j) > maxTime)
					{
						maxTime = mSubTableInfos.get(i).runtime.get(j);
						maxItem = j;
					}
				}
				mSubTableInfos.get(i).runtime.set(maxItem, time);
//				saveSubTableInfos();
				return;
			}	
		}
	}
	

	/**
	 * ����SubTable����ֹ��¼�����ݹ���
	 * @param position
	 */
	private void updateSubTable(int position) {
		long currtenTime = System.currentTimeMillis();
		for (int i = 0; i < mSubTableInfos.get(position).runtime.size(); i++)
		{
			if (currtenTime - mSubTableInfos.get(position).runtime.get(i) > T.Time.MONTH)
			{
				mSubTableInfos.get(position).runtime.remove(i);
				i--;
			}
		}
	}

	//ˢ��mCommonAppList
	private void updateCommonAppList()
	{
		sortAppInfos();
		List<AppBean> tmpCommonList = new ArrayList<AppBean>();
		for (int i = 0; i < mCommonAppList.size(); i++)
		{
			tmpCommonList.add(mCommonAppList.get(i));
		}
		mCommonAppList.clear();
		for (int i = 0; i < mAppInfos.size()&& i <4; i++)
		{
			mCommonAppList.add(new AppBean(mAppInfos.get(i).pkgName, mAppInfos.get(i).lastClassName));
		}
//		if (!mCommonAppList.equals(tmpCommonList))
		if (!isTwoListEqual(tmpCommonList, mCommonAppList))
		{
			//TODO ֪ͨ�б�仯
			LOG("updateCommonAppList");
			if (mCallback != null)
			{
				mCallback.callback();
			}
			
		}
		else
		{
//			LOG("updateCommonAppList Same");	
		}
	}

	private boolean isTwoListEqual(List<AppBean> list1,  List<AppBean> list2)
	{
		/*if (list1.size() == 0 && list2.size() == 0)
		{
			return false;
		}*/
		if (list1.size() != list2.size())
		{
			return false;
		}
		for (int i = 0; i < list1.size(); i++)
		{
			if (!list1.get(i).toString().equals(list2.get(i).toString()))
			{
				return false;
			}
		}
		return true;
	}
	
	//����AppInfos�����ݴ������ٸ���ʱ��
	private void sortAppInfos() {
		
//		LOG("Befor Sort");
//		printAppInfo();
		//�Ŵ���
		sortByCount();
//		LOG("After SortCount");
//		printAppInfo();
		//��ʱ��
		sortByTime();
//		LOG("After SortTime");
//		printAppInfo();
	}

	private void printAppInfo()
	{
		for (AppInfoBean bean: mAppInfos)
		{
			LOG(bean.toString());
		}
	}
	
	private void sortByTime() {
		for (int i = 0; i < mAppInfos.size()-1; i++)
		{
			if (mAppInfos.get(i).count != mAppInfos.get(i+1).count)
			{
			}
			else
			{
				//���Ҵ�����ͬ����ʼ�������
				int sameItemStart = i;
				int sameItemEnd = i;
				for (int j = i; j < mAppInfos.size()-1; j++)
				{
					if (mAppInfos.get(j).count != mAppInfos.get(j+1).count)
					{
						sameItemEnd = j;
						i = j;
						break;
					}
					else
					{
						sameItemEnd = j+1;
					}
				}
//				LOG("sameItemStart = " + sameItemStart + ",sameItemEnd = " + sameItemEnd);
				//����ʱ������
				for (int j = sameItemStart; j < sameItemEnd; j++)
				{
					AppInfoBean bean = mAppInfos.get(j);
					for (int k = j+1; k <= sameItemEnd; k++)
					{
						if (bean.lastTime < mAppInfos.get(k).lastTime)
						{
							swapAppInfoItem(k, j);
							bean = mAppInfos.get(j);
						}
					}
				}
				
			}
			if (i >= 3)	//����4��
			{
				break;
			}
		}
	}

	private void sortByCount() {
		for (int i = 0; i < mAppInfos.size() -1; i++)
		{
			AppInfoBean bean = mAppInfos.get(i);
			for (int j = i+1; j < mAppInfos.size(); j++)
			{
				if (bean.count < mAppInfos.get(j).count)
				{
					swapAppInfoItem(i, j);
					bean = mAppInfos.get(i);
				}
			}
		}
	}

	private void swapAppInfoItem(int item1, int item2)
	{
		AppInfoBean tmpBean = new AppInfoBean(mAppInfos.get(item1));
		
		mAppInfos.get(item1).count = mAppInfos.get(item2).count;
		mAppInfos.get(item1).lastTime = mAppInfos.get(item2).lastTime;
		mAppInfos.get(item1).pkgName = mAppInfos.get(item2).pkgName;
		mAppInfos.get(item1).lastClassName = mAppInfos.get(item2).lastClassName;
		
		mAppInfos.get(item2).count = tmpBean.count;
		mAppInfos.get(item2).lastTime = tmpBean.lastTime;
		mAppInfos.get(item2).pkgName = tmpBean.pkgName;
		mAppInfos.get(item2).lastClassName = tmpBean.lastClassName;
	}

	/*//��������.ת��aaa,��Ϊ�����������������
	private String genNewSubTable(String pkgName)
	{
		return pkgName.replace(".", T.REPLACE_STRING);
	}
	
	//��ԭ
	private String getOldSubTable(String pkgName)
	{
		return pkgName.replace(T.REPLACE_STRING, ".");
	}*/
	// ��ʼ���
	private void startThread()
	{
		if (!isRunning)
		{
			isRunning = true;
			mThread = new Thread(this);
			mThread.start();
		}
	}
	
	@Override
	public void run() {
		//TODO �̼߳��
		long lastCheckTime = 0;
		ComponentName lastComponent = getTopComponent();
		while (isRunning)
		{
			long nowTime = SystemClock.elapsedRealtime();
			long timeInterVal = nowTime - lastCheckTime;
//			LOG("timeInterVal = " + timeInterVal);
			if (timeInterVal >  T.THREAD_CHECH_TIME)//���
			{
				lastCheckTime = SystemClock.elapsedRealtime();
				ComponentName topComponent = getTopComponent();
//				LOG("topAppPackage = " + topComponent.getPackageName());
//				LOG("topAppClass = " + topComponent.getClassName());
				if (!topComponent.equals(lastComponent))	//����һ��Ӧ�ò�ͬ
				{
					lastComponent = topComponent;
					if (!isInWhiteList(topComponent.getPackageName()))
					{
//						printSubtableInfo();
						pushInSubTableInfos(topComponent.getPackageName(), topComponent.getClassName(), System.currentTimeMillis());//��ӽ���
//						printSubtableInfo();
						mAppInfos = getAppInfosFromSubTableInfos();//��ȡӦ����Ϣ
//						printAppInfo();
						updateCommonAppList();
						
					}
				}
				else
				{
					if (!isInWhiteList(topComponent.getPackageName()))
					{
						updateSubTableInfoTime(topComponent.getPackageName(), System.currentTimeMillis());
					}
					mAppInfos = getAppInfosFromSubTableInfos();//��ȡӦ����Ϣ
					updateCommonAppList();
				}
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
	
	//��ȡ�������
	private String getTopAppPackage() {
//		List<RunningAppProcessInfo> processes = mAm.getRunningAppProcesses();
//		return processes.get(0).processName;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = activityManager.getRunningTasks(Integer.MAX_VALUE).get(0).topActivity;
		return cn.getPackageName();
	}
	//��ȡ����Ӧ����Ϣ
	private ComponentName getTopComponent() {
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		return activityManager.getRunningTasks(Integer.MAX_VALUE).get(0).topActivity;
	}
	
	//�ڰ������б�
	private boolean isInWhiteList(String pkgName)
	{
		if (mDefaultWhiteList.length > 0)
		{
			for (String item: mDefaultWhiteList)
			{
				if (pkgName.equals(item))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private void LOG(String string)
	{
		Log.i(T.TAG.COMMONAPPHELPER, string);
	}

}
