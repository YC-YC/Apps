package com.zhonghong.cold;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.zhonghong.cold.ColdHelper.ColdListChangedCallBack;
import com.zhonghong.cold.client.ClientFaceMode;
import com.zhonghong.cold.utils.T;
import com.zhonghong.coldlist.ColdListFragment;
import com.zhonghong.coldsetup.ColdSetupFragment;

public class MainActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private PagerTabStrip mTabStrip;
	
	private List<String> mTitle;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		ClientFaceMode.getInstance().bindRemoteService(this);
	}

	@Override
	protected void onDestroy() {
		// TODO �Զ����ɵķ������
		super.onDestroy();
		ClientFaceMode.getInstance().unBindRemoteService(this);
	}
	
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mTabStrip = (PagerTabStrip) findViewById(R.id.tab);
		
		/*
		 * ����PagerTabStrip����
		 */
		mTabStrip.setBackgroundColor(Color.BLACK);
		mTabStrip.setTextColor(Color.WHITE);
		mTabStrip.setDrawFullUnderline(false);
		//����������ɫ
		mTabStrip.setTabIndicatorColor(Color.GREEN);
		
		mTitle = new ArrayList<String>();
		mTitle.add("��ؿ�");
		mTitle.add("����");
		
		mFragments = new ArrayList<Fragment>();
		T.mColdListFragment = new ColdListFragment();
		T.mColdSetupFragment = new ColdSetupFragment();
		mFragments.add(T.mColdListFragment);
		mFragments.add(T.mColdSetupFragment);
		
		mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), 
				mFragments,mTitle);
		
		mViewPager.setAdapter(mAdapter);
	}
	private void LOG(String string)
	{
		Log.i(T.TAG.MAINACTIVITY, string);
	}
	
	
}
