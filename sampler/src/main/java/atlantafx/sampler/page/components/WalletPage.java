package atlantafx.sampler.page.components;

import atlantafx.sampler.entities.Transactions;
import atlantafx.sampler.entities.Wallet;
import atlantafx.sampler.page.OutlinePage;
import atlantafx.sampler.services.Currencies;
import atlantafx.sampler.services.TransactionListService;
import atlantafx.sampler.services.TransactionsService;
import atlantafx.sampler.services.WalletService;
import atlantafx.sampler.services.serviceImpl.TransactionListServiceImpl;
import atlantafx.sampler.services.serviceImpl.TransactionsServiceImpl;
import atlantafx.sampler.services.serviceImpl.WalletServiceImpl;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import javafx.scene.layout.HBox;

public class WalletPage extends OutlinePage {

    public static final String NAME = "Wallet";
    private final WalletService walletService = new WalletServiceImpl();
    private final TransactionListService transactionListService = new TransactionListServiceImpl();
    private final TransactionsService transactionsService = new TransactionsServiceImpl();
    private static TableView<Wallet> walletTable;
    private static double systemBank = 10000.0; // Initial amount for the system bank

    public WalletPage() {
        super();

        addPageHeader();

        addSection("Create a new Wallet", createAddWalletSection());

        walletTable = createWalletTable();
//        refreshWalletTable();
        addSection("Your Wallets", walletTable);
        addSection("Delete wallets", addDeleteAllButton());

        refreshWalletTable();

    }

    private VBox createAddWalletSection() {
        VBox addWalletBox = new VBox();
        addWalletBox.setSpacing(10);

        ComboBox<Currencies> currencyComboBox = new ComboBox<>();
        currencyComboBox.getItems().setAll(Currencies.values());
        currencyComboBox.setPromptText("Select Currency");

        Button addButton = new Button("Create Your Wallet");
        addButton.setOnAction(e -> {
            Currencies selectedCurrency = currencyComboBox.getValue();
            if (selectedCurrency != null) {
                List<Wallet> existingWallets = walletService.getAllWallets();
                Optional<Wallet> duplicateWallet = existingWallets.stream()
                        .filter(wallet -> wallet.getCurrency().equals(selectedCurrency.getSymbol()))
                        .findFirst();
                if (duplicateWallet.isPresent()) {
//                    Alert alert = new Alert(Alert.AlertType.WARNING, "A wallet with this currency already exists.");
//                    alert.showAndWait();
                    var dialog = new Dialog<ButtonType>();
                    dialog.setTitle("Warning");
                    dialog.setHeaderText("A wallet with this currency already exists.");
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                    dialog.initOwner(getScene().getWindow());
                    Optional<ButtonType> buttonType = dialog.showAndWait();
                } else {
                    var wallet = new Wallet(0, 0, selectedCurrency.getSymbol());
                    walletService.createWallet(wallet);
                    refreshWalletTable();
                }
            } else {
//                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a currency.");
//                alert.showAndWait();
                var dialog = new Dialog<ButtonType>();
                dialog.setTitle("Warning");
                dialog.setHeaderText("Please select a currency.");
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                dialog.initOwner(getScene().getWindow());
                Optional<ButtonType> buttonType = dialog.showAndWait();
            }

        });

        addWalletBox.getChildren().addAll(currencyComboBox, addButton);
        return addWalletBox;
    }

