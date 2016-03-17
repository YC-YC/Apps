package com.zhonghong.gesture;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zhonghong.gesture.GestureJni.OnTouchListener;
import com.zhonghong.gesture.GestureResultDo.ResultDo;
import com.zhonghong.gesture.facemode.FaceMode;

public class MainActivity extends Activity {

	private Button mBtOnOff;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mBtOnOff = (Button) findViewById(R.id.bt_onoff);
		
	}

	
	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.bt_start:
			if (FaceMode.getInstance().mGestureResultDo == null) {
				FaceMode.getInstance().mGestureResultDo = new GestureResultDo(
						MainActivity.this);
				FaceMode.getInstance().mGestureResultDo
						.setOwnTouchListener(mOwnTouchLinstenser);
				FaceMode.getInstance().mGestureResultDo.setResultDo(mResultDo);
				mBtOnOff.setText(FaceMode.getInstance().mGestureResultDo
						.getGestureONOFF()?"On":"OFF");
			}
			break;
		case R.id.bt_onoff:
			if (FaceMode.getInstance().mGestureResultDo != null) {
				FaceMode.getInstance().mGestureResultDo
						.setGestureONOFF(!FaceMode.getInstance().mGestureResultDo
								.getGestureONOFF());
				mBtOnOff.setText(FaceMode.getInstance().mGestureResultDo
								.getGestureONOFF()?"On":"OFF");
			}
			break;
		}
	}
	
	OnTouchListener mOwnTouchLinstenser = new FivePointOnTouchListener();
	ResultDo mResultDo = new CustomGestureResultDo();

	
}
