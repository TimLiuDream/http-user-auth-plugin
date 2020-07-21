package org.elasticsearch.plugin.http.user.auth;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


import org.elasticsearch.common.component.LifecycleComponent;
import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.common.inject.multibindings.Multibinder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.repositories.RepositoriesModule;
import org.elasticsearch.rest.action.cat.AbstractCatAction;

public class HttpUserAuthPlugin extends Plugin {
    private Settings settings;
    @Inject public HttpUserAuthPlugin(Settings settings) {
		this.settings = settings;
    }

	public String description() {
		return "Http User Auth plugin";
	}

	public String name() {
		return "Http User Auth plugin";
	}

	@Override
	public Collection<Module> nodeModules() {
		return Collections.<Module>singletonList(new ConfiguredExampleModule());
	}

	@Override
	public Collection<Class<? extends LifecycleComponent>> nodeServices() {
		Collection<Class<? extends LifecycleComponent>> services = new ArrayList<>();
		return services;
	}

	@Override
	public Collection<Module> indexModules(Settings indexSettings) {
		return Collections.emptyList();
	}

	@Override
	public Collection<Class<? extends Closeable>> indexServices() {
		return Collections.emptyList();
	}

	@Override
	public Collection<Module> shardModules(Settings indexSettings) {
		return Collections.emptyList();
	}

	@Override
	public Collection<Class<? extends Closeable>> shardServices() {
		return Collections.emptyList();
	}

	@Override
	public Settings additionalSettings() {
		return Settings.EMPTY;
	}

	public void onModule(RepositoriesModule repositoriesModule) {
	}

	/**
	 * Module decalaring some example configuration and a _cat action that uses
	 * it.
	 */
	public static class ConfiguredExampleModule extends AbstractModule {
		@Override
		protected void configure() {
			bind(ExamplePluginConfiguration.class).asEagerSingleton();
			Multibinder<AbstractCatAction> catActionMultibinder = Multibinder.newSetBinder(binder(), AbstractCatAction.class);
			catActionMultibinder.addBinding().to(ExampleCatAction.class).asEagerSingleton();
		}
	}
}


