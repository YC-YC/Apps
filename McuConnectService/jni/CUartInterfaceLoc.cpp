/*
 * CUartInterfaceLoc.cpp
 * 负责协议
 *  Created on: 2016-2-22
 *      Author: YC
 */
#include "CUartInterfaceLoc.h"
#include "CConnectMCU.h"

extern CConnectMCU* g_pCConnectMCU;

const char* CJavaForString(int cmd, const char*val);//调用Java层函数

static void ProcUartRxTemp(const BYTE* buff, int len)
{
	if (g_pCConnectMCU != NULL)
	{
		g_pCConnectMCU->GetUartInterface()->ProcReceiveData(buff, len);
	}
}
static bool ProcSendGetStatus()
{

}

//TODO 发送数据表
static const S_SENDCMD_TAB sSendCmdTab[]
{
		SEND_CMD_GET_STATUS, ProcSendGetStatus,		//获取状态
		0, NULL
};

CUartInterfaceLoc::CUartInterfaceLoc()
{
	m_pArg = new char[CMD_ARG_LEN];
	m_pSendBuff = new BYTE[SEND_DATA_LEN];
	ClearArgBuff();
	mUart = new CUartCtrl();
	OpenUart();
}
CUartInterfaceLoc::~CUartInterfaceLoc()
{
	if (mUart != NULL)
	{
		delete mUart;
		mUart = NULL;
	}
	if (m_pSendBuff != NULL)
	{
		delete[] m_pSendBuff;
		m_pSendBuff = NULL;
	}
	if (m_pArg != NULL)
	{
		delete[] m_pArg;
		m_pArg = NULL;
	}
}
bool CUartInterfaceLoc::Looper()
{

	return true;
}

void CUartInterfaceLoc::ProcReceiveData(const BYTE* buff, int len)	//接收处理
{
	static BYTE rxBuff[RECEIVE_DATA_LEN + 1] = {0};
	static int rxLen = 0;

	if (len < 0)
	{
		return;
	}

	if (rxLen + len > RECEIVE_DATA_LEN)
	{
		CopyMemory(rxBuff, buff, RECEIVE_DATA_LEN - rxLen);
		rxLen = RECEIVE_DATA_LEN;
	}
	PrintBufferHex(rxBuff, rxLen);
	while(1)
	{
		return;
	}
}

bool CUartInterfaceLoc::ProcCmd(int cmd, const char* arg)
{
	ClearArgBuff();
	if (arg != NULL)
	{
		m_ArgLen = sizeof(arg);
		if (m_ArgLen > 0)
		{
			if (m_ArgLen >= CMD_ARG_LEN)
			{
				m_ArgLen = CMD_ARG_LEN - 1;
			}
			CopyMemory(m_pArg, arg, m_ArgLen);
		}
	}
	int totalNum = sizeof(sSendCmdTab)/sizeof(sSendCmdTab[0]);
	for (int i = 0; i < totalNum; i++)
	{
		if (sSendCmdTab[i].cmd == cmd)
		{
			CleanSendBuff();
			sSendCmdTab[i].ProcData();
			ProcSendData(m_pSendBuff, m_SendLen);
			return true;
		}
	}
	return false;
}

void CUartInterfaceLoc::PrintBufferHex(const BYTE* buf, int len)	//打印接收到的信息
{
	int newLen = 1024;
	char* printbuff = new char[newLen];
	ZeroMemory(printbuff, newLen);
	char temp[10] = {0};
	for (int i = 0; i < len && i < newLen-2; i++)
	{
		ZeroMemory(temp, 10);
		sprintf(temp, "%02x ", buf[i]);
		strcat(printbuff, temp);
	}
	strcat(printbuff, "\r\n");
//	printf("%s", printbuff);
	LOGI("Receive:%s", printbuff);
	delete printbuff;
	printbuff = NULL;
}

bool CUartInterfaceLoc::ProcSendData(const BYTE* buff, int len)	//发送处理
{
	if (mUart != NULL)
	{
		mUart->Send(buff, len);
		return true;
	}
	return false;
}

void CUartInterfaceLoc::OpenUart()	//打开串口
{
	LOGI("OpenUart\r\n");
	if (mUart != NULL)
	{
		LOGI("mUart != NULL\r\n");
		if (!mUart->IsOpen())
		{
			LOGI("!mUart->IsOpen()\r\n");
			mUart->RegRxFun(ProcUartRxTemp);
			mUart->OpenUart(UART_NAME, UART_BAUD);
		}
	}
}
void CUartInterfaceLoc::CloseUart()
{
	if (mUart != NULL)
	{
		if (mUart->IsOpen())
		{
			mUart->CloseUart();
		}
	}
}

void CUartInterfaceLoc::ClearArgBuff()	//清空参数数据
{
	ZeroMemory(m_pArg, sizeof(char)*CMD_ARG_LEN);
	m_ArgLen = 0;
}

void CUartInterfaceLoc::CleanSendBuff()	//清空发送数据
{
	ZeroMemory(m_pSendBuff, sizeof(BYTE)*SEND_DATA_LEN);
	m_SendLen = 0;
}