    private TableView<Wallet> createWalletTable() {
        TableView<Wallet> tableView = new TableView<>(){};

        TableColumn<Wallet, Number> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getWalletId()));

        TableColumn<Wallet, Number> balanceColumn = new TableColumn<>("Balance");
        balanceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getBalance()));

        TableColumn<Wallet, String> currencyColumn = new TableColumn<>("Currency");
        currencyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCurrency()));

        TableColumn<Wallet, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setCellFactory(param -> getWalletVoidTableColumn());

        tableView.getColumns().addAll(idColumn, balanceColumn, currencyColumn, actionColumn);

        return tableView;

    }

    private @NotNull TableCell<Wallet, Void> getWalletVoidTableColumn() {
        return new TableCell<>() {
            final Button deleteButton = new Button("Delete");
            final Button viewTransactionsButton = new Button("View Transactions");
            final Button createTransactionButton = new Button("Create Transaction");

            {
                deleteButton.setOnAction(e -> {
                    Wallet wallet = getTableView().getItems().get(getIndex());
                    var list = transactionListService.getTransactionsByWalletId(wallet.getWalletId());
//                    transactionListService.removeTransactionFromWallet()
                    list.forEach(p -> transactionListService.removeTransactionFromWallet(p, wallet.getWalletId()));
                    list.forEach(p -> transactionsService.deleteTransaction(p));
                    walletService.deleteWallet(wallet.getWalletId());
//                    transactionsService.deleteTransaction()
                    refreshWalletTable();
                });

                viewTransactionsButton.setOnAction(e -> {
                    Wallet wallet = getTableView().getItems().get(getIndex());
                    showTransactionsForWallet(wallet);
                });

                createTransactionButton.setOnAction(e -> {
                    Wallet wallet = getTableView().getItems().get(getIndex());
                    showCreateTransactionDialog(wallet);
                });

                HBox actionBox = new HBox(deleteButton, viewTransactionsButton, createTransactionButton);
                actionBox.setSpacing(10);
                setGraphic(actionBox);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(getGraphic());
                }
            }
        };
    }


    private void showTransactionsForWallet(Wallet wallet) {
        List<Integer> transactionIds = transactionListService.getTransactionsByWalletId(wallet.getWalletId());
        List<Transactions> transactions = transactionsService.getTransactionsByIds(transactionIds);

        var dialog = new Dialog<ButtonType>();
        dialog.setTitle("Transactions for Wallet ID " + wallet.getWalletId());
        dialog.setHeaderText("List of transactions:");

        TableView<Transactions> transactionTable = new TableView<>();

        TableColumn<Transactions, Number> transactionIdColumn = new TableColumn<>("Transaction ID");
        transactionIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTransactionId()));

        TableColumn<Transactions, Date> transactionDateColumn = new TableColumn<>("Date");
        transactionDateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTransactionDate()));

        TableColumn<Transactions, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));

        TableColumn<Transactions, Number> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAmount()));

        TableColumn<Transactions, String> sourceColumn = new TableColumn<>("Source");
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSource()));

        transactionTable.getColumns().addAll(transactionIdColumn, transactionDateColumn, typeColumn, amountColumn, sourceColumn);
        transactionTable.getItems().addAll(transactions);

        VBox dialogContent = new VBox();
        dialogContent.setSpacing(10);
        dialogContent.getChildren().addAll(transactionTable);
        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);
        dialog.initOwner(getScene().getWindow());

        dialog.showAndWait();
    }

    private void showCreateTransactionDialog(Wallet wallet) {
        var dialog = new Dialog<ButtonType>();
        dialog.setTitle("Create Transaction for Wallet ID " + wallet.getWalletId());
        dialog.setHeaderText("Enter transaction details:");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        VBox dialogContent = new VBox();
        dialogContent.setSpacing(10);
        dialogContent.getChildren().addAll(new Label("Amount:"), amountField);
        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.initOwner(getScene().getWindow());

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                double amount = Double.parseDouble(amountField.getText());
                if (amount > systemBank) {
                    var warningDialog = new Dialog<ButtonType>();
                    warningDialog.setTitle("Warning");
                    warningDialog.setHeaderText("Insufficient funds in the system bank.");
                    warningDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
                    warningDialog.initOwner(getScene().getWindow());
                    warningDialog.showAndWait();
                } else {
                    systemBank -= amount;

                    Transactions newTransaction = new Transactions(transactionsService.getAllTransactions().size()+1, new Date(System.currentTimeMillis()), "achat", wallet.getCurrency(), amount, "System Bank");
                    transactionsService.createTransaction(newTransaction);
                    transactionListService.addTransactionToWallet(newTransaction.getTransactionId(), wallet.getWalletId());

                    wallet.setBalance(wallet.getBalance() + amount);
                    walletService.updateWallet(wallet);
                    refreshWalletTable();
                }
            }
        });
    }



    private Button addDeleteAllButton() {
        Button deleteAllButton = new Button("Delete All Wallets");
        deleteAllButton.setOnAction(e -> {
            List<Wallet> wallets = walletService.getAllWallets();
            for (Wallet wallet : wallets) {
                walletService.deleteWallet(wallet.getWalletId());
            }
            refreshWalletTable();
        });
        return deleteAllButton;
    }

    private void refreshWalletTable() {
//        walletTable.getItems().clear();
        List<Wallet> wallets = walletService.getAllWallets();
        walletTable.getItems().setAll(wallets);

//        walletTable = createWalletTable();
        if (wallets.isEmpty()) {
            walletTable.setPlaceholder(new Label("No wallets available"));
        }
        walletTable.refresh();

//        addSection("your wallets", walletTable);

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
