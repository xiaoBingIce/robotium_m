package auto.basic;

import android.os.Environment;
import android.os.SystemClock;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.v4.view.ViewPager;
import android.test.InstrumentationTestCase;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.By;
import com.robotium.solo.Solo;
import com.robotium.solo.SystemUtils;
import com.robotium.solo.WebElement;

import java.util.ArrayList;
import java.util.List;

import auto.connect.GetFile;
import auto.connect.HttpJson;
import auto.Information;


public class TinyOperate extends InstrumentationTestCase {

    // 检查文本字符串 type:text,id.....bool为true，屏幕可以滚动

    /**
     * @param text 需要查找的字符串
     * @param bool true 查找的范围为当前显示的和隐藏的字符
     * @return
     */
    public boolean checkText(Solo solo, String text, boolean bool) {
        boolean cb = false;
        if (null == text || "" == text) {
            return cb;
        }
        long endTime = SystemClock.uptimeMillis() + Information.timeOut;
        while (SystemClock.uptimeMillis() < endTime) {
            cb = solo.searchText(text, !bool);
            if (cb)
                return cb;
            solo.sleep(500);
        }
        return cb;
    }

    // 点击、长点击 ,,,,,,,,,,,,,,name:操作名 type:id、text value:值
    public void clickMethod(Solo solo, String name, String type, String value, int index) {
        if (name.equals("点击")) {
            if (type.equals("text")) {
                checkText(solo, value, false);
                solo.clickOnText(value, index);
            }
            if (type.equals("id")) {
                solo.clickOnView(this.getViewMethod(solo, value, index));
            }
        } else if (name.equals("长点击")) {
            if (type.equals("text")) {
                checkText(solo, value, false);
                solo.clickLongOnText(value);
            }
            if (type.equals("id")) {
                solo.clickLongOnView(this.getViewMethod(solo, value, index));
            }
        }
        solo.sleep(500);
    }

    //长点击
    public void clickLongMethod(Solo solo, String type, String value, int time, int index){
        if(time < 1000)
            time = 1000;
        if (type.equals("text")) {
            checkText(solo, value, false);
            solo.clickLongOnText(value, time);
        }
        if (type.equals("id")) {
            View view = this.getViewMethod(solo, value, index);
            solo.clickLongOnView(view, time);
        }
        solo.sleep(500);
    }

    /**
     * 点击dialog上面的指定类型和位置的控件，在dialog弹出时调用
     * viewType:控件类型，比如：Button.class
     */
    public void ClickViewFromDialogByIndex(Solo solo, Class viewType, int index) {
        solo.sleep(2000);
        ArrayList<View> dialogViews = solo.getCurrentViews();
        ArrayList<View> typeViews = new ArrayList<View>();
        for (View currentTypeView : dialogViews) {
            if (viewType.isInstance(currentTypeView)) {
                typeViews.add(currentTypeView);
            }
        }
        if (dialogViews.size() > 0) {
            solo.clickOnView(typeViews.get(index));
        } else {
            return;
        }
    }

    // 输入字符串 type:text,id..... 方法1直接键入，方法2可以看到敲键盘的轨迹
    public void enterView(Solo solo, String stringId, int index, String text) {
        EditText et = (EditText) this.getViewMethod(solo, stringId, index);
        solo.clearEditText(et);
        solo.enterText(et, text);
    }

    public void enterView02(Solo solo, String stringId, int index, String text) {
        EditText et = (EditText) this.getViewMethod(solo, stringId, index);
        solo.clearEditText(et);
        solo.typeText(et, text);
    }

    // 判断一个按钮是否被选中
    public boolean getChecked(Solo solo, String type, String value) {
        CompoundButton view = null;
        if (type.equals("id")) {
            view = (CompoundButton) getViewMethod(solo, value, 0);
        } else if (type.equals("text")) {
            view = (CompoundButton) solo.getText(value);
        } else {
            return false;
        }
        boolean bool = view.isChecked();
        return bool;
    }

