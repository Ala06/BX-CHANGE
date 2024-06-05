package atlantafx.sampler.page.components;

import atlantafx.sampler.page.OutlinePage;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

public final class DashboardPage extends OutlinePage {

    public static final String NAME = "Dashboard";
    public DashboardPage() {
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
