package com.imooc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * NioClient
 *
 * @author Joseph
 * @version 1.0
 * 2019/9/15 9:13
 **/
public class NioClient {

    public void start(String nickName) throws IOException {
        //连接服务器
        SocketChannel socketChannel = SocketChannel
                .open(new InetSocketAddress("127.0.0.1", 8000));

        //接收服务端响应

        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        new Thread(new NioClientHandler(selector)).start();

//向服务器发送数据

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String request = scanner.nextLine();
            if (request != null && request.length() > 0) {
                socketChannel.write(Charset.forName("UTF-8").encode(nickName+":"+request));
            }
        }

        //接收服务器响应


    }

    public static void main(String[] args) throws IOException {
      //  new NioClient().start("");
    }
}
