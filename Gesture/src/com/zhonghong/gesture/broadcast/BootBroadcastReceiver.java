package com.zhonghong.gesture.broadcast;

import com.zhonghong.gesture.CustomGestureResultDo;
import com.zhonghong.gesture.FivePointOnTouchListener;
import com.zhonghong.gesture.GestureResultDo;
import com.zhonghong.gesture.GestureJni.OnTouchListener;
import com.zhonghong.gesture.GestureResultDo.ResultDo;
import com.zhonghong.gesture.facemode.FaceMode;
import com.zhonghong.gesture.utils.T;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;

public class BootBroadcastReceiver extends BroadcastReceiver {
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(T.Broadcast.ACTION_BOOT))
		{
			Log.i("BootBroadcastReceiver", "receive " + T.Broadcast.ACTION_BOOT);
			if (FaceMode.getInstance().mGestureResultDo == null)
			{
				FaceMode.getInstance().mGestureResultDo = new GestureResultDo(context);
				FaceMode.getInstance().mGestureResultDo.setOwnTouchListener(mOwnTouchLinstenser);
				FaceMode.getInstance().mGestureResultDo.setResultDo(mResultDo);
			}
		}
	}
	
	OnTouchListener mOwnTouchLinstenser = new FivePointOnTouchListener();
	ResultDo mResultDo = new CustomGestureResultDo();

}
