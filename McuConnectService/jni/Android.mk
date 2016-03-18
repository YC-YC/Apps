LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#链接日志模块
LOCAL_LDLIBS    := -lm -llog -landroid

LOCAL_MODULE    := Connection

LOCAL_C_INCLUDES	:= F:\zhonghong\include

LOCAL_SRC_FILES := JniInterface.cpp CConnectMCU.cpp CUartCtrl.cpp CUartInterfaceLoc.cpp

include $(BUILD_SHARED_LIBRARY)
