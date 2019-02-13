package babadi.countrypedia.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import babadi.countrypedia.data.model.Country;

public class DbHelper extends SQLiteOpenHelper implements CountryDAO{


    public DbHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void addCountry(Country country) {

    }

    @Override
    public ArrayList<Country> getCountryList() {
        return null;
    }
}
