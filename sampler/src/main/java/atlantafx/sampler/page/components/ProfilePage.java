package atlantafx.sampler.page.components;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.sampler.entities.Role;
import atlantafx.sampler.entities.User;
import atlantafx.sampler.page.OutlinePage;
import atlantafx.sampler.services.UserService;
import atlantafx.sampler.services.serviceImpl.UserServiceImpl;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ProfilePage extends OutlinePage {
    public static final String NAME = "Profile";
    private final UserService userService = new UserServiceImpl();
    private VBox userCardsBox;
    private TextField searchField;

    public ProfilePage() {
        super();
        addPageHeader();
        addSection("Add a user", createAddUserSection());
        searchField = createSearchField();
        addSection("Search Users", searchField);
        userCardsBox = createUserCards("");
        addSection("Users", userCardsBox);
    }

    private TextField createSearchField() {
        TextField searchField = new TextField();
        searchField.setPromptText("Search users...");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> refreshUserCards(newValue));
        return searchField;
    }

    private VBox createAddUserSection() {
        VBox addUserBox = new VBox();
        addUserBox.setSpacing(10);

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        ComboBox<Role> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll(Role.admin, Role.Trader);
        roleComboBox.setPromptText("Role");

        Button addButton = new Button("Add User");
        addButton.setOnAction(e -> {
            User user = new User();
            user.setName(nameField.getText());
            user.setEmail(emailField.getText());
            user.setPassword(passwordField.getText());
            user.setRole(roleComboBox.getValue());

                try {
                    userService.createUser(user);
                }catch (SQLException y){
                    System.out.println("login exists");                }
            refreshUserCards(searchField.getText());
        });

        addUserBox.getChildren().addAll(nameField, emailField, passwordField, roleComboBox, addButton);

        return addUserBox;
    }

    private VBox createUserCards(String filter) {
        VBox vbox = new VBox();
        vbox.setSpacing(20);

        if(!filter.isEmpty()){
            List<User> users = userService.getAllUsers().stream()
                    .filter(user -> user.getName().toLowerCase().contains(filter.toLowerCase()) ||
                            user.getEmail().toLowerCase().contains(filter.toLowerCase()))
                    .collect(Collectors.toList());
            GridPane gridPane = new GridPane();
            gridPane.setHgap(20);
            gridPane.setVgap(20);

            int column = 0;
            int row = 0;

            for (User user : users) {
                if (column == 3) {
                    column = 0;
                    row++;
                }
                gridPane.add(createCard(user), column++, row);
            }
            vbox.getChildren().add(gridPane);
        }else {
            List<User> users = userService.getAllUsers();
            GridPane gridPane = new GridPane();
            gridPane.setHgap(20);
            gridPane.setVgap(20);

            int column = 0;
            int row = 0;

            for (User user : users) {
                if (column == 3) {
                    column = 0;
                    row++;
                }
                gridPane.add(createCard(user), column++, row);
            }

            vbox.getChildren().add(gridPane);

        }

        return vbox;
    }

    private Card createCard(User user) {
        var card = new Card();
        card.getStyleClass().add(Styles.ELEVATED_1);
        card.setMinWidth(200);
        card.setMaxWidth(200);
        card.setMaxHeight(200);

        var header = new Tile(user.getName(), "", new ImageView());
        card.setHeader(header);

        var text = new TextFlow(new Text("Email: " + user.getEmail() + "\nRole: " + user.getRole()));
        text.setMaxWidth(180);
        card.setBody(text);

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {

                userService.deleteUser(user.getUserID());
            refreshUserCards(searchField.getText());
        });

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> showUpdateDialog(user));

        VBox cardContent = new VBox(text, deleteButton, updateButton);
        cardContent.setSpacing(5);
        cardContent.setPadding(new Insets(5));
        card.setBody(cardContent);

        return card;
    }

    private void showUpdateDialog(User user) {
        var dialog = new Dialog<ButtonType>();
        dialog.setTitle("Update User");
        dialog.setHeaderText("Update the details of the user:");

        TextField nameField = new TextField(user.getName());
        nameField.setPromptText("Name");

        TextField emailField = new TextField(user.getEmail());
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        ComboBox<Role> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll(Role.admin, Role.user);
        roleComboBox.setValue(user.getRole());

        VBox dialogContent = new VBox();
        dialogContent.setSpacing(10);
        dialogContent.getChildren().addAll(nameField, emailField, passwordField, roleComboBox);
        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.initOwner(getScene().getWindow());

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                user.setName(nameField.getText());
                user.setEmail(emailField.getText());
                user.setPassword(passwordField.getText());
                user.setRole(roleComboBox.getValue());

                    userService.updateUser(user);
                refreshUserCards(searchField.getText());
            }
        });
    }

    private void refreshUserCards(String filter) {
        userCardsBox.getChildren().clear();
        userCardsBox.getChildren().add(createUserCards(filter));
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
