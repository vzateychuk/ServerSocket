package srv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class SampleServer extends Thread {
    //  create logger to work with logs
    private final static Logger logger = LogManager.getLogger(SampleServer.class.getName());
    // сокет подключившийся к серверу
    private Socket socket;
    // номер - идентификатор сервера
    private int num;
    // входной

    // создаем сервер
    public SampleServer(int num, Socket socket) {
        // копируем данные - созданный сокет и номер сервера
        this.num = num;
        this.socket = socket;

        // определяем приоритет и помечаем новый вычислительный поток как Daemon
        // setDaemon(true);
        setPriority(NORM_PRIORITY);

        // пишем в лог об успешном создании сервера
        logger.info("Created SampleServer("+num+"), socket="+socket.toString());
    }

    // обработка сервером входящих подключений
    public void run()
    {
        logger.info("Run SampleServer("+num+") in Thread.id=" + this.getId());
        try
        {
            // из сокета клиента берём поток входящих данных
            InputStream in = socket.getInputStream();
            InputStreamReader inr = new InputStreamReader(in);
            BufferedReader bin = new BufferedReader(inr);

            // и оттуда же - поток данных от сервера к клиенту
            OutputStream out = socket.getOutputStream();
            OutputStreamWriter wout = new OutputStreamWriter(out);
            BufferedWriter bout = new BufferedWriter(wout);
            PrintWriter pout = new PrintWriter(bout, true);

            // Строка куда будет записываться информация, присылаемая клиентом
            String data = "";
            // буффер данных в 64 килобайта
            // byte buf[] = new byte[64*1024];

            // Пока сервер не прислал сигнал к завершению работы, читаем в цикле информацию из входного потока
            while (!data.equals("Bue.")) {
                // читаем 64кб от клиента, результат - кол-во реально принятых данных
                // int bytes_read = in.read(buf);

                // создаём строку, содержащую полученную от клиента информацию
                // data = new String(buf, 0, bytes_read);
                data = bin.readLine();
                // возвращаем клиенту присланную строку:
                // out.write(data.getBytes());
                pout.println(data);
                logger.info("Server("+num+") responded:"+data);
                // System.out.println("Server("+num+") responded:"+data);
            }

            // завершаем соединение
            socket.close();
        }
        catch(Exception e) {
            // вывод исключений
            logger.error("init error: "+e);
        }

        // logging
        logger.info("Server("+num+") stopped Thread.id="+this.getId());
    }
}
