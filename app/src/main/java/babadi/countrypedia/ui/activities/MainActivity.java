package babadi.countrypedia.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import babadi.countrypedia.R;
import babadi.countrypedia.data.local.DbHelper;
import babadi.countrypedia.data.model.Country;
import babadi.countrypedia.data.remote.CountryBI;
import babadi.countrypedia.ui.adapters.CountryAdapter;
import babadi.countrypedia.ui.adapters.OnItemClickListener;
import babadi.countrypedia.util.AppController;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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

    CompositeDisposable compositeDisposable;
    RecyclerView countryListRv;
    ProgressBar loadingProgressBar;
    Button retryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((AppController) getApplication()).getRepositoryComponent().inject(this);
        compositeDisposable = new CompositeDisposable();

        initViews();
        initRecyclerView();

        isDatabaseEmpty();
    }

    private void initViews() {
        countryListRv = findViewById(R.id.country_list_rv);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);
        retryBtn = findViewById(R.id.retry_btn);

        retryBtn.setOnClickListener(v -> {
            retryBtn.setVisibility(View.GONE);
            isDatabaseEmpty();
        });
    }


    private void isDatabaseEmpty() {
        Log.i(TAG, "FUNCTION : isDatabaseEmpty");
        dbHelper.getCountryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<Country>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(ArrayList<Country> countries) {
                        Log.i(TAG, "FUNCTION : isDatabaseEmpty => onNext => Countries List Size In Database => " + countries.size());
                        if (countries.size() < 1) {
                            getCountriesFromApi();
                        } else {
                            fillRecyclerView(countries);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(TAG, "FUNCTION : isDatabaseEmpty => onError => " + e.toString());
                    }
                });
    }

    public void saveCountriesListToDatabase(List<Country> countries) {
        Log.i(TAG, "FUNCTION : saveCountriesListToDatabase");
        dbHelper.bulkInsertCountry(countries)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.i(TAG, "FUNCTION : saveCountriesListToDatabase => onNext => Saved RowId " + aLong);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "FUNCTION : saveCountriesListToDatabase => onComplete => Saved Successfully");
                        hideLoadingAndShowRecyclerView();
                        fillRecyclerView(countries);

                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoadingAndShowRecyclerView();
                        Log.e(TAG, "FUNCTION : saveCountriesListToDatabase => onError => " + e.toString());
                        e.printStackTrace();
                    }
                });
    }

    private void getCountriesFromApi() {
        Log.i(TAG, "FUNCTION : getCountriesFromApi");
        showLoadingAndHideRecyclerView();
        retrofit.create(CountryBI.class).getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Country>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Country> countries) {
                        Log.i(TAG, "FUNCTION : getCountriesFromApi => onSuccess => Country size From API :" + countries.size());
                        saveCountriesListToDatabase(countries);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(TAG, "FUNCTION : getCountriesFromApi => onError => " + e.toString());
                        hideLoadingAndShowRecyclerView();
                        retryBtn.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "خطا در برقراری ارتباط...", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initRecyclerView() {
        countryListRv.setHasFixedSize(true);
        countryListRv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fillRecyclerView(List<Country> countries) {
        CountryAdapter countryAdapter = new CountryAdapter(MainActivity.this, countries);
        countryAdapter.setClickListener(country -> {
            Toast.makeText(MainActivity.this, "Population is : " + String.format("%,d",Long.parseLong(String.valueOf(country.getPopulation()))), Toast.LENGTH_LONG).show();
        });
        countryListRv.setAdapter(countryAdapter);
    }

    private void showLoadingAndHideRecyclerView() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        countryListRv.setVisibility(View.GONE);
    }

    private void hideLoadingAndShowRecyclerView() {
        loadingProgressBar.setVisibility(View.GONE);
        countryListRv.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        compositeDisposable.clear();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
