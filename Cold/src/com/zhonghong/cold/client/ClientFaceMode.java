package com.zhonghong.cold.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.zhonghong.cold.service.ITaskBinder;
import com.zhonghong.cold.service.ITaskCallback;
import com.zhonghong.cold.utils.T;

public class ClientFaceMode {
	private static final String TAG = "ClientFaceMode";
	private static final int MSG_UPDATE = 100;
	
	private static ClientFaceMode instance = null;

	private ITaskBinder mAidlTaskBinder;
	private MyHandle mHandler;
	private ClientFaceMode() {
		mHandler = new MyHandle();
	}

	public static ClientFaceMode getInstance() {
		return (instance == null) ? (instance = new ClientFaceMode())
				: instance;
	}
	
	public void bindRemoteService(Context context)
	{
		LOG("bindRemoteService");
		Intent it = new Intent("com.zhonghong.cold.service.ColdService");
//		context.startService(it);
		context.bindService(it, conn, Context.BIND_AUTO_CREATE);
	}
	
	public void unBindRemoteService(Context context)
	{
		LOG("unBindRemoteService");
		if (conn != null)
		{
			context.unbindService(conn);
		}
	}
	
	public ITaskBinder getTaskBinder()
	{
		return mAidlTaskBinder;
	}
	
	private volatile ServiceConnection conn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			LOG("onServiceDisconnected");
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LOG("onServiceConnected");
			mAidlTaskBinder = ITaskBinder.Stub.asInterface(service);
			try {
				mAidlTaskBinder.registerCallback(new ITaskCallback.Stub() {
					
					@Override
					public void ColdListChanged() throws RemoteException {
						// TODO 自动生成的方法存根
						mHandler.sendEmptyMessageDelayed(MSG_UPDATE, 0);
					}
				});
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			T.mColdListFragment.updateColdListData();
			T.mColdSetupFragment.updateColdTime();
		}
	};
	
	private void LOG(String string)
	{
		Log.i(TAG, string);
	}
	
	class MyHandle extends Handler
	{
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_UPDATE)
			{
				//更新列表信息
				T.mColdListFragment.updateColdListData();
			}
		}
		
	}
}
