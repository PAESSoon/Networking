import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.time.LocalDate;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;

public class GreetServer {

   /**
    * Runs the server. When a client connects, the server creates a new thread The
    * maximum number of threads via a thread pool set to 20 (otherwise there can be
    * a clients requests overload that could cause the server to run out of
    * resources by allocating too many threads).
    */
   public static void main(String[] args) throws Exception {

      int port = Integer.parseInt(args[0]);
      try {
         ServerSocket listener = new ServerSocket(port);

         System.out.println("Server is running...");
         var pool = Executors.newFixedThreadPool(20);
         while (true) {
            pool.execute(new GreetingServer(listener.accept()));
         }

      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   private static class GreetingServer implements Runnable {
      private Socket socket;

      GreetingServer(Socket socket) {
         this.socket = socket;
      }

      public void run() {
         try {

            System.out.println("Waiting for client on port " + socket.getLocalPort() + "...");

            System.out.println("Connected: " + socket.getRemoteSocketAddress());

            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            System.out.println("CLIENT : " + in.readUTF());

            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            out.writeUTF("Hello CCCS431 SDTP Server written by ... " + "READY" + "\n\n" + "Hit enter to stop. \n");

            String day = LocalDate.now().getDayOfWeek().name();
            Date time = new java.util.Date(System.currentTimeMillis());
            Date dateTime = new java.util.Date(System.currentTimeMillis());
            String help = "*** These are the commands that you can use:-"
                  + "\nHELLO: required for starting the STDP session."
                  + "\nHELP:  displays the help information about the protocol. It may be received any time whether and STDP session is established or not."
                  + "\nDOW: returns the day of week" + "\nTIME: returns the current time."
                  + "\nDATE: returns the current date." + "\nDATETIME: returns the current date and time."
                  + "\nBYE: ends the session. \n";

            String line = "";

            // keep reading until "Over" is input
            while (!line.equals("BYE") && !line.equals("EXIT")) {
               try {

                  line = in.readUTF();
                  System.out.println(line.toUpperCase());

                  if (Objects.equals(line, "HELP")) {

                     out.writeUTF(help + "\n");

                  } else if (Objects.equals(line, "DOW")) {

                     out.writeUTF(day + "\n");

                  } else if (Objects.equals(line, "TIME")) {
                     out.writeUTF(new SimpleDateFormat("HH:mm:ss").format(time) + "\n");

                  } else if (Objects.equals(line, "DATE")) {

                     out.writeUTF(java.time.LocalDate.now() + "\n");

                  } else if (Objects.equals(line, "DATETIME")) {

                     out.writeUTF(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateTime) + "\n");

                  } else if (Objects.equals(line, "HELLO")) {

                     out.writeUTF("ERROR Already in session" + "\n");

                  } else if (line.isEmpty()) {
                     break;

                  } else {
                     if (!line.equals("BYE") && !line.equals("EXIT")) {
                        out.writeUTF("Input Invalid - Closing connection. \n");
                        socket.close();
                     } else {

                        break;
                     }
                  }

               } catch (IOException e) {
                  // e.printStackTrace();
                  break;

               } catch (Exception e) {
                  System.out.println("Error:" + socket);
               }
            }
            out.writeUTF("Thank you for connecting to " + socket.getLocalSocketAddress() + "\nGoodbye!");
            socket.close();

         } catch (

         IOException e) {
            // e.printStackTrace();
            // break;
         }

      }
   }
}