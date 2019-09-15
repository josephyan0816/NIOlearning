package com.imooc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * NioServer
 *
 * @author Joseph
 * @version 1.0
 * 2019/9/15 9:13
 **/

public class NioServer {


    public void start() throws IOException {
        //1.创建Selector
        Selector selector = Selector.open();
        //2.通过ServerSocketChannel创建channel通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //3.为channel通道绑定监听窗口
        serverSocketChannel.bind(new InetSocketAddress(8000));
        //4.设置channel为非阻塞式
        serverSocketChannel.configureBlocking(false);
        //5.将channel注册到selector上，监听连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动成功");

        //6.循环等待新入连接

        for(;;)
        {
            int readyChannels = selector.select();

            if(readyChannels==0) continue;
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext())
                {
                    SelectionKey selectionKey = iterator.next();

                    iterator.remove();


                    //7.根据就绪方法，调用对应方法处理业务逻辑

                    if(selectionKey.isAcceptable())
                    {
                        acceptHandler(serverSocketChannel,selector);
                    }

                    if(selectionKey.isReadable())
                    {
                        readHandler(selectionKey,selector);
                    }



                }

        }


    }

    private void acceptHandler(ServerSocketChannel serverSocketChannel,Selector selector) throws IOException {

        SocketChannel socketChannel = serverSocketChannel.accept();

        socketChannel.configureBlocking(false);

        socketChannel.register(selector,SelectionKey.OP_READ);


        socketChannel.write(Charset.forName("UTF-8")
                .encode("您与聊天室里其他人都不是朋友关系，请注意隐私安全"));



    }

    private void readHandler(SelectionKey selectionKey,Selector selector) throws IOException {
       SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        String request="";

        while (socketChannel.read(byteBuffer)>0)
        {
            byteBuffer.flip();

            request+=Charset.forName("UTF-8").decode(byteBuffer);

        }

        socketChannel.register(selector,SelectionKey.OP_READ);


        if(request.length()>0)
        {
            System.out.println("::"+request);
            broadCast(selector,socketChannel,request);
        }

    }

    private void broadCast(Selector selector,SocketChannel sourceChannel,String request)
    {

        //获取所有已经介入到客户端channel
        Set<SelectionKey> selectionKeySet = selector.keys();

        //循环向所有channel广播
        selectionKeySet.forEach(selectionKey -> {
            Channel targetChannel = selectionKey.channel();


            //剔除发消息的客户端
            if(targetChannel instanceof SocketChannel &&
            targetChannel!=sourceChannel)
            {
                try{
                    ((SocketChannel)targetChannel)
                            .write(Charset.forName("UTF-8").encode(request));

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        });

    }


    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer();
        nioServer.start();

    }
}
