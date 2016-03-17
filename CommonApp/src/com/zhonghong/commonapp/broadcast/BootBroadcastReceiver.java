package com.zhonghong.commonapp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zhonghong.commonapp.CommonAppHelper;
import com.zhonghong.commonapp.utils.T;


public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (intent.getAction().equals(T.Broadcast.ACTION_BOOT))
		{
		/*	Log.i("BootBroadcastReceiver", "receive " + T.Broadcast.ACTION_BOOT);
			Intent it = new Intent("com.zhonghong.cold.service.ColdService");
			context.startService(it);*/
		}
		else if (action.equals(T.Broadcast.PACKAGE_ADDED))
		{
		/*	LOG("receive " + T.Broadcast.PACKAGE_ADDED);
			String packageName = intent.getData().getSchemeSpecificPart();
			if (T.Package.MY_OWN.equals(packageName))
			{
				return;
			}*/
			
		}
		else if (action.equals(T.Broadcast.PACKAGE_REMOVED))
		{
			LOG("receive " + T.Broadcast.PACKAGE_REMOVED);
			String packageName = intent.getData().getSchemeSpecificPart();
			boolean replaceing = intent.getBooleanExtra(Intent.EXTRA_REPLACING, false);//是否重新安装
			if (!replaceing)
			{
				LOG("Remove " + T.Broadcast.PACKAGE_REMOVED);
//				T.mColdHelper.deleteDatabaseItem(packageName);
				if (T.mCommonAppHelper == null)
				{
					new CommonAppHelper(context);
				}
				T.mCommonAppHelper.removeFromCommonAppList(packageName);
			}
			else
			{
				LOG("Replace " + T.Broadcast.PACKAGE_REMOVED);
	
			}
		}
	}

	private void LOG(String string)
	{
		Log.i(T.TAG.BROADCAST, string);
	}
	
}
