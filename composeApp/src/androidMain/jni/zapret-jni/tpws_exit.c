#include "tpws_exit.h"
#include <setjmp.h>

jmp_buf tpws_exit_env;

void tpws_exit(int code)
{
    longjmp(tpws_exit_env, code ? code : 1);
}