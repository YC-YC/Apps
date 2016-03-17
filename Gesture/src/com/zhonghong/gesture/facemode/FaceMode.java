package com.zhonghong.gesture.facemode;

import com.zhonghong.gesture.GestureResultDo;

public class FaceMode {

	private static FaceMode instance = null;
	
	private FaceMode(){}
	
	public static FaceMode getInstance()
	{
		return (instance == null) ? (instance = new FaceMode()):instance;
	}

	public GestureResultDo mGestureResultDo;
	
}
