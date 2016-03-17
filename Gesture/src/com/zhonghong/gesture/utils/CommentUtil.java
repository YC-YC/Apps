package com.zhonghong.gesture.utils;

import android.app.Instrumentation;

public class CommentUtil {
	public static void sendKeyIntent(final int keycode)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				new Instrumentation().sendKeyDownUpSync(keycode);
			}
		}).start();
	}
}
