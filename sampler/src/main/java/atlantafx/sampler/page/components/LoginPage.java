package atlantafx.sampler.page.components;

import atlantafx.sampler.entities.Event;
import atlantafx.sampler.entities.User;
import atlantafx.sampler.page.OutlinePage;
import atlantafx.sampler.services.UserService;
import atlantafx.sampler.services.serviceImpl.UserServiceImpl;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.feather.Feather;

import java.util.List;
import java.util.function.Supplier;

public class LoginPage extends OutlinePage {

    private final UserService userService = new UserServiceImpl();
    public LoginPage(){
        super();

        addPageHeader();
//        addSection("login", );
    }

    public void start() {
        VBox loginBox = new VBox(10);
        loginBox.setPadding(new Insets(10));

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        userField.setPromptText("Enter your username");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = userField.getText();
            System.out.println("Login attempt with username: " + username);
            if (!username.isEmpty()) {
                User user = userService.getUserByUsername(username);
                if (user == null) {
                    System.out.println("User not found, creating new user.");
                    user = new User();
                    user.setUserName(username);
                    userService.addUser(user);
                    user = userService.getUserByUsername(username); // Re-fetch to get the ID
                } else {
                    System.out.println("User found: " + user.getUserName());
                }
                // Open XChangePage in a new stage and close the login stage
                try {
                    System.out.println("Attempting to open XChangePage for user: " + user.getUserName());
                    Stage xChangeStage = new Stage();
                    XChangePage xChangePage = new XChangePage();
                    xChangePage.start(xChangeStage); // Start XChangePage with the user
//                    primaryStage.close();
                } catch (Exception ex) {
                    System.err.println("Failed to open XChangePage: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        loginBox.getChildren().addAll(userLabel, userField, loginButton);

//        Scene scene = new Scene(loginBox, 300, 200);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Login");
//        primaryStage.show();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public <T> List<T> generate(Supplier<T> supplier, int count) {
        return super.generate(supplier, count);
    }

    @Override
    public Feather randomIcon() {
        return super.randomIcon();
    }

    @Override
    public Node createFormattedText(String text, boolean handleUrl) {
        return super.createFormattedText(text, handleUrl);
    }

    @Override
    public Label captionLabel(String text) {
        return super.captionLabel(text);
    }

    @Override
    public Node getStyleableNode() {
        return super.getStyleableNode();
    }
}
