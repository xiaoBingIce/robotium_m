package auto.basic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.robotium.solo.Solo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandMenthod {

	// 获取当前时间
	@SuppressLint("SimpleDateFormat")
	public String getDate() {
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-kk-mm");
		String strTime = sdf.format(d);
		return strTime;
	}

	// 执行CMD命令
	public void myCMD(String str) {
		try {
			Runtime.getRuntime().exec(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] getStr(String str, String middle) {
		String[] s = str.split(middle);
		return s;
	}

	/**
	 * @param file    cases文件
	 * @return        返回文件中的cases
	 */
	public List<List> getCases(String file){
		List<List> cs = new ArrayList<>();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			List<String> co = new ArrayList<>();
			String line;
			while((line=br.readLine()) != null){
				if(!line.contains("-----")){
					co.add(line);
				}else{
					cs.add(co);
					co.clear();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cs;
	}

	// 唤醒屏幕
	@SuppressWarnings("deprecation")
	@SuppressLint("Wakelock")
	public void WakeUp(Solo solo) {
		PowerManager pm;
		WakeLock mWakeLock;
		pm = (PowerManager) solo.getCurrentActivity().getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,
				"SimpleTimer");

		mWakeLock.acquire();

		solo.sleep(2000);
		solo.unlockScreen();
		solo.sleep(3000);
	}

}
