package com.cnm.cnmrc.provider;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.cnm.cnmrc.provider.CnmRcContract.SearchWord;
import com.cnm.cnmrc.provider.CnmRcContract.TvReserving;
import com.cnm.cnmrc.provider.CnmRcContract.VodJjim;

public class TestProviderDatabase {
	//private static final String TAG = TestProviderDatabase.class.getSimpleName();
	private static final String TAG = "SmartEduViewerProvider";
	
//	public static final String DATABASE_PATH = "/data/data/" + "com.daouincube.android.dtext.viewer" + "/databases/";
	private static File externalStorage = Environment.getExternalStorageDirectory();
	private static final String DATABASE_NAME = externalStorage.getAbsolutePath() + "/" + "smartEduViewer.db";	
	private static Context mContext;
	private static Uri uri;
	
	public static void checkDatabase(Context context) {
		{
			 Boolean exist = databaseExist();
			 if(!exist) return;
		}
		
		{
			// delete database : smartEduViewer.db
			Uri uri = CnmRcContract.BASE_CONTENT_URI;
			mContext.getContentResolver().delete(uri, null, null);
		}
		
		{
			 Boolean exist = isLibraryTableNull();
			 if(exist) return;
		}
	}
	
	/**
	 * TestProviderDatabase.testUrl(getApplicationContext());
	 */
	public static void testUri(Context context) {
		mContext = context;
	
		/**
		 * insert 구문은 table name만 있는 한 가지 uri 타입만 사용된다.
		 */
		 
		if (isLibraryTableNull()) {
			insertUser();
		}
		
		/**
		 * update 구문
		 */
//		updateUserAllItems();
		
		/**
		 * delete 구문
		 */
//		deleteBooksAllItems();
//		deleteBooksItem();
		

		/**
		 * query 구문
		 */
//		queryUserLibrary();			// (사용자의) 서재 지정
		
		
	}
	
	/**
	 * query
	 */
	
	/**
	 * delete
	 */
//	private static int deleteBooksAllItems() {
//		return mContext.getContentResolver().delete(Books.CONTENT_URI, null, null);	
//	}
//	
//	private static int deleteBooksItem() {
//		String booksId = "2";
//		uri = CnmRcContract.Books.buildBooksId(booksId);
//		return mContext.getContentResolver().delete(uri, null, null);	
//	}
	

	/**
	 * update
	 */
	// 사용자, 책, Annotation Type 단위
//	private static int updateAnnotationByAnnoid() {
//		ContentValues values = new ContentValues();
//		values.put(Annotation.STATE, "deactive");
//		String userId = "aaa";
//		String bookUuid = "book_uuid_1";
//		String annoId = "anno_id_1";
//		uri = CnmRcContract.Annotation.buildAnnoId(userId, bookUuid, annoId);
//		int count = mContext.getContentResolver().update(uri, values, null, null);
//		return count;
//	}
//	
//	private static int updateBookshelfItem() {
//		ContentValues values = new ContentValues();
//		values.put(VodJjim.DATE, "그럴까 교과서");
//		String bookshelfId = "2";
//		Uri uri = CnmRcContract.VodJjim.buildBookshelfId(bookshelfId);
//		return mContext.getContentResolver().update(uri, values, null, null);	
//	}
//
//	private static int updateUserAllItems() {
//		ContentValues values = new ContentValues();
//		values.put(SearchWord.PASSWORD, "3333");
//		return mContext.getContentResolver().update(SearchWord.CONTENT_URI, values, null, null);	
//	}
	
	/**
	 * insert
	 */
		
	private static void insertUser() {
		ContentValues values = new ContentValues();
		values.put(SearchWord.SEARCHWORD_ID, "aaa");
		uri = mContext.getContentResolver().insert(SearchWord.CONTENT_URI, values);
//		
//		values.clear();
//		values.put(User.USER_ID, "bbb");
//		values.put(User.PASSWORD, "2222");
//		uri = mContext.getContentResolver().insert(User.CONTENT_URI, values);
	}
	
	
	public static boolean databaseExist()
	{
	    SQLiteDatabase checkDB = null;
	    try {
	        checkDB = SQLiteDatabase.openDatabase(DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY);
	        checkDB.close();
	        return true;
	    }
	    catch(SQLiteException e) {
	        // Log an info message stating database doesn't exist.
	    	Log.e(TAG, " database doesn't exist <---- ");
	    }
	     
	    return false;
	}
	
	public static boolean isLibraryTableNull()
	{
        Cursor cursor = mContext.getContentResolver().query(CnmRcContract.TvReserving.CONTENT_URI, null, null, null, null);
        int count = cursor.getCount();
        
        if (count == 0) return true;
        else return false;
	}
	
    /**
     * {@link com.cnm.cnmrc.provider.CnmRcContract.VodJjim}
     * query parameters.
     */
    public static interface BookshelfQuery {
    	int _TOKEN = 0x2;
    	
    	String[] PROJECTION = {
    			VodJjim.ASSET_ID,
    			VodJjim.TR_NAME,
    			VodJjim.DATE,
    	};
    	
    	int _ID = 0;
    	int TYPE = 1;
    	int TITLE = 2;
    	int CREATED = 3;
    	int LIBRARY_ID = 4;
    }
    
    /**
     * {@link com.cnm.cnmrc.provider.CnmRcContract.TvReserving}
     * query parameters.
     */
    public interface LibraryQuery {
    	int _TOKEN = 0x1;
    	
    	String[] PROJECTION = {
    			TvReserving.RESERVERINGDATE_ID,
    	};
    	
    	int RESERVERINGDATE_ID = 0;
    }
    
	
}
