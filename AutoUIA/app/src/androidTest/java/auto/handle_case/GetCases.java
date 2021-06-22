package auto.handle_case;

import android.util.Log;
import android.widget.ListView;

import com.robotium.solo.Solo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import auto.basic.TinyOperate;

/**
 * Created by mm on 2017-11-17.
 */

public class GetCases {
    //操作，无返回结果
    public void operateNRe(Solo solo, String...handle){
        if(handle[0].equals("等待")){
            //参数 0-等待 1-等待的时长
            solo.sleep(Integer.parseInt(handle[1]));
        }else if(handle[0].equals("搜索")){
            //参数 0-搜索 1-text的值 2-boolean 是否滚动
            to.checkText(solo, handle[1], Boolean.parseBoolean(handle[2]));
        }else if(handle[0].equals("点击")){
            //参数 0-点击 1-id/text 2-id/text的值 3-控件index
            to.clickMethod(solo, handle[0], handle[1], handle[2], Integer.parseInt(handle[3]));
        }else if (handle[0].equals("长点击")){
            //参数 0-长点击 1-id/text 2-id/text的值 3-控件index
            to.clickLongMethod(solo, handle[1], handle[2], Integer.parseInt(handle[3]), Integer.parseInt(handle[4]));
        }else if(handle[0].equals("输入")){
            //0-输入 1-id的值 2-控件index 3-输入的内容
            to.enterView(solo, handle[1], Integer.parseInt(handle[2]), handle[3]);
        }else if(handle[0].equals("截屏")){
            //0-截屏 1-截图名 2-尺寸大小
            to.screenshot(solo, handle[1], Integer.parseInt(handle[2]));
        }else if(handle[0].equals("下拉")){
            //0-下拉 1-控件id
            to.scollToUp(solo, handle[1]);
        }else if(handle[0].equals("跨应用点击")){
            //0-跨应用点击 1-id/text 2-id/text的值
            to.OtherThreadClick(handle[1], handle[2]);
        }
    }
    //操作，有返回结果
    public String operateRe(Solo solo, String...handle){
        if(handle[0].equals("获取是否选中")){
            //0-是否选中 1-id/text 2-id/text的值
            boolean bool = to.getChecked(solo, handle[0], handle[1]);
            return String.valueOf(bool);
        }else if(handle[0].equals("获取activity")){
            return to.getCurrentActivityStr(solo);
        }else if(handle[0].equals("获取文本")){
            //0-获取文本 1-id的值 2-控件index
            return to.getViewText(solo, handle[1], Integer.parseInt(handle[2]));
        }else if(handle[0].equals("获取列表数")){
            //0-列表数 1-id的值 2-控件index 3-翻页数（0表示不限页）
            ListView list = (ListView)to.getViewMethod(solo, handle[1], Integer.parseInt(handle[2]));
            int count = to.getListViewCount(solo, list, Integer.parseInt(handle[3]));
            return String.valueOf(count);
        }else if(handle[0].equals("获取控件数")){
            //0-控件数 1-id的值
            int num = to.getViews(solo, handle[1]);
            return String.valueOf(num);
        }else if(handle[0].equals("查找控件")){
            //0-查找控件 1-id/text 2-id/text的值 3-是否需要滑动 4-超时时间
            boolean bool = to.waitOperate(solo, handle[1], handle[2], Boolean.parseBoolean(handle[3]), Integer.parseInt(handle[4]));
            return String.valueOf(bool);
        }
        return "error！！";
    }

