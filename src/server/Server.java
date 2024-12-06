package server;

import protocol.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerSocket ss;

        try {
            ss = new ServerSocket(Connection.PORT);
        } catch (IOException e) {
            System.out.println("Errore nella creazione del server");
            System.exit(1);
            return;
        }

        while (true) {
            try {
                Socket s = ss.accept();
                ConnectionManager net = new ConnectionManager(s);
                net.on(UserMessage.class, msg -> {
                    try {
                        if (Integer.parseInt(msg.color, 16) > 0xffffff) {
                            net.send(new ResponseMessage("Colore non valido").toString());
                        }
                    } catch (NumberFormatException e) {
                        net.send(new ResponseMessage("Colore non valido").toString());
                        return;
                    }

                    User user = new User(net, msg.uname, msg.color);
                    user.net.addDisposeRunnable(() -> {
                        Channel.leaveChannel(user);
                        User.removeUser(user);
                    });
                    User.addUser(user);
                }).on(IllformedMessage.class, msg -> {
                    switch (msg.message.charAt(0)) {
                        case 'o':
                        case 'u':
                        case 'c':
                            net.send(new ResponseMessage("Messaggio Malformato").toString());
                            break;

                        default:
                            System.out.println("Messaggio malformato monodirezionale");
                    }
                });
                net.start();
            } catch (IOException e) {
                System.out.println("Errore nell'apertura della connessione");
            }
        }
    }
}
