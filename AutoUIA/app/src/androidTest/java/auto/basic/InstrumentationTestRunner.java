package auto.basic;

import android.os.Bundle;
import android.os.Environment;

import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import auto.Information;

public class InstrumentationTestRunner extends android.test.InstrumentationTestRunner {

	private Writer mWriter;
	private XmlSerializer mTestSuiteSerializer;
	private long mTestStarted;


	public void onStart() {
		try {
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddkkmmss");
			String strTime = sdf.format(d);
			String xmlName = "android." + Information.in_version + ".i." + strTime + ".xml";
			// 如果被测的应用本身有读写sdcard权限的话级可以直接放在sdcard里面，否则机会失败，
			// 有测试应用源码的情况下是可以在AndroidManifest.xml里添加权限，当然所数情况下是没有源码的，
			// 只能放在被测应用的files目录里了，这个是不需要权限的
			String SDPath = Environment.getExternalStorageDirectory() + "/";
			String logPath = SDPath + "QABlued/";
			File file = new File(logPath);
			if (!file.exists()) {
				file.mkdir();
			}
			file = new File(logPath + "TestLog/");
			if (!file.exists()) {
				file.mkdir();
			}
			
			if (isSDCardAvaliable()) {
//				File resultFile = new File(getTestResultDir(getTargetContext()), xmlName);
//				startJUnitOutput(new FileWriter(resultFile));
				startJUnitOutput(new FileWriter(new File(file, xmlName)));
			} else {
				startJUnitOutput(new FileWriter(new File(getTargetContext().getFilesDir(), xmlName)));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		super.onStart();
	}
	
	void startJUnitOutput(Writer writer) {
		try {
			mWriter = writer;
			mTestSuiteSerializer = newSerializer(mWriter);
			mTestSuiteSerializer.startDocument(null, null);
			mTestSuiteSerializer.startTag(null, "testsuites");
			mTestSuiteSerializer.startTag(null, "testsuite");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private boolean isSDCardAvaliable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

//	private String getTestResultDir(Context context) {
//		String packageName = "/" + "robotium";
//		String filepath = context.getCacheDir().getPath() + packageName;
//		if (android.os.Build.VERSION.SDK_INT < 8) {
//			if (isSDCardAvaliable()) {
//				filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + packageName;
//			}
//		} else {
//			if (isSDCardAvaliable()) {
//				filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + packageName;
//			}
//		}
//		return filepath;
//	}

	private XmlSerializer newSerializer(Writer writer) {
		try {
			XmlPullParserFactory pf = XmlPullParserFactory.newInstance();
			XmlSerializer serializer = pf.newSerializer();
			serializer.setOutput(writer);
			return serializer;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void sendStatus(int resultCode, Bundle results) {
		super.sendStatus(resultCode, results);
		switch (resultCode) {
		case REPORT_VALUE_RESULT_ERROR:
		case REPORT_VALUE_RESULT_FAILURE:
		case REPORT_VALUE_RESULT_OK:
			try {
				recordTestResult(resultCode, results);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			break;
		case REPORT_VALUE_RESULT_START:
			recordTestStart(results);
		default:
			break;
		}
	}

	void recordTestStart(Bundle results) {
		mTestStarted = System.currentTimeMillis();
	}

	void recordTestResult(int resultCode, Bundle results) throws IOException {
		float time = (System.currentTimeMillis() - mTestStarted) / 1000.0f;
		String className = results.getString(REPORT_KEY_NAME_CLASS);
		String testMethod = results.getString(REPORT_KEY_NAME_TEST);
		String stack = results.getString(REPORT_KEY_STACK);
		int current = results.getInt(REPORT_KEY_NUM_CURRENT);
		int total = results.getInt(REPORT_KEY_NUM_TOTAL);
		mTestSuiteSerializer.startTag(null, "testcase");
		mTestSuiteSerializer.attribute(null, "classname", className);
		mTestSuiteSerializer.attribute(null, "name", testMethod);
		if (resultCode != REPORT_VALUE_RESULT_OK) {
			mTestSuiteSerializer.startTag(null, "failure");
			if (stack != null) {
				String reason = stack.substring(0, stack.indexOf('\n'));
				String message = "";
				int index = reason.indexOf(':');
				if (index > -1) {
					message = reason.substring(index + 1);
					reason = reason.substring(0, index);
				}
				mTestSuiteSerializer.attribute(null, "message", message);
				mTestSuiteSerializer.attribute(null, "type", reason);
				mTestSuiteSerializer.text(stack);
			}
			mTestSuiteSerializer.endTag(null, "failure");
		} else {
			mTestSuiteSerializer.attribute(null, "time", String.format("%.3f", time));
		}
		mTestSuiteSerializer.endTag(null, "testcase");
		if (current == total) {
			mTestSuiteSerializer.startTag(null, "system-out");
			mTestSuiteSerializer.endTag(null, "system-out");
			mTestSuiteSerializer.startTag(null, "system-err");
			mTestSuiteSerializer.endTag(null, "system-err");
			mTestSuiteSerializer.endTag(null, "testsuite");
			mTestSuiteSerializer.flush();
		}
	}

	@Override
	public void finish(int resultCode, Bundle results) {
		endTestSuites();
		super.finish(resultCode, results);
	}

	void endTestSuites() {
		try {
			mTestSuiteSerializer.endTag(null, "testsuites");
			mTestSuiteSerializer.endDocument();
			mTestSuiteSerializer.flush();
			mWriter.flush();
			mWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}