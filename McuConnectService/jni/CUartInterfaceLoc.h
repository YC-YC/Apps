/*
 * CUartInterfaceLoc.h
 * 管理串口协议
 *  Created on: 2016-2-22
 *      Author: YC
 */

#ifndef CUARTINTERFACELOC_H_
#define CUARTINTERFACELOC_H_

#include "asm/termbits.h"
#include "CUartCtrl.h"

//#define BLUETOOTH_UART_NAME		"/dev/ttyS3"
//#define BLUETOOTH_UART_BAUD		B9600

#define UART_NAME		"/dev/ttyS0"
#define UART_BAUD		B115200


class Interface
{
public:
	virtual bool Looper() = 0;	//循环处理
	virtual void ProcReceiveData(const BYTE* buff, int len) = 0;	//接收处理
private:
	virtual bool ProcSendData(const BYTE* buff, int len) = 0;	//发送处理
};

class CUartInterfaceLoc:public Interface
{
public:
	CUartInterfaceLoc();
	~CUartInterfaceLoc();
	bool Looper();
	void ProcReceiveData(const BYTE* buff, int len);	//接收处理
	bool ProcCmd(int cmd, const char* arg);
private:
	void PrintBufferHex(const BYTE* buf, int len);
	bool ProcSendData(const BYTE* buff, int len);	//发送处理
	void OpenUart();	//打开串口
	void CloseUart();
	void ClearArgBuff();	//清空参数数据
	void CleanSendBuff();	//清空发送数据


private:
	CUartCtrl* mUart;	//串口通讯
	char* m_pArg;	//命令参数
	int m_ArgLen;	//命令长度

	BYTE* m_pSendBuff;	//发送数据
	int m_SendLen;	//发送长度

};

#endif /* CUARTINTERFACELOC_H_ */
