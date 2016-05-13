#include "parserthread.h"



ParserThread::ParserThread(QObject *parent) :
    QThread(parent)
{
    mTerminated = false;
}

ParserThread::~ParserThread()
{
    // Nothing
}

void ParserThread::stop()
{
    mTerminated = true;
}

void ParserThread::run()
{

}
