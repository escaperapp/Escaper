#pragma once

#include <stdbool.h>

void tpws_start(int argc, char **argv);
void tpws_stop(void);
bool tpws_is_running(void);