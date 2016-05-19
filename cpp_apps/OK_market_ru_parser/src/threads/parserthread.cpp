#include "parserthread.h"

#include <QApplication>
#include <QDebug>
#include <QtNetwork/QNetworkAccessManager>
#include <QtNetwork/QNetworkCookie>
#include <QtNetwork/QNetworkCookieJar>
#include <QtNetwork/QNetworkProxy>
#include <QtNetwork/QNetworkReply>
#include <QtNetwork/QNetworkRequest>



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

    qDebug() << "";
    qDebug() << "===================================";
    qDebug() << "";
    qDebug() << "Done";
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

QString ParserThread::russianTransliteration(const QString &text)
{
    QString res;

    static QString transliteration[] = { "a", "b", "v", "g", "d", "e", "zh", "z", "i", "y", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "f", "kh", "ts", "ch", "sh", "shch", "", "y", "", "eh", "yu", "ya" };

    for (int i = 0; i < text.length(); ++i)
    {
        QChar letter           = text.at(i);
        bool isLetterUpperCase = letter.isUpper();

        if (isLetterUpperCase)
        {
            letter = letter.toLower();
        }

        QString letterTransliteration;

        if (letter.unicode() >= 0x0430 && letter.unicode() <= 0x044F) // letter >= 'а' && letter <= 'я'
        {
            letterTransliteration = transliteration[letter.unicode() - 0x0430]; // letter - 'а'
        }
        else
        if (letter.unicode() == 0x0451) // letter == 'ё'
        {
            letterTransliteration = "yo";
        }
        else
        {
            letterTransliteration = letter;
        }

        if (isLetterUpperCase)
        {
            if (letterTransliteration.length() > 0)
            {
                if (letterTransliteration.length() == 1)
                {
                    letterTransliteration = letterTransliteration.toUpper();
                }
                else
                {
                    letterTransliteration = letterTransliteration.at(0).toUpper() + letterTransliteration.mid(1);
                }
            }
        }

        res.append(letterTransliteration);
    }

    return res;
}

QString ParserThread::precedeTranslations(QString text)
{
    return text.replace("\'", "\\\'").replace("\"", "\\\"").replace("<", "&lt;").replace(">", "&gt;");
}

void ParserThread::readFileToStringList(QFile &file, QStringList &fileContents)
{
    file.open(QIODevice::ReadOnly);

    QTextStream stream(&file);
    stream.setCodec("UTF-8");

    while (!stream.atEnd())
    {
        fileContents.append(stream.readLine());
    }

    file.close();
}

