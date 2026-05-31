#pragma once
#include <setjmp.h>

extern jmp_buf tpws_exit_env;

void tpws_exit(int code);

/* replace ALL exit() calls */
#define exit(code) tpws_exit(code)