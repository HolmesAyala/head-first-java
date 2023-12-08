package chapter_17.networking_and_threads;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetSocketAddress;

import java.io.IOException;
import java.io.PrintWriter;

import java.nio.channels.SocketChannel;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import javax.swing.WindowConstants;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;

class ChatServerTask implements Runnable {
    private final ChatServer chatServer;

    public ChatServerTask(ChatServer chatServer) {
        this.chatServer = chatServer;
    }

    @Override
    public void run() {
        this.chatServer.start();
    }
}

public class SimpleChatClient {
    private final Logger logger = Logger.getLogger(SimpleChatClient.class.getName());

    private Optional<PrintWriter> serverPrintWriter = Optional.empty();

    private Optional<JTextField> messageTextField = Optional.empty();

    public void start() {
        ChatServer chatServer = new ChatServer();

        Executor executor = Executors.newSingleThreadExecutor();

        executor.execute(new ChatServerTask(chatServer));

        if(this.startConnectionWithServer(chatServer)) {
            this.createUI();
        }
    }

    private void createUI() {
        this.logger.info("Creating UI");

        Font font = new Font("Arial", Font.PLAIN, 32);

        JTextField newMessageTextField = new JTextField();
        newMessageTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        newMessageTextField.setFont(font);

        SimpleChatClient currentSimpleChatClient = this;

        newMessageTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                currentSimpleChatClient.onKeyTypedFromMessageTextField(e.getKeyChar());
            }

            @Override
            public void keyPressed(KeyEvent e) { /* Unused event */ }

            @Override
            public void keyReleased(KeyEvent e) { /* Unused event */ }
        });

        this.messageTextField = Optional.of(newMessageTextField);

        JButton sendButton = new JButton("Send");
        sendButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sendButton.setFont(font);
        sendButton.addActionListener(event -> this.onClickFromSendButton());

        JPanel panel = new JPanel();

        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);

        panel.add(newMessageTextField);
        panel.add(sendButton);

        JFrame frame = new JFrame("Simple chat client");
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setSize(600, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private boolean startConnectionWithServer(ChatServer chatServer) {
        this.logger.info("Starting connection with server");

        if(!verifyIfServerIsAlive(chatServer)) return false;

        InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", chatServer.port());

        try {
            SocketChannel socketChannel = SocketChannel.open(serverAddress);

            PrintWriter printWriter = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8));

            this.serverPrintWriter = Optional.of(printWriter);

            this.logger.info("Connection with server started");

            return true;
        } catch(IOException ioException) {
            this.logger.severe("Unable to open the socket channel with the server");
        }

        return false;
    }

    private boolean verifyIfServerIsAlive(ChatServer chatServer) {
        int maxAttemptsToVerifyIfSeverIsAlive = 3;
        int attempt = 1;

        while(attempt++ <= maxAttemptsToVerifyIfSeverIsAlive) {
            this.logger.log(Level.INFO, String.format("Attempt %d of %d to verify if server is alive", attempt, maxAttemptsToVerifyIfSeverIsAlive));

            if(chatServer.isAlive()) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException interruptedException) {
                this.logger.severe("Unable to sleep the thread");
                Thread.currentThread().interrupt();
                
                return false;
            }

            if(attempt > maxAttemptsToVerifyIfSeverIsAlive) {
                this.logger.severe("Unable to verify if server is alive");

                return false;
            }
        }

        return true;
    }

    private void onClickFromSendButton() {
        this.extractAndSendMessageFromMessageTextField();
    }

    private void onKeyTypedFromMessageTextField(char keyChar) {
        if(keyChar == '\n') {
            this.extractAndSendMessageFromMessageTextField();
        }
    }

    private void extractAndSendMessageFromMessageTextField() {
        this.messageTextField.ifPresent(textField -> {
            if(textField.getText().isBlank()) return;

            this.sendMessageToServer(textField.getText().trim());
            textField.setText("");
            textField.requestFocus();
        });
    }

    private void sendMessageToServer(String message) {
        this.serverPrintWriter.ifPresent(printWriter -> {
            printWriter.println(message);
            printWriter.flush();
        });
    }
}
