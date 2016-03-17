package com.zhonghong.gesture.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zhonghong.gesture.utils.T;

public class ShutDownBroadcastReceiver extends BroadcastReceiver {
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO 自动生成的方法存根
		if (intent.getAction().equals(T.Broadcast.ACTION_SHUTDOWN))
		{
			Log.i("ShutDownBroadcastReceiver", "receive " + T.Broadcast.ACTION_SHUTDOWN);
		}
	}
	
	

}
