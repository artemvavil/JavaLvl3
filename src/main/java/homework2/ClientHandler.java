package homework2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler {
    private MyServer myServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    private String name;

    public String getName() {
        return name;
    }

    public ClientHandler(MyServer myServer, Socket socket) {
        logger.setLevel(Level.ALL);
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);

        try {
            this.myServer = myServer;
            this.socket = socket;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.name =getName();
            new Thread(() -> {
                try {
                    authentication();
                    readMessages();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException("Проблемы при создании обработчика клиента");
        }
    }

    public void authentication() throws IOException {
        while (true) {
            String str = in.readUTF();
            if (str.startsWith("/auth")) {
                logger.log(Level.FINE, "Пользователь проходит аутентификацию");
                String[] parts = str.split("\\s");
                String nick = myServer.getAuthService().getNickname(parts[1], parts[2]);
                if (nick != null) {
                    if (!myServer.isNickBusy(nick)) {
                        sendMsg("/authok " + nick);
                        name = nick;
                        myServer.broadcastMsg(name + " зашел в чат");
                        myServer.subscribe(this);
                        return;
                    } else {
                        sendMsg("Учетная запись уже используется");
                    }
                } else {
                    sendMsg("Неверные логин/пароль");
                }
                logger.log(Level.FINE, "Пользователь не прошел аутентификацию");
            }

        }
    }

    public void changeNick() throws IOException {
        while (true) {
            String str = in.readUTF();

            if (str.startsWith("/changenick ")) {
                logger.log(Level.FINER,"User " + this.name + " is trying to change nickname");
                String newNick = str.split("\\s", 2)[1];
                if (newNick.contains(" ")) {
                    sendMsg("Nickname cannot contain spaces");
                    logger.log(Level.FINER,"User " + this.name +
                            "'s new nickname contains invalid characters");
                    continue;
                }
                if (myServer.getAuthService().changeNick (this.name, newNick)) {
                    this.name = newNick;
                    sendMsg("/changenick " + newNick);
                    sendMsg("Nickname has been changed");
                    myServer.broadcastClientsList();
                } else {
                    sendMsg("Nickname is already taken");
                    logger.log(Level.FINER,"User " + this.name +
                            "'s new nickname is already taken");
                }
            }
        }
    }

    public void readMessages() throws IOException {
        while (true) {
            String strFromClient = in.readUTF();
            System.out.println("от " + name + ": " + strFromClient);
            if (strFromClient.equals("/end")) {
                return;
            }
            myServer.broadcastMsg(name + ": " + strFromClient);
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public void closeConnection() {
        myServer.unsubscribe(this);
        myServer.broadcastMsg(name + " вышел из чата");
        try {
            in.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        try {
            out.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
        try {
            socket.close();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
