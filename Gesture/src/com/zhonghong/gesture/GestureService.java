/**
 * 
 */
package com.zhonghong.gesture;

import com.zhonghong.gesture.facemode.FaceMode;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @author YC
 * @time 2016-3-18 ÏÂÎç2:13:27
 */
public class GestureService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i("GestureService", "onCreate");
		super.onCreate();
		if (FaceMode.getInstance().mGestureResultDo == null)
		{
			FaceMode.getInstance().mGestureResultDo = new GestureResultDo(this);
			FaceMode.getInstance().mGestureResultDo.setOwnTouchListener(new FivePointOnTouchListener());
			FaceMode.getInstance().mGestureResultDo.setResultDo(new CustomGestureResultDo());
		}
	}

}
