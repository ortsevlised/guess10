package org.ortsevlised;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.ortsevlised.GuessProtocol.*;


public class GuessClient {
    private static int portNumber = PORT;
    private static String hostName = SERVER_ADDRESS;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Connecting to server on default values, if you would like to specify a hostname and port" +
                    "\nplease run the application as 'java GuessClient <host number> <port number>'\n");
        } else {
            hostName = args[0];
            portNumber = Integer.parseInt(args[1]);
        }

        try (
                Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            System.out.println("Connected to Server: " + socket.getInetAddress() + "\n");
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
            System.err.println("Unknown host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }
}
