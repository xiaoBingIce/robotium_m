package auto.connect;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpJson implements Runnable {

    public static String BASIC_URL;
	public Object entity;

	public HttpJson() {}

	public HttpJson(String BASIC_URL, Object entity) {
		this.BASIC_URL = BASIC_URL;
		this.entity = entity;
	}

	public void get(){
		String getURL = ".........?a=11&b=123";
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(getURL);
		try {
			HttpResponse response = httpClient.execute(httpget);

			//获取返回结果
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == HttpStatus.SC_OK){
				String responseStr = EntityUtils.toString(response.getEntity(), "utf-8");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void postLog() {

		HttpClient httpClient = new DefaultHttpClient();
		// 设置超时
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);

		HttpPost httpPost = new HttpPost(BASIC_URL);
		try {
			Gson gson = new Gson();
			String jsonStr = gson.toJson(entity);
			Log.i("TAGM", "jsonStr:" + jsonStr);
			StringEntity entity = new StringEntity(jsonStr,"utf-8");
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);

			//获取返回结果
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == HttpStatus.SC_OK){
				String responseStr = EntityUtils.toString(response.getEntity(), "utf-8");
				Log.i("TAGM", "responseStr:" + responseStr);
//				JSONObject jsonObj = new JSONObject(responseStr);
//				int code = jsonObj.getInt("code");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.postLog();
	}
}