void ParserThread::writeStringListToFile(const QStringList &fileContents, QFile &file)
{
    file.open(QIODevice::WriteOnly);

    QTextStream stream(&file);
    stream.setCodec("UTF-8");

    for (int i = 0; i < fileContents.length(); ++i)
    {
#ifdef Q_OS_WIN
        stream << fileContents.at(i) << "\r\n";
#else
        stream << fileContents.at(i) << "\n";
#endif
    }

    file.close();
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
                addError(tr("Failed to get city in line: \"%1\"").arg(line));
            }
        }
        else
        if (line.startsWith("<span class=\"shop-filter-wide__item-text\">"))
        {
            int index = line.indexOf("</span>", 42);

            if (index >= 0)
            {
                QString service = line.mid(42, index - 42);

                if (
                        service == "Оплата карточкой"
                        ||
                        service == "Бутик косметики"
                        ||
                        service == "Детская площадка"
                        ||
                        service == "Рыбный остров"
                        ||
                        service == "Пекарня"
                        ||
                        service == "Кулинария"
                        ||
                        service == "Заказ такси"
                        ||
                        service == "Аптека"
                        ||
                        service == "Блюда на заказ"
                        ||
                        service == "Дегустации"
                        ||
                        service == "Кафе"
                        ||
                        service == "Подарочные карты"
                        ||
                        service == "Парковка"
                        ||
                        service == "ПВЗ интернет-магазина"
                   )
                {
                    if (!mServices.contains(service))
                    {
                        mServices.append(service);
                    }
                }
                else
                if (
                        service != "Супермаркет"
                        &&
                        service != "Гипермаркет"
                        &&
                        service != "Круглосуточно"
                   )
                {
                    addError(tr("Unknown service: \"%1\"").arg(service));
                }
            }
            else
            {
                addError(tr("Failed to get service in line: \"%1\"").arg(line));
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

    manager.setCookieJar(0);
    QList<QNetworkCookie> cookies;



    for (int i = 0; i < mCities.length() && !mTerminated; ++i)
    {
        cookies.clear();
        cookies.append(QNetworkCookie("BITRIX_SM_CURRENT_CITY", QUrl::toPercentEncoding(mCities.at(i))));

        QNetworkRequest request(QUrl("http://okmarket.ru/stores/"));
        request.setHeader(QNetworkRequest::CookieHeader, QVariant::fromValue(cookies));
        QNetworkReply *reply = manager.get(request);

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
                int index = line.indexOf("\"", 17);

                if (index >= 0)
                {
                    QString shopId = line.mid(17, index - 17);

                    if (shopId.endsWith("/"))
                    {
                        shopId.remove(shopId.length() - 1, 1);
                    }

                    ShopInfo shop;

                    shop.id      = shopId.toULongLong();
                    shop.city_id = i + 1;

                    if (shop.id > 0)
                    {
                        mShops.append(shop);
                    }
                    else
                    {
                        addError(tr("Incorrect shop id \"%1\" in city \"%2\"").arg(shopId).arg(mCities.at(i)));
                    }
                }
                else
                {
                    addError(tr("Failed to get shop ID in line: \"%1\"").arg(line));
                }

                break; // TODO: Remove it
            }
        }

        if (mTerminated)
        {
            return false;
        }

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



    quint64 previousCity = 0;

    QLocale russianLocale(QLocale::Russian);

    for (int i = 0; i < mShops.length() && !mTerminated; ++i)
    {
        ShopInfo &shop = mShops[i];

        emit progressChanged(i + 1, mShops.length());



        if (previousCity != shop.city_id)
        {
            cookies.clear();
            cookies.append(QNetworkCookie("BITRIX_SM_CURRENT_CITY", QUrl::toPercentEncoding(mCities.at(shop.city_id - 1))));

            previousCity = shop.city_id;
        }



        QNetworkRequest request(QUrl("http://okmarket.ru/stores/" + shopId + "/"));
        request.setHeader(QNetworkRequest::CookieHeader, QVariant::fromValue(cookies));
        QNetworkReply *reply = manager.get(request);

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
                            else
                            {
                                addError(tr("Unexpected property value \"%1\" for property \"%2\" in shop \"%3\" (%4). It is expected that square is specified in square meters").arg(propertyValue).arg(property).arg(shop.name).arg(mCities.at(i)));
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
                            addError(tr("Incorrect property \"%1\" in shop \"%2\" (%3)").arg(property).arg(shop.name).arg(mCities.at(i)));
                        }
                    }
                    else
                    {
                        addError(tr("Failed to read value for property \"%1\" in shop \"%2\" (%3)").arg(property).arg(shop.name).arg(mCities.at(i)));
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
                    addError(tr("Unknown property \"%1\" in shop \"%2\" (%3)").arg(property).arg(shop.name).arg(mCities.at(i)));
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
                                addError(tr("Failed to get coordinates for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(i)));
                            }
                        }
                        else
                        {
                            addError(tr("Failed to get coordinates for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(i)));
                        }
                    }
                    else
                    {
                        addError(tr("Failed to get coordinates for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(i)));
                    }
                }
                else
                {
                    addError(tr("Failed to get coordinates for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(i)));
                }

                shop.is_hypermarket = line.contains("\"giper\":\"Y\"");
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

                            if (mServices.contains(service))
                            {
                                shop.services_set.append(service);
                            }
                            else
                            {
                                addError(tr("Unexpected service \"%1\" for shop \"%2\" (%3)").arg(service).arg(shop.name).arg(mCities.at(i)));
                            }
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
                        addError(tr("Unexpected line during parsing services set: \"%1\"").arg(line));
                    }
                }
            }
        }

        if (mTerminated)
        {
            return false;
        }



        if (shop.name == "")
        {
            addError(tr("Incorrect shop name in city \"%1\"").arg(mCities.at(i)));
        }

        if (shop.is_hypermarket)
        {
            if (!shop.name.startsWith("Гипермаркет"))
            {
                addError(tr("Shop name \"%1\" (2) is not specifying that shop is hypermarket").arg(shop.name).arg(mCities.at(i)));
            }
        }
        else
        {
            if (!shop.name.startsWith("Супермаркет"))
            {
                addError(tr("Shop name \"%1\" (2) is not specifying that shop is supermarket").arg(shop.name).arg(mCities.at(i)));
            }
        }

        if (shop.latitude == 0)
        {
            addError(tr("Incorrect latitude \"%1\" for shop \"%2\" (%3)").arg(shop.latitude).arg(shop.name).arg(mCities.at(i)));
        }

        if (shop.longitude == 0)
        {
            addError(tr("Incorrect longitude \"%1\" for shop \"%2\" (%3)").arg(shop.longitude).arg(shop.name).arg(mCities.at(i)));
        }

        if (shop.phone == "")
        {
            addError(tr("Incorrect phone for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(i)));
        }

        if (shop.work_hours == "")
        {
            addError(tr("Incorrect work hours for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(i)));
        }

        if (shop.square == 0)
        {
            addError(tr("Incorrect square \"%1\" for shop \"%2\" (%3)").arg(shop.square).arg(shop.name).arg(mCities.at(i)));
        }

        if (!shop.opening_date.isValid())
        {
            addError(tr("Incorrect opening date for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(i)));
        }

        if ((shop.parking_places > 0) != (shop.services_set.contains("Парковка")))
        {
            if (shop.parking_places > 0)
            {
                addError(tr("Parking places \"%1\" specified for shop \"%2\" (%3) while parking service is not available").arg(shop.parking_places).arg(shop.name).arg(mCities.at(i)));
            }
            else
            {
                addError(tr("Parking places is not specified for shop \"%1\" (%2) while parking service is available").arg(shop.name).arg(mCities.at(i)));
            }
        }

        if (shop.number_of_cashboxes == 0)
        {
            addError(tr("Incorrect number of cashboxes \"%1\" for shop \"%2\" (%3)").arg(shop.number_of_cashboxes).arg(shop.name).arg(mCities.at(i)));
        }

        if (shop.services_set.length() == 0)
        {
            addError(tr("Incorrect services set for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(i)));
        }



        qDebug() << shop.id;
        qDebug() << shop.city_id;
        qDebug() << shop.name;
        qDebug() << shop.is_hypermarket;
        qDebug() << shop.latitude;
        qDebug() << shop.longitude;
        qDebug() << shop.phone;
        qDebug() << shop.work_hours;
        qDebug() << shop.square;
        qDebug() << shop.opening_date;
        qDebug() << shop.parking_places;
        qDebug() << shop.number_of_cashboxes;
        qDebug() << shop.services_set;
    }

    if (mTerminated)
    {
        return false;
    }



    for (int e = 0; e < mShops.length() - 1; ++e)
    {
        int minIndex = e;

        for (int i = e + 1; i < mShops.length(); ++i)
        {
            if (mShops.at(i).isLess(mShops.at(minIndex)))
            {
                minIndex = i;
            }
        }

        mShops.swap(minIndex, e);
    }



    return true;
}

bool ParserThread::updateSourceCode()
{
    generateIDs();
    updateStringsXml();
    updateMainDatabaseJava();

    return true;
}

void ParserThread::generateIDs()
{
    for (int i = 0; i < mCities.length(); ++i)
    {
        QString city = mCities.at(i);
        QString cityId;

        if (city == "Москва")
        {
            cityId = "moscow";
        }
        else
        if (city == "Санкт-Петербург")
        {
            cityId = "st_petersburg";
        }
        else
        if (city == "Ростов-на-Дону")
        {
            cityId = "rostov_on_don";
        }
        else
        {
            city = city.replace(" ", "_").replace("-", "_");
            cityId = russianTransliteration(city).toLower();
        }

        mCitiesIDs.append(cityId);
    }

    for (int i = 0; i < mServices.length(); ++i)
    {
        QString service = mServices.at(i);
        QString serviceId;

        if (service == "Оплата карточкой")
        {
            serviceId = "clearing_settlement";
        }
        else
        if (service == "Бутик косметики")
        {
            serviceId = "cosmetics";
        }
        else
        if (service == "Детская площадка")
        {
            serviceId = "playground";
        }
        else
        if (service == "Рыбный остров")
        {
            serviceId = "fish_island";
        }
        else
        if (service == "Пекарня")
        {
            serviceId = "bakery";
        }
        else
        if (service == "Кулинария")
        {
            serviceId = "cookery";
        }
        else
        if (service == "Заказ такси")
        {
            serviceId = "taxi_ordering";
        }
        else
        if (service == "Аптека")
        {
            serviceId = "pharmacy";
        }
        else
        if (service == "Блюда на заказ")
        {
            serviceId = "ordering_food";
        }
        else
        if (service == "Дегустации")
        {
            serviceId = "degustation";
        }
        else
        if (service == "Кафе")
        {
            serviceId = "cafe";
        }
        else
        if (service == "Подарочные карты")
        {
            serviceId = "gift_cards";
        }
        else
        if (service == "Парковка")
        {
            serviceId = "parking";
        }
        else
        if (service == "ПВЗ интернет-магазина")
        {
            serviceId = "point_of_issuing_orders";
        }
        else
        {
            addError(tr("Unknown service: \"%1\"").arg(service));

            service = service.replace(" ", "_").replace("-", "_");
            serviceId = russianTransliteration(service).toLower();
        }

        mServicesIDs.append(serviceId);
    }

    for (int i = 0; i < mShops.length(); ++i)
    {
        QString shop = mShops.at(i).name;
        QString shopId;

        shop = shop.replace("Гипермаркет", "Hypermarket").replace("Супермаркет", "Supermarket").replace("О'КЕЙ", "OK").replace("О’КЕЙ", "OK")
                .replace("Москва", "moscow").replace("Санкт-Петербург", "st_petersburg").replace("Ростов-на-Дону", "rostov_on_don")
                .replace(" ", "_").replace("-", "_").replace("«", "").replace("»", "").replace("\"", "");

        shopId = mCitiesIDs.at(mShops.at(i).city_id - 1) + "_" + russianTransliteration(shop).toLower();

        mShopsIDs.append(shopId);
    }
}

void ParserThread::updateStringsXml()
{
    updateRussianStringsXml();
    updateEnglishStringsXml();
}

void ParserThread::updateRussianStringsXml()
{
    QFile file(mProjectDir + "/app/src/main/res/values-ru/strings.xml");

    if (file.exists())
    {
        QStringList fileContents;

        readFileToStringList(file, fileContents);

        updateRussianStringsXmlCities(fileContents);
        updateRussianStringsXmlServices(fileContents);
        updateRussianStringsXmlShops(fileContents);

        writeStringListToFile(fileContents, file);
    }
    else
    {
        addError(tr("File \"%1\" not found").arg(file.fileName()));
    }
}

void ParserThread::updateRussianStringsXmlCities(QStringList &fileContents)
{
    QStringList newLines;

    for (int i = 0; i < mCities.length(); ++i)
    {
        newLines.append("    <string name=\"city_" + mCitiesIDs.at(i) + "\">" + precedeTranslations(mCities.at(i)) + "</string>");
    }



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed().startsWith("<string name=\"city_"))
        {
            if (start < 0)
            {
                start = i;
            }

            end = i;
        }
        else
        {
            if (end >= 0)
            {
                break;
            }
        }
    }



    if (start >= 0 && start <= end)
    {
        for (int i = end; i >= start; --i)
        {
            fileContents.removeAt(i);
        }

        for (int i = 0; i < newLines.length(); ++i)
        {
            fileContents.insert(start + i, newLines.at(i));
        }
    }
    else
    {
        addError(tr("Failed to modify cities in values-ru/strings.xml"));
    }
}

void ParserThread::updateRussianStringsXmlServices(QStringList &fileContents)
{
    QStringList newLines;

    for (int i = 0; i < mServices.length(); ++i)
    {
        newLines.append("    <string name=\"service_" + mServicesIDs.at(i) + "\">" + precedeTranslations(mServices.at(i)) + "</string>");
    }



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed().startsWith("<string name=\"service_"))
        {
            if (start < 0)
            {
                start = i;
            }

            end = i;
        }
        else
        {
            if (end >= 0)
            {
                break;
            }
        }
    }



    if (start >= 0 && start <= end)
    {
        for (int i = end; i >= start; --i)
        {
            fileContents.removeAt(i);
        }

        for (int i = 0; i < newLines.length(); ++i)
        {
            fileContents.insert(start + i, newLines.at(i));
        }
    }
    else
    {
        addError(tr("Failed to modify services in values-ru/strings.xml"));
    }
}

void ParserThread::updateRussianStringsXmlShops(QStringList &fileContents)
{
    QStringList newLines;

    for (int i = 0; i < mShops.length(); ++i)
    {
        newLines.append("    <string name=\"shop_" + mShopsIDs.at(i) + "\">" + precedeTranslations(mShops.at(i).name) + "</string>");
    }



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed().startsWith("<string name=\"shop_"))
        {
            if (start < 0)
            {
                start = i;
            }

            end = i;
        }
        else
        {
            if (end >= 0)
            {
                break;
            }
        }
    }



    if (start >= 0 && start <= end)
    {
        for (int i = end; i >= start; --i)
        {
            fileContents.removeAt(i);
        }

        for (int i = 0; i < newLines.length(); ++i)
        {
            fileContents.insert(start + i, newLines.at(i));
        }
    }
    else
    {
        addError(tr("Failed to modify shops in values-ru/strings.xml"));
    }
}

void ParserThread::updateEnglishStringsXml()
{
    QFile file(mProjectDir + "/app/src/main/res/values/strings.xml");

    if (file.exists())
    {
        QStringList fileContents;

        readFileToStringList(file, fileContents);

        updateEnglishStringsXmlCities(fileContents);
        updateEnglishStringsXmlServices(fileContents);
        updateEnglishStringsXmlShops(fileContents);

        writeStringListToFile(fileContents, file);
    }
    else
    {
        addError(tr("File \"%1\" not found").arg(file.fileName()));
    }
}

void ParserThread::updateEnglishStringsXmlCities(QStringList &fileContents)
{
    QStringList newLines;

    for (int i = 0; i < mCities.length(); ++i)
    {
        QString city = mCities.at(i);

        if (city == "Москва")
        {
            city = "Moscow";
        }
        else
        if (city == "Санкт-Петербург")
        {
            city = "St. Petersburg";
        }
        else
        if (city == "Ростов-на-Дону")
        {
            city = "Rostov-on-Don";
        }
        else
        {
            city = russianTransliteration(city);
        }

        newLines.append("    <string name=\"city_" + mCitiesIDs.at(i) + "\">" + precedeTranslations(city) + "</string>");
    }



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed().startsWith("<string name=\"city_"))
        {
            if (start < 0)
            {
                start = i;
            }

            end = i;
        }
        else
        {
            if (end >= 0)
            {
                break;
            }
        }
    }



    if (start >= 0 && start <= end)
    {
        for (int i = end; i >= start; --i)
        {
            fileContents.removeAt(i);
        }

        for (int i = 0; i < newLines.length(); ++i)
        {
            fileContents.insert(start + i, newLines.at(i));
        }
    }
    else
    {
        addError(tr("Failed to modify cities in values-ru/strings.xml"));
    }
}

