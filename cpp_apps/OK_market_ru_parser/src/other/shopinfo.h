#ifndef SHOPINFO_H
#define SHOPINFO_H

#include <QDate>
#include <QString>



struct ShopInfo
{
    ShopInfo();

    quint64     id;
    quint64     city_id;
    QString     name;
    bool        is_hyper_market;
    double      latitude;
    double      longitude;
    QString     phone;
    QString     work_hours;
    quint64     square;
    QDate       opening_date;
    quint64     parking_places;
    quint64     number_of_cashboxes;
    QStringList services_set;
};

#endif // SHOPINFO_H
