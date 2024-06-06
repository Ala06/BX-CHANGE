package atlantafx.sampler.page.components;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.sampler.Resources;
import atlantafx.sampler.entities.Event;
import atlantafx.sampler.page.OutlinePage;

import atlantafx.sampler.services.EventService;
import atlantafx.sampler.services.serviceImpl.EventServiceImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventsPage extends OutlinePage {

    public static final String NAME = "Events";
    private final EventService eventService = new EventServiceImpl();
    private VBox eventCardsBox;

    public EventsPage() {
        super();
        addPageHeader();
        addSection("Add an event", createAddEventSection());
        eventCardsBox = createEventCards();
        addSection("Events", eventCardsBox);
    }

    private VBox createAddEventSection() {
        VBox addEventBox = new VBox();
        addEventBox.setSpacing(10);

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        TextField dateField = new TextField();
        dateField.setPromptText("Date (DD-MM-YYYY)");

        TextField durationField = new TextField();
        durationField.setPromptText("Duration");

        TextField imageField = new TextField();
        imageField.setPromptText("Image URL");

        Button addButton = new Button("Add Event");
        addButton.setOnAction(e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(dateField.getText(), formatter);

            Event event = new Event(
                    Date.valueOf(localDate),
                    titleField.getText(),
                    descriptionField.getText(),
                    Integer.parseInt(durationField.getText()),
                    imageField.getText()
            );
            eventService.addEvent(event);
            refreshEventCards();
        });

        addEventBox.getChildren().addAll(titleField, descriptionField, dateField, durationField, imageField, addButton);
        return addEventBox;
    }



    private VBox createEventCards() {
        VBox vbox = new VBox();
        vbox.setSpacing(20);

        List<Event> events = eventService.getAllEvents();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        int column = 0;
        int row = 0;

        for (Event event : events) {
            if (column == 3) {
                column = 0;
                row++;
            }
            gridPane.add(createCard(event), column++, row);
        }

        vbox.getChildren().add(gridPane);
        return vbox;
    }

    private Card createCard(Event event) {
        var card = new Card();
        card.getStyleClass().add(Styles.ELEVATED_1);
        card.setMinWidth(200);
        card.setMaxWidth(200);
        card.setMaxHeight(200);

        var header = new Tile(
                event.getTitle(), ""
                ,new ImageView()
        );
        card.setHeader(header);

        var imageView = new ImageView();
        if (event.getImage() != null && !event.getImage().isEmpty()) {
            imageView.setImage(new Image(event.getImage()));
            imageView.setFitWidth(180);
            imageView.setFitHeight(150);
            imageView.setPreserveRatio(true);
        }

        HBox imageBox = new HBox(imageView);
        imageBox.setAlignment(Pos.CENTER);
        card.setSubHeader(imageBox);

        var text = new TextFlow(new Text(event.getDescription() + "\n" + event.getDate().toString()));
        text.setMaxWidth(180);
        card.setBody(text);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            eventService.deleteEvent(event.getEventId());
            refreshEventCards();
        });

        VBox cardContent = new VBox(text, deleteButton);
        cardContent.setSpacing(5);
        cardContent.setPadding(new Insets(5));
        card.setBody(cardContent);

        return card;
    }



    private void refreshEventCards() {
        eventCardsBox.getChildren().clear();
        eventCardsBox.getChildren().add(createEventCards());
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
