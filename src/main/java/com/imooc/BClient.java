package com.imooc;

import java.io.IOException;

/**
 * BClient
 *
 * @author Joseph
 * @version 1.0
 * 2019/9/15 10:12
 **/
public class BClient {

    public static void main(String[] args) throws IOException {
        new NioClient().start("BClient");
    }
}
