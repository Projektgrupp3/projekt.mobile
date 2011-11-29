package tddd36.grupp3.controllers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import tddd36.grupp3.models.LoginModel;
import android.os.AsyncTask;
import android.util.Log;

	public class ConnectionController extends AsyncTask<Void, Void, Void> {
	public static final String REQ_ALL_UNITS = "REQ_ALL_UNITS";
	private static final String COM_IP = "130.236.227.199";
	public static final int LISTEN_PORT = 4445;
	//public static final int LISTEN_PORT = 2225;

	public String serverOutput;

	private Socket s;
	private LoginModel cm;
	private ServerSocket serversocket;
	private Socket socket;

	public ConnectionController(LoginModel cm) throws IOException {
		this.cm = cm;
		serversocket = new ServerSocket(LISTEN_PORT);
	}

	@Override
	protected Void doInBackground(Void... params) {
		while (true) {
			Log.d("Loop", "Lyssnar efter inkommande server connections");
			try {
				socket = serversocket.accept();
				new ConnectionTask((socket), this, cm).execute();
			} catch (IOException ioException) {
				ioException.printStackTrace();
				System.exit(-1);
			}
		}
	}
}
