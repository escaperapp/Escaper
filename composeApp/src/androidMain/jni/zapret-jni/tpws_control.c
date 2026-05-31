#include "tpws_control.h"
#include "tpws_exit.h"

#include <pthread.h>
#include <stdlib.h>
#include <string.h>

/* zapret internal API */
extern int main(int argc, char *argv[]);
extern void resolver_deinit(void);

static pthread_t worker;
static volatile bool running = false;

/* saved argv */
static char **saved_argv = NULL;
static int saved_argc = 0;

static void* worker_thread(void *arg)
{
    int rc;

    int jmp_rc = setjmp(tpws_exit_env);

    if (jmp_rc == 0) {
        rc = main(saved_argc, saved_argv);
        (void)rc;
    } else {
        /* exit() intercepted here */
    }

    running = false;
    return NULL;
}

void tpws_start(int argc, char **argv)
{
    if (running) return;

    saved_argc = argc;
    saved_argv = calloc(argc + 1, sizeof(char*));

    for (int i = 0; i < argc; i++) {
        saved_argv[i] = strdup(argv[i]);
    }

    running = true;
    pthread_create(&worker, NULL, worker_thread, NULL);
}

void tpws_stop(void)
{
    if (!running) return;

    /* 1. trigger internal zapret shutdown */
    resolver_deinit();

    /* 2. in case main loop is blocked */
    pthread_kill(worker, SIGUSR1);

    /* 3. wait for thread exit */
    pthread_join(worker, NULL);

    /* 4. cleanup argv */
    for (int i = 0; i < saved_argc; i++) {
        free(saved_argv[i]);
    }
    free(saved_argv);
    saved_argv = NULL;

    running = false;
}

bool tpws_is_running(void)
{
    return running;
}