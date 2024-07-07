package atlantafx.sampler.page.components;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.sampler.Resources;
import atlantafx.sampler.entities.Event;
import atlantafx.sampler.page.ExampleBox;
import atlantafx.sampler.page.OutlinePage;

import atlantafx.sampler.page.Snippet;
import atlantafx.sampler.services.EventService;
import atlantafx.sampler.services.serviceImpl.EventServiceImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.jetbrains.annotations.Nullable;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class EventsPage extends OutlinePage {

    public static final String NAME = "Events";
    private final EventService eventService = new EventServiceImpl();
    private VBox eventCardsBox;
    private TextField searchField;

    public EventsPage() {
        super();
        addPageHeader();
        addSection("Add an event", createAddEventSection());
        searchField = createSearchField();
        addSection("Search Events", searchField);
        eventCardsBox = createEventCards("");
        addSection("Events", eventCardsBox);
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search events...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> refreshEventCards(newValue));
        return searchField;
    }

    private VBox createAddEventSection() {
        VBox addEventBox = new VBox();
        addEventBox.setSpacing(10);

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        Label titleLabel = new Label();
        titleLabel.setStyle("-fx-text-fill: red");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        Label descriptionLabel = new Label();
        descriptionLabel.setStyle("-fx-text-fill: red");

        TextField dateField = new TextField();
        dateField.setPromptText("Date (DD-MM-YYYY)");

        Label dateLabel = new Label();
        dateLabel.setStyle("-fx-text-fill: red");

        TextField durationField = new TextField();
        durationField.setPromptText("Duration");

        Label durationLabel = new Label();
        durationLabel.setStyle("-fx-text-fill: red");

        TextField imageField = new TextField();
        imageField.setPromptText("Image URL");

        Label imageLabel = new Label();
        imageLabel.setStyle("-fx-text-fill: red");

        Label successLabel = new Label();
        successLabel.setStyle("-fx-text-fill: green");

        Button addButton = new Button("Add Event");
        addButton.setOnAction(e -> {
            boolean isValid = true;

            // Validate title
            if (titleField.getText().isEmpty()) {
                titleLabel.setText("Title cannot be empty");
                isValid = false;
            } else {
                titleLabel.setText("");
            }

            // Validate description
            if (descriptionField.getText().isEmpty()) {
                descriptionLabel.setText("Description cannot be empty");
                isValid = false;
            } else {
                descriptionLabel.setText("");
            }

            // Validate date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            try {
                LocalDate localDate = LocalDate.parse(dateField.getText(), formatter);
                dateLabel.setText("");
            } catch (DateTimeParseException ex) {
                dateLabel.setText("Invalid date format (DD-MM-YYYY)");
                isValid = false;
            }

            // Validate duration (assuming it's an integer)
            try {
                if (!durationField.getText().matches("\\d+")) {
                    durationLabel.setText("Duration must be a number");
                    isValid = false;
                } else {
                    durationLabel.setText("");
                }
            } catch (NumberFormatException ex) {
                durationLabel.setText("Invalid duration format");
                isValid = false;
            }

            // Validate image URL (optional)
            if (!imageField.getText().isEmpty() && !isValidURL(imageField.getText())) {
                imageLabel.setText("Invalid image URL");
                isValid = false;
            } else {
                imageLabel.setText("");
            }

            // If all fields are valid, proceed to add the event
            if (isValid) {
                Event event = new Event(
                        Date.valueOf(LocalDate.parse(dateField.getText(), formatter)),
                        titleField.getText(),
                        descriptionField.getText(),
                        Integer.parseInt(durationField.getText()),
                        imageField.getText()
                );
                eventService.addEvent(event);
                refreshEventCards(searchField.getText());

                // Reset fields and show success message
                titleField.clear();
                descriptionField.clear();
                dateField.clear();
                durationField.clear();
                imageField.clear();
                successLabel.setText("Event added successfully!");
            }
        });

        addEventBox.getChildren().addAll(
                titleField, titleLabel,
                descriptionField, descriptionLabel,
                dateField, dateLabel,
                durationField, durationLabel,
                imageField, imageLabel,
                addButton,
                successLabel
        );
        return addEventBox;
    }

    private boolean isValidURL(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }



    private VBox createEventCards(String filter) {
        VBox vbox = new VBox();
        vbox.setSpacing(20);

        List<Event> events = eventService.getAllEvents().stream()
                .filter(event -> event.getTitle().toLowerCase().contains(filter.toLowerCase()) ||
                        event.getDescription().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toList());

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

        var header = new Tile(event.getTitle(), "", new ImageView());
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
            refreshEventCards(searchField.getText());
        });

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> showUpdateDialog(event));

        VBox cardContent = new VBox(text, deleteButton, updateButton);
        cardContent.setSpacing(5);
        cardContent.setPadding(new Insets(5));
        card.setBody(cardContent);

        return card;
    }

    private void showUpdateDialog(Event event) {
        var dialog = new Dialog<ButtonType>();
        dialog.setTitle("Update Event");
        dialog.setHeaderText("Update the details of the event:");

        TextField titleField = new TextField(event.getTitle());
        titleField.setPromptText("Title");

        TextField descriptionField = new TextField(event.getDescription());
        descriptionField.setPromptText("Description");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(event.getDate().toString(), formatter);
        TextField dateField = new TextField(localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        dateField.setPromptText("Date (DD-MM-YYYY)");

        TextField durationField = new TextField(String.valueOf(event.getDuration()));
        durationField.setPromptText("Duration");

        TextField imageField = new TextField(event.getImage());
        imageField.setPromptText("Image URL");

        VBox dialogContent = new VBox();
        dialogContent.setSpacing(10);
        dialogContent.getChildren().addAll(titleField, descriptionField, dateField, durationField, imageField);
        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.initOwner(getScene().getWindow());

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                event.setTitle(titleField.getText());
                event.setDescription(descriptionField.getText());

                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate newLocalDate = LocalDate.parse(dateField.getText(), inputFormatter);
                event.setDate(Date.valueOf(newLocalDate));

                event.setDuration(Integer.parseInt(durationField.getText()));
                event.setImage(imageField.getText());

                eventService.updateEvent(event);
                refreshEventCards(searchField.getText());
            }
        });
    }

    private void refreshEventCards(String filter) {
        eventCardsBox.getChildren().clear();
        eventCardsBox.getChildren().add(createEventCards(filter));
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
