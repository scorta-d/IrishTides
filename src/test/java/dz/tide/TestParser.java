package dz.tide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;
import android.util.Log;

public class TestParser extends TestCase {
	private String TAG = TestParser.class.getSimpleName();

	public void test() {
		String s = "";
		InputStream is = getClass().getResourceAsStream("/tides");
		if (is != null) {
			BufferedReader in = new BufferedReader( //
					new InputStreamReader(is)//
			);
			String line = "";
			try {
				while ((line = in.readLine()) != null) {
					s += line;
					Log.d(TAG, " len = " + s.length());
				}
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
				}
			}
			Log.d(TAG, " len = " + s.length());
			Log.d(TAG, " len = " + s.substring(0, 10));
		}
		if (s.length() == 0) {
			fail();
		}
		s = TableData.extract(s);
		Log.d(TAG, " len = " + s.length());
		Log.d(TAG, " len = " + s.substring(0, 10));
	}
}
