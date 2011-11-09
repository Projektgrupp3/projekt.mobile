package tddd36.grupp3.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import tddd36.grupp3.models.ClientModel;
import android.os.AsyncTask;

public class ConnectionTask extends AsyncTask<Void, Integer, String> implements
Runnable, Observer {

	public static final int LISTEN_PORT = 4445;
	private BufferedReader br;
	public String serverOutput;
	private Socket s;
	private ClientModel cm;
	private ServerSocket serverSocket;
	private Socket socket;
	private String input;

	public ConnectionTask(ClientModel cm) {
		this.cm = cm;
	}

	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
	}

	protected void disconnect() throws IOException {
		s.close();
	}

	public void run() {
		// TODO Auto-generated method stub
	}

	public String listen() throws IOException {
		try {
			serverSocket = new ServerSocket(LISTEN_PORT);
			socket = serverSocket.accept();
			br = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {
			while ((input = br.readLine()) != "") {
				socket.close();
				break;
			}
			break;
		}
		return input;
	}

	@Override
	protected String doInBackground(Void... params) {
		String str = null;
		try {
			str = listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	@Override
	protected void onPostExecute(String result) {
		cm.executeChange();
		cm.notifyObservers(result);
	}
}
