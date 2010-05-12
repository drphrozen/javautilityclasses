package dk.au.perpos.tailing.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import dk.au.perpos.tailing.TailingAgent.Login;
import dk.au.perpos.tailing.TailingAgent.Login.Type;

public class Client implements Runnable {

	@Override
	public void run() {
		Socket socket;
		try {
			socket = new Socket("localhost", 15339);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		Login.Builder loginBuilder = Login.newBuilder();
		loginBuilder.setClientType(Type.Agent);
		loginBuilder.setName("Mr. T");
		try {
			System.out.println("Sending login request..");
			loginBuilder.build().writeDelimitedTo(socket.getOutputStream());
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Client().run();
	}
}
