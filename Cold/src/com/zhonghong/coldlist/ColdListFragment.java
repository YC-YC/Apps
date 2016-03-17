package com.zhonghong.coldlist;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhonghong.cold.R;
import com.zhonghong.cold.client.ClientFaceMode;
import com.zhonghong.cold.service.ITaskBinder;

@SuppressLint("ValidFragment")
public class ColdListFragment extends Fragment{

	private static final String TAG = "ColdListFragment";

	private PackageManager mPm;
	
	private View mView;
	private ListView mListView;
	
	private MyAdapter adapter;
	private ArrayList<ColdListBean> mDataList = new ArrayList<ColdListBean>();
	
	private Context mContext;
	
	public ColdListFragment() {
		LOG("new ColdListFragment");
	}
	
	/*public ColdListFragment(Context context) {
		LOG("new ColdListFragment mContext");
		mContext = context;
		mPm = mContext.getPackageManager();
		if (getServiceConnected())
		{
			updateColdListData();
		}
	}*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		LOG("onCreate");
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LOG("onCreateView");
		mView = inflater.inflate(R.layout.listview, null);
		mContext = mView.getContext();
		mPm = mContext.getPackageManager();
		if (getServiceConnected())
		{
			updateColdListData();
		}
		return mView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO 自动生成的方法存根
		super.onAttach(activity);
		LOG("onAttach");
		/*mContext = activity.getBaseContext();
		mPm = mContext.getPackageManager();
		if (getServiceConnected())
		{
			updateColdListData();
		}*/
	}

	@Override
	public void onDestroyView() {
		LOG("onDestroyView");
		super.onDestroyView();
	}
	
	public void updateColdListData()
	{
		if (mView == null)
		{
			return;
		}
		LOG("updateColdListData");
		mDataList = getColdListData();
		showListView(mDataList);
	}
	
	private ITaskBinder getTaskBinder()
	{
		return ClientFaceMode.getInstance().getTaskBinder();
	}
	
	private boolean getServiceConnected()
	{
		return (getTaskBinder() != null);
	}
	
	/*
	 * 将获取到的数据转成适合本地的数据
	 */
	private ArrayList<ColdListBean> getColdListData() {
		if (!getServiceConnected())
		{
			return null;
		}
		ArrayList<ColdListBean>list = new ArrayList<ColdListBean>();
			List<String> coldList = null;
			try {
				coldList = getTaskBinder().getColdList();
				if (coldList == null)
				{
					return null;
				}
			} catch (RemoteException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
//			LOG("getColdList num = " + coldList.size());
			for (int i = 0; i < coldList.size(); i++)
			{
				String pkgName = coldList.get(i);
				String label = getPackageLabel(pkgName);
				Drawable icon = getPackageIcon(pkgName);
				if (label != null && icon != null)
				{
					ColdListBean listBean = new ColdListBean(icon, pkgName, label);
					list.add(listBean);
				}
			}
//			LOG("getColdList num +++= " + coldList.size());
		return list;
	}

	private String getPackageLabel(String pkgName)
	{
//		LOG("pkgName = " + pkgName);
		if (mPm == null)
		{
			LOG("mPm = null");
		}
		try {
			PackageInfo info = mPm.getPackageInfo(pkgName, 0);
			ApplicationInfo applicationInfo = info.applicationInfo;
			return mPm.getApplicationLabel(applicationInfo).toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Drawable getPackageIcon(String pkgName)
	{
		try {
			PackageInfo info = mPm.getPackageInfo(pkgName, 0);
			ApplicationInfo applicationInfo = info.applicationInfo;
			return mPm.getApplicationIcon(applicationInfo);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//更新列表数据
	private void showListView(ArrayList<ColdListBean> list2) {
		if (adapter == null){
			mListView = (ListView) mView.findViewById(R.id.coldlist);
			adapter = new MyAdapter(mContext, list2);
			mListView.setAdapter(adapter);
		}else{
			adapter.OnDataChange(list2);
		}
		
	}
	
	private void LOG(String string)
	{
		Log.e(TAG, string);
	}
	
	
}
