package atlantafx.sampler.page.components;

import atlantafx.base.theme.Styles;
import atlantafx.sampler.entities.Messages;
import atlantafx.sampler.page.OutlinePage;
import atlantafx.sampler.services.MessageService;
import atlantafx.sampler.services.serviceImpl.MessageServiceImpl;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.jetbrains.annotations.Nullable;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class XChangePage extends OutlinePage {
    public static final String NAME = "X-Change";
    private final MessageService messageService = new MessageServiceImpl();
    private VBox messageCardsBox;
    private TextField searchField;

    public XChangePage() {
        super();
        addPageHeader();
        addSection("Discussion", createMessageSection());
    }

    private VBox createMessageSection() {
        messageCardsBox = new VBox();
        messageCardsBox.setSpacing(10);
        ScrollPane scrollPane = new ScrollPane(messageCardsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox container = new VBox();
        container.setSpacing(10);
        container.getChildren().addAll(createSearchSection(), scrollPane, createAddMessageSection());
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        refreshMessageCards();
        return container;
    }

    private HBox createSearchSection() {
        HBox searchBox = new HBox();
        searchBox.setSpacing(10);
        searchBox.setPadding(new Insets(10));

        searchField = new TextField();
        searchField.setPromptText("Search messages...");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> refreshMessageCards());

        searchBox.getChildren().addAll(searchField, searchButton);
        return searchBox;
    }

    private HBox createAddMessageSection() {
        HBox addMessageBox = new HBox();
        addMessageBox.setSpacing(10);
        addMessageBox.setPadding(new Insets(10));

        TextField contentField = new TextField();
        contentField.setPromptText("Type your message here...");
        HBox.setHgrow(contentField, Priority.ALWAYS);

        Button addButton = new Button("Send");
        addButton.setOnAction(e -> {
            Messages message = new Messages();
            message.setDate(Date.valueOf(LocalDate.now()));
            message.setContent(contentField.getText());

            messageService.addMessage(message);
            refreshMessageCards();
            contentField.clear();
        });

        Button addVideoButton = new Button(null, new FontIcon(Feather.VIDEO));
        addVideoButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.LEFT_PILL);
        addVideoButton.setOnAction(e -> {
            // Add logic to handle adding a video message
        });

        Button addPhotoButton = new Button(null, new FontIcon(Feather.IMAGE));
        addPhotoButton.getStyleClass().addAll(Styles.BUTTON_ICON);
        addPhotoButton.setOnAction(e -> {
            // Add logic to handle adding a photo message
        });

        Button addVoiceButton = new Button(null, new FontIcon(Feather.MIC));
        addVoiceButton.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.RIGHT_PILL);
        addVoiceButton.setOnAction(e -> {
            // Add logic to handle adding a voice message
        });

        addMessageBox.getChildren().addAll(addVideoButton, addPhotoButton, addVoiceButton, contentField, addButton);
        return addMessageBox;
    }

    private void refreshMessageCards() {
        messageCardsBox.getChildren().clear();
        List<Messages> messages;
        if (searchField != null && !searchField.getText().isEmpty()) {
            messages = messageService.searchMessagesByText(searchField.getText());
        } else {
            messages = messageService.getAllMessages();
        }

        for (Messages message : messages) {
            messageCardsBox.getChildren().add(createMessageBubble(message));
        }
    }
    private HBox createMessageBubble(Messages message) {
        HBox messageBubble = new HBox();
        messageBubble.setSpacing(10);
        messageBubble.setPadding(new Insets(10));
        messageBubble.getStyleClass().add("message-bubble");

        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().add(new Text(message.getContent()));
        textFlow.setPadding(new Insets(10));
        textFlow.getStyleClass().add("message-text");

        Text dateText = new Text(message.getDate().toString());
        dateText.getStyleClass().add("message-date");

        Button updateButton = new Button(null, new FontIcon(Feather.PEN_TOOL));
        updateButton.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.ACCENT);
        updateButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog(message.getContent());
            dialog.setTitle("Edit Message");
            dialog.setHeaderText(null);
            dialog.setContentText("Edit the message:");
            dialog.initOwner(getScene().getWindow());
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(updatedContent -> {
                Messages updatedMessage = new Messages();
                updatedMessage.setMessageId(message.getMessageId());
                updatedMessage.setDate(message.getDate());
                updatedMessage.setContent(updatedContent);
                messageService.updateMessage(updatedMessage);
                refreshMessageCards();
            });
        });

        Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));
        deleteButton.getStyleClass().addAll(Styles.BUTTON_CIRCLE, Styles.BUTTON_OUTLINED, Styles.DANGER);
        deleteButton.setOnAction(e -> {
            messageService.deleteMessage(message.getMessageId());
            refreshMessageCards();
        });

        HBox rightContainer = new HBox(updateButton, deleteButton);
        rightContainer.setSpacing(5);
        rightContainer.getStyleClass().add("right-container");

        VBox bottomContainer = new VBox(rightContainer, dateText);
        bottomContainer.setSpacing(5);
        bottomContainer.getStyleClass().add("bottom-container");

        HBox.setHgrow(textFlow, Priority.ALWAYS);
        messageBubble.getChildren().addAll(textFlow, bottomContainer);
        return messageBubble;
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
