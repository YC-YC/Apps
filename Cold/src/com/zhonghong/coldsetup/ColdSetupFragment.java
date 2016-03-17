package com.zhonghong.coldsetup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhonghong.cold.R;
import com.zhonghong.cold.client.ClientFaceMode;
import com.zhonghong.cold.service.ITaskBinder;

public class ColdSetupFragment extends Fragment implements OnClickListener{

	private static final String TAG = "ColdSetupFragment";
	
	private View mView;
	private RelativeLayout mSeleteLimiteTime;
	private TextView mColdtime;
	
	private Context mContext;
	private long mColdTime;
	private String[] sel_list = { "30秒", "1分钟", "5分钟", "1小时", "1天", "1个月"};
	private long[] sel_time = {30, 60, 5*60, 60*60, 24*60*60, 30*24*60*60};
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LOG("onCreateView");
		mView = inflater.inflate(R.layout.setup, null);
		mContext = mView.getContext();
		initView();
		return mView;
	}

	@Override
	public void onDestroyView() {
		LOG("onDestroyView");
		super.onDestroyView();
	}
	
	public void updateColdTime()
	{
		if (mView == null)
		{
			return;
		}
		mColdtime = (TextView) mView.findViewById(R.id.tv_coldtime);
		mColdtime.setText(getColdTime());		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectlimitetime:
			if (getServiceConnected())
			{
				showSelDialog();
			}
			break;
		}
	}
	
	private void initView() {
		mSeleteLimiteTime = (RelativeLayout) mView.findViewById(R.id.selectlimitetime);
		mSeleteLimiteTime.setOnClickListener(this);
		
		mColdtime = (TextView) mView.findViewById(R.id.tv_coldtime);
		mColdtime.setText(getColdTime());
	}

	private ITaskBinder getTaskBinder()
	{
		return ClientFaceMode.getInstance().getTaskBinder();
	}
	
	private boolean getServiceConnected()
	{
		return (getTaskBinder() != null);
	}
	
	private String getColdTime() {
		if (getServiceConnected())
		{
			try {
				mColdTime = getTaskBinder().getColdTime();
				for (int i = 0; i < sel_time.length; i++)
				{
					if (mColdTime == sel_time[i])
					{
						return sel_list[i];
					}
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private int getCheckItem() {
		for (int i = 0; i < sel_time.length; i++)
		{
			if (sel_time[i] == mColdTime)
			{
				return i;
			}
		}
		return 0;
	}

	private void showSelDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("多选按键对话框");
		getCheckItem();
		builder.setSingleChoiceItems(sel_list, getCheckItem(),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					/*	Toast.makeText(mContext,
								"点击了 " + sel_list[which], Toast.LENGTH_LONG)
								.show();*/
						try {
							getTaskBinder().setColdTime(sel_time[which]);
							mColdtime.setText(getColdTime());
						} catch (RemoteException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						dialog.dismiss();
					}
				});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void LOG(String string)
	{
		Log.e(TAG, string);
	}
}
