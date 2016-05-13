#ifndef PARSERTHREAD_H
#define PARSERTHREAD_H

#include <QThread>



class ParserThread : public QThread
{
    Q_OBJECT

public:
    explicit ParserThread(QObject *parent = 0);
    ~ParserThread();

    void stop();

protected:
    void run();

private:
    bool mTerminated;
};

#endif // PARSERTHREAD_H
