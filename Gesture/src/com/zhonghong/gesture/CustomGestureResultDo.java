package com.zhonghong.gesture;

import android.content.ComponentName;
import android.content.Intent;
import android.os.UserHandle;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.KeyEvent;

import com.zhonghong.gesture.GestureResultDo.ResultDo;
import com.zhonghong.gesture.utils.CommentUtil;
import com.zhonghong.gesture.utils.T;

public class CustomGestureResultDo implements ResultDo {
	private static final String TAG = "CustomGestureResultDo";
	@Override
	public void ResultDo(String gestureName) {
		Log.i(TAG, "gestureName = " + gestureName);	
		if (GestureInfo.G_NAME_5_POINT_IN.equals(gestureName))//5指收--》home
		{
//			Log.i(TAG, "" + gestureName);
			CommentUtil.sendKeyIntent(KeyEvent.KEYCODE_HOME);
		}
		else if (GestureInfo.G_NAME_THREE_LEFT.equals(gestureName) 
				|| GestureInfo.G_NAME_THREE_RIGHT.equals(gestureName))//3左右--》返回
		{
//			Log.i(TAG, "" + gestureName);
			CommentUtil.sendKeyIntent(KeyEvent.KEYCODE_BACK);
		}
		else if (GestureInfo.G_NAME_FOUR_DOWN.equals(gestureName))	//4下--》进程管理
		{
//			Log.i(TAG, "" + gestureName);	com.android.systemui/.recent.RecentsActivity
			openRecentsActivity();
		}
	}
	private void openRecentsActivity() {
		Intent it = new Intent(Intent.ACTION_MAIN);
		ComponentName name = new ComponentName("com.android.systemui", "com.android.systemui.recent.RecentsActivity");
		it.setComponent(name);
		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		T.mContext.startActivity(it);
	}

}
