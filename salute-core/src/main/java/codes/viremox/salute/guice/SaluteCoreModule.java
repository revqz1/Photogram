package codes.viremox.salute.guice;

import codes.viremox.salute.SaluteOrchestrator;
import codes.viremox.salute.command.SaluteCommandHandler;
import codes.viremox.salute.pipeline.SkinUploader;
import codes.viremox.salute.storage.ConfigProvider;
import codes.viremox.salute.storage.TextureStore;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import java.nio.file.Path;
import java.util.logging.Logger;

public class SaluteCoreModule extends AbstractModule {

    private final Path dataDirectory;
    private final Logger logger;

    public SaluteCoreModule(Path dataDirectory, Logger logger) {
        this.dataDirectory = dataDirectory;
        this.logger = logger;
    }

    @Override
    protected void configure() {
        bind(Path.class).annotatedWith(Names.named("dataDirectory")).toInstance(dataDirectory);
        bind(Logger.class).annotatedWith(Names.named("salute")).toInstance(logger);

        bind(ConfigProvider.class).asEagerSingleton();
        bind(TextureStore.class).asEagerSingleton();
        bind(SkinUploader.class).asEagerSingleton();
        bind(SaluteOrchestrator.class).asEagerSingleton();
        bind(SaluteCommandHandler.class);
    }
}
