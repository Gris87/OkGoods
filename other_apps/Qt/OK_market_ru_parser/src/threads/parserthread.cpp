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

#define TOTAL_PROGRESS                       100
#define GET_PROJECT_DIR_PROGRESS             1
#define REQUEST_CITIES_AND_SERVICES_PROGRESS 5
#define REQUEST_SHOP_STEP_1_PROGRESS         25
#define REQUEST_SHOP_STEP_2_PROGRESS         95
#define GENERATE_IDS_PROGRESS                96
#define UPDATE_STRINGS_XML_PROGRESS          98
#define UPDATE_MAIN_DATABASE_JAVA_PROGRESS   100



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
    qDebug() << "Started";
    qDebug() << "===================================";
    qDebug() << "";



    CHECK_AND_CALL(getProjectDir());
    CHECK_AND_CALL(requestCitiesAndServices());
    CHECK_AND_CALL(requestShops());
    CHECK_AND_CALL(updateSourceCode());



    qDebug() << "";
    qDebug() << "===================================";
    qDebug() << "Done";
    qDebug() << "===================================";
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
        stream << fileContents.at(i) << "\n";
    }

    file.close();
}

bool ParserThread::getProjectDir()
{
    emit progressChanged(GET_PROJECT_DIR_PROGRESS, TOTAL_PROGRESS);



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



    qDebug() << "";
    qDebug() << "Project found in folder:" << mProjectDir;



    return true;
}

