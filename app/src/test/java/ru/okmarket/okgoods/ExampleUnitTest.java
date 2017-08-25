package ru.okmarket.okgoods;

import org.junit.Assert;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@SuppressWarnings({"PublicConstructor", "JUnitTestNG", "ClassWithoutConstructor"})
public class ExampleUnitTest
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "ExampleUnitTest";
    // endregion
    // endregion



    @Test
    public void addition_isCorrect() throws Exception
    {
        Assert.assertEquals("WTF", 4, 2 + 2);
    }
}
