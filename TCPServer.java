
import java.util.*;
import java.net.*;
import java.io.*;

/*
 * Class Name: TCPServer.java
 * Purpose: Application designed to handle server implementation
 * @author Bilal Raza
 * Student#: 211166030
 */

public class TCPServer extends Thread {

boolean keepRunning;
HashMap<String, Integer> users = new HashMap<String, Integer>();
//ArrayList of clients that keeps track of the list of online players on the server.

Scanner in = new Scanner(System.in);

int port = 29313;
Socket s1 = null;
ServerSocket server = null;

	public TCPServer() {
		keepRunning = true;
	}
	
	public void run(){
		String exit = in.next(); 
		
		//Type close on server application to shut down server process and close all connections.
		if (exit.equals("close")){
			keepRunning = false;
			try {
				closeHostServer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//Starts the server. Server has capability to accept multiple concurrent clients requests
	public void StartServer() throws IOException {
		start();
		try {
			s1 = null;
			server = new ServerSocket(port);
			System.out.println("Server is ready for communication on port: " + port);
			while (keepRunning) {
				s1 = server.accept();
				if (keepRunning) {
					Concurrency con = new Concurrency(s1, users);
				}
			}
			System.out.println("The server session has been closed.");
			s1.close();
			server.close();
			System.exit(0);
		}
		catch (SocketException e){
			System.out.println("Cannot create or access the socket.");
			System.out.println("Please Try Again.");
			System.exit(0);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		finally{
			s1.close(); //closing the server socket
			server.close(); //closing the server
		}
	}
	
	//Starts a dummy client process to close the concurrency thread.
	//And terminates the host server process.
	public void closeHostServer() throws IOException{
		int ClosePort = this.port;
		String Host = Inet4Address.getLocalHost().getHostAddress().toString();
		Socket s = new Socket(Host, ClosePort);
		s.close();
	}
	
}
