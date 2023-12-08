package chapter_17.networking_and_threads;

import java.net.InetSocketAddress;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.Optional;

class ClientHandler implements Runnable {
    private final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    private final BufferedReader bufferedReader;

    public ClientHandler(SocketChannel clientSocketChannel) {
        this.bufferedReader = new BufferedReader(Channels.newReader(clientSocketChannel, StandardCharsets.UTF_8));
    }

    @Override
    public void run() {
        this.logger.info("Start of run method");

        String message;

        try {
            while((message = this.bufferedReader.readLine()) != null) {
                this.logger.info("Message from client: " + message);
            }
        } catch (Exception exception) {
            this.logger.warning("Unable to get message from client. " + exception.getMessage());
        }

        this.logger.info("End of run method");
    }
}

public class ChatServer {
    private final int socketPort = 5000;

    private final Logger logger = Logger.getLogger(ChatServer.class.getName());

    private Optional<ServerSocketChannel> serverSocketChannel = Optional.empty();

    private ArrayList<PrintWriter> clientsWriters = new ArrayList<>();

    public void start() {
        ExecutorService executorService = Executors.newCachedThreadPool();

        try {
            this.logger.info("Opening server socket");

            ServerSocketChannel newServerSocketChannel = ServerSocketChannel.open();
            newServerSocketChannel.bind(new InetSocketAddress("127.0.0.1", this.socketPort));

            this.serverSocketChannel = Optional.of(newServerSocketChannel);

            while(this.serverSocketChannel.orElseThrow().isOpen()) {
                this.logger.info("Waiting client connection");

                SocketChannel clientSocketChannel = this.serverSocketChannel.orElseThrow().accept();

                PrintWriter printWriter = new PrintWriter(Channels.newWriter(clientSocketChannel, StandardCharsets.UTF_8));

                this.clientsWriters.add(printWriter);

                ClientHandler clientHandler = new ClientHandler(clientSocketChannel);

                executorService.submit(clientHandler);

                this.logger.info("Client connection created");
            }
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public boolean isAlive() {
        return this.serverSocketChannel.isPresent() && this.serverSocketChannel.get().isOpen();
    }

    public int port() {
        return this.socketPort;
    }
}
