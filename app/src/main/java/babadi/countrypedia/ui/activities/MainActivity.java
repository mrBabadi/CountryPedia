package babadi.countrypedia.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import babadi.countrypedia.R;
import babadi.countrypedia.data.local.DbHelper;
import babadi.countrypedia.data.model.Country;
import babadi.countrypedia.data.remote.CountryBI;
import babadi.countrypedia.util.AppController;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    public static String TAG = MainActivity.class.getName();

    @Inject
    AppController appController;
    @Inject
    Retrofit retrofit;
    @Inject
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((AppController) getApplication()).getRepositoryComponent().inject(this);

        if (dbHelper.getCountryList().size() < 1){
            //get the countries
            retrofit.create(CountryBI.class).getCountries()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<List<Country>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(List<Country> countries) {
                            Log.i(TAG, "onSuccess => Country size : " + countries.size());
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            Log.e(TAG, "onError : " + e.toString());
                        }
                    });
        }else {
            //show it from local storage
        }
    }
}
