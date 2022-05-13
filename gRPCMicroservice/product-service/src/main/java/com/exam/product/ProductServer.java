package com.exam.product;

import com.exam.product.api.ProductService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ProductServer {

    private final int port;
    private final Server server;

    public ProductServer(int port) {
        this.port = port;
        var productService = new ProductService();
        this.server = ServerBuilder.forPort(port).addService(productService).build();
    }

    public void start() throws IOException {
        server.start();
        System.out.println("Server started on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutDown() throws InterruptedException {
        if (this.server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        var productServer = new ProductServer(5000);
        productServer.start();
        productServer.blockUntilShutDown();
    }
}
