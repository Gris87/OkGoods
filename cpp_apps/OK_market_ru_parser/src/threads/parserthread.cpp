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
    CHECK_AND_CALL(requestCitiesAndServices());
    CHECK_AND_CALL(requestShops());

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
            addError(tr("Failed to find project directory"));

            return false;
        }

        res = res.left(index);
    }

    if (mTerminated)
    {
        return false;
    }

    mProjectDir = res;

    return true;
}

bool ParserThread::requestCitiesAndServices()
{
    QNetworkAccessManager manager;

    QNetworkReply *reply = manager.get(QNetworkRequest(QUrl("http://okmarket.ru/stores/")));
    QEventLoop loop;
    QObject::connect(reply, SIGNAL(finished()), &loop, SLOT(quit()));
    loop.exec();

    while (!reply->atEnd() && !mTerminated)
    {
        QString line = reply->readLine().trimmed();

        if (line.startsWith("<a data-city="))
        {
            int index = line.indexOf("\"", 14);

            QString city = line.mid(14, index - 14);
            mCities.append(city);
        }
        else
        if (line.startsWith("<span class=\"shop-filter-wide__item-text\">"))
        {
            int index = line.indexOf("</span>", 42);

            QString service = line.mid(42, index - 42);

            if (!mServices.contains(service))
            {
                mServices.append(service);
            }
        }
    }

    if (mTerminated)
    {
        return false;
    }

    if (mCities.length() == 0)
    {
        addError(tr("Failed to get cities list"));

        return false;
    }

    if (mServices.length() == 0)
    {
        addError(tr("Failed to get services list"));

        return false;
    }

    return true;
}

bool ParserThread::requestShops()
{
    QNetworkAccessManager manager;

    for (int i = 0; i < mCities.length() && !mTerminated; ++i)
    {
        QNetworkReply *reply = manager.get(QNetworkRequest(QUrl("http://okmarket.ru/stores/")));
        QEventLoop loop;
        QObject::connect(reply, SIGNAL(finished()), &loop, SLOT(quit()));
        loop.exec();

        while (!reply->atEnd() && !mTerminated)
        {
            QString line = reply->readLine().trimmed();

            if (line.startsWith("<a href=\"/stores/") && line.contains("shop-list-item"))
            {
                ShopInfo shop;



                int index = line.indexOf("\"", 17);

                shop.id = line.mid(17, index - 17);

                if (shop.id.endsWith("/"))
                {
                    shop.id.remove(shop.id.length() - 1, 1);
                }



                QNetworkReply *reply = manager.get(QNetworkRequest(QUrl("http://okmarket.ru/stores/" + shop.id + "/")));
                QEventLoop loop;
                QObject::connect(reply, SIGNAL(finished()), &loop, SLOT(quit()));
                loop.exec();

                while (!reply->atEnd() && !mTerminated)
                {
                    QString line = reply->readLine().trimmed();

                    if (line == "<div class=\"main-info__title\">")
                    {
                        shop.name = reply->readLine().trimmed();

                        if (shop.name.endsWith("</div>"))
                        {
                            shop.name = shop.name.remove(shop.name.length() - 6, 6).trimmed();
                        }
                    }

                    qDebug() << line;
                }

                if (mTerminated)
                {
                    return false;
                }

                qDebug() << shop.id;
                qDebug() << shop.name;

                mShops.append(shop);

                break; // TODO: Remove it
            }
        }

        if (mTerminated)
        {
            return false;
        }

        emit progressChanged(i + 1, mCities.length());

        break; // TODO: Remove it
    }

    if (mTerminated)
    {
        return false;
    }

    if (mShops.length() == 0)
    {
        addError(tr("Failed to get shops list"));

        return false;
    }

    return true;
}
