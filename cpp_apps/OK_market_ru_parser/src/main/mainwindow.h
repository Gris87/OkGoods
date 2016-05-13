#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>

#include "src/threads/parserthread.h"



namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();

private:
    void startThread();
    void stopThread();

    Ui::MainWindow *ui;
    ParserThread   *mParserThread;

private slots:
    void on_startButton_clicked();
};

#endif // MAINWINDOW_H
