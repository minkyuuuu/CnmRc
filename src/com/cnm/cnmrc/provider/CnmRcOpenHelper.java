package com.cnm.cnmrc.provider;

import java.io.File;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.cnm.cnmrc.provider.CnmRcContract.SearchWord;
import com.cnm.cnmrc.provider.CnmRcContract.TvReserving;
import com.cnm.cnmrc.provider.CnmRcContract.VodJjim;


/**
 * 1) Tables 인터페이스가 여기에 선언된 이유는 join문 때문... 그렇지 않으면 Contract 클래스의 각 테이블 클래스에 선언되는게 깔끔하다.
 *
 */
public class CnmRcOpenHelper extends SQLiteOpenHelper{
	private static final String TAGS = CnmRcOpenHelper.class.getSimpleName();
	
	private static File externalStorage = Environment.getExternalStorageDirectory();
	private static final String DATABASE_NAME = externalStorage.getAbsolutePath() + "/" + "cnmrc.db";	
	//private static final String DATABASE_NAME = "cnmrc.db";
	private static final int DATABASE_VERSION = 2;
	
    interface Tables {
    	final String SEARCHWORD = "searchword";
    	final String TVRESERVING = "tvreserving";
    	final String VODJJIM = "vodjjim";
    }
    
    Context mContext;
	public CnmRcOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		Log.d(TAGS, "CnmRcOpenHelper() from CnmRcOpenHelper");
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAGS, "onCreate() from CnmRcOpenHelper");
		
        db.execSQL("CREATE TABLE " + Tables.SEARCHWORD + " ("
                + SearchWord._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SearchWord.SEARCHWORD_ID + " TEXT NOT NULL UNIQUE ON CONFLICT REPLACE"
                + ")");
        
        db.execSQL("CREATE TABLE " + Tables.TVRESERVING + " ("
        		+ TvReserving.RESERVERINGDATE_ID + " INTEGER PRIMARY KEY"
        		+ ")");
        
        db.execSQL("CREATE TABLE " + Tables.VODJJIM + " ("
        		+ VodJjim.ASSET_ID + " TEXT PRIMARY KEY,"
        		+ VodJjim.TR_NAME + " TEXT NOT NULL DEFAULT 'CM01',"
        		+ VodJjim.DATE + " TEXT NOT NULL"
        		+ ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	   	Log.d(TAGS, "onUpgrade() from " + oldVersion + " to " + newVersion);

        int version = oldVersion;

        if (version != DATABASE_VERSION) {
        	Log.d(TAGS, "Destroying old data during upgrade");

            db.execSQL("DROP TABLE IF EXISTS " + Tables.SEARCHWORD);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.TVRESERVING);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.VODJJIM);

            onCreate(db);
        }
		
	}

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}