bool ParserThread::requestCitiesAndServices()
{
    emit progressChanged(REQUEST_CITIES_AND_SERVICES_PROGRESS, TOTAL_PROGRESS);



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
                CityInfo cityInfo;
                cityInfo.name = line.mid(14, index - 14);

                mCities.append(cityInfo);
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



    qDebug() << "";
    qDebug() << "Cities found:";

    for (int i = 0; i < mCities.length() && !mTerminated; ++i)
    {
        qDebug() << mCities.at(i).name;
    }



    qDebug() << "";
    qDebug() << "Services found:";

    for (int i = 0; i < mServices.length() && !mTerminated; ++i)
    {
        qDebug() << mServices.at(i);
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
        emit progressChanged(REQUEST_CITIES_AND_SERVICES_PROGRESS + (i + 1) * (REQUEST_SHOP_STEP_1_PROGRESS - REQUEST_CITIES_AND_SERVICES_PROGRESS) / mCities.length(), TOTAL_PROGRESS);

        CityInfo& cityInfo = mCities[i];



        cookies.clear();
        cookies.append(QNetworkCookie("BITRIX_SM_CURRENT_CITY", QUrl::toPercentEncoding(cityInfo.name)));

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

            if (line.contains("cityData"))
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

                                cityInfo.latitude  = latitude.toDouble();
                                cityInfo.longitude = longitude.toDouble();
                            }
                            else
                            {
                                addError(tr("Failed to get coordinates for city \"%1\"").arg(cityInfo.name));
                            }
                        }
                        else
                        {
                            addError(tr("Failed to get coordinates for city \"%1\"").arg(cityInfo.name));
                        }
                    }
                    else
                    {
                        addError(tr("Failed to get coordinates for city \"%1\"").arg(cityInfo.name));
                    }
                }
                else
                {
                    addError(tr("Failed to get coordinates for city \"%1\"").arg(cityInfo.name));
                }
            }
            else
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
                        addError(tr("Incorrect shop id \"%1\" in city \"%2\"").arg(shopId).arg(cityInfo.name));
                    }
                }
                else
                {
                    addError(tr("Failed to get shop ID in line: \"%1\"").arg(line));
                }
            }
        }

        if (mTerminated)
        {
            return false;
        }



        if (cityInfo.latitude == 0)
        {
            addError(tr("Incorrect latitude \"%1\" for city \"%2\"").arg(cityInfo.latitude).arg(cityInfo.name));
        }

        if (cityInfo.longitude == 0)
        {
            addError(tr("Incorrect longitude \"%1\" for city \"%2\"").arg(cityInfo.longitude).arg(cityInfo.name));
        }
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

        emit progressChanged(REQUEST_SHOP_STEP_1_PROGRESS + (i + 1) * (REQUEST_SHOP_STEP_2_PROGRESS - REQUEST_SHOP_STEP_1_PROGRESS) / mShops.length(), TOTAL_PROGRESS);



        if (previousCity != shop.city_id)
        {
            cookies.clear();
            cookies.append(QNetworkCookie("BITRIX_SM_CURRENT_CITY", QUrl::toPercentEncoding(mCities.at(shop.city_id - 1).name)));

            previousCity = shop.city_id;
        }



        QNetworkRequest request(QUrl("http://okmarket.ru/stores/" + QString::number(shop.id) + "/"));
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

                            if (shop.work_hours.compare("Круглосуточно", Qt::CaseInsensitive) == 0)
                            {
                                shop.work_hours = "0:00 - 24:00";
                            }
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
                                addError(tr("Unexpected property value \"%1\" for property \"%2\" in shop \"%3\" (%4). It is expected that square is specified in square meters").arg(propertyValue).arg(property).arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
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
                            addError(tr("Incorrect property \"%1\" in shop \"%2\" (%3)").arg(property).arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
                        }
                    }
                    else
                    {
                        addError(tr("Failed to read value for property \"%1\" in shop \"%2\" (%3)").arg(property).arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
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
                    addError(tr("Unknown property \"%1\" in shop \"%2\" (%3)").arg(property).arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
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
                                addError(tr("Failed to get coordinates for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
                            }
                        }
                        else
                        {
                            addError(tr("Failed to get coordinates for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
                        }
                    }
                    else
                    {
                        addError(tr("Failed to get coordinates for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
                    }
                }
                else
                {
                    addError(tr("Failed to get coordinates for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
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
                                addError(tr("Unexpected service \"%1\" for shop \"%2\" (%3)").arg(service).arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
                            }
                        }
                    }
                    else
                    if (line == "</div>")
                    {
                        break;
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
            addError(tr("Incorrect shop name in city \"%1\"").arg(mCities.at(shop.city_id - 1).name));
        }

        if (shop.is_hypermarket)
        {
            if (!shop.name.startsWith("Гипермаркет"))
            {
                addError(tr("Shop name \"%1\" (2) is not specifying that shop is hypermarket").arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
            }
        }
        else
        {
            if (!shop.name.startsWith("Супермаркет"))
            {
                addError(tr("Shop name \"%1\" (2) is not specifying that shop is supermarket").arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
            }
        }

        if (shop.latitude == 0)
        {
            addError(tr("Incorrect latitude \"%1\" for shop \"%2\" (%3)").arg(shop.latitude).arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
        }

        if (shop.longitude == 0)
        {
            addError(tr("Incorrect longitude \"%1\" for shop \"%2\" (%3)").arg(shop.longitude).arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
        }

        if (shop.phone == "")
        {
            addError(tr("Incorrect phone for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
        }

        if (shop.work_hours == "")
        {
            addError(tr("Incorrect work hours for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
        }

        if (shop.square == 0)
        {
            addError(tr("Incorrect square \"%1\" for shop \"%2\" (%3)").arg(shop.square).arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
        }

        if (!shop.opening_date.isValid())
        {
            addError(tr("Incorrect opening date for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
        }

        if ((shop.parking_places > 0) != (shop.services_set.contains("Парковка")))
        {
            if (shop.parking_places > 0)
            {
                addError(tr("Parking places \"%1\" specified for shop \"%2\" (%3) while parking service is not available").arg(shop.parking_places).arg(shop.name).arg(mCities.at(shop.city_id - 1).name));

                shop.services_set.append("Парковка");
            }
            else
            {
                addError(tr("Parking places is not specified for shop \"%1\" (%2) while parking service is available").arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
            }
        }

        if (shop.number_of_cashboxes == 0)
        {
            addError(tr("Incorrect number of cashboxes \"%1\" for shop \"%2\" (%3)").arg(shop.number_of_cashboxes).arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
        }

        if (shop.services_set.length() == 0)
        {
            addError(tr("Incorrect services set for shop \"%1\" (%2)").arg(shop.name).arg(mCities.at(shop.city_id - 1).name));
        }



        qDebug() << "";
        qDebug() << "id                  =" << shop.id;
        qDebug() << "city_id             =" << shop.city_id;
        qDebug() << "name                =" << shop.name;
        qDebug() << "is_hypermarket      =" << shop.is_hypermarket;
        qDebug() << "latitude            =" << shop.latitude;
        qDebug() << "longitude           =" << shop.longitude;
        qDebug() << "phone               =" << shop.phone;
        qDebug() << "work_hours          =" << shop.work_hours;
        qDebug() << "square              =" << shop.square;
        qDebug() << "opening_date        =" << shop.opening_date;
        qDebug() << "parking_places      =" << shop.parking_places;
        qDebug() << "number_of_cashboxes =" << shop.number_of_cashboxes;
        qDebug() << "services_set        =" << shop.services_set;
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
    emit progressChanged(GENERATE_IDS_PROGRESS, TOTAL_PROGRESS);



    for (int i = 0; i < mCities.length(); ++i)
    {
        QString city = mCities.at(i).name;
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
    emit progressChanged(UPDATE_STRINGS_XML_PROGRESS, TOTAL_PROGRESS);



    updateRussianStringsXml();
    updateEnglishStringsXml();
}

void ParserThread::updateRussianStringsXml()
{
    updateRussianStringsXmlCities();
    updateRussianStringsXmlServices();
    updateRussianStringsXmlShops();
}

void ParserThread::updateRussianStringsXmlCities()
{
    QStringList fileContents;

    fileContents.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    fileContents.append("<resources>");

    for (int i = 0; i < mCities.length(); ++i)
    {
        fileContents.append("    <string name=\"city_" + mCitiesIDs.at(i) + "\">" + precedeTranslations(mCities.at(i).name) + "</string>");
    }

    fileContents.append("</resources>");



    QFile file(mProjectDir + "/app/src/main/res/values-ru/strings_db_cities.xml");
    writeStringListToFile(fileContents, file);
}

void ParserThread::updateRussianStringsXmlServices()
{
    QStringList fileContents;

    fileContents.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    fileContents.append("<resources>");

    for (int i = 0; i < mServices.length(); ++i)
    {
        fileContents.append("    <string name=\"service_" + mServicesIDs.at(i) + "\">" + precedeTranslations(mServices.at(i)) + "</string>");
    }

    fileContents.append("</resources>");



    QFile file(mProjectDir + "/app/src/main/res/values-ru/strings_db_services.xml");
    writeStringListToFile(fileContents, file);
}

void ParserThread::updateRussianStringsXmlShops()
{

    QStringList fileContents;

    fileContents.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    fileContents.append("<resources>");

    for (int i = 0; i < mShops.length(); ++i)
    {
        fileContents.append("    <string name=\"shop_" + mShopsIDs.at(i) + "\">" + precedeTranslations(mShops.at(i).name) + "</string>");
    }

    fileContents.append("</resources>");



    QFile file(mProjectDir + "/app/src/main/res/values-ru/strings_db_shops.xml");
    writeStringListToFile(fileContents, file);
}

void ParserThread::updateEnglishStringsXml()
{
    updateEnglishStringsXmlCities();
    updateEnglishStringsXmlServices();
    updateEnglishStringsXmlShops();
}

void ParserThread::updateEnglishStringsXmlCities()
{
    QStringList fileContents;

    fileContents.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    fileContents.append("<resources>");

    for (int i = 0; i < mCities.length(); ++i)
    {
        QString city = mCities.at(i).name;

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

        fileContents.append("    <string name=\"city_" + mCitiesIDs.at(i) + "\">" + precedeTranslations(city) + "</string>");
    }

    fileContents.append("</resources>");



    QFile file(mProjectDir + "/app/src/main/res/values/strings_db_cities.xml");
    writeStringListToFile(fileContents, file);
}

void ParserThread::updateEnglishStringsXmlServices()
{
    QStringList fileContents;

    fileContents.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    fileContents.append("<resources>");

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

        fileContents.append("    <string name=\"service_" + mServicesIDs.at(i) + "\">" + precedeTranslations(service) + "</string>");
    }

    fileContents.append("</resources>");



    QFile file(mProjectDir + "/app/src/main/res/values/strings_db_services.xml");
    writeStringListToFile(fileContents, file);
}

void ParserThread::updateEnglishStringsXmlShops()
{
    QStringList fileContents;

    fileContents.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    fileContents.append("<resources>");

    for (int i = 0; i < mShops.length(); ++i)
    {
        QString shop = mShops.at(i).name;

        shop = shop.replace("Гипермаркет", "Hypermarket").replace("Супермаркет", "Supermarket").replace("О'КЕЙ", "OK").replace("О’КЕЙ", "OK")
                .replace("Москва", "Moscow").replace("Санкт-Петербург", "St. Petersburg").replace("Ростов-на-Дону", "Rostov-on-Don");

        shop = russianTransliteration(shop);

        fileContents.append("    <string name=\"shop_" + mShopsIDs.at(i) + "\">" + precedeTranslations(shop) + "</string>");
    }

    fileContents.append("</resources>");



    QFile file(mProjectDir + "/app/src/main/res/values/strings_db_shops.xml");
    writeStringListToFile(fileContents, file);
}

void ParserThread::updateMainDatabaseJava()
{
    emit progressChanged(UPDATE_MAIN_DATABASE_JAVA_PROGRESS, TOTAL_PROGRESS);



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

    newLines.append("");
    newLines.append("    // Autogenerated code. Please do not modify manually");
    newLines.append("");

    for (int i = 0; i < mCities.length(); ++i)
    {
        newLines.append(QString("    private static final int CITY_ID_%1 = %2;").arg(mCitiesIDs.at(i).toUpper(), -maxLength, QChar(' ')).arg(i + 1));
    }



    newLines.append("");
    newLines.append("    @SuppressWarnings(\"PublicStaticArrayField\")");
    newLines.append("    public static final String[] CITIES = {");
    newLines.append("              \"" + mCitiesIDs.at(0).toUpper() + "\"");

    for (int i = 1; i < mCities.length(); ++i)
    {
        newLines.append("            , \"" + mCitiesIDs.at(i).toUpper() + "\"");
    }

    newLines.append("    };");



    newLines.append("");
    newLines.append("    @SuppressWarnings(\"PublicStaticArrayField\")");
    newLines.append("    public static final GeoPoint[] CITIES_COORDS = {");
    newLines.append(QString("              new GeoPoint(%1, %2) // %3").arg(mCities.at(0).latitude, -16, 'f', 12, QChar('0')).arg(mCities.at(0).longitude, -16, 'f', 12, QChar('0')).arg(mCitiesIDs.at(0).toUpper()));

    for (int i = 1; i < mCities.length(); ++i)
    {
        newLines.append(QString("            , new GeoPoint(%1, %2) // %3").arg(mCities.at(i).latitude, -16, 'f', 12, QChar('0')).arg(mCities.at(i).longitude, -16, 'f', 12, QChar('0')).arg(mCitiesIDs.at(i).toUpper()));
    }

    newLines.append("    };");



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed() == "// AUTOGENERATED BLOCK CITY_ID [BEGIN]")
        {
            start = i + 1;

            break;
        }
    }



    if (start >= 0)
    {
        for (int i = start; i < fileContents.length(); ++i)
        {
            if (fileContents.at(i).trimmed() == "// AUTOGENERATED BLOCK CITY_ID [END]")
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

    newLines.append("");
    newLines.append("        // Autogenerated code. Please do not modify manually");
    newLines.append("");

    for (int i = 0; i < mCities.length(); ++i)
    {
        newLines.append(QString("        insertToTable(db, CITIES_TABLE_NAME, CITIES_COLUMNS, CITY_ID_%1 mContext.getResources().getString(R.string.city_%2));").arg(mCitiesIDs.at(i).toUpper() + ",", -maxLength - 1, QChar(' ')).arg(mCitiesIDs.at(i)));
    }



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed() == "// AUTOGENERATED BLOCK CITY_FILLING [BEGIN]")
        {
            start = i + 1;

            break;
        }
    }



    if (start >= 0)
    {
        for (int i = start; i < fileContents.length(); ++i)
        {
            if (fileContents.at(i).trimmed() == "// AUTOGENERATED BLOCK CITY_FILLING [END]")
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

    newLines.append("");
    newLines.append("    // Autogenerated code. Please do not modify manually");
    newLines.append("");

    for (int i = 0; i < mServices.length(); ++i)
    {
        newLines.append(QString("    public static final int SERVICE_%1 = 0x%2;").arg(mServicesIDs.at(i).toUpper() + "_MASK", -maxLength - 5, QChar(' ')).arg(1 << i, 8, 16, QChar('0')));
    }



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed() == "// AUTOGENERATED BLOCK SERVICE_ID [BEGIN]")
        {
            start = i + 1;

            break;
        }
    }



    if (start >= 0)
    {
        for (int i = start; i < fileContents.length(); ++i)
        {
            if (fileContents.at(i).trimmed() == "// AUTOGENERATED BLOCK SERVICE_ID [END]")
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

    newLines.append("");
    newLines.append("    // Autogenerated code. Please do not modify manually");
    newLines.append("");

    for (int i = 0; i < mShops.length(); ++i)
    {
        newLines.append(QString("    private static final int SHOP_ID_%1 = %2;").arg(mShopsIDs.at(i).toUpper(), -maxLength, QChar(' ')).arg(mShops.at(i).id));
    }



    int start = -1;
    int end   = -1;

    for (int i = 0; i < fileContents.length(); ++i)
    {
        if (fileContents.at(i).trimmed() == "// AUTOGENERATED BLOCK SHOP_ID [BEGIN]")
        {
            start = i + 1;

            break;
        }
    }



    if (start >= 0)
    {
        for (int i = start; i < fileContents.length(); ++i)
        {
            if (fileContents.at(i).trimmed() == "// AUTOGENERATED BLOCK SHOP_ID [END]")
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

void ParserThread::updateMainDatabaseJavaShopsFilling(QStringList &fileContents)
{
    QStringList newLines;

    newLines.append("");
    newLines.append("        // Autogenerated code. Please do not modify manually");
    newLines.append("");

    for (int i = 0; i < mShops.length(); ++i)
    {
        if (i > 0)
        {
            newLines.append("");
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
        if (fileContents.at(i).trimmed() == "// AUTOGENERATED BLOCK SHOP_FILLING [BEGIN]")
        {
            start = i + 1;

            break;
        }
    }



    if (start >= 0)
    {
        for (int i = start; i < fileContents.length(); ++i)
        {
            if (fileContents.at(i).trimmed() == "// AUTOGENERATED BLOCK SHOP_FILLING [END]")
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
