package tddd36.grupp3.controllers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import tddd36.grupp3.models.LoginModel;
import android.os.AsyncTask;
import android.util.Log;


	//private static final String COM_IP = "130.236.226.47";
	
	public class ConnectionController extends AsyncTask<Void, Void, Void> {
	public static final String REQ_ALL_UNITS = "REQ_ALL_UNITS";
<<<<<<< HEAD
	 public static final int LISTEN_PORT = 4445;
=======
	 public static final int LISTEN_PORT = 4447;
>>>>>>> branch 'master' of git@github.com:Projektgrupp3/projekt.mobile.git
//	public static final int LISTEN_PORT = 3435;

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
