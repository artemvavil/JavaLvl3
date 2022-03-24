package homework2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServer {
    private final int PORT = 8189;

    private List<ClientHandler> clients;
    private AuthService authService;
    private ExecutorService clientsExecutorService;
    private static final Logger logger = Logger.getLogger(MyServer.class.getName());

    public AuthService getAuthService() {
        return authService;
    }

    public MyServer() {
        logger.setLevel(Level.ALL);
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);

        clientsExecutorService = Executors.newCachedThreadPool();
        try (ServerSocket server = new ServerSocket(PORT)) {
            authService = new BaseAuthService();
            authService.start();
            clients = new ArrayList<>();
            while (true) {
                logger.log(Level.INFO, "Сервер ожидает подключения " );
                Socket socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } finally {
            clientsExecutorService.shutdown();
            Database.disconnect();
            logger.log(Level.INFO, "Серер отключился");
        }
    }
    public ExecutorService getClientsExecutorService() {
        return clientsExecutorService;
    }

    public synchronized void sendMsgToClient(ClientHandler from, String nickTo, String msg) {
        for (ClientHandler o : clients) {
            if (o.getName().equals(nickTo)) {
                o.sendMsg("от " + from.getName() + ": " + msg);
                from.sendMsg("клиенту " + nickTo + ": " + msg);
                return;
            }
        }
        from.sendMsg("Участника с ником " + nickTo + " нет в чат-комнате");
    }
    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler o : clients) {
            if (o.getName().equals(nick)) {
                return true;
            }
        }
        return false;
    }
    public synchronized void broadcastMsg(String msg) {
        logger.log(Level.FINEST,"User " + logger.getName() + " sent a message");
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }
    public synchronized void broadcastClientsList() {
        StringBuilder sb = new StringBuilder("/clients ");
        for (ClientHandler o : clients) {
            sb.append(o.getName() + " ");
        }
        broadcastMsg(sb.toString());
    }
    public synchronized void unsubscribe(ClientHandler o) {
        clients.remove(o);
        broadcastClientsList();
        logger.log(Level.FINE, "User " + logger.getName() + " disconnected");
    }
    public synchronized void subscribe(ClientHandler o) {
        clients.add(o);
        broadcastClientsList();
    }
}
