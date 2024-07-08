package atlantafx.sampler.page.components;


import atlantafx.sampler.entities.Messages;
import atlantafx.sampler.page.OutlinePage;
import atlantafx.sampler.services.MessageService;
import atlantafx.sampler.services.serviceImpl.MessageServiceImpl;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import atlantafx.sampler.entities.Discussions;
import atlantafx.sampler.entities.User;
import atlantafx.sampler.services.DiscussionService;
import atlantafx.sampler.services.UserService;
import atlantafx.sampler.services.serviceImpl.DiscussionServiceImpl;
import atlantafx.sampler.services.serviceImpl.UserServiceImpl;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;
import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class XChangePage extends OutlinePage {
  /*  public static final String NAME = "X-Change";
    private final MessageService messageService = new MessageServiceImpl();
    private final UserService userService = new UserServiceImpl();
    private final DiscussionService discussionService = new DiscussionServiceImpl();
    private VBox messageCardsBox;
    private TextField searchField;
    private User selectedUser;
    private VBox discussionsBox;
    private Discussions currentDiscussion;

    public XChangePage() {
        super();
        addPageHeader();
        addSection("Users", createUserListSection());
        addSection("Discussions", createDiscussionsSection());
        addSection("Messages", createMessageSection());
    }

    private VBox createUserListSection() {
        VBox userListBox = new VBox();
        userListBox.setSpacing(10);
        ScrollPane scrollPane = new ScrollPane(userListBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        List<User> users = userService.getAllUsers();
        for (User user : users) {
            Button userButton = new Button(user.getUserName());
            userButton.setOnAction(e -> {
                selectedUser = user;
                System.out.println("Selected user: " + selectedUser.getUserName());
                refreshDiscussions();
            });
            userListBox.getChildren().add(userButton);
        }

        VBox container = new VBox();
        container.setSpacing(10);
        container.getChildren().addAll(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return container;
    }

    private VBox createDiscussionsSection() {
        discussionsBox = new VBox();
        discussionsBox.setSpacing(10);
        ScrollPane scrollPane = new ScrollPane(discussionsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox container = new VBox();
        container.setSpacing(10);
        container.getChildren().addAll(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return container;
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
            if (currentDiscussion != null && selectedUser != null) {
                Messages newMessage = new Messages();
                newMessage.setDate(Date.valueOf(LocalDate.now()));
                newMessage.setContent(contentField.getText());
                newMessage.setDiscussionId(currentDiscussion.getDiscussionId());
                newMessage.setAuthor(1); // Assuming user ID 1 for the author for now
                messageService.addMessage(newMessage);
                contentField.clear();
                refreshMessageCards();
            }
        });

        addMessageBox.getChildren().addAll(contentField, addButton);
        return addMessageBox;
    }

    private void refreshMessageCards() {
        messageCardsBox.getChildren().clear();

        List<Messages> messages;
        if (searchField.getText().isEmpty()) {
            messages = currentDiscussion == null ? List.of() : messageService.getMessagesByDiscussionId(currentDiscussion.getDiscussionId());
        } else {
            messages = messageService.searchMessagesByText(searchField.getText());
        }

        for (Messages message : messages) {
            TextFlow messageCard = new TextFlow(new Text(message.getContent()));
            messageCard.setPadding(new Insets(10));
            messageCard.getStyleClass().add("message-card");
            messageCardsBox.getChildren().add(messageCard);
        }
    }

    private void refreshDiscussions() {
        discussionsBox.getChildren().clear();
        if (selectedUser != null) {
            List<Discussions> discussions = discussionService.getDiscussionsByUserId(selectedUser.getUserId());
            for (Discussions discussion : discussions) {
                Button discussionButton = new Button("Discussion with " + selectedUser.getUserName());
                discussionButton.setOnAction(e -> {
                    currentDiscussion = discussion;
                    refreshMessageCards();
                });
                discussionsBox.getChildren().add(discussionButton);
            }
        }
    }*/
  public static final String NAME = "X-Change";

    private final UserService userService = new UserServiceImpl();
    private final DiscussionService discussionService = new DiscussionServiceImpl();
    private final MessageService messageService = new MessageServiceImpl();

    private final User currentUser = new User(1, "Alaa");
    private User selectedUser;
    private Discussions currentDiscussion;

    private VBox messageCardsBox;
    private TextField searchField;
    private HBox inputBox;

    public XChangePage() {
        super();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        root.setRight(createUserListSection());
        root.setCenter(createMessageSection());

        scrollPane.setContent(root);
    }

    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        root.setRight(createUserListSection());
        root.setCenter(createMessageSection());

        Scene scene = new Scene(root, 300, 600); // Adjust the width and height
        primaryStage.setScene(scene);
        primaryStage.setTitle(NAME);
        primaryStage.show();
    }

    private VBox createUserListSection() {
        VBox userListBox = new VBox();
        userListBox.setSpacing(10);
        ScrollPane scrollPane = new ScrollPane(userListBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        List<User> users = userService.getAllUsers();
        for (User user : users) {
            if (user.getUserId() != currentUser.getUserId()) { // Exclude current user from list
                Button userButton = new Button(user.getUserName());
                userButton.setOnAction(e -> {
                    selectedUser = user;
                    currentDiscussion = discussionService.findOrCreateDiscussion(currentUser.getUserId(), selectedUser.getUserId());
                    refreshMessages();
                    inputBox.setVisible(true);
                });
                userListBox.getChildren().add(userButton);
            }
        }

        VBox container = new VBox();
        container.setSpacing(10);
        container.getChildren().addAll(new Label("Users"), scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return container;
    }

    private VBox createMessageSection() {
        VBox messageBox = new VBox();
        messageBox.setSpacing(10);

        searchField = new TextField();
        searchField.setPromptText("Search messages...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> refreshMessages());

        messageCardsBox = new VBox();
        messageCardsBox.setSpacing(8);
        ScrollPane scrollPane = new ScrollPane(messageCardsBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        inputBox = new HBox();
        inputBox.setSpacing(10);
        inputBox.setPadding(new Insets(10));
        inputBox.setVisible(false);

        TextField messageInput = new TextField();
        messageInput.setPromptText("Type your message here...");
        HBox.setHgrow(messageInput, Priority.ALWAYS);

        Button sendButton = new Button("Send");
        sendButton.setOnAction(e -> {
            if (currentDiscussion != null && selectedUser != null && !messageInput.getText().isEmpty()) {
                Messages newMessage = new Messages();
                newMessage.setDate(Date.valueOf(LocalDate.now()));
                newMessage.setContent(messageInput.getText());
                newMessage.setDiscussionId(currentDiscussion.getDiscussionId());
                newMessage.setAuthor(currentUser.getUserId());
                newMessage.setReceiver(selectedUser.getUserId());
                messageService.addMessage(newMessage);
                messageInput.clear();
                refreshMessages();
            }
        });

        inputBox.getChildren().addAll(messageInput, sendButton);

        messageBox.getChildren().addAll(new Label("Messages"), searchField, scrollPane, inputBox);

        return messageBox;
    }

    private void refreshMessages() {
        messageCardsBox.getChildren().clear();
        List<Messages> messages;
        if (currentDiscussion != null) {
            messages = messageService.getMessagesByDiscussionId(currentDiscussion.getDiscussionId());
            if (!searchField.getText().isEmpty()) {
                messages.removeIf(message -> !message.getContent().contains(searchField.getText()));
            }
        } else {
            messages = List.of();
        }
        for (Messages message : messages) {
            HBox messageBox = new HBox();
            messageBox.setSpacing(10);
            messageBox.setPadding(new Insets(10));

            TextFlow messageFlow = new TextFlow(new Text(message.getContent()));
            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");

            editButton.setOnAction(e -> {
                TextField editField = new TextField(message.getContent());
                Button saveButton = new Button("Save");
                saveButton.setOnAction(saveEvent -> {
                    message.setContent(editField.getText());
                    messageService.updateMessage(message);
                    refreshMessages();
                });
                messageBox.getChildren().setAll(editField, saveButton);
            });

            deleteButton.setOnAction(e -> {
                messageService.deleteMessage(message.getMessageId());
                refreshMessages();
            });

            messageBox.getChildren().addAll(messageFlow, editButton, deleteButton);
            messageCardsBox.getChildren().add(messageBox);
        }
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