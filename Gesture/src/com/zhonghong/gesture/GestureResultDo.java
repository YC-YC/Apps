package com.zhonghong.gesture;

import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.tencent.bugly.crashreport.CrashReport;
import com.zhonghong.gesture.GestureJni.OnTouchListener;
import com.zhonghong.gesture.GlobalTouchHandle.DefultGestureResultListener;
import com.zhonghong.gesture.utils.T;

public class GestureResultDo {

	private static final String TAG = "GestureResultDo";
	private static final String GESTURE_FILE = "gesturepref";
	private static final String GESTURE_ONOFF_KEY = "gestureonoff";
	private Context mContext;
	Handler mHandler;
	
	private GlobalTouchHandle mGlobalTouchHandle;
	private SharedPreferences mPref = null;
	private Editor mEditor = null;
	
	private boolean mGestureONOFF = true;	//手势开关

	public GestureResultDo(Context context) {
		super();
		this.mContext = context;
		T.mContext = context;
		CrashReport.initCrashReport(mContext, "900022770", false);
		mGlobalTouchHandle = new GlobalTouchHandle(context);
		init();
	}
	public GestureResultDo(Context context, View view) {
		super();
		this.mContext = context;
		mGlobalTouchHandle = new GlobalTouchHandle(context, view);
		init();
	}
	
	/*
	 * 开关事件处理
	 */
	public void setGestureONOFF(boolean onOff)
	{
		if (mGestureONOFF != onOff)
		{
			mEditor.putBoolean(GESTURE_ONOFF_KEY, onOff);
			mEditor.commit();
		}
		mGestureONOFF = onOff;
		mGlobalTouchHandle.setONOFF(onOff);
	}
	/*
	 * 获取事件处理开关
	 */
	public boolean getGestureONOFF()
	{
		return mGestureONOFF;
	}
	
	private OnTouchListener mOwnTouchListener = null;
	public void setOwnTouchListener(OnTouchListener ownTouchListener)
	{
		mOwnTouchListener = ownTouchListener;
	}
	
	
	private ResultDo mResultDo = null;
	public interface ResultDo
	{
		public void ResultDo(String gestureName);
	}
	public void setResultDo(ResultDo resultDo)
	{
		mResultDo = resultDo;
	}
	
	private void init() {

//		mHandler = new Handler();
//	    FaceMode.getInstance().mGestureResultDo = this;
	    mGlobalTouchHandle.setOnTouchListener(mOnTouchListener);
		mGlobalTouchHandle.setDefultGestureResultListener(mDefultGestureResultListener);
		//要设置这个，持续移动才有作用
//		mGlobalTouchHandle.addLineGestureOnOffs(new LineGestureOnOff(2, GlobalTouchHandle.HORIZONTAL));
		mPref = mContext.getSharedPreferences(GESTURE_FILE, Context.MODE_PRIVATE);
		mEditor = mPref.edit();
		mGestureONOFF = mPref.getBoolean(GESTURE_ONOFF_KEY, true);
		setGestureONOFF(mGestureONOFF);
		
	}
	
	OnTouchListener mOnTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(MotionEvent event) {
			
			if (mOwnTouchListener != null)
			{
				if (mOwnTouchListener.onTouch(event))
				{
					String gestureName = GestureInfo.G_NAME_5_POINT_IN;
					LOG("get " + gestureName);
					if (mResultDo != null)
					{
						mResultDo.ResultDo(gestureName);
					}
//					Runtime runtime = Runtime.getRuntime();
//					try {
//						runtime.exec("input keyevent " + KeyEvent.KEYCODE_HOME);
//						LOG("send key home");
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
					return true;
				}
			}
			return false;
		}
	};
	
	DefultGestureResultListener mDefultGestureResultListener = new DefultGestureResultListener() {
		
		@Override
		public void lineGuestUpDo(int pointCount, int orientation) {
			if (mGestureONOFF)
			{
				GestureResultDo.this.lineGuestUpDo(pointCount, orientation);
			}
		}
		
		@Override
		public void lineGuestContinueDo(int pointCount, boolean isAdd, boolean isHorizontal) {
			
			LOG("lineGuestContinueDo");
			if (mGestureONOFF)
			{
				GestureResultDo.this.lineGuestContinueDo(pointCount, isAdd, isHorizontal);
			}
		}
	};

	/*
	 * 线性手势up时执行
	 */
	protected void lineGuestUpDo(int pointCount, int orientation) {
		String gestureName = null;
		switch (orientation) {
		case GlobalTouchHandle.UP:
			gestureName = pointCount + GestureInfo.G_NAME_UP;
			break;
		case GlobalTouchHandle.DOWN:
			gestureName = pointCount + GestureInfo.G_NAME_DOWN;
			break;
		case GlobalTouchHandle.LEFT:
			gestureName = pointCount + GestureInfo.G_NAME_LEFT;
			break;
		case GlobalTouchHandle.RIGHT:
			gestureName = pointCount + GestureInfo.G_NAME_RIGHT;
			break;
		}
		if (gestureName != null)
		{
			LOG("get " + gestureName);
			if (mResultDo != null)
			{
				mResultDo.ResultDo(gestureName);
			}
		}
	}
	
	/*
	 * 线性持续手势
	 */
	protected void lineGuestContinueDo(int pointCount, boolean isAdd,
			boolean isHorizontal) {
		String gestureName = null;
		gestureName = pointCount + 
				(isHorizontal ? GestureInfo.G_NAME_CONTINUE_HORIZONTAL :GestureInfo.G_NAME_CONTINUE_VERTICAL)
				+ (isAdd?GestureInfo.G_NAME_INC:GestureInfo.G_NAME_DEC);
		LOG("get " + gestureName);
		if (mResultDo != null)
		{
			mResultDo.ResultDo(gestureName);
		}
	}
	
	
	private void LOG(String str){
		Log.i(TAG, str);
	}
}
