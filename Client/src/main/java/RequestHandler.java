import io.grpc.stub.StreamObserver;
import org.example.grpc.HelloServiceGrpc;

public class RequestHandler extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void hello(
            org.example.grpc.MessageRequest request, StreamObserver<org.example.grpc.MessageResponse> responseObserver) {
        org.example.grpc.MessageResponse response = org.example.grpc.MessageResponse.newBuilder().setMsgResponse
                ("200").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
