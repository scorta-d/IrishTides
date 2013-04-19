package dz.tide;

import android.content.Context;

public final class Configuration {
	private static String imageUrl;
	private static int requestTimeout;
	private static int bufferSize;
	private static String changeImageURLDialogTitle;
	private static String changeCoordinatesURLFormatDialogTitle;
	private static String urlPreferenceName;
	private static String imageCacheFileName;
	private static String coordinatesAcceptorURLFormat;
	private static String coordinatesAcceptorURLFormatPreferenceName;
	private static boolean mustLogTime;
	private static int vibroResponseDuration;
	private static String clickProcessedSuccessfullyMark;

	static {
		imageUrl = null;
		requestTimeout = 0;
		bufferSize = 0;
		changeImageURLDialogTitle = null;
		changeCoordinatesURLFormatDialogTitle = null;
		urlPreferenceName = null;
		imageCacheFileName = null;
		coordinatesAcceptorURLFormat = null;
		coordinatesAcceptorURLFormatPreferenceName = null;
		clickProcessedSuccessfullyMark = null;
		vibroResponseDuration = 0;
		mustLogTime = false;
	}

	public static final int getRequestTimeout(final Context context) {
		if (requestTimeout == 0) {
			requestTimeout = context.getResources().getInteger(
					R.integer.request_timeout) * 1000;
		}

		return requestTimeout;
	}

	public static final int getBufferSize(final Context context) {
		if (bufferSize == 0) {
			bufferSize = context.getResources().getInteger(
					R.integer.socket_buffer_size) * 1024;
		}

		return bufferSize;
	}

	public static final String getChangeImageURLDialogTitle(
			final Context context) {
		if (changeImageURLDialogTitle == null) {
			changeImageURLDialogTitle = context.getResources().getString(
					R.string.change_image_url_title);
		}

		return changeImageURLDialogTitle;
	}

	public static final String getChangeCoordinatesURLFormatDialogTitle(
			final Context context) {
		if (changeCoordinatesURLFormatDialogTitle == null) {
			changeCoordinatesURLFormatDialogTitle = context.getResources()
					.getString(R.string.change_coords_url_format_title);
		}

		return changeCoordinatesURLFormatDialogTitle;
	}

	public static final String getImageURL(final Context context) {
		if (imageUrl == null) {
			imageUrl = context.getResources().getString(
					R.string.image_provider_url);
		}

		return imageUrl;
	}

	public static final boolean mustLogTime(final Context context) {
		if (!mustLogTime) {
			mustLogTime = context.getResources().getBoolean(
					R.bool.must_log_time);
		}

		return mustLogTime;
	}

	public static final String getURLPreferenceName(final Context context) {
		if (urlPreferenceName == null) {
			urlPreferenceName = context.getResources().getString(
					R.string.url_preference_name);
		}

		return urlPreferenceName;
	}

	public static final String getImageCacheFileName(final Context context) {
		if (imageCacheFileName == null) {
			imageCacheFileName = context.getResources().getString(
					R.string.image_cache_file_name);
		}

		return imageCacheFileName;
	}

	public static final String getCoordinatesAcceptorURLFormat(
			final Context context) {
		if (coordinatesAcceptorURLFormat == null) {
			coordinatesAcceptorURLFormat = context.getResources().getString(
					R.string.coordinates_acceptor_url_format);
		}

		return coordinatesAcceptorURLFormat;
	}

	public static final String getCoordsURLFormatPreferenceName(
			final Context context) {
		if (coordinatesAcceptorURLFormatPreferenceName == null) {
			coordinatesAcceptorURLFormatPreferenceName = context.getResources()
					.getString(R.string.coordinates_url_format_preference_name);
		}

		return coordinatesAcceptorURLFormatPreferenceName;
	}

	public static final String getClickProcessedSuccessfullyMark(
			final Context context) {
		if (clickProcessedSuccessfullyMark == null) {
			clickProcessedSuccessfullyMark = context.getResources().getString(
					R.string.click_processed_successfully_mark);
		}

		return clickProcessedSuccessfullyMark;
	}

	public static final int getVibroResponseDuration(final Context context) {
		if (vibroResponseDuration == 0) {
			vibroResponseDuration = context.getResources().getInteger(
					R.integer.vibro_response_duration);
		}

		return vibroResponseDuration;
	}
}
