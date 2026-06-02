cmake_minimum_required(VERSION 3.22)

project(nfqwsjni)

add_library(
        zapret-jni
        SHARED

        jni_bridge.c
            ../zapret/nfq/checksum.c
            ../zapret/nfq/desync.c
            ../zapret/nfq/hostlist.c
            ../zapret/nfq/packet_queue.c
            ../zapret/nfq/protocol.c
            ../zapret/nfq/conntrack.c
            ../zapret/nfq/gzip.c
            ../zapret/nfq/ipset.c
            ../zapret/nfq/params.c
            ../zapret/nfq/sec.c
            ../zapret/nfq/darkmagic.c
            ../zapret/nfq/helpers.c
            ../zapret/nfq/nfqws.c
            ../zapret/nfq/pools.c
            ../zapret/nfq/win.c
)

target_include_directories(
        zapret-jni
        PRIVATE
        ../zapret/nfq
)

find_library(log-lib log)

target_link_libraries(
        zapret-jni
        ${log-lib}
)