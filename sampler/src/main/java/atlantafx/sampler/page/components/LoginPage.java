
package atlantafx.sampler.page.components;

import atlantafx.sampler.DBManager;
import atlantafx.sampler.Launcher;
import atlantafx.sampler.entities.User;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPage {

    private final Launcher launcher;

    public LoginPage(Launcher launcher) {
        this.launcher = launcher;
    }

    public VBox createLoginPage(Stage primaryStage) {
        VBox loginBox = new VBox();
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        loginButton.setOnAction(event -> handleLogin(usernameField.getText(), passwordField.getText(), primaryStage));

        loginBox.getChildren().addAll(usernameLabel, usernameField, passwordLabel, passwordField, loginButton);
        return loginBox;
    }

    private void handleLogin(String username, String password, Stage primaryStage) {
        User user = findUserByUsername(username);

        if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
            launcher.showMainApplication(primaryStage);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private User findUserByUsername(String username) {
        String query = "SELECT nom, password FROM user WHERE nom = ?";
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setName (resultSet.getString("nom"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
