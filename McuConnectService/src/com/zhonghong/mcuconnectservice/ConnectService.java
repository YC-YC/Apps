package com.zhonghong.mcuconnectservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.zhonghong.jni.JniClient;

public class ConnectService extends Service implements Runnable {

	private static final int MSG_UPDATE = 100;
	private static final String TAG = "ConnectService";
	
	private Context mContext;
	private MyHandle mHandler;
	private Thread mThread;
	private boolean mThreadRun = false;
	private int registerNum = 0;	//remote rigister num
	final RemoteCallbackList<ITaskCallback> mRemoteCallbackList = new RemoteCallbackList<ITaskCallback>();

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
		init();
	}


	private void init() {
		startThread();
		/*if (T.mColdHelper == null)
		{
			new ColdHelper(mContext);
		}
		T.mColdHelper.setColdListChangedCallBack(new ColdListChangedCallBack() 
		{
			@Override
			public void callback() 
			{
				mHandler.sendEmptyMessageDelayed(MSG_UPDATE, 0);
			}
		});*/
	}

	// 开始检测
	private void startThread()
	{
		if (!mThreadRun)
		{
			mThreadRun = true;
			mThread = new Thread(this, "jni_thread");
			mThread.start();
		}
	}
	
	// bindService will do
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
	
	private final ITaskBinder.Stub mBinder = new ITaskBinder.Stub(){

		//register remote Callback
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
		
		//unregister remote Callback
		@Override
		public void unregisterCallback(ITaskCallback callback)
				throws RemoteException {
			if (callback != null)
			{
				mRemoteCallbackList.unregister(callback);
				registerNum--;
			}
			LOG("unregisterCallback num = " + registerNum);
		}

		@Override
		public int getStatus() throws RemoteException {
			return JniClient.getStatus();
		}

		@Override
		public int getKeyStat() throws RemoteException {
			return JniClient.getKeyStat();
		}

		@Override
		public int getLEDStat() throws RemoteException {
			return JniClient.getLEDStat();
		}

		@Override
		public int getIOStat() throws RemoteException {
			return JniClient.getIOStat();
		}
		
		@Override
		public void postCmd(int cmd, String arg) throws RemoteException {
			LOG("postCmd = " + cmd);
			JniClient.postCmd(cmd, arg);
		}

		@Override
		public String getVersion() throws RemoteException {
			// TODO 自动生成的方法存根
			return JniClient.getVersion();
		}
		
	};
	
	//callback all register
	private void serviceCallback()
	{
		int num = mRemoteCallbackList.beginBroadcast();
		LOG("callback client num = " + num);
		for (int i = 0; i < num; i++)
		{
			try {
				mRemoteCallbackList.getBroadcastItem(i).MCUCallback();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		mRemoteCallbackList.finishBroadcast();
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
	
	
	private void LOG(String string)
	{
		Log.i(TAG, string);
	}
	
	static{
		System.loadLibrary("Connection");
	}

	/* （非 Javadoc）
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO 自动生成的方法存根
		long lastTime = SystemClock.elapsedRealtime();
		while (mThreadRun)
		{
			long nowTime = SystemClock.elapsedRealtime();
			if (nowTime - lastTime > 20)
			{
				lastTime = nowTime;
				JniClient.looper();
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
