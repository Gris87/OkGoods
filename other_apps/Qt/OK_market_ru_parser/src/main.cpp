#include <QApplication>

#include "src/main/mainwindow.h"
#include "src/threads/threads.h"



int main(int argc, char *argv[])
{
    QApplication a(argc, argv);

    MainWindow *mainwindow = new MainWindow();
    mainwindow->show();



    int res = a.exec();



    delete mainwindow;

    Threads::waitAllThreads();

    return res;
}
