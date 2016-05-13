#ifndef THREADS_H
#define THREADS_H

#include <QThread>
#include <QList>



class Threads
{
public:
    static void registerThread(QThread *thread);
    static void unregisterThread(QThread *thread);
    static void waitAllThreads();

private:
    static QList<QThread *> sThreads;
};

#endif // THREADS_H
