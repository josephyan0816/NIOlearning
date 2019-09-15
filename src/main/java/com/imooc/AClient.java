package com.imooc;

import java.io.IOException;

/**
 * AClient
 *
 * @author Joseph
 * @version 1.0
 * 2019/9/15 10:12
 **/
public class AClient {

    public static void main(String[] args) throws IOException {
        new NioClient().start("AClient");
    }
}
