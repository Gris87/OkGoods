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

void MainWindow::parserThreadFinished()
{
    if (mParserThread)
    {
        Threads::unregisterThread(mParserThread);

        stopThread();
    }
}

void MainWindow::parserThreadProgressChanged(int value, int maxValue)
{
    if (mParserThread)
    {
        ui->progressBar->setMaximum(maxValue);
        ui->progressBar->setValue(value);
    }
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
    ui->startButton->setIcon(QIcon(":/images/Stop.png"));

    mParserThread = new ParserThread();
    Threads::registerThread(mParserThread);

    QObject::connect(mParserThread, SIGNAL(progressChanged(int,int)), this, SLOT(parserThreadProgressChanged(int,int)), Qt::QueuedConnection);
    QObject::connect(mParserThread, SIGNAL(finished()),               this, SLOT(parserThreadFinished()),               Qt::QueuedConnection);

    mParserThread->start(QThread::TimeCriticalPriority);
}

void MainWindow::stopThread()
{
    Q_ASSERT(mParserThread != 0);

    mParserThread->blockSignals(true);
    mParserThread->stop();

    mParserThread = 0;

    ui->startButton->setText(tr("Start"));
    ui->startButton->setIcon(QIcon(":/images/Start.png"));
    ui->progressBar->setValue(0);
}
