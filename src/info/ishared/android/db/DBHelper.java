package info.ishared.android.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import info.ishared.android.AppConfig;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-12-7
 * Time: 下午5:06
 */
public class DBHelper {
    private static final String TAG = "DBAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private Context context;

    private DBHelper(Context context) {
        this.context = context;
    }

    private static info.ishared.android.db.DBHelper DBHelper = null;

    public static info.ishared.android.db.DBHelper getInstance(Context context) {
        if (null == DBHelper) DBHelper = new info.ishared.android.db.DBHelper(context);
        return DBHelper;
    }

    public info.ishared.android.db.DBHelper open() throws SQLException {
        mDbHelper = new DatabaseHelper(this.context);
        Log.d(AppConfig.TAG,"open");
        mDb = mDbHelper.getWritableDatabase();
        Log.d(AppConfig.TAG,"md open"+mDb.getPath());

        return this;
    }

    public info.ishared.android.db.DBHelper close() throws SQLException {
        mDb.close();
        mDbHelper.close();

        return this;
    }

    public void clearDBTableByName(String tableName) {
        mDb.delete(tableName, "1=1", null);
    }

    public SQLiteDatabase getMDB() {
        return mDb;
    }



    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DBConfig.DB_NAME, null, DBConfig.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DBConfig.MockLatLng.CREATE_MOCK_LAT_LNG_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + DBConfig.MockLatLng.TABLE_NAME);
            onCreate(db);
        }

    }



}
