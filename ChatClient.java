import java.net.*;
import java.io.*;
import java.util.*;

class ChatClient{
	public static void main(String[] args) {
		while(true){
			try{
				System.setProperty("java.net.preferIPv4Stack", "true");

				InetAddress group = InetAddress.getByName("239.0.202.1");
				int port = 40202;
				MulticastSocket mcs = new MulticastSocket(port);
				mcs.joinGroup(group);
				
				ReadThread rt = new ReadThread(group, port);
				rt.start();
				
				//Generation of the packet to be sent using keyboard input
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String message = br.readLine();
				byte[] mess = message.getBytes();
				
				DatagramPacket packet = new DatagramPacket(mess,mess.length,group,port);
				mcs.send(packet);
				mcs.close();
			}
			catch(UnknownHostException e) {
				System.err.println("unknown host");
			}
			catch(Exception e){
				System.err.println("Execption: " + e);
			}
		}
		
	}

}

class ReadThread extends Thread{
	InetAddress group;
	int port;
	ReadThread(InetAddress g, int p){
		group = g;
		port = p;
	}
	public void run(){
		try{
			MulticastSocket mcRead = new MulticastSocket(port);
			mcRead.joinGroup(group);
			
			byte[] buf = new byte[100];
			DatagramPacket recepack = new DatagramPacket(buf, buf.length, group, port);
			mcRead.receive(recepack);

			String received = new String(recepack.getData());
			System.out.println(recepack.getAddress().toString() + ": " + received);
			
		}
		catch(Exception e){
			System.err.println("Execption: " + e);
		}
				
	}
}
