
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Objects;

public class GreetClient {
   public static void main(String[] args) throws Exception {
      String serverName = args[0];
      int port = Integer.parseInt(args[1]);

      Scanner input = new Scanner(System.in);
      System.out.print("CLIENT: ");
      String firstInput = "";

      while (!Objects.equals(firstInput, "EXIT")) {
         firstInput = input.nextLine();
         if (Objects.equals(firstInput, "HELP")) {
            System.out.println("*** These are the commands that you can use:-"
                  + "\nHELLO: required for starting the STDP session."
                  + "\nHELP:  displays the help information about the protocol. It may be received any time whether and STDP session is established or not."
                  + "\nDOW: returns the day of week" + "\nTIME: returns the current time."
                  + "\nDATE: returns the current date." + "\nDATETIME: returns the current date and time."
                  + "\nBYE: ends the session.");
            System.out.print("CLIENT: ");
         } else if (Objects.equals(firstInput, "HELLO")) {
            break;
         } else {
            System.out.print("CLIENT: ");
         }
      }

      if (Objects.equals(firstInput, "HELLO")) {

         try {
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);

            System.out.println("Just connected to " + client.getRemoteSocketAddress() + "\n");

            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            out.writeUTF("Hello from " + client.getLocalSocketAddress());
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            System.out.println(in.readUTF());
            System.out.print("CLIENT: ");

            // string to read message from input
            String line = "";
            String inBound = "";
            String onBye = ((Object) client.getRemoteSocketAddress()).toString();
            int index = onBye.indexOf("/");
            String address = onBye.substring(index, onBye.length());

            while (!line.equals("BYE") && !line.equals("EXIT")) {
               try {
                  line = input.nextLine();
                  out.writeUTF(line);
                  inBound = in.readUTF();
                  System.out.println("SERVER: " + inBound);
                  if ((Objects.equals(inBound, "Input Invalid - Closing connection. \n"))
                        || (Objects.equals(inBound, "Thank you for connecting to " + address + "\nGoodbye!"))) {

                     break;
                  }
                  System.out.print("CLIENT: ");
               } catch (IOException i) {
               }
            }

            client.close();
            input.close();
         } catch (IOException e) {
            // System.out.println("Exception");
            // e.printStackTrace();
         }
      }
   }
}