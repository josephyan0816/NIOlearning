package com.imooc;

import java.io.IOException;

/**
 * CClient
 *
 * @author Joseph
 * @version 1.0
 * 2019/9/15 10:13
 **/
public class CClient {

    public static void main(String[] args) throws IOException {
        new NioClient().start("CClient");
    }
}
