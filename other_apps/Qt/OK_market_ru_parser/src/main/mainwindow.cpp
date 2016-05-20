#include "mainwindow.h"
#include "ui_mainwindow.h"

#include <QMessageBox>
#include <QSettings>

#include "src/threads/threads.h"



MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);

    mParserThread = 0;

    loadConfig();
}

MainWindow::~MainWindow()
{
    saveConfig();

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
        QString errors = mParserThread->getErrors();

        if (errors == "")
        {
            ui->logTextEdit->setPlainText(tr("Everything is OK"));
        }
        else
        {
            ui->logTextEdit->setPlainText(errors);
        }

        Threads::unregisterThread(mParserThread);

        ParserThread *thread = mParserThread;
        stopThread();
        delete thread;
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

    QString proxyHost = "";
    quint16 proxyPort = 0;

    if (ui->proxyGroupBox->isChecked())
    {
        proxyHost = ui->proxyHostEdit->text();

        if (proxyHost == "")
        {
            ui->proxyHostEdit->setFocus();
            QMessageBox::information(this, tr("Invalid proxy"), tr("Please specify proxy host"), QMessageBox::Ok);

            return;
        }

        if (ui->proxyPortEdit->text() == "")
        {
            ui->proxyPortEdit->setFocus();
            QMessageBox::information(this, tr("Invalid proxy"), tr("Please specify proxy port"), QMessageBox::Ok);

            return;
        }

        bool ok;

        proxyPort = ui->proxyPortEdit->text().toUShort(&ok);

        if (!ok)
        {
            ui->proxyPortEdit->setFocus();
            QMessageBox::information(this, tr("Invalid proxy"), tr("Proxy port is invalid"), QMessageBox::Ok);

            return;
        }
    }

    ui->startButton->setText(tr("Stop"));
    ui->startButton->setIcon(QIcon(":/images/Stop.png"));
    ui->logTextEdit->clear();

    mParserThread = new ParserThread(proxyHost, proxyPort);
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

void MainWindow::saveConfig()
{
    QSettings settings(QApplication::applicationDirPath() + "/settings.ini", QSettings::IniFormat);

    settings.beginGroup("General");
    settings.setValue("UseProxy",  ui->proxyGroupBox->isChecked());
    settings.setValue("ProxyHost", ui->proxyHostEdit->text());
    settings.setValue("ProxyPort", ui->proxyPortEdit->text());
    settings.endGroup();
}

void MainWindow::loadConfig()
{
    QSettings settings(QApplication::applicationDirPath() + "/settings.ini", QSettings::IniFormat);

    settings.beginGroup("General");
    ui->proxyGroupBox->setChecked(settings.value("UseProxy",  false).toBool());
    ui->proxyHostEdit->setText(   settings.value("ProxyHost", "").toString());
    ui->proxyPortEdit->setText(   settings.value("ProxyPort", "").toString());
    settings.endGroup();
}
