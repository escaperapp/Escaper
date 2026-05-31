LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := tpwsjni

LOCAL_SRC_FILES := \
    jni_bridge.c \
    tpws_exit.c \
    tpws_control.c \
    ../zapret/tpws/tpws.c \
    ../zapret/tpws/tpws_conn.c \
    ../zapret/tpws/helpers.c \
    ../zapret/tpws/hostlist.c \
    ../zapret/tpws/ipset.c \
    ../zapret/tpws/params.c \
    ../zapret/tpws/pools.c \
    ../zapret/tpws/protocol.c \
    ../zapret/tpws/redirect.c \
    ../zapret/tpws/resolver.c \
    ../zapret/tpws/sec.c \
    ../zapret/tpws/tamper.c \
    ../zapret/tpws/andr/getifaddrs.c \
    ../zapret/tpws/andr/netlink.c \
    ../zapret/tpws/gzip.c

LOCAL_C_INCLUDES := \
    $(LOCAL_PATH)/../zapret/tpws

LOCAL_LDLIBS := -llog -lz

include $(BUILD_SHARED_LIBRARY)