#include "mainwindow.h"
#include "ui_mainwindow.h"

#include "src/threads/threads.h"



MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    mParserThread = 0;
}

MainWindow::~MainWindow()
{
    if (mParserThread)
    {
        stopThread();
    }

    delete ui;
}

void MainWindow::on_startButton_clicked()
{
    if (mParserThread)
    {
        stopThread();
    }
    else
    {
        startThread();
    }
}

void MainWindow::startThread()
{
    Q_ASSERT(mParserThread == 0);

    ui->startButton->setText(tr("Stop"));

    mParserThread = new ParserThread();
    Threads::registerThread(mParserThread);


    mParserThread->start(QThread::TimeCriticalPriority);
}

void MainWindow::stopThread()
{
    Q_ASSERT(mParserThread != 0);

    ui->startButton->setText(tr("Start"));

    mParserThread->blockSignals(true);
    mParserThread->stop();

    mParserThread = 0;
}
