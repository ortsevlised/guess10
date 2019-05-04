package org.ortsevlised;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import static org.ortsevlised.GuessProtocol.*;


public class GuessServer {

    private static final String EMPTY = "";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Running Server: " + "Host=" + SERVER_ADDRESS + " Port=" + PORT);
            Socket clientSocket = serverSocket.accept();

            String clientAddress = clientSocket.getInetAddress().getHostAddress();
            System.out.println("New client connected from " + clientAddress);

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;


            GuessProtocol gameProtocol = new GuessProtocol();
            gameProtocol.setNumberToGuess(getRandomNumber());
            outputLine = gameProtocol.processInput(EMPTY);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = gameProtocol.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.contains(SERVER_WINS) || outputLine.contains(CLIENT_WINS)) {
                    gameProtocol.setNumberToGuess(getRandomNumber());
                }
                if (outputLine.equals(BYE))
                    break;
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + PORT + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }


    private static int getRandomNumber() {
        int number = new Random().nextInt(1000);
        System.out.println(number + 1);

        return number + 1;
    }

}
