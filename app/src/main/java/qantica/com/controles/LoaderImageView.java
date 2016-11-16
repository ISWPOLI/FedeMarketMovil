package qantica.com.controles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import qantica.com.conf.RecursosRed;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;


public class LoaderImageView extends LinearLayout {

	private static final int COMPLETE = 0;
	private static final int FAILED = 1;

	private Context mContext;
	private Drawable mDrawable;
	private ProgressBar mSpinner;
	private ImageView mImage;

	/**
	 * This is used when creating the view in XML To have an image load in XML
	 * use the tag
	 * 'image="http://developer.android.com/images/dialog_buttons.png"'
	 * Replacing the url with your desired image Once you have instantiated the
	 * XML view you can call setImageDrawable(url) to change the image
	 * 
	 * @param context
	 * @param attrSet
	 */
	public LoaderImageView(final Context context, final AttributeSet attrSet) {
		super(context, attrSet);
		final String url = attrSet.getAttributeValue(null, "image");
		if (url != null) {
			instantiate(context, url, null);
		} else {
			instantiate(context, null, null);
		}
	}

	/**
	 * This is used when creating the view programatically Once you have
	 * instantiated the view you can call setImageDrawable(url) to change the
	 * image
	 * 
	 * @param context
	 *            the Activity context
	 * @param imageUrl
	 *            the Image URL you wish to load
	 */
	public LoaderImageView(final Context context, final String imageUrl,
			final String servlet) {
		super(context);
		instantiate(context, imageUrl, servlet);
	}

	/**
	 * First time loading of the LoaderImageView Sets up the LayoutParams of the
	 * view, you can change these to get the required effects you want
	 */
	private void instantiate(final Context context, final String imageUrl,
			final String servlet) {
		mContext = context;

		mImage = new ImageView(mContext);
		mImage.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		mSpinner = new ProgressBar(mContext);
		mSpinner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		mSpinner.setIndeterminate(true);

		addView(mSpinner);
		addView(mImage);

		if (imageUrl != null) {
			setImageDrawable(imageUrl, servlet);
		}
	}

	/**
	 * Set's the view's drawable, this uses the internet to retrieve the image
	 * don't forget to add the correct permissions to your manifest
	 * 
	 * @param imageUrl
	 *            the url of the image you wish to load
	 */
	public void setImageDrawable(final String imageUrl, final String servlet) {
		mDrawable = null;
		mSpinner.setVisibility(View.VISIBLE);
		mImage.setVisibility(View.GONE);
		new Thread() {
			public void run() {

				try {

					File file = new File(mContext.getCacheDir()
							.getAbsolutePath(), imageUrl);

					if (file.exists()) {
						mDrawable = Drawable.createFromPath(file
								.getAbsolutePath());
						imageLoadedHandler.sendEmptyMessage(COMPLETE);

					} else {

						mDrawable = getDrawableFromUrl(imageUrl, servlet);
						try {
							Bitmap bm = ((BitmapDrawable) mDrawable)
									.getBitmap();

							File _file = new File(mContext.getCacheDir()
									.getAbsolutePath(), imageUrl);
							OutputStream outStream = new FileOutputStream(_file);
							bm.compress(Bitmap.CompressFormat.PNG, 100,
									outStream);
							outStream.flush();
							outStream.close();
							imageLoadedHandler.sendEmptyMessage(COMPLETE);
						}

						catch (Exception e) {
							Log.i("fed", "error cargando una imagen "
									+ imageUrl);
						}
					}

				} catch (MalformedURLException e) {
					imageLoadedHandler.sendEmptyMessage(FAILED);
				} catch (IOException e) {
					imageLoadedHandler.sendEmptyMessage(FAILED);
				}
			};
		}.start();
	}

	/**
	 * Callback that is received once the image has been downloaded
	 */
	private final Handler imageLoadedHandler = new Handler(new Callback() {

		@SuppressLint("LongLogTag")
		public boolean handleMessage(Message msg) {
			switch (msg.what) {

			case COMPLETE:
				mImage.setImageDrawable(mDrawable);
				mImage.setVisibility(View.VISIBLE);
				mSpinner.setVisibility(View.GONE);
				break;
			case FAILED:
			default:
				Log.e("Error descargando la imagen",
						"Error descargando la imagen");
				mImage.setVisibility(View.VISIBLE);
				mSpinner.setVisibility(View.GONE);
				break;
			}
			return true;
		}
	});

	/**
	 * Pass in an image url to get a drawable object
	 * 
	 * @return a drawable object
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	private static Drawable getDrawableFromUrl(final String url,
			final String servlet) throws IOException, MalformedURLException {

		if (servlet != null) {

			return Drawable.createFromStream(
					((java.io.InputStream) new java.net.URL(servlet
							+ "?id_img=" + url).getContent()), "name");
		}

		return Drawable.createFromStream(
				((java.io.InputStream) new java.net.URL(servlet).getContent()),
				"name");
	}

}
