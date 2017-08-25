package ru.okmarket.okgoods;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@SuppressWarnings({"ClassWithoutConstructor", "PublicConstructor", "JUnitTestNG"})
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "ExampleInstrumentedTest";
    // endregion
    // endregion



    @Test
    public void useAppContext() throws Exception
    {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        Assert.assertEquals("WTF", "ru.okmarket.okgoods", appContext.getPackageName());
    }
}
