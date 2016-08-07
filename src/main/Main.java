package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import srv.SampleServer;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class/Interface purpose.
 *
 * @author vzateychuk
 */
public class Main {
    //  create logger to work with logs
    private final static Logger logger = LogManager.getLogger(Main.class.getName());
    // адрес сервера на котором слушаем порт
    private final static String LOCALHOST = "localhost";
    // порт на который происходит подключение
    public final static int PORT = 5050;

    public static void main(String[] args) {

        // Забираем адрес сервера для прослушивания как первое значение
        String serverAddress = LOCALHOST;
        if (args.length == 1) {
            serverAddress = args[0];
        } else {
            logger.error("Use address as the first argument. default value will be used: "+LOCALHOST);
        }
        // начинаем слушать порт PORT и на каждое подключение создавать сервер, передавать ему сокет и запускать
        try
        {
            int i = 0; // счётчик подключений
            // привинтить сокет на сервер serverAddress
            InetAddress address = InetAddress.getByName(serverAddress);
            ServerSocket server = new ServerSocket(PORT, 0, address);
            logger.info("Server started");
            System.out.println("Server started");

            // слушаем порт
            while(true)
            {
                logger.info("Server is waiting for new client connection on "+address.toString()+":"+PORT);
                // ждём нового подключения, после чего запускаем обработку клиента
                // в новый вычислительный поток и увеличиваем счётчик на единичку
                Socket socket = server.accept();
                SampleServer sampleServer = new SampleServer(i, socket);
                sampleServer.start();
                i++;
            }
        }
        catch(Exception e) {
            logger.error("init error: " + e);
        }
    }
}
