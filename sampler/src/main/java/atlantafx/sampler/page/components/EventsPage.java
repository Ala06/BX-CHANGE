package atlantafx.sampler.page.components;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.BBCodeParser;
import atlantafx.sampler.Resources;
import atlantafx.sampler.page.ExampleBox;
import atlantafx.sampler.page.OutlinePage;
import atlantafx.sampler.page.Snippet;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.jetbrains.annotations.Nullable;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;

import java.net.URI;

public class EventsPage extends OutlinePage {

    public static final String NAME = "Events";

    public EventsPage() {
        super();
        addPageHeader();
        addSection("Latest Release", createCard());
    }


    private VBox createCard() {
        var card1 = new Card();
        card1.getStyleClass().add(Styles.ELEVATED_1);
        card1.setMinWidth(300);
        card1.setMaxWidth(300);


        var header1 = new Tile(
                "Event 1",
                "",
                new ImageView()
        );
        card1.setHeader(header1);

        var image1 = new WritableImage(
                new Image(
                        Resources.getResourceAsStream("images/pattern.jpg")
                ).getPixelReader(), 0, 0, 298, 150
        );
        card1.setSubHeader(new ImageView(image1));

        var text1 = new TextFlow(new Text(FAKER.lorem().sentence(10)));
        text1.setMaxWidth(260);
        card1.setBody(text1);



        var box = new HBox(HGAP_20, card1);
        box.setPadding(new Insets(0, 0, 10, 0));



        return new VBox(box);
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
