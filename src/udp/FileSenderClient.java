package udp;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static java.lang.Thread.sleep;

public class FileSenderClient {
	
	static void sendFile(InetSocketAddress server, String filename) {
		try (DatagramSocket socket = new DatagramSocket(); FileInputStream fis = new FileInputStream(filename)) {
			socket.send(new DatagramPacket(filename.getBytes(), filename.length(), server));
			
			byte[] buffer = new byte[FileSenderServer.BLOCK_SIZE];
			int numBytes;
			
			do {
				numBytes = fis.readNBytes(buffer, 0, FileSenderServer.BLOCK_SIZE);
				socket.send(new DatagramPacket(buffer, numBytes, server));
			} while (fis.available() > 0);
			
			if (numBytes == FileSenderServer.BLOCK_SIZE) {
				socket.send(new DatagramPacket(buffer, 0, server));
			}
			
			fis.close();
		}
		catch (IOException x) {
			x.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length != 3) {
			System.err.println("usage: <server> <port> <filename>");
			System.exit(0);
		}

		String host = args[0];
		String filename = args[2];
		int port = Integer.parseInt(args[1]);
		
		InetSocketAddress server = new InetSocketAddress(host, port);
		sendFile(server, filename);
	}
	
}
