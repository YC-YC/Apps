package com.zhonghong.commonapp.setup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.zhonghong.commonapp.CommonAppHelper;
import com.zhonghong.commonapp.R;
import com.zhonghong.commonapp.utils.T;

public class SetupActivity extends Activity implements OnClickListener{

	private RelativeLayout mSelectcommontime;
	private TextView tv_Commontime;
	private TextView tvShowTime;
	private ListView mLvSelTime;
	private long mCommonTime; 
	
	private String[] sel_list = {"5分钟","1小时", "1天", "1周", "10天", "1个月"};
	private long[] sel_time = {5*T.Time.MINUTE, T.Time.HOUR, T.Time.DAY, 7*T.Time.DAY, 10*T.Time.DAY, T.Time.MONTH};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setup);
		if (T.mCommonAppHelper == null)
		{
			new CommonAppHelper(this);
		}
		initView();
	}

	private void initView() {
		mSelectcommontime = (RelativeLayout) findViewById(R.id.selectcommontime);
		mSelectcommontime.setOnClickListener(this);
		
		tv_Commontime = (TextView) findViewById(R.id.tv_commontime);
		tv_Commontime.setText(getCommonTime());
		
		tvShowTime = (TextView) findViewById(R.id.tv_showtime);
		tvShowTime.setText("当前设置时间为:" + getCommonTime());
		mLvSelTime = (ListView) findViewById(R.id.lv_seltime);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sel_list);
		List<Map<String, String>> data = getData();
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_item, 
				new String[]{"content"}, new int[]{R.id.tv_list_item});
		mLvSelTime.setAdapter(adapter);
		mLvSelTime.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				T.mCommonAppHelper.setCommonAppTime(sel_time[position]);
				tv_Commontime.setText(getCommonTime());
				tvShowTime.setText("当前设置时间为:" + getCommonTime());
//				selListViewItem(position);
			}
		});
//		selListViewItem(getCheckItem());
	}

	private List<Map<String, String>> getData() {
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		for(int i = 0; i < sel_list.length; i++)
		{
			Map<String, String> map = new HashMap<String, String>();
			map.put("content", sel_list[i]);
			data.add(map);
		}
		return data;
	}
	
	private void selListViewItem(final int item)
	{
		new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				mLvSelTime.setSelection(item);
			}
		});
	}
	
	private String getCommonTime() {
		mCommonTime = T.mCommonAppHelper.getCommonAppTime();
		for (int i = 0; i < sel_time.length; i++)
		{
			if (mCommonTime == sel_time[i])
			{
				return sel_list[i];
			}
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selectcommontime:
				showSelDialog();
			break;
		}
	}
	
	private void showSelDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("多选按键对话框");
		getCheckItem();
		builder.setSingleChoiceItems(sel_list, getCheckItem(),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
							T.mCommonAppHelper.setCommonAppTime(sel_time[which]);
							tv_Commontime.setText(getCommonTime());
						dialog.dismiss();
					}
				});
		
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private int getCheckItem() {
		for (int i = 0; i < sel_time.length; i++)
		{
			if (sel_time[i] == mCommonTime)
			{
				return i;
			}
		}
		return 0;
	}
}
