package com.imooc;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * NioClientHandler
 *
 * @author Joseph
 * @version 1.0
 * 2019/9/15 9:50
 **/
/*
客户端线程类，专门接收服务器端响应信息
 */

public class NioClientHandler implements Runnable{

  private Selector selector;

    public NioClientHandler(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
     try{
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

                 if(selectionKey.isReadable())
                 {
                     readHandler(selectionKey,selector);
                 }



             }

         }
     }catch (IOException e)
     {
         e.printStackTrace();
     }

    }
    private void readHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

        //创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);


        //循环读取服务器端的响应信息
        String response="";

        while (socketChannel.read(byteBuffer)>0)
        {
            byteBuffer.flip();

           response+= Charset.forName("UTF-8").decode(byteBuffer);

        }

        socketChannel.register(selector,SelectionKey.OP_READ);


        //将服务器端响应信息打印到本地
        if(response.length()>0)
        {
            System.out.println(response);
        }

    }
}
