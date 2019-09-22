package controllers;

import dbmethods.DBModule;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

class MainController {

    private HashMap<String, String> notes;
    private ListView<String> notesView;
    private TextArea notesArea;
    private NoteNameEditor noteNameEditor;
    private Button addButton;
    private Button deleteButton;
    private DBModule dbModule;

    public HashMap<String, String> getNotes() { return notes; }

    public ListView<String> getNotesView() { return notesView; }

    public DBModule getDbModule() { return dbModule; }

    public MainController(String dbPath) throws ClassNotFoundException, SQLException {
        notes = new HashMap<>();
        notesView = new ListView<>();
        notesArea = new TextArea();
        dbModule = new DBModule(dbPath);
        noteNameEditor = new NoteNameEditor(this);
        download();
    }

    private void download() throws SQLException {
        notes = dbModule.loadFromDB();
        notesView.getItems().addAll(notes.keySet());
    }

    public Scene createScene() {
        buttons();
        notesView();
        notesArea();
        return new Scene(gridPane(), 800, 400);
    }

    private void buttons() {
        addButton();
        deleteButton();
    }

    private void addButton() {
        addButton = new Button("+");
        addButton.setMinWidth(20);
        addButton.setOnAction(e -> {
            String key = new Date().toString();
            String defaultText = "Новая запись";
            notes.put(key, defaultText);
            notesView.getItems().add(key);
            notesView.getSelectionModel().select(key);
            notesArea.setText(defaultText);
            try {
                dbModule.addNote(key, defaultText);
            } catch (SQLException exc) {
                System.err.println(exc.getMessage() + " code: " + exc.getErrorCode());
            }
        });
    }

    private void deleteButton() {
        deleteButton = new Button("-");
        deleteButton.setMinWidth(20);
        deleteButton.setOnAction(e -> {
            String selectedItem = notesView.getSelectionModel().getSelectedItem();
            notes.remove(selectedItem);
            notesArea.setText("");
            notesView.getItems().remove(selectedItem);
            try {
                dbModule.deleteNote(selectedItem);
            } catch (SQLException exc) {
                System.err.println(exc.getMessage() + " code: " + exc.getErrorCode());
            }
        });
    }

    private void notesView() {
        notesView.setOnMouseClicked(e -> {
            String selectedItem = notesView.getSelectionModel().getSelectedItem();
            notesArea.setText(notes.get(selectedItem));
        });
        notesView.setOnKeyPressed(e -> noteNameEditor.changeName());
    }

    private void notesArea() {
        notesArea.setOnMouseExited(e -> {
            String selectedItem = notesView.getSelectionModel().getSelectedItem();
            String text = notesArea.getText();
            notes.put(selectedItem, text);
            try {
                dbModule.editNote(selectedItem, text);
            } catch (SQLException exc) {
                System.err.println(exc.getMessage() + " code: " + exc.getErrorCode());
            }
        });
    }

    private GridPane gridPane() {
        HBox controls = new HBox();
        controls.setSpacing(10);
        controls.getChildren().addAll(addButton, deleteButton);

        GridPane gridPaneForControls = new GridPane();
        gridPaneForControls.setPadding(new Insets(10, 10, 10 ,10));
        gridPaneForControls.setVgap(20);
        gridPaneForControls.setHgap(20);

        notesView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        GridPane.setConstraints(notesView, 0, 0);
        GridPane.setConstraints(controls, 0, 1);
        GridPane.setConstraints(notesArea, 1, 0);

        gridPaneForControls.getChildren().addAll(notesView, controls, notesArea);

        return gridPaneForControls;
    }
}
