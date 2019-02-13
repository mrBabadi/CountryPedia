package babadi.countrypedia.data.local;

import java.util.ArrayList;

import babadi.countrypedia.data.model.Country;

public interface CountryDAO {

    void addCountry(Country country);

    ArrayList<Country> getCountryList();

}
