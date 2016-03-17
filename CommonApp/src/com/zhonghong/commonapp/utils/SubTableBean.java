package com.zhonghong.commonapp.utils;

import java.util.ArrayList;
import java.util.List;

public class SubTableBean {
		public String subTable;	//表名,即包名
		public String className;	//类名
		public List<Long> runtime;	//运行的时间列表
		public SubTableBean() {
			runtime = new ArrayList<Long>();
		}
		@Override
		public String toString() {
			return "SubTableBean [subTable=" + subTable + ", className="
					+ className + ", runtime=" + runtime + "]";
		}
		
}