void ParserThread::updateEnglishStringsXmlServices(QStringList &fileContents)
{
    QStringList newLines;

    for (int i = 0; i < mServices.length(); ++i)
    {
        QString service = mServices.at(i);

        if (service == "Оплата карточкой")
        {
            service = "Clearing settlement";
        }
        else
        if (service == "Бутик косметики")
        {
            service = "Cosmetics";
        }
        else
        if (service == "Детская площадка")
        {
            service = "Playground";
        }
        else
        if (service == "Рыбный остров")
        {
            service = "Fish island";
        }
        else
        if (service == "Пекарня")
        {
            service = "Bakery";
        }
        else
        if (service == "Кулинария")
        {
            service = "Cookery";
        }
        else
        if (service == "Заказ такси")
        {
            service = "Taxi ordering";
        }
        else
        if (service == "Аптека")
        {
            service = "Pharmacy";
        }
        else
        if (service == "Блюда на заказ")
        {
            service = "Ordering food";
        }
        else
        if (service == "Дегустации")
        {
            service = "Degustation";
        }
        else
        if (service == "Кафе")
        {
            service = "Cafe";
        }
        else
        if (service == "Подарочные карты")
        {
            service = "Gift cards";
        }
        else
        if (service == "Парковка")
        {
            service = "Parking";
        }
        else
        if (service == "ПВЗ интернет-магазина")
        {
            service = "Point of issuing orders";
        }
        else
        {
            addError(tr("Unknown service: \"%1\"").arg(service));

            service = russianTransliteration(service);
        }

        newLines.append("    <string name=\"service_" + mServicesIDs.at(i) + "\">" + precedeTranslations(service) + "</string>");
    }



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed().startsWith("<string name=\"service_"))
        {
            if (start < 0)
            {
                start = i;
            }

            end = i;
        }
        else
        {
            if (end >= 0)
            {
                break;
            }
        }
    }



    if (start >= 0 && start <= end)
    {
        for (int i = end; i >= start; --i)
        {
            fileContents.removeAt(i);
        }

        for (int i = 0; i < newLines.length(); ++i)
        {
            fileContents.insert(start + i, newLines.at(i));
        }
    }
    else
    {
        addError(tr("Failed to modify services in values-ru/strings.xml"));
    }
}

