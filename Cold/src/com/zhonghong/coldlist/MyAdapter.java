package com.zhonghong.coldlist;

import java.util.ArrayList;

import android.content.Context;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhonghong.cold.R;
import com.zhonghong.cold.client.ClientFaceMode;
import com.zhonghong.cold.utils.T;

public class MyAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<ColdListBean> mListData;
	LayoutInflater mInflater;
	public MyAdapter(Context context, ArrayList<ColdListBean> ListData) {
		this.mContext = context;
		this.mListData = ListData;
		mInflater = LayoutInflater.from(mContext);
	}
	
	/*
	 * 更新数据
	 */
	public void OnDataChange(ArrayList<ColdListBean> list){
		this.mListData = list;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public Object getItem(int position) {
		return mListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ColdListBean apkBean= mListData.get(position);
		ViewHolder holder = null;
		
		if (convertView == null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listview_item, null);
			holder.apkIcom = (ImageView) convertView.findViewById(R.id.img_apkicon);
			holder.apkName = (TextView) convertView.findViewById(R.id.tv_apkname);
			holder.apkLabel = (TextView) convertView.findViewById(R.id.tv_apklabel);
			holder.backup = (Button) convertView.findViewById(R.id.bt_backup);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.apkIcom.setImageDrawable(apkBean.getApkIcon());
		holder.apkName.setText(apkBean.getApkName());
		holder.apkLabel.setText(apkBean.getApkLabel());
		holder.backup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Toast.makeText(mContext, "点击了：" + position, Toast.LENGTH_SHORT).show();
				try {
					ClientFaceMode.getInstance().getTaskBinder().removeFromColdList(mListData.get(position).getApkName());
				} catch (RemoteException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		});
		return convertView;
	}
	
	class ViewHolder{
		ImageView apkIcom;
		TextView apkName;
		TextView apkLabel;
		Button backup;
	}

}
