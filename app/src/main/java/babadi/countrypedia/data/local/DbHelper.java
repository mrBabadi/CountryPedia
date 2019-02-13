package babadi.countrypedia.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import babadi.countrypedia.data.model.Country;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class DbHelper extends SQLiteOpenHelper implements CountryDAO {

    private String TBL_NAME = "country_tbl";

    private String COUNTRY_ID = "_id";
    private String COUNTRY_NAME = "country_name";
    private String COUNTRY_CAPITAL_NAME = "country_capital_name";
    private String COUNTRY_POPULATION = "country_population";

    private String CREATE_TABLE = "CREATE TABLE " + TBL_NAME + "(" + COUNTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COUNTRY_NAME + " NVARCHAR(200)," + COUNTRY_CAPITAL_NAME + " TEXT, " + COUNTRY_POPULATION + " BIGINT);";

    public DbHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //----------------------------------------------------------------------------------------------
    //                                    Database Operations
    // ---------------------------------------------------------------------------------------------
    @Override
    public Completable insertSingleCountry(Country country) {
        return Completable.create(emitter -> {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COUNTRY_NAME, country.getName());
            values.put(COUNTRY_CAPITAL_NAME, country.getCapital());
            values.put(COUNTRY_POPULATION, country.getPopulation());
            db.insert(TBL_NAME, null, values);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<Long> bulkInsertCountry(List<Country> countries) {
        return Observable.create(emitter -> {
            SQLiteDatabase db = this.getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                for (Country country : countries) {
                    values.put(COUNTRY_NAME, country.getNativeName());
                    values.put(COUNTRY_CAPITAL_NAME, country.getCapital());
                    values.put(COUNTRY_POPULATION, country.getPopulation());
                    long rowId = db.insert(TBL_NAME, null, values);
                    emitter.onNext(rowId);
                }
                db.setTransactionSuccessful();
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            } finally {
                db.endTransaction();

            }
        });
    }

    @Override
    public Completable clearTable() {
        return Completable.create(emitter -> {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TBL_NAME, null, null);
            emitter.onComplete();
        });
    }

    @Override
    public Single<ArrayList<Country>> getCountryList() {
        return Single.create(emitter -> {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TBL_NAME, null);
            ArrayList<Country> countries = new ArrayList<>();
            while (cursor.moveToNext()) {
                Country country = new Country();
                country.setNativeName(cursor.getString(cursor.getColumnIndex(COUNTRY_NAME)));
                country.setCapital(cursor.getString(cursor.getColumnIndex(COUNTRY_CAPITAL_NAME)));
                country.setPopulation(cursor.getInt(cursor.getColumnIndex(COUNTRY_POPULATION)));
                countries.add(country);
            }
            cursor.close();
            emitter.onSuccess(countries);
        });
    }
}
