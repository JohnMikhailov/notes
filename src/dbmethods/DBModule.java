package dbmethods;

import java.sql.*;
import java.util.HashMap;

public class DBModule {

    private String dbPath;

    public DBModule(String dbPath) throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        this.dbPath = dbPath;
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(dbPath);
    }

    public HashMap<String, String> loadFromDB() throws SQLException {
        HashMap<String, String> notes = new HashMap<>();
            Statement statement = connect().createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM notes");
            while (rs.next()) {
                notes.put(rs.getString("alias"), rs.getString("note"));
            }
        return notes;
    }

    public void addNote(String alias, String note) throws SQLException {
        String sql = "INSERT INTO notes(alias, note) VALUES(?, ?)";
        PreparedStatement preparedStatement = connect().prepareStatement(sql);
        preparedStatement.setString(1, alias);
        preparedStatement.setString(2, note);
        preparedStatement.execute();
    }

    public void editNote(String alias, String editedNote) throws SQLException {
        String sql = "UPDATE notes SET note = ? WHERE alias = ?";
        PreparedStatement preparedStatement = connect().prepareStatement(sql);
        preparedStatement.setString(1, editedNote);
        preparedStatement.setString(2, alias);
        preparedStatement.executeUpdate();
    }

    public void editNoteName(String editedAlias, String oldAlias) throws SQLException {
        String sql = "UPDATE notes SET alias = ? WHERE alias = ?";
        PreparedStatement preparedStatement = connect().prepareStatement(sql);
        preparedStatement.setString(1, editedAlias);
        preparedStatement.setString(2, oldAlias);
        preparedStatement.executeUpdate();
    }

    public void deleteNote(String alias) throws SQLException {
        String sql = "DELETE FROM notes WHERE alias = ?";
        PreparedStatement preparedStatement = connect().prepareStatement(sql);
        preparedStatement.setString(1, alias);
        preparedStatement.execute();
    }
}
