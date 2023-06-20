package com.nantianba.study.feature.jdk11;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClient实验 {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

// 构建请求
        HttpRequest req = HttpRequest.newBuilder(URI.create("https://www.zhihu.com"))
                .build();

// 发送请求
        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

        System.out.println("Headers");
        resp.headers().map().forEach((k, v) -> System.out.println("\t" + k + ":" + v.get(0)));
        System.out.println("resp.version() = " + resp.version());
        System.out.println("resp.statusCode() = " + resp.statusCode());
        System.out.println("resp.body() = " + resp.body().length());

    }
}
