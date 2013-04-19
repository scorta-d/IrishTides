package dz.tide;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;
import android.util.Log;

public final class Networking {
	private static final String TAG = "Networking";
	private static HttpClient httpClient;

	public static final <TResult> TResult processStreamWithCallback(
			final String url,
			final StreamProcessorCallback<TResult> streamCallback) {
		URL urlObject = convertStringToURL(url);

		TResult result = null;
		if (urlObject != null) {
			result = processStreamWithCallback(urlObject, streamCallback);
		}

		return result;
	}

	public static final <TResult> TResult processStreamWithCallback(
			final URL url, final StreamProcessorCallback<TResult> streamCallback) {
		if (url == null) {
			Log.w(TAG, "Stream URL is null");
			return null;
		}

		HttpURLConnection connection = null;
		TResult result = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
			if (connection != null) {
				Log.d(TAG,
						String.format("Connection with \'%s\' is established",
								url.toString()));
				final BufferedInputStream inStream = new BufferedInputStream(
						connection.getInputStream());
				if (streamCallback != null) {
					result = streamCallback.processStream(inStream);
				}
			} else {
				Log.e(TAG, "URL Connection is null");
			}
		} catch (IOException e) {
			Log.e(TAG,
					String.format("Error accessing stream: %s", e.getMessage()));
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
				Log.d(TAG, String.format(
						"Connection with \'%s\' is closed successfully",
						url.toString()));
			}
		}

		return result;
	}

	public static final boolean sendGetRequest(final Context context,
			final String url, final RequestCallback callback) {
		boolean result = false;
		if (url == null) {
			Log.w(TAG, "URL string is null");
			return result;
		}

		final URI urlObject = convertStringToURI(url);
		if (urlObject != null) {
			final HttpClient httpClient = instantiateHttpClient(context);
			final HttpGet getRequest = new HttpGet(urlObject);
			try {
				final HttpResponse httpResponse = httpClient
						.execute(getRequest);
				httpResponse.getEntity().consumeContent();
				result = httpResponse.getStatusLine().getStatusCode() == 200; // OK
				if (callback != null) {
					final String successHeader = "ok";
					if (httpResponse.getFirstHeader(successHeader) != null) {
						callback.clickProcessedSuccessfully();
					}
				}
				Log.i(TAG, "Send GET request to " + url);
			} catch (ClientProtocolException e) {
				Log.e(TAG, String.format("Bad protocol: %s", e.getMessage()));
				e.printStackTrace();
			} catch (IOException e) {
				Log.e(TAG,
						String.format("Unable to send GET request: %s",
								e.getMessage()));
				e.printStackTrace();
			}
		}

		return result;
	}

	private static final HttpClient instantiateHttpClient(final Context context) {
		if (httpClient == null) {
			final HttpParams params = new BasicHttpParams();

			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, "UTF-8");

			HttpConnectionParams.setStaleCheckingEnabled(params, false);

			final int connectionTimeout = Configuration
					.getRequestTimeout(context);
			HttpConnectionParams
					.setConnectionTimeout(params, connectionTimeout);
			HttpConnectionParams.setSoTimeout(params, connectionTimeout);

			HttpConnectionParams.setSocketBufferSize(params,
					Configuration.getBufferSize(context));

			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			schemeRegistry.register(new Scheme("https", SSLSocketFactory
					.getSocketFactory(), 443));

			ClientConnectionManager manager = new ThreadSafeClientConnManager(
					params, schemeRegistry);
			httpClient = new DefaultHttpClient(manager, params);
		}

		return httpClient;
	}

	private static final URL convertStringToURL(final String stringURL) {
		if (stringURL == null) {
			return null;
		}

		URL url = null;
		try {
			url = new URL(stringURL);
		} catch (MalformedURLException e) {
			Log.e(TAG, String.format("Bad URL: %s", e.getMessage()));
			e.printStackTrace();
		}

		return url;
	}

	private static final URI convertStringToURI(final String stringURI) {
		if (stringURI == null) {
			return null;
		}

		URI result = null;
		try {
			result = URI.create(stringURI);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, String.format("Bad URI argument: %s", e.getMessage()));
		}

		return result;
	}
}
