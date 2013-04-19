package dz.tide;

import static dz.tide.HelloAndroidActivity.TAG;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class TableData {

	static String getResponse() {
		String result = "";
		URL oracle;
		BufferedReader in = null;
		try {
			// String url = "http://irishtides.bshellz.net/tides.php";
			// String url = "http://irishtides.comeze.com";
			String url = "http://www.irishtimes.com/weather/tides.html";
			oracle = new URL(url);
			in = new BufferedReader( //
					new InputStreamReader(oracle.openStream())//
			);

			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				result += inputLine;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		result = extract(result);
		return result;
	}

	public static String extract(String in) {
		String result = in;
		in = in.replaceAll("\r", "");
		in = in.replaceAll("\n", "");
		in = in.replaceAll("\t", "");

		Pattern p = Pattern.compile( //
				".+?<table class=\"tides-table\" width=\"100%\">(.+)</table>.+?" //
				);
		Matcher m = p.matcher(in);
		if (m.matches()) {
			result = m.group(1);
			Log.d(TAG, "data were found.");
		} else {
			result = "";
		}
		return result;
	}

	public static void main(String[] argv) {
		System.out.println(getResponse());

	}

}
