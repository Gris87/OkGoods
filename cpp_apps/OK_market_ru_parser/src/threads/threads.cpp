#include "threads.h"



QList<QThread *> Threads::sThreads;

void Threads::registerThread(QThread *thread)
{
    sThreads.append(thread);
}

void Threads::unregisterThread(QThread *thread)
{
    sThreads.removeOne(thread);
}

void Threads::waitAllThreads()
{
    foreach (QThread *thread, sThreads)
    {
        thread->wait();
    }

    sThreads.clear();
}
