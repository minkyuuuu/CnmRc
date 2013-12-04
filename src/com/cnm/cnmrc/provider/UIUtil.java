package com.cnm.cnmrc.provider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

public class UIUtil {
	public static long getCurrentTime(final Context context) {
		return System.currentTimeMillis();
	}

	public static String splitUriPathWithExtension(String path) {
		Uri uri = Uri.parse(path);
		List<String> pathSegments = uri.getPathSegments();
		
		String pathWithExtension = null;
		pathWithExtension = pathSegments.get(pathSegments.size() - 1);
		return pathWithExtension;
	}

	public static String splitUriPathWithNoExtension(String path) {
		Uri uri = Uri.parse(path);
		List<String> pathSegments = uri.getPathSegments();
		
		String pathWithNoExtension;
		pathWithNoExtension = pathSegments.get(pathSegments.size() - 1);
		pathWithNoExtension = path.split("\\.")[0];
		return pathWithNoExtension;
	}
	
	public static String splitUuid(String id) {
		return id.split(":")[2];
	}

	// contents 저장될 기본 폴더
	public static String getBasePath() {
		File externalStorage = Environment.getExternalStorageDirectory();
		return externalStorage + "/SmartEdu2013pp/shared/dtext" + "/";
		//return "/mnt/sdcard" + "/SmartEdu2013pp/shared/dtext" + "/";
	}
	
	public static String getDtextBasePath(String dtextBasePath) {
		return getBasePath() + dtextBasePath + "/";
	}
	
	public static String getSectionBasePath(String dtextBasePath, String sectionBasePath) {
		return getDtextBasePath(dtextBasePath) + sectionBasePath + "/";
	}

	private static SimpleDateFormat annoDateFormat = null;
	/**
	 * 주어진 데이트를 yyyy.MM.dd 형태로 출력.
	 * @param date
	 * @param ctx
	 * @return
	 */
	public static String formatAnnotationDate(Date date, Context ctx) {
		if (annoDateFormat == null) {
			synchronized (UIUtil.class) {
				if (annoDateFormat == null) {
					annoDateFormat = new SimpleDateFormat("yyyy.MM.dd", ctx.getResources().getConfiguration().locale);
				}
			}
		}
		return annoDateFormat.format(date);
	}
}
