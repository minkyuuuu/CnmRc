package com.cnm.cnmrc.provider;

import android.net.Uri;

/**
 * Cantract 클래스는 Provider에서 처리되는 상수들의 정의이다.
 * 
 * Contract class for interacting with {@link CnmRcProvider}. 
 * 
 * A contract class defines constants that help applications work with the content URIs, 
 * column names, intent actions, and other features of a content provider. 
 * Contract classes are not included automatically with a provider; 
 * the provider's developer has to define them and then make them available to other developers. 
 * Many of the providers included with the Android platform have corresponding contract classes 
 * in the package android.provider.
 * 
 * Naming Convention
 * 1) Class and Interface names : UpperCamelCase
 * 2) 테이블이름과 테이블의 컬럼명 : lowercase_lowercase, 단어연결시 "_"
 * 3) 테이블의 primary key : _id (synthetic key)
 * 4) MiME type 즉 Content Type : lowercase_lowercase, "vnd.android.cursor.item/vnd.daoincube.viewer.library"
 * 5) 상수 : Uppercase_Uppercase
 * 6) 메소드, 로컬변수 : lowerCamelCase 
 * 
 * 선언
 * 1) 테이블컬럼 인터페이스 선언
 * 2) 테이블 클래스 선언
 * 
 * 테이블 클래스 선언
 * 1) CONTENT_URI
 * 2) CONTENT_TYPE / CONTENT_ITEM_TYPE
 * 3) uri 생성 및 uri 관련 함수
 * 4) uri에 따라 쿼리문 작성에 사용되는 상수
 * 
 */
public class CnmRcContract {
	// 테이블에 대한 컬럼 상수 선언
	// interface의 상수는 static일 필요가 없는것 같다. 이유는 인터페이스를 가져다쓰는 클래스가 static 이기에...
	
	// 검색어
	public interface SearchWordColumns {
		final String _ID = "_id";						// Natural Primary Key
		final String SEARCHWORD_ID = "searchword_id";	// unique
	}
	
	// TV 예약하기
	public interface TvReservingColumns {
		final String RESERVERINGDATE_ID = "reservingdate_id";				// _id Primary Key
	}
	
	// Vod 찜
	public interface VodJjimColumns {
		final String ASSET_ID = "asset_id";				// _id Primary Key
		final String TR_NAME = "tr_name";				// "CM01"
		final String DATE = "date";						// "20131107115843"
	}
	
	// Url객체를 위한 authority 상수 선언, package name of application
    public static final String CONTENT_AUTHORITY = "com.cnm.cnmrc";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    
    // Url객체를 위한 path 선언 (데이타베이스의 테이블명 또는 특정 데이타셋을 의미한다.)
    private static final String PATH_SEARCHWORD = "searchword";
    private static final String PATH_TVRESERVING = "tvreserving";
    private static final String PATH_VODJJIM = "vodjjim";
    
    /**
     * SearchWord class (검색어)
     */
	public static class SearchWord implements SearchWordColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SEARCHWORD).build();

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cnm.cnmrc.searchword";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.cnm.cnmrc.searchword";
		
		/** Default "ORDER BY" clause. */
        public static final String DEFAULT_SORT = SearchWord._ID + " COLLATE NOCASE DESC";
		
		/** Build {@link Uri} for requested {@link #SEARCHWORD_ID}. */
        public static Uri buildSearchWordId(String searchwordId) {
        	return CONTENT_URI.buildUpon().appendPath(searchwordId).build();
        }
        
        /** Read {@link #SEARCHWORD_ID} from {@link SearchWord} {@link Uri}. */
        public static String getSearchWordId(Uri uri) {
        	return uri.getPathSegments().get(1);
        }
        
	}
	
	/**
	 * TvReserving class (TV 예약하기)
	 */
	public static class TvReserving implements TvReservingColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TVRESERVING).build();
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cnm.cnmrc.tvreserving";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.cnm.cnmrc.tvreserving";
		
		/** Default "ORDER BY" clause. */
        public static final String DEFAULT_SORT = TvReserving.RESERVERINGDATE_ID + " COLLATE NOCASE ASC";

        /** Build {@link Uri} for requested {@link #RESERVERINGDATE_ID}. */
        public static Uri buildTvReservingId(String tvreservingId) {
        	return CONTENT_URI.buildUpon().appendPath(tvreservingId).build();
        }
        
		/** Read {@link #RESERVERINGDATE_ID} from {@link TvReserving} {@link Uri}. */
        public static String getTvReservingId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
	}
	
	/**
	 * VodJjim class (Vod 찜)
	 */
	public static class VodJjim implements VodJjimColumns {
		public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_VODJJIM).build();
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.cnm.cnmrc.vodjjim";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.cnm.cnmrc.vodjjim";
		
		/** Build {@link Uri} for requested {@link #ASSET_ID}. */
        public static Uri buildVodJjimId(String vodjjimId) {
        	return CONTENT_URI.buildUpon().appendPath(vodjjimId).build();
        }
        
		/** Read {@link #ASSET_ID} from {@link VodJjim} {@link Uri}. */
        public static String getVodJjimId(Uri uri) {
            return uri.getPathSegments().get(1);
        }	
	}
	
    private CnmRcContract() {
    }

}
