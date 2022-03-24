package homework2;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement createUserStatement;
    private static PreparedStatement getUserNicknameStatement;
    private static PreparedStatement changeUserNickStatement;
    private static PreparedStatement deleteUserStatement;
    private static final Logger logger = Logger.getLogger(Database.class.getName());

    public static boolean connect() {
        logger.setLevel(Level.ALL);
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database/chat.db");
            logger.log(Level.INFO, "Connected to the database");
            Statement statement = connection.createStatement();
            createUserTable();
            prepareAllStatement();
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return false;
        }
    }

    public static void disconnect() {
        try {
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public static void createUserTable() throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS user (" +
                "    id       INTEGER      PRIMARY KEY AUTOINCREMENT" +
                "                          NOT NULL" +
                "                          UNIQUE," +
                "    login    VARCHAR (32) UNIQUE" +
                "                          NOT NULL," +
                "    password VARCHAR (32) NOT NULL," +
                "    nickname VARCHAR (32) UNIQUE" +
                "                          NOT NULL" +
                ");"
        );
    }

    public static void prepareAllStatement() throws SQLException {
        createUserStatement = connection.prepareStatement("INSERT INTO user (login, password, nickname) VALUES (?, ?, ?);");
        getUserNicknameStatement = connection.prepareStatement("SELECT nickname FROM user WHERE login = ? AND password = ?;");
        changeUserNickStatement = connection.prepareStatement("UPDATE user SET nickname = ? WHERE nickname = ?;");
        deleteUserStatement = connection.prepareStatement("DELETE FROM user WHERE login = ?;");
    }

    public static boolean createUser(String login, String password, String nickname) {
        try {
            createUserStatement.setString(1, login);
            createUserStatement.setString(2, password);
            createUserStatement.setString(3, nickname);
            createUserStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static String getUserNickname(String login, String password) {
        String nickname = null;
        try {
            getUserNicknameStatement.setString(1, login);
            getUserNicknameStatement.setString(2, password);
            ResultSet rs = getUserNicknameStatement.executeQuery();
            if (rs.next()) {
                nickname = rs.getString(1);
            }
            rs.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        return nickname;
    }

    public static boolean changeUserNick(String currentNick, String newNick) {
        try {
            changeUserNickStatement.setString(1, newNick);
            changeUserNickStatement.setString(2, currentNick);
            changeUserNickStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean deleteUser(String login) {
        try {
            deleteUserStatement.setString(1, login);
            deleteUserStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}



