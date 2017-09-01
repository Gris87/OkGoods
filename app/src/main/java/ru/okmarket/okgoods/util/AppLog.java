package ru.okmarket.okgoods.util;

import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;

import java.io.PrintWriter;
import java.io.StringWriter;

import ru.okmarket.okgoods.OkGoodsApplication;

@SuppressWarnings("unused")
public final class AppLog
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "AppLog";
    // endregion
    // endregion



    private AppLog()
    {
        // Nothing
    }

    public static int v(String tag, String msg)
    {
        return Log.v(tag, msg);
    }

    public static int v(String tag, String msg, Throwable tr)
    {
        return Log.v(tag, msg, tr);
    }

    public static int d(String tag, String msg)
    {
        return Log.d(tag, msg);
    }

    public static int d(String tag, String msg, Throwable tr)
    {
        return Log.d(tag, msg, tr);
    }

    public static int i(String tag, String msg)
    {
        return Log.i(tag, msg);
    }

    public static int i(String tag, String msg, Throwable tr)
    {
        return Log.i(tag, msg, tr);
    }

    public static int w(String tag, String msg)
    {
        return Log.w(tag, msg);
    }

    public static int w(String tag, String msg, Throwable tr)
    {
        OkGoodsApplication.sendToDefaultTracker(new HitBuilders.EventBuilder()
                .setCategory("Log")
                .setAction("Warning")
                .setLabel(tag + ": " + msg)
                .build());

        return Log.w(tag, msg, tr);
    }

    public static int w(String tag, Throwable tr)
    {
        StringWriter stringWriter = new StringWriter();
        PrintWriter  printWriter  = new PrintWriter(stringWriter);
        tr.printStackTrace(printWriter);

        OkGoodsApplication.sendToDefaultTracker(new HitBuilders.EventBuilder()
                .setCategory("Log")
                .setAction("Exception")
                .setLabel(stringWriter.toString())
                .build());

        return Log.w(tag, tr);
    }

    public static int e(String tag, String msg)
    {
        OkGoodsApplication.sendToDefaultTracker(new HitBuilders.EventBuilder()
                .setCategory("Log")
                .setAction("Error")
                .setLabel(tag + ": " + msg)
                .build());

        return Log.e(tag, msg);
    }

    public static int e(String tag, String msg, Throwable tr)
    {
        StringWriter stringWriter = new StringWriter();
        PrintWriter  printWriter  = new PrintWriter(stringWriter);
        tr.printStackTrace(printWriter);

        OkGoodsApplication.sendToDefaultTracker(new HitBuilders.EventBuilder()
                .setCategory("Log")
                .setAction("Exception")
                .setLabel(stringWriter.toString())
                .build());

        return Log.e(tag, msg, tr);
    }

    public static int wtf(String tag, String msg)
    {
        try
        {
            //noinspection ProhibitedExceptionThrown
            throw new Exception("Logged stacktrace");
        }
        catch (Exception ex)
        {
            return e(tag, msg, ex);
        }
    }

    public static int wtf(String tag, String msg, Throwable tr)
    {
        try
        {
            //noinspection ProhibitedExceptionThrown
            throw new Exception("Logged stacktrace");
        }
        catch (Exception ex)
        {
            return e(tag, msg + '\n' + Log.getStackTraceString(tr), ex);
        }
    }

    public static int wtf(String tag, Throwable tr)
    {
        try
        {
            //noinspection ProhibitedExceptionThrown
            throw new Exception("Logged stacktrace");
        }
        catch (Exception ex)
        {
            return e(tag, Log.getStackTraceString(tr), ex);
        }
    }

    public static String getStackTraceString(Throwable tr)
    {
        return Log.getStackTraceString(tr);
    }

    public static int println(int priority, String tag, String msg)
    {
        return Log.println(priority, tag, msg);
    }

    public static boolean isLoggable(String s, int i)
    {
        return Log.isLoggable(s, i);
    }
}
