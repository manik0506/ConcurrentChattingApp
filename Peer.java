

import java.util.*;
import java.net.*;
import java.io.*;
public class Peer extends Thread {
	
	
	boolean keepRunning = true;
	//ArrayList of clients that keeps track of the list of online players on the server.
	ArrayList<Socket> users = new ArrayList<Socket>();
	Scanner in = new Scanner(System.in);
	
	int port;
	ServerSocket server = null;

	public Peer(int port) {
		this.port = port;
	}
	
	public int getPort() {
		return this.port;
	}
	
//	public void run(){
//		DataInputStream dis1;
//		Scanner in = new Scanner(System.in);
//		
//		System.out.print("Enter the Host name: ");
//		String serverIP = in.next();
//		
//		// Allows the client to enter port number of the server they want to connect
//		System.out.print("Enter the Port Number: ");
//		int serverPort = in.nextInt();
//		
//		try {
//			// Assigning a socket for communication with server
//			Socket s1 = new Socket(serverIP, serverPort);// Server's response to initial connection request
//			InputStream s1In = s1.getInputStream();
//			DataInputStream dis = new DataInputStream(s1In);
//			dis1 = new DataInputStream(s1In);
//			String st = dis.readUTF();
//			System.out.println(st);
//
//			String command = "";
//			
//			
//			//Unless client selects exit, keep the connection stream open between client and server
//			while(!command.toUpperCase().equals("EXIT")){
//					
//				System.out.print("Command:");
//				command = in.next();
//				
//				//Transmitting clients responses to the server
//				OutputStream s2out = s1.getOutputStream();
//				DataOutputStream dos1 = new DataOutputStream(s2out);
//				dos1.writeUTF(command);
//				
//				if (command.equalsIgnoreCase(Commands.JOIN.toString())){
//					//Transmitting clients responses to the server
//					s2out = s1.getOutputStream();
//					dos1 = new DataOutputStream(s2out);
//					dos1.writeInt(this.getPort());
//					//Receiving server's response to clients requests
//					s1In = s1.getInputStream();
//					dis1 = new DataInputStream(s1In);
//					st = dis1.readUTF();
//					System.out.println(st);
//				}
//				else if (command.equalsIgnoreCase(Commands.CHAT.toString())){
//					s1In = s1.getInputStream();
//					dis1 = new DataInputStream(s1In);
//					st = dis1.readUTF();
//					System.out.println(st);
//					
//					System.out.println("Enter peer IP: ");
//					String peerIP = in.next();
//					System.out.println("Enter peer port: ");
//					int peerPort = in.nextInt();
//					Socket p2p = new Socket(peerIP, peerPort);
//					boolean exit = false;
//					while(!exit){
//						System.out.print(">>");
//						String msg = in.next();
//						s2out = p2p.getOutputStream();
//						dos1 = new DataOutputStream(s2out);
//						dos1.writeUTF(msg);
//						if (msg.equalsIgnoreCase("exit")){
//							exit = true;
//						}
//						s1In = s1.getInputStream();
//						dis1 = new DataInputStream(s1In);
//						st = dis1.readUTF();
//						System.out.println(st);
//						if (st.equalsIgnoreCase("exit")){
//							exit = true;
//						}
//					}
//					p2p.close();
//				}
//				else{
//					//Receiving server's response to clients requests
//					s1In = s1.getInputStream();
//					dis1 = new DataInputStream(s1In);
//					st = dis1.readUTF();
//					System.out.println(st);			
//				}
//			}
//			//If client selects exit, close all connections to the server.
//			s1.close();
//			dis.close();
//			s1In.close();	
//			dis1.close();
//			in.close();
//			
//		// Exception Handling
//		}catch (EOFException e) {
//			System.out.println("The server you are trying to connect to has been closed");
//			System.out.println("Please Try Again Later.");
//			System.exit(0);
//		} 
//		catch (ConnectException e) {
//			System.out
//			.println("Either the server has been closed or wrong port number.");
//			System.out.println("Please Try Again.");
//			System.exit(0);
//		} catch (UnknownHostException e) {
//			System.out.println("Incorrect Host IP Address.");
//			System.out.println("Please Try Again.");
//			System.exit(0);
//		} catch (SocketException e) {
//			System.out.println("Cannot create or access the socket.");
//			System.out.println("Please Try Again.");
//			System.exit(0);
//		} catch (IllegalArgumentException e) {
//			System.out.println("Port Number out of Range");
//			System.out.println("Please Try Again.");
//			System.exit(0);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(0);
//		}
//	}
	
	
		
		//Starts the server. Server has capability to accept multiple concurrent clients requests
		public void StartServer() throws IOException {
			Socket s1 = null;
			try {
				server = new ServerSocket(port);
				//System.out.println("Server is ready for communication on port: " + port);
				while (keepRunning) {
					//System.out.println("Peer is requesting for a chat.");
					s1 = server.accept();
					PeerConcurrency PeerClients = new PeerConcurrency(s1, port);
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
		
		public void EndServer(){
			keepRunning = false;
		}
}
