
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Scanner;

/*
 * Class Name: Concurrency.java
 * Purpose: Application designed to start threads to achieve concurrency in server.
 * @author Bilal Raza
 * Student#: 211166030
 */
public class Concurrency extends Thread {

	Socket newsocket, peerInfo;
	InputStream UserRes, UserIPRes, UserPortRes;
	DataInputStream dis, dis2, dis3;
	OutputStream s1out;
	DataOutputStream dos;
	Scanner in = new Scanner(System.in);
	boolean keepRunning;
	
	//ArrayList of clients that keeps track of the list of online peers on the server.
	HashMap<String, Integer> users = new HashMap<String, Integer>();
	
	Concurrency(Socket newSoc, HashMap users) {
		this.newsocket = newSoc;
		this.users = users;
		start();
	}
	
	public void run() {
		try {
			
			//Creating output data stream from server to client informing every
			//client that connects with the server that they are successfully connected.
			String msg1 = "You are now connected to the Server\n";
			OutputStream s1out = newsocket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(s1out);			
			
			//Once connected to the server, every client receives a welcome menu from the server
			//to either JOIN, LEAVE, LIST or EXIT.
			String menu = msg1
						+ "\n|--------------|\n"
						+ "\n|Welcome Client|\n"
						+ "\n|--------------|\n"
						+ "\n|1.JOIN |\n"
						+ "\n|2.LEAVE |\n"
						+ "\n|3.LIST |\n"
						+ "\n|4.CHAT |\n"
						+ "\n|5.EXIT |\n"
						+ "\n|--------------|\n";
			
			dos.writeUTF(menu);
			
			boolean keepRunning = true;
			while(keepRunning){
				//Creating input data stream from client to server.
				InputStream UserRes = newsocket.getInputStream();
				DataInputStream dis = new DataInputStream(UserRes);
				String userResponse = dis.readUTF().toUpperCase();
				System.out.println(userResponse);
				
				//All the clients responses logic is implemented here..
				switch (userResponse){
					case "JOIN":
					{
						UserRes = newsocket.getInputStream();
						dis = new DataInputStream(UserRes);
						int portPeer = dis.readInt();
						//In case client is already in the list and selects JOIN.
						if(users.containsKey(newsocket.getInetAddress().toString()))
						{
							String join = "Client is already in the list of online peers";
							s1out = newsocket.getOutputStream();
							dos = new DataOutputStream(s1out);			
							dos.writeUTF(join);
							break;
						}
						//In case client is not in the list and selects JOIN.
						else{
							
							users.put(newsocket.getInetAddress().toString(), portPeer);
							String join = "Welcome to the list of online peers";
							s1out = newsocket.getOutputStream();
							dos = new DataOutputStream(s1out);			
							dos.writeUTF(join);
							//PeerAsServer peerServer = new PeerAsServer(newsocket.getPort());
							//peerServer.StartServer();
						
							break;
						}	
						
					}
					case "LEAVE":
					{
						//In case client selects to LEAVE the list.
						if(users.containsKey(newsocket.getInetAddress().toString()))
						{
							users.remove(newsocket.getInetAddress().toString());
							String leave = "User has left the list of online peers";
							s1out = newsocket.getOutputStream();
							dos = new DataOutputStream(s1out);			
							dos.writeUTF(leave);
							break;
						}
						//In case client is either not in the list or has already left the list and still
						//tries to select LEAVE.
						else{
							String leave = "Client is either not in the list OR has already left the list of online peers";
							s1out = newsocket.getOutputStream();
							dos = new DataOutputStream(s1out);			
							dos.writeUTF(leave);
							break;
						}						
					}
					case "LIST":
					{
						//In case client selects to LIST the current online peers on server
						if(users.isEmpty())
						{
							String emptyList = "There is nobody currently joined to list of online peers";
							s1out = newsocket.getOutputStream();
							dos = new DataOutputStream(s1out);			
							dos.writeUTF(emptyList);	
							break;
						}
						else{
							String list = "Current online peers: \n" + getList();
							s1out = newsocket.getOutputStream();
							dos = new DataOutputStream(s1out);			
							dos.writeUTF(list);	
							break;
						}
						
					}
					case "CHAT":
					{
						String msg = ""; 
						if(users.containsKey(newsocket.getInetAddress().toString())){
							//In case client selects to CHAT with an online peer
							msg = "Current online peers: \n" + getList() + "\nEstablish the connection with peer from the list.";
						}
						else{
							msg = "Join the list of online peers in order to begin chat. Use command: JOIN.";
						}
										
						s1out = newsocket.getOutputStream();
						dos = new DataOutputStream(s1out);			
						dos.writeUTF(msg);	
						
						break;
					
					}
					case "EXIT" :
						//In case client selects to EXIT.
						users.remove(newsocket.getInetAddress().toString());
						String exit = "Closing connection. Bye! Bye!";
						s1out = newsocket.getOutputStream();
						dos = new DataOutputStream(s1out);			
						dos.writeUTF(exit);
						keepRunning = false;
						break;
					default:
					{
						//In case client enters something that is not on the menu.
						String msg2 = "Please choose from the list";
						s1out = newsocket.getOutputStream();
						dos = new DataOutputStream(s1out);
						dos.writeUTF(msg2);			
					}
					
				}
			}
			dos.close(); //closing data streams
			s1out.close();
			newsocket.close(); //closing connection with the client
			//System.exit(-1)
			
					
		}
		catch (EOFException e){
			System.out.println("A client session was terminated without completing the task.");
		}
		// Exception Handling
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getList(){
		String list = "";
		for (String user : users.keySet()){
			list = list + "\nPort: " + users.get(user) + " IP: " + user;
			}
		return list;
	}
}
