import java.io.*;
import java.net.*;
import java.util.Scanner;

/*
 * Class Name: Concurrency.java
 * Purpose: Application designed to start threads to achieve concurrency in server.
 * @author Bilal Raza
 * Student#: 211166030
 */
public class PeerConcurrency extends Thread {

	Socket newsocket;
	//ServerSocket newServerSoc;
	String IPAdress;
	InputStream SocketStreamIn;
	DataInputStream PeerData;
	OutputStream s1out;
	DataOutputStream dos;
	Scanner in = new Scanner(System.in);
	boolean keepRunning, flip;
	String peerInitMsg, peerMsg;
	int port;
	
	//ArrayList of clients that keeps track of the list of online peers on the server.
	PeerConcurrency(Socket newSoc, int port) {
		this.newsocket = newSoc;
		this.port = port;
		start();
		flip = true;
	}
	
	public void run() {
		try {
			
			if (flip == true){
				DataInputStream dis1;
				Scanner in = new Scanner(System.in);
				
				System.out.print("Enter the Host name: ");
				String serverIP = in.next();
				
				// Allows the client to enter port number of the server they want to connect
				System.out.print("Enter the Port Number: ");
				int serverPort = in.nextInt();
				
				try {
					// Assigning a socket for communication with server
					Socket s1 = new Socket(serverIP, serverPort);// Server's response to initial connection request
					InputStream s1In = s1.getInputStream();
					DataInputStream dis = new DataInputStream(s1In);
					dis1 = new DataInputStream(s1In);
					String st = dis.readUTF();
					System.out.println(st);

					String command = "";
					
					
					//Unless client selects exit, keep the connection stream open between client and server
					while(!command.toUpperCase().equals("EXIT")){
							
						System.out.print("Command:");
						command = in.next();
						
						//Transmitting clients responses to the server
						OutputStream s2out = s1.getOutputStream();
						DataOutputStream dos1 = new DataOutputStream(s2out);
						dos1.writeUTF(command);
						
						if (command.equalsIgnoreCase(Commands.JOIN.toString())){
							//Transmitting clients responses to the server
							s2out = s1.getOutputStream();
							dos1 = new DataOutputStream(s2out);
							dos1.writeInt(port);
							//Receiving server's response to clients requests
							s1In = s1.getInputStream();
							dis1 = new DataInputStream(s1In);
							st = dis1.readUTF();
							System.out.println(st);
						}
						else if (command.equalsIgnoreCase(Commands.CHAT.toString())){
							s1In = s1.getInputStream();
							dis1 = new DataInputStream(s1In);
							st = dis1.readUTF();
							System.out.println(st);
							
							System.out.println("Enter peer IP: ");
							String peerIP = in.next();
							System.out.println("Enter peer port: ");
							int peerPort = in.nextInt();
							Socket p2p = new Socket(peerIP, peerPort);
							boolean exit = false;
							while(!exit){
								System.out.print(">>");
								String msg = in.next();
								s2out = p2p.getOutputStream();
								dos1 = new DataOutputStream(s2out);
								dos1.writeUTF(msg);
								if (msg.equalsIgnoreCase("exit")){
									exit = true;
								}
								s1In = s1.getInputStream();
								dis1 = new DataInputStream(s1In);
								st = dis1.readUTF();
								System.out.println(st);
								if (st.equalsIgnoreCase("exit")){
									exit = true;
								}
							}
							p2p.close();
						}
						else if (command.equalsIgnoreCase("YES")){
							flip = false;
						}
						else{
							//Receiving server's response to clients requests
							s1In = s1.getInputStream();
							dis1 = new DataInputStream(s1In);
							st = dis1.readUTF();
							System.out.println(st);			
						}
					}
					//If client selects exit, close all connections to the server.
					s1.close();
					dis.close();
					s1In.close();	
					dis1.close();
					in.close();
					
				// Exception Handling
				}catch (EOFException e) {
					System.out.println("The server you are trying to connect to has been closed");
					System.out.println("Please Try Again Later.");
					System.exit(0);
				} 
				catch (ConnectException e) {
					System.out
					.println("Either the server has been closed or wrong port number.");
					System.out.println("Please Try Again.");
					System.exit(0);
				} catch (UnknownHostException e) {
					System.out.println("Incorrect Host IP Address.");
					System.out.println("Please Try Again.");
					System.exit(0);
				} catch (SocketException e) {
					System.out.println("Cannot create or access the socket.");
					System.out.println("Please Try Again.");
					System.exit(0);
				} catch (IllegalArgumentException e) {
					System.out.println("Port Number out of Range");
					System.out.println("Please Try Again.");
					System.exit(0);
				} catch (Exception e) {
					e.printStackTrace();
					System.exit(0);
				}
			}
			else{
				boolean exit = false;
				while(!exit){
					SocketStreamIn = newsocket.getInputStream();
					PeerData = new DataInputStream(SocketStreamIn);
					String st = PeerData.readUTF();
					System.out.println(st);
				
					if (st.equalsIgnoreCase("exit")){
						exit = true;
					}
					
					System.out.print(">>");
					String msg = in.next();
					s1out = newsocket.getOutputStream();
					dos = new DataOutputStream(s1out);
					dos.writeUTF(msg);
					
					if (msg.equalsIgnoreCase("exit")){
						exit = true;
					}
				}
			}
			newsocket.close();
			PeerData.close();
			SocketStreamIn.close();
			dos.close(); //closing data streams
			s1out.close();
		}
		
			catch (EOFException e){
			System.out.println("A client session was terminated without completing the task.");
		}
		// Exception Handling
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
