#include "parserthread.h"

#include <QApplication>
#include <QFile>
#include <QDebug>
#include <QtNetwork/QNetworkAccessManager>
#include <QtNetwork/QNetworkRequest>
#include <QtNetwork/QNetworkReply>



#define CHECK_AND_CALL(function) \
{ \
    if (mTerminated || !function) \
    { \
        return false; \
    } \
}



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
    startPoint();
}

void ParserThread::addError(const QString& error)
{
    mErrors.append(error);
}

bool ParserThread::startPoint()
{
    CHECK_AND_CALL(getProjectDir());
    CHECK_AND_CALL(requestCities());

    return true;
}

bool ParserThread::getProjectDir()
{
    QString res = QApplication::applicationDirPath();

    while (!mTerminated)
    {
        if (QFile::exists(res + "/build.gradle"))
        {
            break;
        }

        int index = res.lastIndexOf('/');

        if (index < 0)
        {
            addError("Failed to find project directory");

            return false;
        }

        res = res.left(index);
    }

    mProjectDir = res;

    return true;
}

bool ParserThread::requestCities()
{
    QNetworkAccessManager manager;

    QNetworkReply *reply = manager.get(QNetworkRequest(QUrl("http://okmarket.ru/stores/")));

    reply->waitForReadyRead(30000);

    while (!reply->atEnd())
    {
        qDebug() << reply->readLine();
    }

    return true;
}
