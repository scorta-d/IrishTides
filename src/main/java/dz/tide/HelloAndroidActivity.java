package dz.tide;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class HelloAndroidActivity extends Activity {
	private static String PREFS_NAME = "tides";
	private static String PREF_PLACE = "place";
	private static String PREF_XML = "xml";

	public static String TAG = "IrishTides";
	private ArrayList<String> mItemList = new ArrayList<String>();
	Pattern splitter = Pattern.compile("<!--", Pattern.LITERAL);
	private String mXml;
	private boolean mCorrectResponse;

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            If the activity is being re-initialized after previously being
	 *            shut down then this Bundle contains the data it most recently
	 *            supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it
	 *            is null.</b>
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			Log.i(TAG, "onCreate");
			setContentView(R.layout.main);
			mCorrectResponse = false;
			// mXml = testResponse();
			mXml = getResponse();
			SharedPreferences mPrefs = getSharedPreferences(PREFS_NAME, 0);
			if (mXml == null || mXml.trim().length() == 0) {
				mXml = mPrefs.getString(PREF_XML, "");
			} else {
				mCorrectResponse = true;
			}
			if (mXml != null && mXml.trim().length() > 0) {
				Log.i(TAG, mXml);
				getInfos(mXml);
				String place = mPrefs.getString(PREF_PLACE, "");
				if (place != null) {
					Spinner spinner = (Spinner) findViewById(R.id.place);
					if (spinner != null && mItemList != null) {
						int idx = mItemList.indexOf(place);
						if (idx > 0) {
							spinner.setSelection(idx);
						}
					}
				}
			}
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
	}

	protected void onPause() {
		super.onPause();
		SharedPreferences mPrefs = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor ed = mPrefs.edit();
		Spinner spinner = (Spinner) findViewById(R.id.place);
		if (spinner != null) {
			ed.putString(PREF_PLACE, (String) spinner.getSelectedItem());
		}

		if (mCorrectResponse) {
			if (mXml != null && mXml.trim().length() > 0) {
				ed.putString(PREF_XML, mXml);
			}
		}
		ed.commit();
	}

	List<TideInfo> getInfos(String content) {
		final List<TideInfo> list = new ArrayList<TideInfo>();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder b = dbf.newDocumentBuilder();
			Document doc = b.parse( //
					new ByteArrayInputStream(content.getBytes())//
					);
			NodeList childs = doc.getChildNodes();
			for (int i = 0; i < childs.getLength(); i++) {
				Node node = childs.item(i);
				String localName = node.getNodeName();
				if ("tbody".equals(localName)) {
					processTable(node, list);
				}
			}
			View spinnerView = findViewById(R.id.place);

			if (spinnerView instanceof Spinner) {
				Spinner spinner = (Spinner) spinnerView;
				// mItemList = new ArrayList<String>();
				spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						String place = mItemList.get(position);
						for (TideInfo info : list) {
							if (place != null && place.equals(info.place)) {
								((TextView) findViewById(R.id.firstLowTime))
										.setText(info.firstLowTime);
								((TextView) findViewById(R.id.firstLowDepth))
										.setText(info.firstLowDepth);
								((TextView) findViewById(R.id.firstHighTime))
										.setText(info.firstHighTime);
								((TextView) findViewById(R.id.firstHighDepth))
										.setText(info.firstHighDepth);
								((TextView) findViewById(R.id.secondLowTime))
										.setText(info.secondLowTime);
								((TextView) findViewById(R.id.secondLowDepth))
										.setText(info.secondLowDepth);
								((TextView) findViewById(R.id.secondHighTime))
										.setText(info.secondHighTime);
								((TextView) findViewById(R.id.secondHighDepth))
										.setText(info.secondHighDepth);

							}
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}
				});

				for (TideInfo info : list) {
					if (info.place != null) {
						mItemList.add(info.place);
					}
				}

				Collections.sort(mItemList);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>( //
						this, //
						android.R.layout.simple_spinner_item, //
						mItemList.toArray(new String[] {}) //
				);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner.setAdapter(adapter);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return list;
	}

	private void processTable(Node parent, List<TideInfo> list) {
		NodeList childs = parent.getChildNodes();
		TideInfo info = new TideInfo();
		for (int i = 0; i < childs.getLength(); i++) {
			Node node = childs.item(i);
			if ("tr".equals(node.getNodeName())) {
				RowKind kind = processRow(node, info);
				if (RowKind.HEIGHT_ROW.equals(kind)) {
					if (info.place != null) {
						list.add(info);
						info = new TideInfo();
					}
				}
			}
		}
	}

	private RowKind processRow(Node parent, TideInfo info) {
		NodeList childs = parent.getChildNodes();
		Node classNode = parent.getAttributes().getNamedItem("class");
		if (classNode == null) {
			return RowKind.HEAD_ROW;
		}
		String classValue = classNode.getNodeValue();

		Log.d(TAG, classValue);
		int idx = 0;
		RowKind kind = RowKind.TIME_ROW;
		if (classValue != null && classValue.contains("bottom")) {
			kind = RowKind.HEIGHT_ROW;
		}

		for (int i = 0; i < childs.getLength(); i++) {
			Node node = childs.item(i);
			if ("td".equals(node.getNodeName())) {
				if (RowKind.HEIGHT_ROW.equals(kind)) {
					// process heights
					if (idx == 0) {
						// nbsp - ignoring
					} else if (idx == 1) {
						info.firstLowDepth = getText(node);
					} else if (idx == 2) {
						info.firstHighDepth = getText(node);
					} else if (idx == 3) {
						info.secondLowDepth = getText(node);
					} else if (idx == 4) {
						info.secondHighDepth = getText(node);
					}
				} else {
					// process times
					if (idx == 0) {
						info.place = getText(node);
					} else if (idx == 1) {
						info.firstLowTime = getText(node);
					} else if (idx == 2) {
						info.firstHighTime = getText(node);
					} else if (idx == 3) {
						info.secondLowTime = getText(node);
					} else if (idx == 4) {
						info.secondHighTime = getText(node);
					}
				}
				idx++;
			}
		}
		return kind;
	}

	private String getText(Node node) {
		String result = "";
		NodeList childs = node.getChildNodes();
		for (int i = 0; i < childs.getLength(); i++) {
			Node t = childs.item(i);
			if ("#text".equals(t.getNodeName())) {
				result += t.getNodeValue();
			}
		}

		return result;
	}

	public String testResponse() {
		String s = "";
		InputStream is = getClass().getResourceAsStream("/tides");
		if (is != null) {
			BufferedReader in = new BufferedReader( //
					new InputStreamReader(is)//
			);
			String line = "";
			boolean head = true;
			char[] buf = new char[100000];
			boolean isbuf = true;
			try {
				if (isbuf) {
					int n = 0;
					while ((n = in.read(buf)) > 0) {
						line += new String(buf, 0, n);
					}

				} else {
					while ((line = in.readLine()) != null) {
						if (head) {
							if (line.contains("tides-table")) {
								s += line;
								head = false;
							} else {
								continue;
							}
						} else {
							s += line;
							// Log.d(TAG, " len = " + s.length());
						}
					}
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
		if (s.length() != 0) {
			s = TableData.extract(s);
		}
		return s;
	}

	String getResponse() {
		String result = "";
		URL oracle;
		BufferedReader in = null;
		try {
			String url = "http://www.irishtimes.com/news/weather/tides";
			oracle = new URL(url);
			in = new BufferedReader( //
					new InputStreamReader(oracle.openStream())//
			);
			
			char[] buf = new char[100000];
			int n = 0;

			while (0 < (n = in.read(buf))) {
				result += new String (buf, 0 , n);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
		}
		result = TableData.extract(result);
		return result;

	}
}