void ParserThread::updateEnglishStringsXmlShops(QStringList &fileContents)
{
    QStringList newLines;

    for (int i = 0; i < mShops.length(); ++i)
    {
        QString shop = mShops.at(i).name;

        shop = shop.replace("Гипермаркет", "Hypermarket").replace("Супермаркет", "Supermarket").replace("О'КЕЙ", "OK").replace("О’КЕЙ", "OK")
                .replace("Москва", "Moscow").replace("Санкт-Петербург", "St. Petersburg").replace("Ростов-на-Дону", "Rostov-on-Don");

        shop = russianTransliteration(shop);

        newLines.append("    <string name=\"shop_" + mShopsIDs.at(i) + "\">" + precedeTranslations(shop) + "</string>");
    }



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed().startsWith("<string name=\"shop_"))
        {
            if (start < 0)
            {
                start = i;
            }

            end = i;
        }
        else
        {
            if (end >= 0)
            {
                break;
            }
        }
    }



    if (start >= 0 && start <= end)
    {
        for (int i = end; i >= start; --i)
        {
            fileContents.removeAt(i);
        }

        for (int i = 0; i < newLines.length(); ++i)
        {
            fileContents.insert(start + i, newLines.at(i));
        }
    }
    else
    {
        addError(tr("Failed to modify shops in values-ru/strings.xml"));
    }
}