    public String operate(Solo solo, String...handle){
        if(handle[0].equals("等待")){
            //参数 0-等待 1-等待的时长
            solo.sleep(Integer.parseInt(handle[1]));
            return "no return!";
        }else if(handle[0].equals("搜索")){
            //参数 0-搜索 1-text的值 2-boolean 是否滚动
            to.checkText(solo, handle[1], Boolean.parseBoolean(handle[2]));
            return "no return!";
        }else if(handle[0].equals("点击")){
            //参数 0-点击 1-id/text 2-id/text的值 3-控件index
            to.clickMethod(solo, handle[0], handle[1], handle[2], Integer.parseInt(handle[3]));
            return "no return!";
        }else if (handle[0].equals("长点击")){
            //参数 0-长点击 1-id/text 2-id/text的值 3-长按时间 4-控件index
            to.clickLongMethod(solo, handle[1], handle[2], Integer.parseInt(handle[3]), Integer.parseInt(handle[4]));
            return "no return!";
        }else if(handle[0].equals("输入")){
            //0-输入 1-id的值 2-控件index 3-输入的内容
            to.enterView(solo, handle[1], Integer.parseInt(handle[2]), handle[3]);
            return "no return!";
        }else if(handle[0].equals("截屏")){
            //0-截屏 1-截图名 2-尺寸大小
            to.screenshot(solo, handle[1], Integer.parseInt(handle[2]));
            return "no return!";
        }else if(handle[0].equals("下拉")){
            //0-下拉 1-控件id
            to.scollToUp(solo, handle[1]);
            return "no return!";
        }else if(handle[0].equals("跨应用点击")){
            //0-跨应用点击 1-id/text 2-id/text的值
            to.OtherThreadClick(handle[1], handle[2]);
            return "no return!";
        }else if(handle[0].equals("获取是否选中")){
            //0-获取是否选中 1-id/text 2-id/text的值
            boolean bool = to.getChecked(solo, handle[0], handle[1]);
            return String.valueOf(bool);
        }else if(handle[0].equals("获取activity")){
            return to.getCurrentActivityStr(solo);
        }else if(handle[0].equals("获取文本")){
            //0-获取文本 1-id的值 2-控件index
            return to.getViewText(solo, handle[1], Integer.parseInt(handle[2]));
        }else if(handle[0].equals("获取ListView数")){
            //0-获取ListView数 1-id的值 2-控件index 3-翻页数（0表示不限页）
            ListView list = (ListView)to.getViewMethod(solo, handle[1], Integer.parseInt(handle[2]));
            int count = to.getListViewCount(solo, list, Integer.parseInt(handle[3]));
            return String.valueOf(count);
        }else if(handle[0].equals("获取控件数")){
            //0-控件数 1-id的值
            int num = to.getViews(solo, handle[1]);
            return String.valueOf(num);
        }else if(handle[0].equals("查找控件")){
            //0-查找控件 1-id/text 2-id/text的值 3-是否需要滑动 4-超时时间
            Boolean bool = to.waitOperate(solo, handle[1], handle[2], Boolean.parseBoolean(handle[3]), Integer.parseInt(handle[4]));
            return String.valueOf(bool);
        }else{
            return "空操作！！";
        }
    }

    //获取cases组
    public List<List<String>> getCases(String divide, String file){
        List<List<String>> cases = new ArrayList();
        List<String> oneCase = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String str;
            while((str=br.readLine()) != null){
                if(str.contains(divide) || str==null || str.equals("")){
                    cases.add(oneCase);
                    oneCase = new ArrayList<>();
                }else{
                    oneCase.add(str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cases;
    }
    //获取case原指令
    public String[] getCaseOriginal(String caseLine){
        if(caseLine.equals("") || caseLine==null){
            return null;
        }
        return caseLine.split("-");
    }

    public String operateCase(Solo solo, List<List<String>> cases, int num){
        String res = null;
        List<String> oneCase = cases.get(num);
        if(oneCase == null){
            return null;
        }
        for(int j=0; j<oneCase.size(); j++){
            String temp;
            Log.i("T10", oneCase.get(j));
            String[]str = getCaseOriginal(oneCase.get(j));
            temp = operate(solo, str);
            if(res == null)
                res = temp;
        }
        return res;
    }

    private TinyOperate to = new TinyOperate();
}
