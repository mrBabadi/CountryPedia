package babadi.countrypedia.data.local;

import java.util.ArrayList;
import java.util.List;

import babadi.countrypedia.data.model.Country;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface CountryDAO {

    Completable insertSingleCountry(Country country);

    Observable<Long> bulkInsertCountry(List<Country> countries);

    Completable clearTable();

    Single<ArrayList<Country>> getCountryList();

}
