package auto.basic;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Environment;
import android.os.PowerManager;
import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;
import com.robotium.solo.SystemUtils;
import org.junit.After;
import org.junit.Before;

/**
 * Created by mm on 2017-11-17.
 */

public class BaseTest extends ActivityInstrumentationTestCase2 {
    private static final String MAIN_ACTIVITY_NAME = "com.test.aaa.xxx.FirstActivity";
    private static Class<?> launcherClass;
    static {
        try {
            launcherClass = Class.forName(MAIN_ACTIVITY_NAME);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public BaseTest() {
        super(launcherClass);
    }

    public Solo solo;
    public SystemUtils su;
    public int mobileVersion;
    public int index = 0;

    @Before
    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        su = new SystemUtils(getInstrumentation());
        solo.getConfig().screenshotFileType = Solo.Config.ScreenshotFileType.PNG;
        String SDPath = Environment.getExternalStorageDirectory() + "/";
        String screenshotsPath = SDPath + "QABlued/Screenshots/";
        solo.getConfig().screenshotSavePath = screenshotsPath;
        mobileVersion = (int)Double.parseDouble((android.os.Build.VERSION.RELEASE).substring(0, 1));
        if(mobileVersion >= 7)
            index = 1;
    }

    @After
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        Activity myActivity = getActivity();
        if (myActivity != null)
            myActivity.finish();
        super.tearDown();
    }

    //唤醒屏幕
    @SuppressWarnings("deprecation")
    public static void wakeUpAndUnlock(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        kl.disableKeyguard();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm
                .newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "log");
        wl.acquire();
        wl.release();
    }
}
