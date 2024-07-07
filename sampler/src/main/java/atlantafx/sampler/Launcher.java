package atlantafx.sampler;

import atlantafx.base.theme.PrimerDark;
import atlantafx.sampler.event.BrowseEvent;
import atlantafx.sampler.event.DefaultEventBus;
import atlantafx.sampler.event.HotkeyEvent;
import atlantafx.sampler.event.Listener;
import atlantafx.sampler.layout.ApplicationWindow;
<<<<<<< Updated upstream
import atlantafx.sampler.services.serviceImpl.SchedulerManager;
=======
import atlantafx.sampler.page.components.LoginPage;
import atlantafx.sampler.page.components.SignupPage;
>>>>>>> Stashed changes
import atlantafx.sampler.theme.ThemeManager;
import fr.brouillard.oss.cssfx.CSSFX;
import fr.brouillard.oss.cssfx.api.URIToPathConverter;
import fr.brouillard.oss.cssfx.impl.log.CSSFXLogger;
import fr.brouillard.oss.cssfx.impl.log.CSSFXLogger.LogLevel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.LogManager;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
<<<<<<< Updated upstream
import org.quartz.SchedulerException;
=======
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
>>>>>>> Stashed changes

public class Launcher extends Application {

    public static final boolean IS_DEV_MODE = true;

    public static final List<KeyCodeCombination> SUPPORTED_HOTKEYS = List.of(
            new KeyCodeCombination(KeyCode.SLASH),
            new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN),
            new KeyCodeCombination(KeyCode.W, KeyCombination.CONTROL_DOWN)
    );

    public static void main(String[] args) {
<<<<<<< Updated upstream

        try {
            SchedulerManager.startScheduler();
        } catch (SchedulerException e) {
            System.out.println(e.getMessage());;
        }
=======
>>>>>>> Stashed changes
        launch(args);


    }

    @Override
    public void start(Stage stage) {
        Application.setUserAgentStylesheet("/atlantafx/base/theme/primer-dark.css");

        Thread.currentThread().setUncaughtExceptionHandler(new DefaultExceptionHandler(stage));
        loadApplicationProperties();

        if (IS_DEV_MODE) {
            System.out.println("[WARNING] Application is running in development mode.");
        }

        // Create and show login page with sign-up button
        VBox loginBox = createLoginPageWithSignup(stage);
        Scene loginScene = new Scene(loginBox, 300, 200);

        stage.setTitle("Login");
        stage.setScene(loginScene);
        stage.show();
    }

    private VBox createLoginPageWithSignup(Stage primaryStage) {
        VBox loginBox = new VBox();
        LoginPage loginPage = new LoginPage(this);
        VBox loginPageBox = loginPage.createLoginPage(primaryStage);
        Button signupButton = new Button("Sign Up");

        signupButton.setOnAction(event -> {
            SignupPage signupPage = new SignupPage(this);
            VBox signupBox = signupPage.createSignupPage(primaryStage);
            Scene signupScene = new Scene(signupBox, 300, 200);
            primaryStage.setScene(signupScene);
        });

        loginBox.getChildren().addAll(loginPageBox, signupButton);
        return loginBox;
    }

    public void showMainApplication(Stage stage) {
        var root = new ApplicationWindow();

        var antialiasing = Platform.isSupported(ConditionalFeature.SCENE3D)
                ? SceneAntialiasing.BALANCED
                : SceneAntialiasing.DISABLED;
        var scene = new Scene(root, ApplicationWindow.MIN_WIDTH + 80, 768, false, antialiasing);
        scene.setOnKeyPressed(this::dispatchHotkeys);

        var tm = ThemeManager.getInstance();
        tm.setScene(scene);
        tm.setTheme(tm.getDefaultTheme());
        if (IS_DEV_MODE) {
            startCssFX(scene);
        }

        scene.getStylesheets().addAll(Resources.resolve("assets/styles/index.css"));

        stage.setScene(scene);
        stage.setTitle(System.getProperty("app.name"));
        loadIcons(stage);
        stage.setResizable(true);
        stage.setOnCloseRequest(t -> Platform.exit());

        // register event listeners
        DefaultEventBus.getInstance().subscribe(BrowseEvent.class, this::onBrowseEvent);

        Platform.runLater(() -> {
            stage.show();
            stage.requestFocus();
        });
    }

    private void loadIcons(Stage stage) {
        int iconSize = 16;
        while (iconSize <= 1024) {
            // we could use the square icons for Windows here
            stage.getIcons().add(new Image(Resources.getResourceAsStream("assets/icon-rounded-" + iconSize + ".png")));
            iconSize *= 2;
        }
    }

    private void loadApplicationProperties() {
        Properties properties = new Properties();
        try (InputStreamReader in = new InputStreamReader(Resources.getResourceAsStream("application.properties"), StandardCharsets.UTF_8)) {
            properties.load(in);
            properties.forEach((key, value) -> System.setProperty(String.valueOf(key), String.valueOf(value)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("CatchAndPrintStackTrace")
    private void startCssFX(Scene scene) {
        URIToPathConverter fileUrlConverter = uri -> {
            try {
                if (uri != null && uri.startsWith("file:")) {
                    return Paths.get(URI.create(uri));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };

        CSSFX.addConverter(fileUrlConverter).start();
        CSSFXLogger.setLoggerFactory(loggerName -> (level, message, args) -> {
            if (level.ordinal() <= LogLevel.INFO.ordinal()) {
                System.out.println("[" + level + "] CSSFX: " + String.format(message, args));
            }
        });
        CSSFX.start(scene);
    }

    private void dispatchHotkeys(KeyEvent event) {
        for (KeyCodeCombination k : SUPPORTED_HOTKEYS) {
            if (k.match(event)) {
                DefaultEventBus.getInstance().publish(new HotkeyEvent(k));
                return;
            }
        }
    }

    @Listener
    private void onBrowseEvent(BrowseEvent event) {
        getHostServices().showDocument(event.getUri().toString());
    }
}
