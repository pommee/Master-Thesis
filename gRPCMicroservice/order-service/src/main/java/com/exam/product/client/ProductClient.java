package com.exam.product.client;

import com.exam.product.api.ProductServiceGrpc;
import com.exam.product.api.Resources;
import io.grpc.ManagedChannelBuilder;

public class ProductClient {

    private final ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    public ProductClient(String host, int port) {
        var managedChannel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.productServiceBlockingStub = ProductServiceGrpc.newBlockingStub(managedChannel);
    }

    public Resources.HelloResponse call() {
        var productRequest = Resources.HelloRequest.newBuilder().setMessage("hello").build();
        var productResponse = productServiceBlockingStub.getProduct(productRequest);
        return productResponse;
    }

    public static void main(String[] args) {
        var client = new ProductClient("localhost", 5000);
        long begin = System.currentTimeMillis();
        long averageRequestTime = 0;

        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                long requestStart = System.nanoTime();
                String code = String.valueOf(client.call());
                long requestStop = System.nanoTime();
                code = code.replaceAll("greeting: ", "").replace("\"", "").strip();
                if (code.equals("200")) {
                    averageRequestTime += requestStop - requestStart;
                } else {
                    System.out.println("Request: " + i + " failed");
                }
            }
        }
        averageRequestTime /= (long) 100 * 100;
        long end = System.currentTimeMillis();
        System.out.println("gRPC - Microservice");
        System.out.println("Iterations: " + 100);
        System.out.println("Message Count: " + 100);
        System.out.println("Total time: " + (end - begin) + " ms");
        System.out.println("Average request time: " + averageRequestTime + " nanoseconds");
    }
}
