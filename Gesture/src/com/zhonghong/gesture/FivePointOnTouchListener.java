package com.zhonghong.gesture;

import android.util.Log;
import android.view.MotionEvent;

import com.zhonghong.gesture.GestureJni.OnTouchListener;

public class FivePointOnTouchListener implements OnTouchListener{
	@Override
	public boolean onTouch(MotionEvent event) {
		if (event.getPointerCount() == 5)
		{
			if (!is5PointTouch)
			{
				is5PointTouch = true;
				record5PointDown(event);
			}
			else
			{
				update5Point(event);
			}
		}
		else
		{
			is5PointTouch = false;
		}
		
		if (event.getPointerCount() == 1 && 
				event.getAction() == MotionEvent.ACTION_UP)
		{
			is5PointTouch = false;
			allUpTime = event.getEventTime();
			LOG("All Points Up during time=" + (allUpTime - last5PointTime));
			if (allUpTime - last5PointTime < 5*100)
			{
				if (judge5Point())
				{
					LOG("get 5Point catch!");
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean is5PointTouch = false;
	private MyPoint start5points[] = null;
	private MyPoint last5Points[] = null;
	private long last5PointTime;
	private long allUpTime;
	private void record5PointDown(MotionEvent event) {
		if (start5points == null)
		{
			start5points = new MyPoint[5];
			for (int i = 0; i < 5; i++)
			{
				start5points[i] = new MyPoint();
			}
		}
		for (int i = 0; i < 5; i++) {
			start5points[i].set(event.getX(i), event.getY(i));
		}
	}
	private void update5Point(MotionEvent event)
	{
		if (last5Points == null)
		{
			last5Points = new MyPoint[5];
			for (int i = 0; i < 5; i++)
			{
				last5Points[i] = new MyPoint();
			}
		}
		for (int i = 0; i < 5; i++) {
			last5Points[i].set(event.getX(i), event.getY(i));
		}
		last5PointTime = event.getEventTime();
	}
	
	private boolean judge5Point() {
		int start5PointXdiff = getXdiff(start5points);
		int start5PointYdiff = getYdiff(start5points);
		int last5PointXdiff = getXdiff(last5Points);
		int last5PointYdiff = getYdiff(last5Points);
		LOG("start5PointXdiff= " + start5PointXdiff + ", start5PointYdiff= " + start5PointYdiff);
		LOG("last5PointXdiff= " + last5PointXdiff + ", last5PointYdiff= " + last5PointYdiff);
		if (start5PointXdiff - last5PointXdiff > 50
				&& start5PointYdiff - last5PointYdiff > 50)
		{
			return true;
		}
		return false;
	}
	
	private int getYdiff(MyPoint[] _5points) {
		int minY = getMinY(_5points);
		int maxY = getMaxY(_5points);
		return (maxY - minY);
	}
	
	private int getXdiff(MyPoint[] _5points) {
		int minX = getMinX(_5points);
		int maxX = getMaxX(_5points);
		return (maxX - minX);
	}
	
	private int getMinY(MyPoint[] _5points) {
		int miny = 0;
		miny = (int) _5points[0].y;
		for (int i = 1; i < 5; i++)
		{
			int tmp = (int) _5points[i].y;
			if (tmp < miny)
			{
				miny = tmp;
			}
		}
		return miny;
	}
	
	private int getMaxY(MyPoint[] _5points) {
		
		int maxy = 0;
		maxy = (int) _5points[0].y;
		for (int i = 1; i < 5; i++)
		{
			int tmp = (int) _5points[i].y;
			if (tmp > maxy)
			{
				maxy = tmp;
			}
		}
		return maxy;
	}
	
	private int getMinX(MyPoint[] _5points) {
		int minx = 0;
		minx = (int) _5points[0].x;
		for (int i = 1; i < 5; i++)
		{
			int tmp = (int) _5points[i].x;
			if (tmp < minx)
			{
				minx = tmp;
			}
		}
		return minx;
	}
	
	private int getMaxX(MyPoint[] _5points) {
		
		int maxX = 0;
		maxX = (int) _5points[0].x;
		for (int i = 1; i < 5; i++)
		{
			int tmp = (int) _5points[i].x;
			if (tmp > maxX)
			{
				maxX = tmp;
			}
		}
		return maxX;
	}
	
	class MyPoint{
		public double x;
		public double y;
		public MyPoint()
		{
			this.x = 0;
			this.y = 0;
		}
		public void set(double x, double y)
		{
			this.x = x;
			this.y = y;
		}
		
	}
	private void LOG(String str)
	{
		Log.i("5PointGesture", str);
	}

}
