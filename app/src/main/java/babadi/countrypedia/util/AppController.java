package babadi.countrypedia.util;

import android.app.Application;

import babadi.countrypedia.BuildConfig;
import babadi.countrypedia.di.component.DaggerRepositoryComponent;
import babadi.countrypedia.di.component.RepositoryComponent;
import babadi.countrypedia.di.module.AppModule;
import babadi.countrypedia.di.module.RepositoryModule;

public class AppController extends Application {

    RepositoryComponent repositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        repositoryComponent = DaggerRepositoryComponent.builder()
                .appModule(new AppModule(this))
                .repositoryModule(new RepositoryModule(getApplicationContext(), BuildConfig.BASE_URL))
                .build();
    }

    public RepositoryComponent getRepositoryComponent(){
        return repositoryComponent;

    }
}
