#ifndef PARSERTHREAD_H
#define PARSERTHREAD_H

#include <QThread>
#include <QStringList>
#include <QFile>

#include "src/other/shopinfo.h"



class ParserThread : public QThread
{
    Q_OBJECT

public:
    explicit ParserThread(const QString &proxyHost = "", quint16 proxyPort = 0, QObject *parent = 0);
    ~ParserThread();

    QString getErrors() const;

    void stop();

protected:
    void run();

private:
    void addError(const QString& error);
    QString removeLetters(const QString &text);
    bool getProjectDir();
    bool requestCitiesAndServices();
    bool requestShops();
    bool updateSourceCode();
    QString russianTransliteration(const QString &text);
    void generateIDs();
    void readFileToStringList(QFile &file, QStringList &fileContents);
    void writeStringListToFile(const QStringList &fileContents, QFile &file);
    void updateStringsXml();
    void updateRussianStringsXml();
    void updateEnglishStringsXml();
    void updateMainDatabaseJava();
    void updateMainDatabaseJavaCities(QStringList &fileContents);
    void updateMainDatabaseJavaServices(QStringList &fileContents);
    void updateMainDatabaseJavaShops(QStringList &fileContents);

    bool            mTerminated;
    QString         mProxyHost;
    quint16         mProxyPort;
    QStringList     mErrors;
    QString         mProjectDir;
    QStringList     mCities;
    QStringList     mServices;
    QList<ShopInfo> mShops;
    QStringList     mCitiesIDs;
    QStringList     mServicesIDs;
    QStringList     mShopsIDs;

signals:
    void progressChanged(int value, int maxValue);
};

#endif // PARSERTHREAD_H
