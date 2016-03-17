package com.zhonghong.cold.service;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.zhonghong.cold.ColdHelper;
import com.zhonghong.cold.ColdHelper.ColdListChangedCallBack;
import com.zhonghong.cold.utils.T;

public class ColdService extends Service {

	private static final int MSG_UPDATE = 100;
	private static final String TAG = "ColdService";
	
	private Context mContext;
	private MyHandle mHandler;
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		LOG("onRebind");
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		LOG("onCreate");
		mContext = this;
		mHandler = new MyHandle();
		initColdHelper();
	}


	/*
	 * bindService时调用
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LOG("onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		LOG("onDestroy");
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		LOG("onUnbind");
		return super.onUnbind(intent);
	}
	
	
	private void initColdHelper() {
		if (T.mColdHelper == null)
		{
			new ColdHelper(mContext);
			T.mColdHelper.setColdListChangedCallBack(new ColdListChangedCallBack() {
				@Override
				public void callback() {
					mHandler.sendEmptyMessageDelayed(MSG_UPDATE, 0);
				}
			});
		}
	}	
	
	
	
	class MyHandle extends Handler
	{
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_UPDATE)
			{
				serviceCallback();
			}
		}
		
	}
	//回调所有注册
	private void serviceCallback()
	{
		int num = mRemoteCallbackList.beginBroadcast();
		LOG("callback client num = " + num);
		for (int i = 0; i < num; i++)
		{
			try {
				mRemoteCallbackList.getBroadcastItem(i).ColdListChanged();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		mRemoteCallbackList.finishBroadcast();
	}
	private int registerNum = 0;
	private final ITaskBinder.Stub mBinder = new ITaskBinder.Stub() {

		@Override
		public void registerCallback(ITaskCallback callback)
				throws RemoteException {
			if (callback != null)
			{
				registerNum++;
				mRemoteCallbackList.register(callback);
			}
			LOG("registerCallback num = " + registerNum);
		}

		@Override
		public void unregisterCallback(ITaskCallback callback)
				throws RemoteException {
			if (callback != null)
			{
				mRemoteCallbackList.unregister(callback);
				registerNum--;
			}
			LOG("registerCallback num = " + registerNum);
		}

		@Override
		public List<String> getColdList() throws RemoteException {
			if (T.mColdHelper != null)
			{
				LOG("getColdList");
				return T.mColdHelper.getColdList();
			}
			LOG("getColdList null");
			return null;
		}

		@Override
		public long getColdTime() throws RemoteException {
			if (T.mColdHelper != null)
			{
				return T.mColdHelper.getColdTime();
			}
			return 0;
		}

		@Override
		public void setColdTime(long second) throws RemoteException {
			if (T.mColdHelper != null)
			{
				T.mColdHelper.setColdTime(second);
			}
		}

		@Override
		public void removeFromColdList(String pkgName) throws RemoteException {
			LOG("removeFromColdList pkgName = " + pkgName);
			if (T.mColdHelper != null)
			{
				T.mColdHelper.RemoveFromColdList(pkgName);
			}
		}
		
	};
	
	final RemoteCallbackList<ITaskCallback> mRemoteCallbackList = new RemoteCallbackList<ITaskCallback>();
	
	private void LOG(String string)
	{
		Log.i(TAG, string);
	}
	
}
