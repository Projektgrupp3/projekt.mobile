package tddd36.grupp3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import tddd36.grupp3.resources.Event;

import com.google.gson.Gson;

public class Sender {
	public static final String REQ_ALL_UNITS = "REQ_ALL_UNITS";

	private static final String COM_IP = "130.236.227.137";
//	private static final int COM_PORT = 4444;
	private static final int COM_PORT = 2222;

	private static PrintWriter pw;
	private static JSONObject jsonobject;

	private static String messageToServer;
	private static String username;
	private static String password;

	private static Socket socket;

	public static void establishConnection() {
		try {
			socket = new Socket(COM_IP, COM_PORT);
			pw = new PrintWriter(socket.getOutputStream(), true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection() {
		try {
			pw.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void send(String str) throws JSONException {
		messageToServer = str;

		jsonobject = new JSONObject();
		jsonobject.put("user", username);
		jsonobject.put("pass", password);
		jsonobject.put("req", messageToServer);

		String jsonString = jsonobject.toString();

		establishConnection();

		pw.println(jsonString);

		closeConnection();
	}	
public static void send(Event ev) throws JSONException {
		jsonobject = new JSONObject();
		Gson gson = new Gson();
		gson.toJson(ev);
		jsonobject.put("user", username);
		jsonobject.put("pass", password);
		jsonobject.put("req", "event");

		String jsonString = jsonobject.toString();

		establishConnection();

		pw.println(jsonString);
		pw.println(gson.toJson(ev));

		closeConnection();
	}

	public static void send(String user, String pass, String message)
	throws JSONException {
		username = user;
		password = pass;
		messageToServer = message;

		jsonobject = new JSONObject();
		jsonobject.put("user", username);
		jsonobject.put("pass", password);
		jsonobject.put("req", messageToServer);

		String jsonString = jsonobject.toString();

		establishConnection();

		pw.println(jsonString);

		closeConnection();
	}
}