    // 判断一组开关按钮，是否正常工作 CheckBox RadioButton ToggleButton, buttonId:控件id；
    // id：进入该页面的id
    public boolean getCheckedMore(Solo solo, String buttonId[], String id) {
        int len = buttonId.length;
        int i = 0;
        boolean bool = true;
        Boolean checked[][] = new Boolean[len][2];
        this.clickMethod(solo, "点击", "id", id, 0); // 进入页面
        solo.sleep(1000);
        while (i < len) {
            CompoundButton view = (CompoundButton) getViewMethod(solo, buttonId[i], 0);
            checked[i][0] = view.isChecked();
            solo.sleep(500);
            solo.clickOnView(view);
            i++;
        }
        i = 0;
        solo.goBack();
        solo.sleep(500);
        this.clickMethod(solo, "点击", "id", id, 0); // 进入页面
        solo.sleep(1000);
        while (i < len) {
            CompoundButton view = (CompoundButton) getViewMethod(solo, buttonId[i], 0);
            checked[i][1] = view.isChecked();
            i++;
        }
        i = 0;
        while (i < len) {
            bool = (checked[i][0] ^ checked[i][1]) && bool;
            i++;
        }
        return bool;
    }

    // 获取空间底部中心的坐标
    public int[] getLocation(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int width = view.getWidth();
        int height = view.getHeight();
        location[0] += width / 2;
        location[1] += height;
        return location;
    }

    // 获取当前页面的Activity的字符串
    public String getCurrentActivityStr(Solo solo) {
        String str;
        if (solo.getCurrentActivity() == null)
            return "no activity!!";
        str = solo.getCurrentActivity().getLocalClassName().toString();
        return str;
    }

    /**
     * 获取控件上的文案
     *
     * @param stringId 控件id，，如果是Toast，传"Toast"
     * @param index    第几个控件
     * @return
     */
    public String getViewText(Solo solo, String stringId, int index) {
        String id;
        String str = "no find text!!";
        TextView view;
        if (stringId.equals("Toast")) {
            id = "android:id/message";
            index = 0;
        } else {
            id = stringId;
        }
//        view = (TextView) getViewMethod(solo, id, index);
        view = (TextView) solo.getView(id, index);
        str = view.getText().toString();

        return str;
    }

    /**
     * @param list：要加载的listview
     * @param count：count为加载的页数，如果count为0，则加载全部
     */
    public int getListViewCount(Solo solo, ListView list, int count) {
        solo.sleep(2000);
        int i = 0, newListCount = 0, listCount = 0;
        boolean bool = true;
        int[] location = this.getLocation(list);
        solo.scrollDown();
        listCount = list.getCount();
        if (count != 0)
            bool = false;
        while ((i < count) || bool) {
            solo.scrollToBottom();
            solo.scrollListToLine(list, listCount);
            while (solo.scrollDown()) {
                solo.scrollListToLine(list, listCount);
            }
            solo.sleep(500);
            solo.drag(location[0], location[0], location[1] - 10, location[1] - 100, 30);
            solo.sleep(3000);
            newListCount = list.getCount();
            if (newListCount == listCount) {
                break;
            } else {
                listCount = newListCount;
            }
            i++;
        }
        int num = list.getCount() - list.getHeaderViewsCount() - list.getFooterViewsCount();
        Log.i("T11", list.getCount()+ "+" + num);
        return num;
    }

    /**
     * 获得页面总某个id的控件数
     *
     * @param solo
     * @param strId：需要查找的控件
     * @return
     */
    public int getViews(Solo solo, String strId) {
        ArrayList<View> al = solo.getCurrentViews();
        int num = 0;
        if (!strId.equals("")) {
            int targetId = solo.getCurrentActivity().getResources()
                    .getIdentifier(strId, "id", Information.packageName);
            for (int t = 0; t < al.size(); t++) {
                if (al.get(t).getId() == targetId)
                    num++;
            }
        }
        return num;
    }

    // 获取view index:the index of the View. 0 if only one is available
    public View getViewMethod(Solo solo, String stringId, int index) {
        boolean bb = false;
        long endTime = SystemClock.uptimeMillis() + Information.timeOut;
        while (SystemClock.uptimeMillis() < endTime) {
            bb = waitOperate(solo, "id", stringId, true, 1000);
            if (bb){
                Log.i("T11", "find");
                break;
            }
            solo.sleep(500);
        }

        if (!bb)
            return null;
        View view = solo.getView(stringId, index);
        return view;
    }


    //获取ViewPager中id相同的view，StringId为控件id，VPId为ViewPager的id
    public View getViewInVP(Solo solo, String stringId, String VPId) {
        ViewPager vp = (ViewPager) solo.getView(VPId);
        int num = vp.getCurrentItem();
        View view = getViewMethod(solo, stringId, num);
        return view;
    }

