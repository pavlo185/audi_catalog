package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BookCatalog extends Application {
    public static final ObservableList<Book> books = FXCollections.observableArrayList();
    BorderPane borderPane;
    Button undoReservationButton;
    Button sortByNameButton;
    Button sortByPriceButton;
    Button sortByQuantityButton;
    private final TextField nameField = new TextField();
    private final TextField priceField = new TextField();
    private final TextField locationField = new TextField();
    private final TextField quantityField = new TextField();
    public static final TextField searchField = new TextField();
    public static final TableView<Book> bookList= new TableView<>();
    private final ChoiceBox<String> searchOptions = new ChoiceBox<>(FXCollections.observableArrayList("quantity", "price","name"));
    private final BookSearcher bookSearcher = new BookSearcher();


    @Override
    public void start(Stage primaryStage) throws IOException {
        Label nameLabel = new Label("Name:");
        Label priceLabel = new Label("Price:");
        Label locationLabel = new Label("Place:");
        Label quantityLabel = new Label("Count:");
        Button addButton = new Button("Add");
        Button deleteButton = new Button("Delete");
        Label searchLabel = new Label("Find by:");
        Button reserveButton = new Button("Reserve");
        undoReservationButton = new Button("Undo reservation");
        Button registerUserButton = new Button("Register User");
        sortByNameButton=new Button("Sort by name");
        sortByPriceButton=new Button("Sort by price");
        sortByQuantityButton = new Button("Sort by quantity");
        searchOptions.getSelectionModel().selectFirst();
        GridPane formPane = new GridPane();
        formPane.addRow(0, nameLabel, nameField);
        formPane.addRow(1, priceLabel, priceField);
        formPane.addRow(2, locationLabel, locationField);
        formPane.addRow(3, quantityLabel, quantityField);
        formPane.add(addButton, 0, 4);
        formPane.add(deleteButton, 1, 4);
        formPane.add(reserveButton, 23, 4);
        formPane.add(undoReservationButton, 24, 4);
        formPane.add(registerUserButton, 25, 4);
        formPane.setHgap(4);
        formPane.setVgap(4);
        formPane.setPadding(new Insets(10));
        Region spacer = new Region();
        spacer.getStyleClass().add("region-with-margin");
        bookList.setPrefHeight(300);
        searchField.setPrefWidth(200);
        searchOptions.setPrefWidth(100);
        HBox searchBox = new HBox(10, searchLabel, searchField, searchOptions);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        borderPane = new BorderPane();
        borderPane.setCenter(bookList);
        borderPane.setTop(formPane);
        borderPane.setBottom(spacer);
        borderPane.setBottom(searchBox);
        borderPane.setRight(new VBox(3,sortByNameButton,sortByPriceButton,sortByQuantityButton));


        TableColumn<Book, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price $");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Book, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Book, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Book, String> reservedStatusColumn = new TableColumn<>("Reserved");
        reservedStatusColumn.setCellValueFactory(new PropertyValueFactory<>("reserved"));

        TableColumn<Book, String> userNameColumn = new TableColumn<>("User Name");
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        bookList.getColumns().addAll(nameColumn, priceColumn, locationColumn, quantityColumn, reservedStatusColumn, userNameColumn);
        bookList.getColumns().forEach(book-> book.setSortable(false));
        bookList.setItems(books);
        // В методе start после создания кнопок
        addButton.getStyleClass().add("button");
        deleteButton.getStyleClass().add("button");
        reserveButton.getStyleClass().add("button");
        undoReservationButton.getStyleClass().add("button");
        registerUserButton.getStyleClass().add("button");
        sortByNameButton.getStyleClass().add("button");
        sortByPriceButton.getStyleClass().add("button");
        sortByQuantityButton.getStyleClass().add("button");
        nameField.getStyleClass().add("text-field");
        priceField.getStyleClass().add("text-field");
        locationField.getStyleClass().add("text-field");
        quantityField.getStyleClass().add("text-field");
        searchField.getStyleClass().add("text-field");
        searchOptions.getStyleClass().add("choice-box");

        addButton.setOnAction(e -> addBook());
        deleteButton.setOnAction(e -> deleteBook());
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchBook());
        reserveButton.setOnAction(e -> reserveBook());
        undoReservationButton.setOnAction(e -> undoReservationBook());
        registerUserButton.setOnAction(e -> registerUser());
        sortByQuantityButton.setOnAction(e->Sorter.sortByQuantity());
        sortByNameButton.setOnAction(e->Sorter.sortByName());
        sortByPriceButton.setOnAction(e->Sorter.sortByPrice());


        ObjectMapper objectMapper = new ObjectMapper();
        List<Book> list = Arrays.asList(objectMapper.readValue(new File("src/main/resources/com/example/demo/books.json"), Book[].class));
        books.addAll(list);
        displayBooks();

        Scene scene = new Scene(borderPane, 1000, 600);
        scene.setFill(Color.rgb(230,230,230,0.7));
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Book Catalog");
        primaryStage.show();
    }

    private void registerUser() {
        int selectedIndex = bookList.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Book selectedBook = books.get(selectedIndex);

            if (selectedBook.isReserved()) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Register User");
                dialog.setHeaderText("Please enter your name:");
                dialog.setContentText("Name:");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    String userName = result.get();
                    selectedBook.setUserName(userName);
                    bookList.refresh();

                    displayBooks();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Reservation Required");
                alert.setContentText("You need to reserve the book first before registering a user.");
                alert.showAndWait();
            }
        }
    }

    private void addBook() {
        String name = nameField.getText();
        double price = Double.parseDouble(priceField.getText());
        String location = locationField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        Book book = new Book(name, price, location, quantity);
        books.add(book);
        displayBooks();
    }

    private void deleteBook() {
        int index = bookList.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            books.remove(index);
            displayBooks();
        }
    }
    public static void displayBooks() {
        ObservableList<Book> books1 = FXCollections.observableArrayList();
        books1.addAll(books);
        bookList.setItems(books1);
    }

    private void undoReservationBook() {
        int selectedIndex = bookList.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Book selectedBook = books.get(selectedIndex);

            if (selectedBook.isReserved()) {
                selectedBook.setReserved(false);
                selectedBook.setUserName(null);
                bookList.refresh();

                displayBooks();
            }
        }
    }

    private void reserveBook() {
        int selectedIndex = bookList.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1) {
            Book selectedBook = books.get(selectedIndex);

            if (!selectedBook.isReserved()) {
                selectedBook.setReserved(true);
                selectedBook.setUserName("{hidden}");
                bookList.refresh();

                displayBooks();
            }
        }
    }
    private void searchBook() {
        if(!searchField.getText().isEmpty()) {
            String searchOption = searchOptions.getValue();
            switch (searchOption) {
                //бинарный поиск 1 алгоритм
                case "price" -> {
                    bookSearcher.searchByPrice();
                    bookList.getColumns().get(1).getStyleClass().add("sorted-column");
                }
                //бинарный поиск 1 алгоритм
                case "quantity" -> {
                    bookSearcher.searchByQuantity();
                    bookList.getColumns().get(3).getStyleClass().add("sorted-column");
                }
                //линейный поиск 2 алогритм
                case "name" -> {
                    bookSearcher.searchByName();
                    bookList.getColumns().get(0).getStyleClass().add("sorted-column");
                }
            }
        } else {
            bookList.getColumns().forEach(column -> column.getStyleClass().removeAll("sorted-column"));
            displayBooks();
        }

    }
    public static void main(String[] args) {
        launch(args);
    }
}
