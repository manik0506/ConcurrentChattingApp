import java.io.IOException;

/*
 * Class Name: RunServer.java
 * Purpose: Application designed to start the main server.
 * @author Bilal Raza
 * Student#: 211166030
 */

public class RunPeer {
	public static void main(String args[]) {
	
	Peer mypeer = new Peer(27157);
	try {
		mypeer.StartServer();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}