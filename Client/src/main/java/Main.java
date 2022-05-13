import Model.Message;
import Model.Settings;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static ArrayList<Message> messages = new ArrayList<>();
    private static Settings settings;


    public static void main(String[] args) throws IOException, InterruptedException {
        menu();
    }

    static void menu() throws IOException, InterruptedException {
        while (true) {
            System.out.println("1) Run");
            System.out.println("2) Settings");
            System.out.println("3) Results");
            System.out.println("4) Terminate");
            System.out.print("> ");
            switch (scanner.nextInt()) {
                case 1:
                    System.out.println("1) REST-API Monolith");
                    System.out.println("2) REST-API Microservices");
                    System.out.println("3) gRPC Monolith");
                    System.out.println("4) gRPC Microservices");
                    switch (scanner.nextInt()) {
                        case 1:
                            restApiMonolith();
                            break;
                        case 2:
                            restApiMicroservice();
                            break;
                        case 3:
                            GrpcMonolith();
                            break;
                        case 4:
                            System.out.println("Not yet implemented");
                            break;
                    }
                case 2:
                    break;
                default:
                    break;
            }
        }
    }

    static void restApiMonolith() throws IOException, InterruptedException {
        initResources();

        final HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        long begin = System.currentTimeMillis();

        long averageRequestTime = 0;

        for (int i = 0; i < settings.getIterations(); i++) {
            for (Message message : messages) {
                Map<Object, Object> data = new HashMap<>();
                data.put("id", message.getId());
                data.put("message", message.getMessage());
                data.put("date", message.getDate());
                HttpRequest request = HttpRequest.newBuilder()
                        .POST(buildFormDataFromMap(data))
                        .uri(URI.create("http://localhost:8080/api/message"))
                        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                        .header("Content-Type", "application/json")
                        .build();

                long requestStart = System.nanoTime();

                try {
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                } catch (ConnectException ignored) {
                    System.out.println("Please start server first!");
                    menu();
                }

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                long requestStop = System.nanoTime();
                if (response.statusCode() != 200) {
                    System.out.println("Request: " + i + " failed");
                } else {
                    averageRequestTime += requestStop - requestStart;
                }
            }
        }
        averageRequestTime /= (long) settings.getIterations() * settings.getMessagesCount();
        long end = System.currentTimeMillis();
        printResults("REST API", "Monolithic", settings.getIterations(), settings.getMessagesCount(), end - begin, averageRequestTime);
    }

    static void restApiMicroservice() throws IOException, InterruptedException {
        initResources();

        final HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        long averageRequestTime = 0;

        long begin = System.currentTimeMillis();
        for (int i = 0; i < settings.getIterations(); i++) {
            for (Message message : messages) {
                Map<Object, Object> data = new HashMap<>();
                data.put("id", message.getId());
                data.put("message", message.getMessage());
                data.put("date", message.getDate());
                HttpRequest request = HttpRequest.newBuilder()
                        .POST(buildFormDataFromMap(data))
                        .uri(URI.create("http://localhost:8080/api/message"))
                        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                        .header("Content-Type", "application/json")
                        .build();

                long requestStart = System.nanoTime();

                try {
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                } catch (ConnectException ignored) {
                    System.out.println("Please start server first!");
                    menu();
                }

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                long requestStop = System.nanoTime();
                if (response.statusCode() != 200) {
                    System.out.println("Request: " + i + " failed");
                } else {
                    averageRequestTime += requestStop - requestStart;
                }
            }
        }
        averageRequestTime /= (long) settings.getIterations() * settings.getMessagesCount();
        long end = System.currentTimeMillis();
        printResults("REST API", "Monolithic", settings.getIterations(), settings.getMessagesCount(), end - begin, averageRequestTime);
    }

    static void GrpcMonolith() throws IOException, InterruptedException {
        initResources();

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        long averageRequestTime = 0;

        org.example.grpc.HelloServiceGrpc.HelloServiceBlockingStub stub
                = org.example.grpc.HelloServiceGrpc.newBlockingStub(channel);

        long begin = System.currentTimeMillis();
        for (int i = 0; i < settings.getIterations(); i++) {
            for (Message message : messages) {
                Map<Object, Object> data = new HashMap<>();
                data.put("id", message.getId());
                data.put("message", message.getMessage());
                data.put("date", message.getDate());

                long requestStart = System.nanoTime();
                try {
                    org.example.grpc.MessageResponse helloResponse = stub.hello(org.example.grpc.MessageRequest.newBuilder()
                            .setId(1)
                            .setMessage(settings.getCustomMessage())
                            .setDate("2022 04 10")
                            .build());
                    long requestStop = System.nanoTime();
                    if (!(helloResponse.getMsgResponse().equals("200"))) {
                        System.out.println("Request: " + i + " failed");
                    } else {
                        averageRequestTime += requestStop - requestStart;
                    }
                } catch (StatusRuntimeException ignored) {
                    System.out.println("Please start server first!");
                    menu();
                }
            }
        }
        channel.shutdown();
        averageRequestTime /= (long) settings.getIterations() * settings.getMessagesCount();
        long end = System.currentTimeMillis();
        printResults("REST API", "Monolithic", settings.getIterations(), settings.getMessagesCount(), end - begin, averageRequestTime);
    }

    static void initResources() {
        try {
            settings = readSettings();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < settings.getMessagesCount(); i++) {
            messages.add(new Message(i, "test", LocalDateTime.now()));
        }
    }

    static Settings readSettings() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        JSONObject a = (JSONObject) parser.parse(new FileReader("E:\\FinalExam\\Client\\src\\main\\java\\settings.json"));

        int iterations = Integer.parseInt((String) a.get("iterations"));
        int messagesCount = Integer.parseInt((String) a.get("messagesCount"));
        String customMessage = (String) a.get("customMessage");

        return new Settings(iterations, messagesCount, customMessage);
    }

    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    private static void printResults(String method, String architecture, int iterations, int messageCount, long totalTime, long averageRequestTime) throws IOException, InterruptedException {
        System.out.println(method + " - " + architecture);
        System.out.println("Iterations: " + iterations);
        System.out.println("Message Count: " + messageCount);
        System.out.println("Total time: " + totalTime + " ms");
        System.out.println("Average request time: " + averageRequestTime + " nanoseconds");
    }
}

