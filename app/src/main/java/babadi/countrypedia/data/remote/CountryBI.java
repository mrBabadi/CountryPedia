package babadi.countrypedia.data.remote;

import java.util.List;

import babadi.countrypedia.data.model.Country;
import io.reactivex.Single;
import retrofit2.http.GET;

public interface CountryBI {

    @GET("all")
    Single<List<Country>> getCountries();
}