    // 截屏PNG图片,name为图片的名称，q为图片的质量（100为原图，越小越不清晰）
    public void screenshot(Solo solo, String name, int q) {
        solo.sleep(1000);
        solo.takeScreenshot(name, q);
    }

    /**
     * @param wifi:  ture为打开，false为关闭
     * @param mobile
     */
    public void setaNetWork(SystemUtils su, boolean wifi, boolean mobile) {
        su.setMobileData(mobile);
        su.setWiFiData(wifi);
    }

    /**
     * 下拉
     *
     * @param idStr: 需要下拉的控件id
     */
    public void scollToUp(Solo solo, String idStr) {
        solo.sleep(500);
        int[] f = this.getLocation(solo.getView(idStr));
        int StepCount = (int) (Math.sqrt(Math.pow((f[1] * 3 / 4 - f[1] / 4), 2.0))) / 2;
        solo.drag(f[0], f[0], f[1] / 4, f[1] * 3 / 4, StepCount);
        solo.sleep(1000);
    }

    /**
     * @param type    id or text
     * @param value   需要等待的值
     * @param bool    true为可滑动，false为不可滑动
     * @param timeOut 超时时间
     * @return
     */
    public boolean waitOperate(Solo solo, String type, String value, boolean bool, int timeOut) {
        solo.sleep(1000);
        boolean getIt = false;
        if (type.equals("text")) {
            return solo.waitForText(value, 1, timeOut, bool);
        }
        if (type.equals("id")) {
            int targetId = 0;
            if (!value.equals("")) {
                targetId = solo.getCurrentActivity().getResources()
                        .getIdentifier(value, "id", "com.blued.international");
            }
            return solo.waitForView(targetId, 1, timeOut, bool);
        }
        if (type.equals("class")) {
            return solo.waitForView(value.getClass());
        }
        if (type.equals("dialogToOpen")) {
            return solo.waitForDialogToOpen(timeOut);
        }
        return getIt;
    }

    /**
     * 跨进程点击
     *
     * @param type  : id 、text
     * @param value : 点击的内容
     */
    public void OtherThreadClick(String type, String value) {
        UiDevice ud = UiDevice.getInstance(getInstrumentation());
//        UiDevice ud=UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        UiObject obj_3 = new UiObject(new UiSelector().text("退出"));
        BySelector bs;
        if ("id".equals(type)) {
            bs = android.support.test.uiautomator.By.res(value);
        } else if ("text".equals(type)) {
            bs = android.support.test.uiautomator.By.text(value);
        } else {
            return;
        }
        UiObject2 uob = ud.findObject(bs);
        uob.click();
    }

    // 方法待定     点击文字为text的UiObject对象
    public void PermissionDialog(String text) {
        UiDevice ud = UiDevice.getInstance(getInstrumentation());
        UiObject btnAllow = new UiObject(new UiSelector().text(text));
        try {
            btnAllow.click();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
    }


    //webView
    public String getURL(Solo solo) {
        return solo.getWebUrl();
    }

    public boolean waitWebelenent(Solo solo, String type, String value, int timeOut) {
        boolean bool = false;
        bool = solo.waitForWebElement(getBy(type, value), timeOut, true);
        return bool;
    }

    /**
     * @param type  : 获取By的方式
     * @param value ：对应的值
     */
    public By getBy(String type, String value) {
        By by = null;
        if (type.equals("id")) {
            by = By.id(value);
        }
        if (type.equals("name")) {
            by = By.name(value);
        }
        if (type.equals("xpath")) {
            by = By.xpath(value);
        }
        if (type.equals("tagName")) {
            by = By.tagName(value);
        }
        if (type.equals("textContent")) {
            by = By.textContent(value);
        }
        if (type.equals("className")) {
            by = By.className(value);
        }
        if (type.equals("cssSelector")) {
            by = By.cssSelector(value);
        }
        return by;
    }

    //text为要输入的文案
    public void enterWebElement(Solo solo, String type, String value, String text) {
        By by = getBy(type, value);
        solo.clearTextInWebElement(by);
        solo.enterTextInWebElement(by, text);
    }

    public void clickWebElement(Solo solo, String type, String value) {
        By by = getBy(type, value);
        solo.clickOnWebElement(by);
    }

    public List<WebElement> getWebElements(Solo solo, String type, String value) {
        return solo.getCurrentWebElements(getBy(type, value));
    }


}
