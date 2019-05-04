package org.ortsevlised;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.ortsevlised.GuessProtocol.*;


public class GuessClient {


    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Connected to Server: " + socket.getInetAddress()+"\n");
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals(BYE))
                    break;

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    out.println(fromUser);
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + SERVER_ADDRESS);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    SERVER_ADDRESS);
            System.exit(1);
        }
    }
}
