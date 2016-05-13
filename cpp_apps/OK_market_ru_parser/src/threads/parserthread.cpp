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

QString ParserThread::getErrors() const
{
    return mErrors.join('\n');
}

void ParserThread::stop()
{
    mTerminated = true;
}

void ParserThread::run()
{

}

void ParserThread::addError(const QString& error)
{
    mErrors.append(error);
}
