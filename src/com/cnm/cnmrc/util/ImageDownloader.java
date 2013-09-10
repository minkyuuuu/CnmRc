package com.cnm.cnmrc.util;

/**
 * 20110108 LinkedHashMap으로 LRU Cash 구현
 * 
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloader {
	private static Bitmap mLoadingImage;
	
	private static int ThreadCounter=0;
	private static final String DEBUG_TAG = "ImageDownloader";
	private static Context mContext = null;
	
	public void setContext(Context mContext) {
		ImageDownloader.mContext = mContext;
	}
	
	public void download(String url, ImageView imageView) {
		
		resetPurgeTimer();
        Bitmap bitmap = getBitmapFromCache(url);

        if (bitmap == null) {
	        	if (cancelPotentialDownload(url, imageView)) {
	    			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
	    			DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
	    			imageView.setImageDrawable(downloadedDrawable);
	    			task.execute(url);			// 작업 실행.
	    		}
        } else {
            cancelPotentialDownload(url, imageView);
            imageView.setImageBitmap(bitmap);
        }
        
	}
	
	public void download(String url, ImageView imageView, Bitmap lodingImage) {
		mLoadingImage=lodingImage;
		
		resetPurgeTimer();
        Bitmap bitmap = getBitmapFromCache(url);

        if (bitmap == null) {
	        	if (cancelPotentialDownload(url, imageView)) {
	    			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
	    			DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
	    			imageView.setImageDrawable(downloadedDrawable);
	    			task.execute(url);			// 작업 실행.
	    		}
        } else {
            cancelPotentialDownload(url, imageView);
            imageView.setImageBitmap(bitmap);
        }
        
        Log.d("ImageDownloader", "Create ImageDownloader!");
        
        /*
		
		if (cancelPotentialDownload(url, imageView)) {
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
			DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
			imageView.setImageDrawable(downloadedDrawable);
			task.execute(url);			// 작업 실행.
		}
		
		*/
	}

	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private String url;
		private String fileName;
		private final WeakReference<ImageView> imageViewReference;

		public BitmapDownloaderTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
		}

		@Override
		// Actual download method, run in the task thread
		protected Bitmap doInBackground(String... params) {
			// params comes from the execute() call: params[0] is the url.
			url = params[0];
			int lastPos = url.lastIndexOf("/");
			int dotPos = url.lastIndexOf(".");
			
//			String str = "[lastPos] : %d  <--->  [dotPos] : %d   <---> [url] : %s";
//			str = String.format(str, lastPos, dotPos, url);
//			Log.e("hwang",str);
			
			fileName = url.substring(lastPos, dotPos) + "list";
			
			//File mkDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), PACKGE_NAME);
			File mkDir = null;
			if (mContext != null) {
				mkDir = mContext.getExternalCacheDir();
			}			
			if (mkDir == null) {
				mkDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), Util.PACKGE_NAME);
			}

			if (!mkDir.exists())
				mkDir.mkdirs();
			
			File file = new File(mkDir.toString(), fileName);
			if ( !file.exists() ) {
				return downloadBitmap(url);
			}

			return BitmapFactory.decodeFile(file.toString(), null);
		}

		@Override
		// Once the image is downloaded, associates it to the imageView
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled()) {
				bitmap = null;
			}

			addBitmapToCache(url, bitmap);
			PictureSaveToBitmapFile(bitmap,fileName);
			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();
				BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
				if (this == bitmapDownloaderTask) {
					imageView.setImageBitmap(bitmap);
					ThreadCounter--;
				}
			}
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			if(ThreadCounter < 5)
				ThreadCounter++;
			else
				onCancelled();
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			
		}
	}


	// 다운로드 중 이미지 설정
	static class DownloadedDrawable extends BitmapDrawable {
		private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
			//super(Color.BLACK);
			super(mLoadingImage);
			
			bitmapDownloaderTaskReference =
				new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
		}

		public BitmapDownloaderTask getBitmapDownloaderTask() {
			return bitmapDownloaderTaskReference.get();
		}
	}

	//
	private static boolean cancelPotentialDownload(String url, ImageView imageView) {
	    BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

	    if (bitmapDownloaderTask != null) {
	        String bitmapUrl = bitmapDownloaderTask.url;
	        if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
	            bitmapDownloaderTask.cancel(true);
	        } else {
	            // The same URL is already being downloaded.
	            return false;
	        }
	    }
	    return true;
	}
	
	//
	private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
	    if (imageView != null) {
	        Drawable drawable = imageView.getDrawable();
	        if (drawable instanceof DownloadedDrawable) {
	            DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
	            return downloadedDrawable.getBitmapDownloaderTask();
	        }
	    }
	    return null;
	}

	
	
	// 이미지 다운로드
	public static Bitmap downloadBitmap(String url) {		

		final HttpClient client = AndroidHttpClient.newInstance("Android");		// 아파치 클라이언트 생성.
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);					// 결과를 받아옴.
			final int statusCode = response.getStatusLine().getStatusCode();	// 결과 상태 코드를 가져온다.
			if (statusCode != HttpStatus.SC_OK) { 								// 200 OK (HTTP/1.0 - RFC 1945) 확인한다.
				Log.w("ImageDownloader", "Error " + statusCode + " while retrieving bitmap from " + url); 		// 수신 실패
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();		// respone에서 inputStream을 받아 가져온다. 
					final Bitmap bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));		// inputStream을 Bitmap으로 변환.
					return bitmap;
				} finally {			// 종료 작업
					if (inputStream != null) {
						inputStream.close();  
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or IllegalStateException
			getRequest.abort();			// Request 작업 진행을 중단한다.
			Log.w("ImageDownloader", "Error while retrieving bitmap from " + url + e.toString());
		} finally {
			if ((client instanceof AndroidHttpClient)) {			// connection 종료.
				((AndroidHttpClient) client).close();
			}
		}
		return null;

	}




	//////////////////////    이미지 다운로드 처리 ///////////////////////
	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break;  // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

    
    /*
     * Cache-related fields and methods.
     * 
     * We use a hard and a soft cache. A soft reference cache is too aggressively cleared by the
     * Garbage Collector.
     */
    
    private static final int HARD_CACHE_CAPACITY = 10;
    private static final int DELAY_BEFORE_PURGE = 10 * 1000; // in milliseconds

    // Hard cache, with a fixed maximum capacity and a life duration
    private final HashMap<String, Bitmap> sHardBitmapCache =
        new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) {
            if (size() > HARD_CACHE_CAPACITY) {
                // Entries push-out of hard reference cache are transferred to soft reference cache
                sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
                return true;
            } else
                return false;
        }
    };

    // Soft cache for bitmaps kicked out of hard cache
    private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache =
        new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);

    private final Handler purgeHandler = new Handler();

    private final Runnable purger = new Runnable() {
        public void run() {
            clearCache();
        }
    };

    /**
     * Adds this bitmap to the cache.
     * @param bitmap The newly downloaded bitmap.
     */
    private void addBitmapToCache(String url, Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (sHardBitmapCache) {
                sHardBitmapCache.put(url, bitmap);
            }
        }
    }

    /**
     * @param url The URL of the image that will be retrieved from the cache.
     * @return The cached bitmap or null if it was not found.
     */
    private Bitmap getBitmapFromCache(String url) {
        // First try the hard reference cache
        synchronized (sHardBitmapCache) {
            final Bitmap bitmap = sHardBitmapCache.get(url);
            if (bitmap != null) {
                // Bitmap found in hard cache
                // Move element to first position, so that it is removed last
                sHardBitmapCache.remove(url);
                sHardBitmapCache.put(url, bitmap);
                return bitmap;
            }
        }

        // Then try the soft reference cache
        SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
        if (bitmapReference != null) {
            final Bitmap bitmap = bitmapReference.get();
            if (bitmap != null) {
                // Bitmap found in soft cache
                return bitmap;
            } else {
                // Soft reference has been Garbage Collected
                sSoftBitmapCache.remove(url);
            }
        }

        return null;
    }
 
    /**
     * Clears the image cache used internally to improve performance. Note that for memory
     * efficiency reasons, the cache will automatically be cleared after a certain inactivity delay.
     */
    public void clearCache() {
        sHardBitmapCache.clear();
        sSoftBitmapCache.clear();
    }

    /**
     * Allow a new delay before the automatic cache clear is done.
     */
    private void resetPurgeTimer() {
        purgeHandler.removeCallbacks(purger);
        purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
    }


    public static boolean PictureSaveToBitmapFile(Bitmap bmp, String fn )
	{
		if(fn == null || fn.length() < 1 )
			return false;

		//	      if( bmp == null )
		//	          return true;

		//	      File mkDir = new File(Environment.getDownloadCacheDirectory().getAbsolutePath(), PACKGE_NAME);
		//File mkDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), PACKGE_NAME);
		File mkDir = null;
		if (mContext != null) {
			mkDir = mContext.getExternalCacheDir();
		}			
		if (mkDir == null) {
			mkDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), Util.PACKGE_NAME);
		}

		if(!mkDir.exists())
			mkDir.mkdirs();

		File file = new File(mkDir.toString(), fn);
		if( file.exists() ) {
			Log.d(DEBUG_TAG, "Save file to "+file.toString());
			return true;
		}

		try {
			FileOutputStream fOut = new FileOutputStream(file);
			if (fOut != null && bmp != null) {
				bmp.compress(Bitmap.CompressFormat.PNG, 50, fOut);
				fOut.flush();
				fOut.close();
				return true;
			}

		} catch (FileNotFoundException e) {
			Log.e(DEBUG_TAG, e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

}
