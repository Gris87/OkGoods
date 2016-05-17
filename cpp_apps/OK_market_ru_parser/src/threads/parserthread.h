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
    QString russianTransliteration(const QString &text);
    void readFileToStringList(QFile &file, QStringList &fileContents);
    void writeStringListToFile(const QStringList &fileContents, QFile &file);

    bool getProjectDir();
    bool requestCitiesAndServices();
    bool requestShops();
    bool updateSourceCode();
    void generateIDs();
    void updateStringsXml();
    void updateRussianStringsXml();
    void updateRussianStringsXmlCities(QStringList &fileContents);
    void updateRussianStringsXmlServices(QStringList &fileContents);
    void updateRussianStringsXmlShops(QStringList &fileContents);
    void updateEnglishStringsXml();
    void updateEnglishStringsXmlCities(QStringList &fileContents);
    void updateEnglishStringsXmlServices(QStringList &fileContents);
    void updateEnglishStringsXmlShops(QStringList &fileContents);
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
