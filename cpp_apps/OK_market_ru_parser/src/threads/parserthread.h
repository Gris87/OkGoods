#ifndef PARSERTHREAD_H
#define PARSERTHREAD_H

#include <QThread>
#include <QStringList>

#include "src/other/shopinfo.h"



class ParserThread : public QThread
{
    Q_OBJECT

public:
    explicit ParserThread(QObject *parent = 0);
    ~ParserThread();

    QString getErrors() const;

    void stop();

protected:
    void run();

private:
    void addError(const QString& error);
    bool startPoint();
    bool getProjectDir();
    bool requestCitiesAndServices();
    bool requestShops();

    bool            mTerminated;
    QStringList     mErrors;
    QString         mProjectDir;
    QStringList     mCities;
    QStringList     mServices;
    QList<ShopInfo> mShops;

signals:
    void progressChanged(int value, int maxValue);
};

#endif // PARSERTHREAD_H
