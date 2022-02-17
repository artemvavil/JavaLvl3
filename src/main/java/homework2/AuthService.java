package homework2;

public interface AuthService {

    void start();

    String getNickname(String login, String password);

    boolean changeNick(String currentNick, String newNick);

    void stop();
}