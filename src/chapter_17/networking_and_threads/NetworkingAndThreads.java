package chapter_17.networking_and_threads;

import java.io.IOException;
import java.io.Reader;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;
import java.nio.channels.ServerSocketChannel;

public class NetworkingAndThreads {
    public void start() {
        SimpleChatClient simpleChatClient = new SimpleChatClient();

        simpleChatClient.start();
    }
}