void ParserThread::updateMainDatabaseJava()
{
    QFile file(mProjectDir + "/app/src/main/java/ru/okmarket/okgoods/db/MainDatabase.java");

    if (file.exists())
    {
        QStringList fileContents;

        readFileToStringList(file, fileContents);

        updateMainDatabaseJavaCities(fileContents);
        updateMainDatabaseJavaServices(fileContents);
        updateMainDatabaseJavaShops(fileContents);

        writeStringListToFile(fileContents, file);
    }
    else
    {
        addError(tr("File \"%1\" not found").arg(file.fileName()));
    }
}

void ParserThread::updateMainDatabaseJavaCities(QStringList &fileContents)
{
    updateMainDatabaseJavaCitiesIDs(fileContents);
    updateMainDatabaseJavaCitiesFilling(fileContents);
}

void ParserThread::updateMainDatabaseJavaCitiesIDs(QStringList &fileContents)
{
    int maxLength = 0;

    for (int i = 0; i < mCities.length(); ++i)
    {
        int cityLength = mCitiesIDs.at(i).length();

        if (cityLength > maxLength)
        {
            maxLength = cityLength;
        }
    }



    QStringList newLines;

    for (int i = 0; i < mCities.length(); ++i)
    {
        newLines.append(QString("    public static final int CITY_ID_%1 = %2;").arg(mCitiesIDs.at(i).toUpper(), -maxLength, QChar(' ')).arg(i + 1));
    }



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed().startsWith("public static final int CITY_ID_"))
        {
            if (start < 0)
            {
                start = i;
            }

            end = i;
        }
        else
        {
            if (end >= 0)
            {
                break;
            }
        }
    }



    if (start >= 0 && start <= end)
    {
        for (int i = end; i >= start; --i)
        {
            fileContents.removeAt(i);
        }

        for (int i = 0; i < newLines.length(); ++i)
        {
            fileContents.insert(start + i, newLines.at(i));
        }
    }
    else
    {
        addError(tr("Failed to modify cities in MainDatabase.java"));
    }
}

