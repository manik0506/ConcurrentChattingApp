import java.io.IOException;

/*
 * Class Name: RunServer.java
 * Purpose: Application designed to start the main server.
 * @author Bilal Raza
 * Student#: 211166030
 */

public class RunServer extends Thread {
	
	public static void main(String args[]) {
		TCPServer myserver = new TCPServer();
		try {
			myserver.StartServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}