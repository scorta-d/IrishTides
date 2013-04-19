package dz.tide;

import java.io.InputStream;

public interface StreamProcessorCallback<TResult> {
	TResult processStream(final InputStream inputStream);
}
