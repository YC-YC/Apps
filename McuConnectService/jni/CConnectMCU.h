/*
 * ConnectMCU.h
 *
 *  Created on: 2016-1-12
 *      Author: YC
 */

#ifndef CONNECTMCU_H_
#define CONNECTMCU_H_
#include "comdef.h"
#include "CUartInterfaceLoc.h"
#include "formData.h"

class CConnectMCU
{
public:

	CConnectMCU();
	~CConnectMCU();
	void Looper();
	const char* GetAppVersion();
	CUartInterfaceLoc* GetUartInterface();
	CFormData* GetFormData();
	int GetKeyStatus();
	int GetLEDStatus();
	int GetIOStatus();

private:
	CFormData* m_pFormData;		//共用数据
	CUartInterfaceLoc* m_pUartInterface;	//协议处理

};


#endif /* CONNECTMCU_H_ */
