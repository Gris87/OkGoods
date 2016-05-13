#ifndef PARSERTHREAD_H
#define PARSERTHREAD_H

#include <QThread>
#include <QStringList>



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

    bool        mTerminated;
    QStringList mErrors;

signals:
    void progressChanged(int value, int maxValue);
};

#endif // PARSERTHREAD_H
