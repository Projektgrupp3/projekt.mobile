package tddd36.grupp3.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class ConnectionController implements Runnable, Observer {

	private static final String COM_IP = "130.236.226.120";
	private static final int COM_PORT = 4040;
	private InputStreamReader isr;
	private PrintWriter pw;
	private BufferedReader br;
	private boolean auth;
	public String serverOutput;
	private Socket s;
	private ServerSocket serv;

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub

	}

	public void run() {
		// TODO Auto-generated method stub

	}

	public void connect() {
		s = new Socket(COM_IP, COM_PORT);
		// Setting up streams
		isr = new InputStreamReader(s.getInputStream());
		pw = new PrintWriter(s.getOutputStream(), true);
		br = new BufferedReader(isr);
		// Sending username and password to server
		pw.println(user);
		pw.println(pass);

		if ((serverOutput = br.readLine()) != "") {
			if (!serverOutput.equals("Authenticated")) {
				auth = true;
			} else {
				auth = false;
			}
		}
	}

	protected void disconnect() throws IOException {
		s.close();
	}

	protected void send(String str) {
		if (auth) {
			pw.println(str);
		}
	}
}
