package atlantafx.sampler.page.components;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.sampler.entities.Event;
import atlantafx.sampler.entities.EventFollower;
import atlantafx.sampler.page.OutlinePage;
import atlantafx.sampler.services.EventService;
import atlantafx.sampler.services.serviceImpl.EventServiceImpl;
import atlantafx.sampler.services.serviceImpl.EventFollowerServiceImpl;
import atlantafx.sampler.services.serviceImpl.QRCodeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.zxing.WriterException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
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

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class UserEventsPage extends OutlinePage {

    public static final String NAME = "User Events";
    private final EventService eventService = new EventServiceImpl();
    private final EventFollowerServiceImpl eventFollowerService = new EventFollowerServiceImpl();
    private VBox eventCardsBox;
    private TextField searchField;
    private final int userId = 1; // Assuming a fixed user ID for demonstration

    // Twilio configuration
    private static final String ACCOUNT_SID = "ACb04bca00e387f13769b60a8cd6489895";
    private static final String AUTH_TOKEN = "0f3da267168a6c2954c6a0a15804161d";
    private static final String FROM_PHONE_NUMBER = "+15626208381";
    private static final String TO_PHONE_NUMBER = "+21628780277";

    public UserEventsPage() {
        super();
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN); // Initialize Twilio
        addPageHeader();
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

        boolean isFollowing = eventFollowerService.getEventFollowerById(event.getEventId(), userId) != null;
        Button followButton = new Button(isFollowing ? "Unfollow" : "Follow");
        followButton.setOnAction(e -> handleFollowButton(event, followButton));

        Button qrCodeButton = new Button("Get QR Code");
        qrCodeButton.setOnAction(e -> showQRCode(event));

        VBox cardContent = new VBox(text, followButton, qrCodeButton);
        cardContent.setSpacing(5);
        cardContent.setPadding(new Insets(5));
        card.setBody(cardContent);

        return card;
    }

    private void handleFollowButton(Event event, Button followButton) {
        boolean isFollowing = eventFollowerService.getEventFollowerById(event.getEventId(), userId) != null;
        followButton.setDisable(true);
        CompletableFuture.runAsync(() -> {
            if (isFollowing) {
                unfollowEvent(event);
            } else {
                followEvent(event);
            }
        }).thenRun(() -> Platform.runLater(() -> {
            refreshEventCards(searchField.getText());
            followButton.setDisable(false);
        })).exceptionally(ex -> {
            ex.printStackTrace();
            Platform.runLater(() -> followButton.setDisable(false));
            return null;
        });
    }

    private void followEvent(Event event) {
        EventFollower eventFollower = new EventFollower(event.getEventId(), userId);
        eventFollowerService.addEventFollower(eventFollower);
        sendSmsNotification(event);
        Platform.runLater(() -> showConfirmationDialog("You have followed the event: " + event.getTitle()));
        System.out.println("Following event: " + event.getTitle());
    }

    private void unfollowEvent(Event event) {
        eventFollowerService.deleteEventFollower(event.getEventId(), userId);
        System.out.println("Unfollowed event: " + event.getTitle());
    }

    private void sendSmsNotification(Event event) {
        Message.creator(
                        new PhoneNumber(TO_PHONE_NUMBER),
                        new PhoneNumber(FROM_PHONE_NUMBER),
                        "You have followed the event: " + event.getTitle())
                .create();
    }

    private void refreshEventCards(String filter) {
        eventCardsBox.getChildren().clear();
        eventCardsBox.getChildren().add(createEventCards(filter));
    }

    private void showConfirmationDialog(String message) {
        var dialog = new Dialog<ButtonType>();
        dialog.setTitle("Confirmation");
        dialog.setHeaderText(null);

        var messageText = new Text(message);
        var content = new VBox(messageText);
        content.setAlignment(Pos.CENTER);
        content.setSpacing(10);
        content.setPadding(new Insets(20));

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        dialog.initOwner(getScene().getWindow());

        dialog.showAndWait();
    }

    private void showQRCode(Event event) {
        String eventDetails = "Event ID: " + event.getEventId() + "\n" +
                "Title: " + event.getTitle() + "\n" +
                "Description: " + event.getDescription() + "\n" +
                "Date: " + event.getDate() + "\n" +
                "Duration: " + event.getDuration();
        try {
            BufferedImage qrCodeImage = QRCodeGenerator.generateQRCodeImage(eventDetails);
            Image fxImage = SwingFXUtils.toFXImage(qrCodeImage, null);

            ImageView qrCodeImageView = new ImageView(fxImage);
            qrCodeImageView.setFitWidth(350);
            qrCodeImageView.setFitHeight(350);
            qrCodeImageView.setPreserveRatio(true);

            Dialog<ButtonType> qrCodeDialog = new Dialog<>();
            qrCodeDialog.setTitle("QR Code for " + event.getTitle());
            qrCodeDialog.setHeaderText(null);

            VBox content = new VBox(qrCodeImageView);
            content.setAlignment(Pos.CENTER);
            content.setSpacing(10);
            content.setPadding(new Insets(20));

            qrCodeDialog.getDialogPane().setContent(content);
            qrCodeDialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
            qrCodeDialog.initOwner(getScene().getWindow());

            qrCodeDialog.showAndWait();

        } catch (WriterException e) {
            e.printStackTrace();
            showErrorDialog("Error generating QR code: " + e.getMessage());
        }
    }

    private void showErrorDialog(String message) {
        var dialog = new Dialog<ButtonType>();
        dialog.setTitle("Error");
        dialog.setHeaderText(null);

        var messageText = new Text(message);
        var content = new VBox(messageText);
        content.setAlignment(Pos.CENTER);
        content.setSpacing(10);
        content.setPadding(new Insets(20));

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        dialog.initOwner(getScene().getWindow());

        dialog.showAndWait();
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

    // Example usage for JSON deserialization
    public void exampleJsonDeserialization() {
        String json = "{\"status\": \"sent\"}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            var message = Message.fromJson(json, mapper);
            System.out.println("Deserialized message: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