void ParserThread::updateMainDatabaseJavaCitiesFilling(QStringList &fileContents)
{
    int maxLength = 0;

    for (int i = 0; i < mCities.length(); ++i)
    {
        int cityLength = mCitiesIDs.at(i).length();

        if (cityLength > maxLength)
        {
            maxLength = cityLength;
        }
    }



    QStringList newLines;

    for (int i = 0; i < mCities.length(); ++i)
    {
        newLines.append(QString("        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_%1 mContext.getResources().getString(R.string.city_%2));").arg(mCitiesIDs.at(i).toUpper() + ",", -maxLength - 1, QChar(' ')).arg(mCitiesIDs.at(i)));
    }



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed().startsWith("private void fillCitiesTable("))
        {
            start = i + 2;

            break;
        }
    }



    if (start >= 0)
    {
        for (int i = start; i < fileContents.length(); ++i)
        {
            if (fileContents.at(i).trimmed() == "}")
            {
                end = i - 1;

                break;
            }
        }
    }



    if (start >= 0 && start <= end)
    {
        for (int i = end; i >= start; --i)
        {
            fileContents.removeAt(i);
        }

        for (int i = 0; i < newLines.length(); ++i)
        {
            fileContents.insert(start + i, newLines.at(i));
        }
    }
    else
    {
        addError(tr("Failed to modify cities in MainDatabase.java"));
    }
}

void ParserThread::updateMainDatabaseJavaServices(QStringList &fileContents)
{
    updateMainDatabaseJavaServicesIDs(fileContents);
    updateMainDatabaseJavaServicesFilling(fileContents);
}

