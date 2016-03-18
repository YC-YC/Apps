/*
 * formData.h
 *
 *  Created on: 2016-2-22
 *      Author: YC
 */

#ifndef FORMDATA_H_
#define FORMDATA_H_

#define CMD_ARG_LEN			100		//命令参数最大长度
#define SEND_DATA_LEN		100		//发送数据最大长度
#define RECEIVE_DATA_LEN	1024	//接收数据最大缓存区

#define  SEND_CMD_GET_STATUS		0x01	//获取状态
#define  SEND_CMD_GET_INT_STAT		0x02;	//读中断
#define  SEND_CMD_LEDS_ONOFF		0x03;	//LED灯开关
#define  SEND_CMD_RESET_SOC			0x04;	//重启SOC
#define  SEND_CMD_SHUTDOWN_FINISH	0x05;	//重启完成
#define  SEND_CMD_SLEEP				0x06;	//休眠
#define  SEND_CMD_WAKE_UP			0x07;	//唤醒
#define  SEND_CMD_SHUTDOWN_DELAY	0x08;	//延时关闭
#define  SEND_CMD_GET_VERSION		0x09;	//获取版本号

typedef struct
{
	int cmd;
	bool (*ProcData) (void);
}S_RECEIVE_TAB;

typedef struct
{
	int cmd;
	bool (*ProcData) (void);
}S_SENDCMD_TAB;

class CFormData
{
public:
	CFormData()
	{
		m_KeyStatus = 0;
		m_LEDStatus = 0;
		m_IOStatus = 0;
	}
	~CFormData()
	{

	}

public:

	int m_KeyStatus;	//Key状态
	int m_LEDStatus;	//LED状态
	int m_IOStatus;		//IO状态
};


#endif /* FORMDATA_H_ */
