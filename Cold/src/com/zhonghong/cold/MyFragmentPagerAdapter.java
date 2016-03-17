package com.zhonghong.cold;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

//动态加载
//public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
//常驻
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragments;
	private List<String> mTitle;
	public MyFragmentPagerAdapter(FragmentManager fm, 
			List<Fragment> fragments,
			List<String> title) {
		super(fm);
		mFragments = fragments;
		mTitle = title;
	}

	@Override
	public Fragment getItem(int arg0) {
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}
	
	/*
	 * 添加标题(non-Javadoc)
	 * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
	 */
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return mTitle.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		return super.instantiateItem(arg0, arg1);
	}
	

}
