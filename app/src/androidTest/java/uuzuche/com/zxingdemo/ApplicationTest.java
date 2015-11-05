package uuzuche.com.zxingdemo;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
        Log.i("tab", System.currentTimeMillis() / 1000 + "s");
        assertEquals(4, 2 + 2);
    }

    public void test1() {
        Log.i("tab", "rrrr");
    }
}