package babadi.countrypedia.di.module;

import android.content.Context;

import javax.inject.Singleton;

import babadi.countrypedia.BuildConfig;
import babadi.countrypedia.data.local.DbHelper;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RepositoryModule {

    Context context;
    String baseUrl;
    public RepositoryModule(Context context, String baseUrl) {
        this.context = context;
        this.baseUrl = baseUrl;
    }


    @Singleton
    @Provides
    Retrofit provideRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    DbHelper provideDatabase(){
        return new DbHelper(context, BuildConfig.DB_NAME, BuildConfig.DB_VERSION);
    }


}
