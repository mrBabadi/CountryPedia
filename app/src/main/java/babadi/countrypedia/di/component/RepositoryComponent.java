package babadi.countrypedia.di.component;

import javax.inject.Singleton;

import babadi.countrypedia.di.module.AppModule;
import babadi.countrypedia.di.module.RepositoryModule;
import babadi.countrypedia.ui.activities.MainActivity;
import dagger.Component;

@Singleton
@Component(modules = {RepositoryModule.class, AppModule.class})
public interface RepositoryComponent {

    void inject(MainActivity mainActivity);
}