void ParserThread::updateMainDatabaseJavaServicesIDs(QStringList &fileContents)
{
    int maxLength = 0;

    for (int i = 0; i < mServices.length(); ++i)
    {
        int serviceLength = mServicesIDs.at(i).length();

        if (serviceLength > maxLength)
        {
            maxLength = serviceLength;
        }
    }



    QStringList newLines;

    for (int i = 0; i < mServices.length(); ++i)
    {
        newLines.append(QString("    public static final int SERVICE_ID_%1 = %2;").arg(mServicesIDs.at(i).toUpper(), -maxLength, QChar(' ')).arg(i + 1));
    }

    newLines.append("");

    for (int i = 0; i < mServices.length(); ++i)
    {
        newLines.append(QString("    public static final int SERVICE_%1 = 0x%2;").arg(mServicesIDs.at(i).toUpper() + "_MASK", -maxLength - 5, QChar(' ')).arg(1 << i, 8, 16, QChar('0')));
    }



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed().startsWith("public static final int SERVICE_ID_"))
        {
            if (start < 0)
            {
                start = i;
            }

            end = i;
        }
        else
        {
            if (end >= 0)
            {
                break;
            }
        }
    }



    if (start >= 0 && start <= end)
    {
        int newEnd = -1;

        for (int i = end + 1; i < fileContents.length(); ++i)
        {
            QString line = fileContents.at(i).trimmed();

            if (line.startsWith("public static final int SERVICE_") && line.contains("_MASK"))
            {
                newEnd = i;
            }
            else
            {
                if (newEnd >= 0)
                {
                    break;
                }
            }
        }

        end = newEnd;
    }



    if (start >= 0 && start <= end)
    {
        for (int i = end; i >= start; --i)
        {
            fileContents.removeAt(i);
        }

        for (int i = 0; i < newLines.length(); ++i)
        {
            fileContents.insert(start + i, newLines.at(i));
        }
    }
    else
    {
        addError(tr("Failed to modify services in MainDatabase.java"));
    }
}

void ParserThread::updateMainDatabaseJavaServicesFilling(QStringList &fileContents)
{
    int maxLength = 0;

    for (int i = 0; i < mServices.length(); ++i)
    {
        int serviceLength = mServicesIDs.at(i).length();

        if (serviceLength > maxLength)
        {
            maxLength = serviceLength;
        }
    }



    QStringList newLines;

    for (int i = 0; i < mServices.length(); ++i)
    {
        newLines.append(QString("        insertToTable(db, SERVICES_TABLE_NAME, SERVICES_COLUMNS, SERVICE_ID_%1 mContext.getResources().getString(R.string.service_%2));").arg(mServicesIDs.at(i).toUpper() + ",", -maxLength - 1, QChar(' ')).arg(mServicesIDs.at(i)));
    }



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed().startsWith("private void fillServicesTable("))
        {
            start = i + 2;

            break;
        }
    }



    if (start >= 0)
    {
        for (int i = start; i < fileContents.length(); ++i)
        {
            if (fileContents.at(i).trimmed() == "}")
            {
                end = i - 1;

                break;
            }
        }
    }



    if (start >= 0 && start <= end)
    {
        for (int i = end; i >= start; --i)
        {
            fileContents.removeAt(i);
        }

        for (int i = 0; i < newLines.length(); ++i)
        {
            fileContents.insert(start + i, newLines.at(i));
        }
    }
    else
    {
        addError(tr("Failed to modify services in MainDatabase.java"));
    }
}

void ParserThread::updateMainDatabaseJavaShops(QStringList &fileContents)
{
    updateMainDatabaseJavaShopsIDs(fileContents);
    updateMainDatabaseJavaShopsFilling(fileContents);
}

void ParserThread::updateMainDatabaseJavaShopsIDs(QStringList &fileContents)
{
    int maxLength = 0;

    for (int i = 0; i < mShops.length(); ++i)
    {
        int shopLength = mShopsIDs.at(i).length();

        if (shopLength > maxLength)
        {
            maxLength = shopLength;
        }
    }



    QStringList newLines;

    for (int i = 0; i < mShops.length(); ++i)
    {
        newLines.append(QString("    public static final int SHOP_ID_%1 = %2;").arg(mShopsIDs.at(i).toUpper(), -maxLength, QChar(' ')).arg(i + 1));
    }



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed().startsWith("public static final int SHOP_ID_"))
        {
            if (start < 0)
            {
                start = i;
            }

            end = i;
        }
        else
        {
            if (end >= 0)
            {
                break;
            }
        }
    }



    if (start >= 0 && start <= end)
    {
        for (int i = end; i >= start; --i)
        {
            fileContents.removeAt(i);
        }

        for (int i = 0; i < newLines.length(); ++i)
        {
            fileContents.insert(start + i, newLines.at(i));
        }
    }
    else
    {
        addError(tr("Failed to modify shops in MainDatabase.java"));
    }
}

