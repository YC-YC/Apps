package com.zhonghong.commonapp.utils;

import java.util.ArrayList;
import java.util.List;

public class SubTableBean {
		public String subTable;	//����,������
		public String className;	//����
		public List<Long> runtime;	//���е�ʱ���б�
		public SubTableBean() {
			runtime = new ArrayList<Long>();
		}
		@Override
		public String toString() {
			return "SubTableBean [subTable=" + subTable + ", className="
					+ className + ", runtime=" + runtime + "]";
		}
		
}
