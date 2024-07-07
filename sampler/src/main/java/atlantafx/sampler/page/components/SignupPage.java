package atlantafx.sampler.page.components;

import atlantafx.sampler.DBManager;
import atlantafx.sampler.Launcher;
import atlantafx.sampler.entities.Role;
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
import java.sql.SQLException;

public class SignupPage {

    private final Launcher launcher;

    public SignupPage(Launcher launcher) {
        this.launcher = launcher;
    }

    public VBox createSignupPage(Stage primaryStage) {
        VBox signupBox = new VBox();
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button signupButton = new Button("Sign Up");

        signupButton.setOnAction(event -> handleSignup(usernameField.getText(),emailField.getText(), passwordField.getText(), primaryStage));

        signupBox.getChildren().addAll(usernameLabel, usernameField,emailLabel,emailField, passwordLabel, passwordField, signupButton);
        return signupBox;
    }

    private void handleSignup(String username, String email,String password, Stage primaryStage) {
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Username and password cannot be empty.");
            return;
        }

        if (createUser(username,email, password)) {
            System.out.println("User created successfully. Please log in.");
            // Redirect to login page after successful sign-up
            LoginPage loginPage = new LoginPage(launcher);
            VBox loginBox = loginPage.createLoginPage(primaryStage);
            Scene loginScene = new Scene(loginBox, 300, 200);
            primaryStage.setScene(loginScene);
        } else {
            System.out.println("Failed to create user. Username might already be taken.");
        }
    }

    private boolean createUser(String username, String email, String password) {
        String query = "INSERT INTO user (nom, email, password, role) VALUES (?,?,?,?)";
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setString(4, Role.user.name()); // Set the role to 'user' by default

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
