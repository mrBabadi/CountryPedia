package babadi.countrypedia.di.module;

import javax.inject.Singleton;

import babadi.countrypedia.util.AppController;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    AppController appController;

    public AppModule(AppController appController){
        this.appController = appController;
    }

    @Singleton
    @Provides
    AppController provideAppController(){
        return appController;
    }
}
