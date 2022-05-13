package com.exam.product.api;

public class ProductService extends ProductServiceGrpc.ProductServiceImplBase {

    @Override
    public void getProduct(
            Resources.HelloRequest request, io.grpc.stub.StreamObserver<Resources.HelloResponse> responseObserver) {
        var getProductResponse =
                Resources.HelloResponse.newBuilder()
                        .setGreeting("200")
                        .build();
        responseObserver.onNext(getProductResponse);
        responseObserver.onCompleted();
    }
}
