package is.ru.tgra.network;

import is.ru.tgra.Point3D;
import is.ru.tgra.Shot;
import is.ru.tgra.Vector3D;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkThread extends Thread {

	private Socket socket = null;
	
	PrintWriter out = null;
	BufferedReader in = null;
	private  boolean alive;
	
	
	public NetworkThread() {
		try {
			this.socket = new Socket("localhost", 7575);
			this.out = new PrintWriter(this.socket.getOutputStream());
			this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			
			
		} 
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public void sendMessage(String message){
		//System.out.println("Sending message: " + message);
		this.out.write(message+"\n");
		this.out.flush();
	}
	
	
	public void run() {
		
		// Read my nickname

		
		while(true) {
			//System.out.println("waiting of server message!");
			try {
				String message = this.in.readLine();
				//System.out.println("Received message from server: " + message);
				
				// We assume that the network messages are on csv format.
				String[] tokens = message.split(";");
				String name = tokens[1];
				
				if(tokens[0].equals("online")) {
					GameState.instance().addPlayer(name);
				}
				
				if(tokens[0].equals("join")) {
					GameState.instance().addPlayer(name);
				}
				
				if(tokens[0].equals("exit")) {
					GameState.instance().removePlayer(name);
				}
				
				if(tokens[0].equals("move")) {
					float x = Float.parseFloat(tokens[2]);
					float y = Float.parseFloat(tokens[3]);
					float z = Float.parseFloat(tokens[4]);
                    Point3D pos = new Point3D(x,y,z);
                    Vector3D forward = new Vector3D(Float.parseFloat(tokens[5]),Float.parseFloat(tokens[6]),Float.parseFloat(tokens[7]));
                    Vector3D up = new Vector3D(Float.parseFloat(tokens[8]),Float.parseFloat(tokens[9]),Float.parseFloat(tokens[10]));

                    GameState.instance().updatePlayer(name, pos, forward, up);
				}
                if(tokens[0].equals("fire")) {
                    Point3D start = new Point3D(Float.parseFloat(tokens[2]),Float.parseFloat(tokens[3]),Float.parseFloat(tokens[4]));
                    Point3D end = new Point3D(Float.parseFloat(tokens[5]),Float.parseFloat(tokens[6]),Float.parseFloat(tokens[7]));
                    Shot shot = new Shot(start,end);
                    GameState.instance().addShot(name,shot);
                }

                } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
}
