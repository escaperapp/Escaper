LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := nfqwsjni

LOCAL_SRC_FILES := \
    jni_bridge.c \
    ../zapret/nfq/checksum.c \
    ../zapret/nfq/desync.c \
    ../zapret/nfq/hostlist.c \
    ../zapret/nfq/packet_queue.c \
    ../zapret/nfq/protocol.c \
    ../zapret/nfq/conntrack.c \
    ../zapret/nfq/gzip.c \
    ../zapret/nfq/ipset.c \
    ../zapret/nfq/params.c \
    ../zapret/nfq/sec.c \
    ../zapret/nfq/darkmagic.c \
    ../zapret/nfq/helpers.c \
    ../zapret/nfq/nfqws.c \
    ../zapret/nfq/pools.c \
    ../zapret/nfq/win.c

LOCAL_C_INCLUDES := \
    $(LOCAL_PATH)/../zapret/tpws

LOCAL_LDLIBS := -llog -lz

include $(BUILD_SHARED_LIBRARY)