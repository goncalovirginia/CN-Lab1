package udp;

import java.io.*;
import java.net.*;

public class FileSenderServer {
	static final int BLOCK_SIZE = 1024;
	public static int PORT = 8000;

	
	static void receiveFile( DatagramSocket socket, String filename) {
		byte[] buffer = new byte[BLOCK_SIZE];
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		
		try (FileOutputStream fos = new FileOutputStream( "Copy-of-"+ filename)){
			do {
				socket.receive(packet);
				fos.write( packet.getData(), 0, packet.getLength());
			} while( packet.getLength() == BLOCK_SIZE );
		} catch(Exception x ) {
			x.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		try (DatagramSocket socket = new DatagramSocket(PORT)) {
			for (;;) {
				byte[] buffer = new byte[BLOCK_SIZE];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				
				socket.receive(packet);
				
				String filename = new String(packet.getData(), 0, packet.getLength());
				receiveFile(socket, filename);
			}
		} catch (IOException x) {
			x.printStackTrace();
		}

	}
}