void ParserThread::updateMainDatabaseJavaShopsFilling(QStringList &fileContents)
{
    QStringList newLines;

    for (int i = 0; i < mShops.length(); ++i)
    {
        if (i > 0)
        {
            newLines.append(" ");
        }

        const ShopInfo &shop = mShops.at(i);



        QStringList servicesLines;

        for (int i = 0; i < shop.services_set.length(); ++i)
        {
            QString service = shop.services_set.at(i);

            int index = mServices.indexOf(service);

            if (index >= 0)
            {
                QString serviceMask = "SERVICE_" + mServicesIDs.at(index).toUpper() + "_MASK";

                if (servicesLines.length() == 0)
                {
                    servicesLines.append("                " + serviceMask);
                }
                else
                {
                    servicesLines.append("                        " + serviceMask);
                }
            }
            else
            {
                addError(tr("Unknown service: \"%1\"").arg(service));
            }
        }



        int maxLength = 0;

        for (int i = 0; i < servicesLines.length(); ++i)
        {
            int lineLength = servicesLines.at(i).length();

            if (lineLength > maxLength)
            {
                maxLength = lineLength;
            }
        }



        for (int i = 0; i < servicesLines.length() - 1; ++i)
        {
            QString &line = servicesLines[i];

            while (line.length() < maxLength)
            {
                line.append(" ");
            }

            line.append(" |");
        }



        QStringList shopLines;

        shopLines.append("        insertToTable(db, SHOPS_TABLE_NAME, SHOPS_COLUMNS,");
        shopLines.append("                SHOP_ID_" + mShopsIDs.at(i).toUpper() + ",");
        shopLines.append("                CITY_ID_" + mCitiesIDs.at(shop.city_id - 1).toUpper() + ",");
        shopLines.append("                mContext.getResources().getString(R.string.shop_" + mShopsIDs.at(i) + "),");
        shopLines.append("                " + QString(shop.is_hypermarket ? "SHOP_HYPERMARKET" : "SHOP_SUPERMARKET") + ",");
        shopLines.append("                " + QString::number(shop.latitude,  'f', 12) + ",");
        shopLines.append("                " + QString::number(shop.longitude, 'f', 12) + ",");
        shopLines.append("                \"" + shop.phone + "\",");
        shopLines.append("                \"" + shop.work_hours + "\",");
        shopLines.append("                " + QString::number(shop.square) + ",");
        shopLines.append("                \"" + shop.opening_date.toString("dd.MM.yyyy") + "\",");
        shopLines.append("                " + QString::number(shop.parking_places) + ",");
        shopLines.append("                " + QString::number(shop.number_of_cashboxes) + ",");
        shopLines.append(servicesLines);
        shopLines.append("        );");



        maxLength = 0;

        for (int i = 1; i <= 13; ++i)
        {
            int lineLength = shopLines.at(i).length();

            if (lineLength > maxLength)
            {
                maxLength = lineLength;
            }
        }

        maxLength += 4 - (maxLength % 4);



        static QString columnNames[] = {
            "COLUMN_ID",
            "COLUMN_CITY_ID",
            "COLUMN_NAME",
            "COLUMN_IS_HYPERMARKET",
            "COLUMN_LATITUDE",
            "COLUMN_LONGITUDE",
            "COLUMN_PHONE",
            "COLUMN_WORK_HOURS",
            "COLUMN_SQUARE",
            "COLUMN_OPENING_DATE",
            "COLUMN_PARKING_PLACES",
            "COLUMN_NUMBER_OF_CASHBOXES",
            "COLUMN_SERVICES_SET"
        };

        for (int i = 1; i <= 13; ++i)
        {
            QString &line = shopLines[i];

            while (line.length() < maxLength)
            {
                line.append(" ");
            }

            line.append("// " + columnNames[i - 1]);
        }



        newLines.append(shopLines);
    }



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed().startsWith("private void fillShopsTable("))
        {
            start = i + 2;

            break;
        }
    }



    if (start >= 0)
    {
        for (int i = start; i < fileContents.length(); ++i)
        {
            if (fileContents.at(i).trimmed() == "}")
            {
                end = i - 1;

                break;
            }
        }
    }



    if (start >= 0 && start <= end)
    {
        for (int i = end; i >= start; --i)
        {
            fileContents.removeAt(i);
        }

        for (int i = 0; i < newLines.length(); ++i)
        {
            fileContents.insert(start + i, newLines.at(i));
        }
    }
    else
    {
        addError(tr("Failed to modify shops in MainDatabase.java"));
    }
}
