package com.zhonghong.mcuconnectservice;

/**
 * @author YC
 * @time 2016-2-22 上午11:03:26
 */
public class MCU {
	private static int BIT0 = 0x00000001<<0;
	private static int BIT1 = 0x00000001<<1;
	private static int BIT2 = 0x00000001<<2;
	private static int BIT3 = 0x00000001<<3;
	private static int BIT4 = 0x00000001<<4;
	private static int BIT5 = 0x00000001<<5;
	private static int BIT6 = 0x00000001<<6;
	private static int BIT7 = 0x00000001<<7;
	
	public static class KEY{
		public static int KEY1 = BIT0;
		public static int KEY2 = BIT1;
		public static int KEY3 = BIT2;
		public static int KEY4 = BIT3;
	};
	
	public static class LED{
		public static int LED_ECO = BIT0;	//省电模式的呼吸灯
		public static int LED_FAX = BIT1;
		public static int LED_STATE_R = BIT2;
		public static int LED_STATE_Y = BIT3;
		public static int LEN_DATA_IN = BIT4;
	};
	
	public static class IO{
		public static int POKUSB = BIT2;	//I,USB电源
		public static int PONOPE_N = BIT3;	//I,关机请求
		public static int SDMODE_N = BIT5;	//I,SOC电源控制
		public static int ECO_SW_N = BIT6;	//O,省电请求
	};
	
	public static class CMD{	//postCmd中的cmd
		public static int GET_STATUS = 0x01;	//获取状态
		public static int GET_INT_STAT = 0x02;	//读中断
		public static int LEDS_ONOFF = 0x03;	//LED灯开关
		public static int RESET_SOC = 0x04;	//重启SOC
		public static int SHUTDOWN_FINISH = 0x05;	//重启完成
		public static int SLEEP = 0x06;	//休眠
		public static int WAKE_UP = 0x07;	//唤醒
		public static int SHUTDOWN_DELAY = 0x08;	//延时关闭
		public static int GET_VERSION = 0x09;	//获取版本号
	};
}
