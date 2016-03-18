package com.zhonghong.mcuconnectservice.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zhonghong.mcuconnectservice.utils.T;


public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(T.Broadcast.ACTION_BOOT))
		{
			LOG("receive " + T.Broadcast.ACTION_BOOT);
			Intent it = new Intent("com.zhonghong.mcuconnectservice.ConnectService");
			context.startService(it);
		}
	}

	private void LOG(String string)
	{
		Log.i(T.TAG.BROADCAST, string);
	}
	
}
