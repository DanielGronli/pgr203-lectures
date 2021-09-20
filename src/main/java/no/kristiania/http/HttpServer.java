package no.kristiania.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    private final ServerSocket serverSocket;

    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        new Thread(this::handleClient).start();
    }

    private void handleClient() {
        try {
            Socket clientSocket = serverSocket.accept();

            String[] requestLine = HttpClient.readLine(clientSocket).split(" ");
            String requestTarget = requestLine[1];
            
            if (requestTarget.equals("/hello")) {
                String responseBody = "<p>Hello world</p>";

                String responseMessage = "HTTP/1.1 200 OK\r\n" +
                        "Content-Length: " + responseBody.length() + "\r\n" +
                        "\r\n" +
                        responseBody;
                clientSocket.getOutputStream().write(responseMessage.getBytes());
                return;
            }
            

            String responseBody = "File not found: " + requestTarget;

            String responseMessage = "HTTP/1.1 404 Not found\r\n" +
                    "Content-Length: " + responseBody.length() + "\r\n" +
                    "\r\n" +
                    responseBody;
            clientSocket.getOutputStream().write(responseMessage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);


        Socket clientSocket = serverSocket.accept();
        
        
        String requestLine = HttpClient.readLine(clientSocket);

        System.out.println(requestLine);
        
        String headerLine;
        while (!(headerLine = HttpClient.readLine(clientSocket)).isBlank()) {
            System.out.println(headerLine);
        }
        
        
        String messageBody = "Hello world";

        String responseMessage = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + messageBody.length() + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                messageBody;
        clientSocket.getOutputStream().write(responseMessage.getBytes());
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }
}
