package com.zhonghong.gesture.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zhonghong.gesture.utils.T;

public class BootBroadcastReceiver extends BroadcastReceiver {
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(T.Broadcast.ACTION_BOOT))
		{
			Log.i("BootBroadcastReceiver", "receive " + T.Broadcast.ACTION_BOOT);
//			if (FaceMode.getInstance().mGestureResultDo == null)
//			{
//				FaceMode.getInstance().mGestureResultDo = new GestureResultDo(context);
//				FaceMode.getInstance().mGestureResultDo.setOwnTouchListener(mOwnTouchLinstenser);
//				FaceMode.getInstance().mGestureResultDo.setResultDo(mResultDo);
//			}
			Intent it = new Intent("com.zhonghong.gesture.GestureService");
			context.startService(it);
		}
	}
	
//	OnTouchListener mOwnTouchLinstenser = new FivePointOnTouchListener();
//	ResultDo mResultDo = new CustomGestureResultDo();

}
