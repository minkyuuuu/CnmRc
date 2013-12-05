package com.cnm.cnmrc.provider;

import java.util.Arrays;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.cnm.cnmrc.provider.CnmRcContract.SearchWord;
import com.cnm.cnmrc.provider.CnmRcContract.TvReserving;
import com.cnm.cnmrc.provider.CnmRcContract.VodJjim;
import com.cnm.cnmrc.provider.CnmRcOpenHelper.Tables;

/**
 * Provider that stores {@link CnmRcContract} data. 
 * 
 * Naming convention
 * 1) Class names should be nouns in UpperCamelCase, with the first letter of every word capitalised. Use whole words — avoid acronyms and abbreviations 
 * 2) Methods should be verbs in lowerCamelCase; that is, with the first letter lowercase and the first letters of subsequent words in uppercase.
 * 3) Local variables, instance variables, and class variables are also written in lowerCamelCase. Variable names should not start with underscore (_) or dollar sign ($) characters, even though both are allowed. Variable names should be short yet meaningful. The choice of a variable name should be mnemonic. 
 * 4) Constants should be written in uppercase characters separated by underscores.
 * 
 * Uri 상수 정의 및 등록
 * 1) Url 상수 정의 (SmartEduViewerProvider)
 * 2) UriMatcher.addUrl() (SmartEduViewerProvider)
 * 
 */
public class CnmRcProvider extends ContentProvider{
	private static final String TAGS = CnmRcProvider.class.getSimpleName();
	
	private CnmRcOpenHelper mOpenHelper;
	
	private static final UriMatcher sUriMatcher = buildUriMatcher();
	
	// Url 상수 정의
	private static final int SEARCHWORD = 100;
	private static final int SEARCHWORD_ID = 101;
	
	private static final int TVRESERVING = 200;
	private static final int TVRESERVING_ID = 201;
	
	private static final int VODJJIM = 300;
	private static final int VODJJIM_ID = 301;
	
	
	/**
     * Build and return a {@link UriMatcher} that catches all {@link Uri}
     * variations supported by this {@link ContentProvider}.
     */
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CnmRcContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "searchword", SEARCHWORD);
        matcher.addURI(authority, "searchword/*", SEARCHWORD_ID);
        
        matcher.addURI(authority, "tvreserving", TVRESERVING);
        matcher.addURI(authority, "tvreserving/*", TVRESERVING_ID);
        
        matcher.addURI(authority, "vodjjim", VODJJIM);
        matcher.addURI(authority, "vodjjim/*", VODJJIM_ID);
        
        return matcher;
    }

	@Override
	public boolean onCreate() {
		mOpenHelper = new CnmRcOpenHelper(getContext());
		
		// provider 실행시 database 존재 체크시 필요
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        return (db == null) ? false : true;
	}
	
    private void deleteDatabase() {
        // TODO: wait for content provider operations to finish, then tear down
        mOpenHelper.close();
        Context context = getContext();
        CnmRcOpenHelper.deleteDatabase(context);
        mOpenHelper = new CnmRcOpenHelper(getContext());
    }
    
	@Override
	public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SEARCHWORD:
            	return SearchWord.CONTENT_TYPE;
            case SEARCHWORD_ID:
                return SearchWord.CONTENT_ITEM_TYPE;
                
            case TVRESERVING:
            	return TvReserving.CONTENT_TYPE;
            case TVRESERVING_ID:
            	return TvReserving.CONTENT_ITEM_TYPE;
                
            case VODJJIM:
            	return VodJjim.CONTENT_TYPE;
            case VODJJIM_ID:
            	return VodJjim.CONTENT_ITEM_TYPE;
            	
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.v(TAGS, "query(uri=" + uri + ", proj=" + Arrays.toString(projection) + ")");
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final SelectionBuilder builder = buildExpandedSelection(uri);
        
        return builder.where(selection, selectionArgs).query(db, projection, sortOrder);
	}

	/** {@inheritDoc} */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAGS, "insert(uri=" + uri + ", values=" + values.toString() + ")");
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final SelectionBuilder builder = buildSimpleSelection(uri);
	    
		long rowId = builder.insert(db, values);
	    if (rowId > -1) {
	        Uri resultUri = ContentUris.withAppendedId(uri, rowId);
	        getContext().getContentResolver().notifyChange(uri, null);	// notify 경우는 더 체크...
	        return resultUri;
	    }
	    
		return null;
	}
	
	/** {@inheritDoc} */
	@Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    	Log.d(TAGS, "update(uri=" + uri + ", values=" + values.toString() + ")");
    	final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        
        int retVal = builder.where(selection, selectionArgs).update(db, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
    }

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAGS, "delete(uri=" + uri + ")");
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        
        int retVal = builder.where(selection, selectionArgs).delete(db);
        getContext().getContentResolver().notifyChange(uri, null);
        return retVal;
	}
	
    /**
     * Build a simple {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually enough to support {@link #insert}
     * {@link #update}, and {@link #delete} operations.
     */
    private SelectionBuilder buildSimpleSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);
        switch (match) {
        case SEARCHWORD: {
            return builder.table(Tables.SEARCHWORD);
        }
        case SEARCHWORD_ID: {
            final String searchwordId = SearchWord.getSearchWordId(uri);
            return builder.table(Tables.SEARCHWORD)
                    .where(SearchWord.SEARCHWORD_ID + "=?", searchwordId);
        }
        
        case TVRESERVING: {
        	return builder.table(Tables.TVRESERVING);
        }
        case TVRESERVING_ID: {
        	final String programid = TvReserving.getTvReservingId(uri);
        	return builder.table(Tables.TVRESERVING)
        			.where(TvReserving.PROGRAM_ID + "=?", programid);
        }
        
        case VODJJIM: {
        	return builder.table(Tables.VODJJIM);
        }
        case VODJJIM_ID: {
        	final String _id = VodJjim.getVodJjimId(uri);
        	return builder.table(Tables.VODJJIM)
        			.where(VodJjim._ID + "=?", _id);
        }
        
		default: {
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		
        }
    }
    
    /**
     * Build an advanced {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually only used by {@link #query}, since it
     * performs table joins useful for {@link Cursor} data.
     */
    private SelectionBuilder buildExpandedSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);
		switch (match) {
		case SEARCHWORD: {
			return builder.table(Tables.SEARCHWORD);
		}
        case SEARCHWORD_ID: {
            final String searchwordId = SearchWord.getSearchWordId(uri);
            return builder.table(Tables.SEARCHWORD)
                    .where(SearchWord.SEARCHWORD_ID + "=?", searchwordId);
        }
        
        case TVRESERVING: {
        	return builder.table(Tables.TVRESERVING);
        }
        case TVRESERVING_ID: {
        	final String programid = TvReserving.getTvReservingId(uri);
        	return builder.table(Tables.TVRESERVING)
        			.where(TvReserving.PROGRAM_ID + "=?", programid);
        }
        
        case VODJJIM: {
        	return builder.table(Tables.VODJJIM);
        }
        case VODJJIM_ID: {
        	final String _id = VodJjim.getVodJjimId(uri);
        	return builder.table(Tables.VODJJIM)
        			.where(VodJjim._ID + "=?", _id);
        }
        
        
		default: {
			throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		
        }
    }
    
}
