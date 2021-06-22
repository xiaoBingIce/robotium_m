
package auto.exe_cases;

import org.junit.Test;

import java.util.List;

import auto.Information;
import auto.basic.BaseTest;
import auto.handle_case.GetCases;

/**
 * Created by mm on 2017-11-17.
 */

public class TestOne extends BaseTest {

    public TestOne(){
        gc = new GetCases();
        String file = Information.filePath + "live/a.txt";
        cases = gc.getCases("---------", file);
        caseNum = cases.size();
        temp = null;
        i = 0;
    }

    @Test
    public void test01() throws Exception {
        i = 1;
        if(i>caseNum)
            return;
        temp = gc.operateCase(solo, cases, i-1);
    }
    @Test
    public void test02() throws Exception {
        i = 2;
        if(i>caseNum)
            return;
        temp = gc.operateCase(solo, cases, i-1);
    }
    @Test
    public void test03() throws Exception {
        i = 3;
        if(i>caseNum)
            return;
        temp = gc.operateCase(solo, cases, i-1);
    }

    private GetCases gc;
    private List<List<String>> cases;
    private int caseNum;
    private Object temp;
    private int i;
}

