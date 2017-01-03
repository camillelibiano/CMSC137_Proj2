import  java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer{
  public static ServerSocket serverSocket;
	public static void main(String args[]) {
    try{
      int port = Integer.parseInt(args[0]);
      serverSocket = new ServerSocket(port);

      System.out.println("Server Ready! Waiting for connections...");

      for(;;){
        try{
          Socket s = serverSocket.accept();
          new HTTPRequestHandler(s).start();
        }catch (Exception e) {
          System.out.println("Error: " + e);//print error
        }
      }

    }catch (Exception e) {
      System.out.println(e);
      System.out.println("Usage: java WebServer <port number>");
    }
  }

}

class HTTPRequestHandler extends Thread{
  private Socket client;

  public HTTPRequestHandler(Socket s){
    client = s;
  }

  @Override
  public void run(){
    try{
      String[] reqheader={};
      BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
      PrintStream output = new PrintStream(new BufferedOutputStream(client.getOutputStream()));
      // String filename = null;

      String request = input.readLine(); //read client's request

      if(request.length()>0){
        reqheader = request.split(" ");//parse the request header
      }


      String body ="<html><table id='header' border= '1'><tr><td>Method:</td><td> "+reqheader[0]+"</td></tr><tr><td> Request URI:</td><td> "+reqheader[1]+"</td></tr> <tr><td> HTTP Version:</td><td> "+reqheader[2]+"</td></tr>";
      String[] temp;
      temp=input.readLine().split(" ");
      body=body+"<tr><td>"+temp[0]+"</td><td>"+temp[1]+"</td></tr>";
      temp=input.readLine().split(" ");
      body=body+"<tr><td>"+temp[0]+"</td><td>"+temp[1]+"</td></tr>";
      temp=input.readLine().split(" ");
      body=body+"<tr><td>"+temp[0]+"</td><td>"+temp[1]+"</td></tr>";
      temp=input.readLine().split(" ");
      body=body+"<tr><td>"+temp[0]+"</td><td>"+temp[1]+"</td></tr>";
      temp=input.readLine().split(" ");
      body=body+"<tr><td>"+temp[0]+"</td><td>"+temp[1]+"</td></tr>";
      temp=input.readLine().split(" ");
      body=body+"<tr><td>"+temp[0]+"</td><td>"+temp[1]+"</td></tr>";

      try{
        temp=input.readLine().split(" ");
        body=body+"<tr><td>"+temp[0]+"</td><td>"+temp[1]+"</td></tr>";
      }catch(Exception r){}
        body=body+"</table>";

        String str = ".";
         while (!str.equals(""))
           str = input.readLine();

      //append request file
      String path =reqheader[1].substring(1,reqheader[1].length());
      File file = new File(path);

      if(!file.exists()){//check if the request file exist
        //Send the response
          output.println("HTTP/1.0 404 Not Found");		//header for file not found
          output.println("Content-Type: text/html");
          output.println("");
          output.println(body+"<h1>File Not Found</h1>");

          try{
            PrintWriter out = new PrintWriter(file);
            output.println("AUTO GENERATED FILE");
            output.close();
            output.print("<p>"+file+" generated on server directory!</p>");
            }catch(FileNotFoundException fnfe2){}
      }else{
        // Send the response
          output.println("HTTP/1.0 200 OK");		//header for successfully find the file
          output.println("Content-Type: text/html");
          output.println("");

          //Read the requested file
          FileReader fw = new FileReader(file);
          BufferedReader bw = new BufferedReader(fw);
          String line;

          if(reqheader[1].contains(".css")){//append for cascading style sheet file
            body=body+"<style>";
          }
          else if(reqheader[1].contains(".js")){//append for javascript file
            body=body+"<script>";
          }

          while((line = bw.readLine()) != null){//read the line of code
                body=body+ line;
                }

          if(reqheader[1].contains(".css")){
            body=body+"</style>";
          }
          else if(reqheader[1].contains(".js")){
            body=body+"</script>";
          }
          bw.close();
          output.println(body);
      }

      output.println("</html>");
      output.flush();// forces any buffered output bytes to be written out.
    }catch (Exception e) {
      System.out.println("Error: " + e);//print error
    }
  }
}
