/*
 * ConnectMCU.cpp
 * 负责与外界对接
 *  Created on: 2016-1-12
 *      Author: YC
 */

#include "CConnectMCU.h"
#include "debug.h"


CConnectMCU::CConnectMCU()
{
	m_pFormData = new CFormData();
	m_pUartInterface = new CUartInterfaceLoc();
}
CConnectMCU::~CConnectMCU()
{
	if (m_pUartInterface != NULL)
	{
		delete m_pUartInterface;
		m_pUartInterface = NULL;
	}
	if (m_pFormData != NULL)
	{
		delete m_pFormData;
		m_pFormData = NULL;
	}
}

void CConnectMCU::Looper()
{
	if (m_pUartInterface != NULL)
	{
		m_pUartInterface->Looper();
	}
}

const char* CConnectMCU::GetAppVersion()
{
	return "1.0";
}

CUartInterfaceLoc* CConnectMCU::GetUartInterface()
{
	return m_pUartInterface;
}

CFormData* CConnectMCU::GetFormData()
{
	return m_pFormData;
}

int CConnectMCU::GetKeyStatus()
{
	return m_pFormData->m_KeyStatus;
}
int CConnectMCU::GetLEDStatus()
{
	m_pFormData->m_LEDStatus;
}
int CConnectMCU::GetIOStatus()
{
	m_pFormData->m_IOStatus;
}

