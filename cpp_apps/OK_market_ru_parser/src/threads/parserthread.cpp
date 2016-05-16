#include "parserthread.h"

#include <QApplication>
#include <QFile>
#include <QStringBuilder>
#include <QDebug>
#include <QtNetwork/QNetworkAccessManager>
#include <QtNetwork/QNetworkProxy>
#include <QtNetwork/QNetworkRequest>
#include <QtNetwork/QNetworkReply>



#define CHECK_AND_CALL(function) \
{ \
    if (mTerminated || !function) \
    { \
        return; \
    } \
}



ParserThread::ParserThread(const QString &proxyHost, quint16 proxyPort, QObject *parent) :
    QThread(parent)
{
    mTerminated = false;
    mProxyHost  = proxyHost;
    mProxyPort  = proxyPort;
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
    CHECK_AND_CALL(getProjectDir());
    CHECK_AND_CALL(requestCitiesAndServices());
    CHECK_AND_CALL(requestShops());
    CHECK_AND_CALL(updateSourceCode());
}

void ParserThread::addError(const QString& error)
{
    mErrors.append(error);
}

QString ParserThread::removeLetters(const QString &text)
{
    QString res = text;

    for (int i = res.length() - 1; i >= 0 && !mTerminated; --i)
    {
        if (res.at(i) < '0' || res.at(i) > '9')
        {
            res.remove(i, 1);
        }
    }

    return res;
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

    if (mProxyHost != "")
    {
        manager.setProxy(QNetworkProxy(QNetworkProxy::HttpProxy, mProxyHost, mProxyPort));
    }

    QNetworkReply *reply = manager.get(QNetworkRequest(QUrl("http://okmarket.ru/stores/")));

    QEventLoop loop;
    QObject::connect(reply, SIGNAL(finished()),                         &loop, SLOT(quit()));
    QObject::connect(reply, SIGNAL(error(QNetworkReply::NetworkError)), &loop, SLOT(quit()));
    QObject::connect(reply, SIGNAL(sslErrors(QList<QSslError>)),        &loop, SLOT(quit()));
    loop.exec();

    while (!reply->atEnd() && !mTerminated)
    {
        QString line = reply->readLine().trimmed();

        if (line.startsWith("<a data-city="))
        {
            int index = line.indexOf("\"", 14);

            if (index >= 0)
            {
                QString city = line.mid(14, index - 14);
                mCities.append(city);
            }
            else
            {
                addError(tr("Failed to get city in line: %1").arg(line));
            }
        }
        else
        if (line.startsWith("<span class=\"shop-filter-wide__item-text\">"))
        {
            int index = line.indexOf("</span>", 42);

            if (index >= 0)
            {
                QString service = line.mid(42, index - 42);

                if (!mServices.contains(service))
                {
                    mServices.append(service);
                }
            }
            else
            {
                addError(tr("Failed to get service in line: %1").arg(line));
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

    if (mProxyHost != "")
    {
        manager.setProxy(QNetworkProxy(QNetworkProxy::HttpProxy, mProxyHost, mProxyPort));
    }

    QLocale russianLocale(QLocale::Russian);

    for (int i = 0; i < mCities.length() && !mTerminated; ++i)
    {
        QNetworkReply *reply = manager.get(QNetworkRequest(QUrl("http://okmarket.ru/stores/")));

        QEventLoop loop;
        QObject::connect(reply, SIGNAL(finished()),                         &loop, SLOT(quit()));
        QObject::connect(reply, SIGNAL(error(QNetworkReply::NetworkError)), &loop, SLOT(quit()));
        QObject::connect(reply, SIGNAL(sslErrors(QList<QSslError>)),        &loop, SLOT(quit()));
        loop.exec();

        while (!reply->atEnd() && !mTerminated)
        {
            QString line = reply->readLine().trimmed();

            if (line.startsWith("<a href=\"/stores/") && line.contains("shop-list-item"))
            {
                ShopInfo shop;



                int index = line.indexOf("\"", 17);

                if (index >= 0)
                {
                    QString shopId = line.mid(17, index - 17);

                    if (shopId.endsWith("/"))
                    {
                        shopId.remove(shopId.length() - 1, 1);
                    }

                    shop.id      = shopId.toULongLong();
                    shop.city_id = i + 1;



                    QNetworkReply *reply = manager.get(QNetworkRequest(QUrl("http://okmarket.ru/stores/" + shopId + "/")));

                    QEventLoop loop;
                    QObject::connect(reply, SIGNAL(finished()),                         &loop, SLOT(quit()));
                    QObject::connect(reply, SIGNAL(error(QNetworkReply::NetworkError)), &loop, SLOT(quit()));
                    QObject::connect(reply, SIGNAL(sslErrors(QList<QSslError>)),        &loop, SLOT(quit()));
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
                        else
                        if (line == "<div class=\"shop-detail-info-item__title\">")
                        {
                            QString property = reply->readLine().trimmed();

                            if (property.endsWith("</div>"))
                            {
                                property = property.remove(property.length() - 6, 6).trimmed();
                            }

                            if (
                                    property == "Телефон"
                                    ||
                                    property == "Часы работы"
                                    ||
                                    property == "Площадь магазина"
                                    ||
                                    property == "Дата открытия"
                                    ||
                                    property == "Парковка"
                                    ||
                                    property == "Количество касс"
                               )
                            {
                                bool    propertyRead  = false;
                                QString propertyValue = "";

                                for (int j = 0; j < 3 && !reply->atEnd() && !mTerminated; j++)
                                {
                                    propertyValue = reply->readLine().trimmed();

                                    if (propertyValue == "<div class=\"shop-detail-info-item__text\">")
                                    {
                                        propertyValue = reply->readLine().trimmed();

                                        if (propertyValue.endsWith("</div>"))
                                        {
                                            propertyValue = propertyValue.remove(propertyValue.length() - 6, 6).trimmed();
                                        }

                                        propertyRead = true;

                                        break;
                                    }
                                }

                                if (propertyRead)
                                {
                                    if (property == "Телефон")
                                    {
                                        shop.phone = propertyValue;
                                    }
                                    else
                                    if (property == "Часы работы")
                                    {
                                        shop.work_hours = propertyValue;
                                    }
                                    else
                                    if (property == "Площадь магазина")
                                    {
                                        if (propertyValue.endsWith("м2"))
                                        {
                                            propertyValue.remove(propertyValue.length() - 2, 2);
                                        }

                                        shop.square = removeLetters(propertyValue).toULongLong();
                                    }
                                    else
                                    if (property == "Дата открытия")
                                    {
                                        shop.opening_date = russianLocale.toDate(propertyValue, "d MMMM yyyy");
                                    }
                                    else
                                    if (property == "Парковка")
                                    {
                                        shop.parking_places = removeLetters(propertyValue).toULongLong();
                                    }
                                    else
                                    if (property == "Количество касс")
                                    {
                                        shop.number_of_cashboxes = propertyValue.toULongLong();
                                    }
                                    else
                                    {
                                        addError(tr("Incorrect property \"%1\" in shop %2 (%3)").arg(property).arg(shop.name).arg(mCities.at(i)));
                                    }
                                }
                                else
                                {
                                    addError(tr("Failed to read value for property \"%1\" in shop %2 (%3)").arg(property).arg(shop.name).arg(mCities.at(i)));
                                }
                            }
                            else
                            if (
                                    property != "Адрес"
                                    &&
                                    property != "Ближайшее метро"
                                    &&
                                    property != "Как добраться"
                               )
                            {
                                addError(tr("Unknown property \"%1\" in shop %2 (%3)").arg(property).arg(shop.name).arg(mCities.at(i)));
                            }
                        }
                        else
                        if (line.startsWith("var jsonStr"))
                        {
                            int index = line.indexOf("\"coords\"");

                            if (index >= 0)
                            {
                                int index2 = line.indexOf("[", index + 8);

                                if (index2 >= 0)
                                {
                                    int index3 = line.indexOf("]", index2 + 1);

                                    if (index3 >= 0)
                                    {
                                        int index4 = line.indexOf(",", index2 + 1);

                                        if (index4 > index2 && index4 < index3)
                                        {
                                            QString latitude  = line.mid(index2 + 1, index4 - index2 - 2).replace("\"", "");
                                            QString longitude = line.mid(index4 + 1, index3 - index4 - 2).replace("\"", "");

                                            shop.latitude  = latitude.toDouble();
                                            shop.longitude = longitude.toDouble();
                                        }
                                        else
                                        {
                                            addError(tr("Failed to get coordinates for shop %1 (%2)").arg(shop.name).arg(mCities.at(i)));
                                        }
                                    }
                                    else
                                    {
                                        addError(tr("Failed to get coordinates for shop %1 (%2)").arg(shop.name).arg(mCities.at(i)));
                                    }
                                }
                                else
                                {
                                    addError(tr("Failed to get coordinates for shop %1 (%2)").arg(shop.name).arg(mCities.at(i)));
                                }
                            }
                            else
                            {
                                addError(tr("Failed to get coordinates for shop %1 (%2)").arg(shop.name).arg(mCities.at(i)));
                            }

                            shop.is_hyper_market = line.contains("\"giper\":\"Y\"");
                        }
                        else
                        if (line == "<div class=\"shop-detail__icon-container\">")
                        {
                            while (!reply->atEnd() && !mTerminated)
                            {
                                QString line = reply->readLine().trimmed();

                                if (line.startsWith("<div title=\""))
                                {
                                    int index = line.indexOf("\"", 12);

                                    if (index >= 0)
                                    {
                                        QString service = line.mid(12, index - 12);
                                        shop.services_set.append(service);
                                    }
                                }
                                else
                                if (line == "</div>")
                                {
                                    break;
                                }
                                else
                                if (line != "")
                                {
                                    addError(tr("Unexpected line during parsing services set: %1").arg(line));
                                }
                            }
                        }
                    }

                    if (mTerminated)
                    {
                        return false;
                    }



                    if (shop.id == 0)
                    {
                        addError(tr("Incorrect shop id %1 in shop %2 (%3)").arg(shopId).arg(shop.name).arg(mCities.at(i)));
                    }

                    if (shop.city_id == 0)
                    {
                        addError(tr("Incorrect city id %1 for shop %2 (%3)").arg(shop.city_id).arg(shop.name).arg(mCities.at(i)));
                    }

                    if (shop.name == "")
                    {
                        addError(tr("Incorrect shop name in city %1").arg(mCities.at(i)));
                    }

                    if (shop.latitude == 0)
                    {
                        addError(tr("Incorrect latitude %1 for shop %2 (%3)").arg(shop.latitude).arg(shop.name).arg(mCities.at(i)));
                    }

                    if (shop.longitude == 0)
                    {
                        addError(tr("Incorrect longitude %1 for shop %2 (%3)").arg(shop.longitude).arg(shop.name).arg(mCities.at(i)));
                    }

                    if (shop.phone == "")
                    {
                        addError(tr("Incorrect phone for shop %1 (%2)").arg(shop.name).arg(mCities.at(i)));
                    }

                    if (shop.work_hours == "")
                    {
                        addError(tr("Incorrect work hours for shop %1 (%2)").arg(shop.name).arg(mCities.at(i)));
                    }

                    if (shop.square == 0)
                    {
                        addError(tr("Incorrect square %1 for shop %2 (%3)").arg(shop.square).arg(shop.name).arg(mCities.at(i)));
                    }

                    if (!shop.opening_date.isValid())
                    {
                        addError(tr("Incorrect opening date for shop %1 (%2)").arg(shop.name).arg(mCities.at(i)));
                    }

                    if ((shop.parking_places > 0) != (shop.services_set.contains("Парковка")))
                    {
                        addError(tr("Incorrect parking places %1 for shop %2 (%3)").arg(shop.parking_places).arg(shop.name).arg(mCities.at(i)));
                    }

                    if (shop.number_of_cashboxes == 0)
                    {
                        addError(tr("Incorrect number of cashboxes %1 for shop %2 (%3)").arg(shop.number_of_cashboxes).arg(shop.name).arg(mCities.at(i)));
                    }

                    if (shop.services_set.length() == 0)
                    {
                        addError(tr("Incorrect services set for shop %1 (%2)").arg(shop.name).arg(mCities.at(i)));
                    }



                    qDebug() << shop.id;
                    qDebug() << shop.city_id;
                    qDebug() << shop.name;
                    qDebug() << shop.is_hyper_market;
                    qDebug() << shop.latitude;
                    qDebug() << shop.longitude;
                    qDebug() << shop.phone;
                    qDebug() << shop.work_hours;
                    qDebug() << shop.square;
                    qDebug() << shop.opening_date;
                    qDebug() << shop.parking_places;
                    qDebug() << shop.number_of_cashboxes;
                    qDebug() << shop.services_set;

                    mShops.append(shop);
                }
                else
                {
                    addError(tr("Failed to get shop ID in line: %1").arg(line));
                }

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

bool ParserThread::updateSourceCode()
{
    return true;
}
