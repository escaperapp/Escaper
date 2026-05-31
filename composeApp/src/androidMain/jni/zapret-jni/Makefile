cmake_minimum_required(VERSION 3.22)

project(tpwsjni)

add_library(
        zapret-jni
        SHARED

        jni_bridge.c
        tpws_exit.c
        tpws_control.c
        zapret_log_android.c
        ../zapret/tpws/tpws.c
        ../zapret/tpws/tpws_conn.c
        ../zapret/tpws/helpers.c
        ../zapret/tpws/hostlist.c
        ../zapret/tpws/ipset.c
        ../zapret/tpws/params.c
        ../zapret/tpws/pools.c
        ../zapret/tpws/protocol.c
        ../zapret/tpws/redirect.c
        ../zapret/tpws/resolver.c
        ../zapret/tpws/sec.c
        ../zapret/tpws/tamper.c

        ../zapret/tpws/andr/getifaddrs.c
        ../zapret/tpws/andr/netlink.c
        ../zapret/tpws/gzip.c
)

target_include_directories(
        zapret-jni
        PRIVATE
        ../zapret/tpws
        ../zapret/tpws/andr
)

find_library(log-lib log)

target_link_libraries(
        zapret-jni
        ${log-lib}
)