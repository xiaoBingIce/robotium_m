package auto.connect;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GetFile {

	public String GetImageStr(String path) {         // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		InputStream in = null;
		byte[] data = null;
		File file = new File(path);
		if(!file.exists())
			return "no image!!";
		// 读取图片字节数组
		try {
			in = new FileInputStream(file);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
//		return  new String(android.util.Base64.encode(data, android.util.Base64.DEFAULT));  // 返回Base64编码过的字节数组字符串  
		return Base64.encodeToString(data, Base64.DEFAULT);
		
	}
	
	public String GetImageStr02(String path) {         // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		Bitmap bitmap = null;
		File file = new File(path);
		if(file.exists()){
			bitmap = BitmapFactory.decodeFile(path);
		}else{
			return "no image!!";
		}
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 40, bos);//参数100表示不压缩
	    byte[] bytes=bos.toByteArray();  
	    return Base64.encodeToString(bytes, Base64.DEFAULT);
	}
}
 