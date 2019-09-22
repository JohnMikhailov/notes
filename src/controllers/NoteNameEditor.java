package controllers;

import database.DBModule;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.HashMap;

class NoteNameEditor {

    private ListView<String> notesView;
    private HashMap<String, String> notes;
    private DBModule dbModule;
    private Label label;
    private Button confirm;
    private TextField textFieldKeyChanging;
    private Stage changeKeyStage;

    public NoteNameEditor(MainController mainController) {
        this.notes = mainController.getNotes();
        this.notesView = mainController.getNotesView();
        this.dbModule = mainController.getDbModule();
    }

    public void button() {
        confirm = new Button("Изменить название записи");
        confirm.setMaxWidth(200);
        confirm.setOnAction(e -> {
            label.setText("");
            String selectedItem = notesView.getSelectionModel().getSelectedItem();
            String newKey = textFieldKeyChanging.getText();
            if (notes.containsKey(newKey) && notes.containsKey(newKey.toUpperCase())
                    && notes.containsKey(newKey.toLowerCase())) {
                label.setText("Название " + newKey + " уже существует");
                return;
            }
            String value = notes.get(selectedItem);
            notes.remove(selectedItem);
            notes.put(newKey, value);
            notesView.getItems().remove(selectedItem);
            notesView.getItems().add(newKey);
            notesView.getSelectionModel().select(newKey);
            try {
                dbModule.editNoteName(newKey, selectedItem);
            } catch (SQLException exc) {
                System.err.println(exc.getMessage() + " code: " + exc.getErrorCode());
            }
            changeKeyStage.close();
        });
    }

    public void changeName() {
        VBox keyChanging = new VBox();
        keyChanging.setAlignment(Pos.CENTER);
        keyChanging.setSpacing(10);
        textFieldKeyChanging = new TextField();
        textFieldKeyChanging.setMaxWidth(200);
        label = new Label("");
        changeKeyStage = new Stage();
        button();
        keyChanging.getChildren().addAll(label, textFieldKeyChanging, confirm);
        changeKeyStage.setScene(new Scene(keyChanging, 300, 150));
        changeKeyStage.show();
    }
}
