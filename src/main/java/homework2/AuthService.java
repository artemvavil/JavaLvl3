package homework2;

public interface AuthService {

    void start();

    String getNickByLoginPass(String login, String pass);

    void stop();


    @Override
    public boolean changeNick(String currentNick, String newNick) {
        return false;
    }
}