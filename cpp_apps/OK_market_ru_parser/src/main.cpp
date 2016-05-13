#include <QApplication>

#include "src/main/mainwindow.h"
#include "src/threads/threads.h"



int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    MainWindow w;
    w.show();

    int res = a.exec();

    Threads::waitAllThreads();

    return res;
}
