package atlantafx.sampler.page.components;

import atlantafx.sampler.page.OutlinePage;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

public class WatchlistPage extends OutlinePage {
    public static final String NAME = "Watchlist";
    public WatchlistPage() {
        super();

        addPageHeader();


    }
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean canDisplaySourceCode() {
        return false;
    }

    @Override
    public @Nullable URI getJavadocUri() {
        return null;
    }

    @Override
    protected void onRendered() {
        super.onRendered();
    }


}

