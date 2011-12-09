package tddd36.grupp3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.json.JSONException;
import org.json.JSONObject;

import tddd36.grupp3.misc.NetworkManager;
import tddd36.grupp3.misc.QoSManager;
import tddd36.grupp3.reports.Report;
import tddd36.grupp3.resources.Event;
import tddd36.grupp3.views.MainView;
import android.util.Log;

/**
 * KLIENT-SENDER-KLASS
 *
 */
public class Sender {
	public static final String REQ_ALL_UNITS = "REQ_ALL_UNITS";
	public static final String ACK_VERIFICATION_REPORT = "ACK_VERIFICATION_REPORT";
	public static final String ACK_WINDOW_REPORT = "ACK_WINDOW_REPORT";

	public static final String REQ_MAP_OBJECTS = "REQ_MAP_OBJECTS";
	public static final String REQ_ALL_CONTACTS ="REQ_ALL_CONTACTS";
	public static final String REQ_CONTACT = "REQ_CONTACT";
	public static final String REQ_JOURNAL = "REQ_JOURNAL";
	public static final String UPDATE_MAP_OBJECT = "UPDATE_MAP_OBJECT";
	public static final String ACK_RECIEVED_EVENT = "ACK_RECIEVED_EVENT";
	public static final String ACK_ACCEPTED_EVENT = "ACK_ACCEPTED_EVENT";
	public static final String ACK_REJECTED_EVENT = "ACK_REJECTED_EVENT";
	public static final String ACK_STATUS = "ACK_STATUS";
	public static final String ACK_CHOSEN_UNIT = "ACK_CHOSEN_UNIT";
	public static final String LOG_OUT = "LOG_OUT";

	private static final String COM_IP = "130.236.227.237";
	private static final int COM_PORT = 1560;

	public static String NETWORK_STATUS;

	private static PrintWriter pw;
	private static JSONObject jsonobject;

	private static String messageToServer;
	private static String username;
	private static String password;

	private static ArrayList<String> buffer = new ArrayList<String>();

	private static SSLSocket socket;

	public static void establishConnection() {
		try {
			SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			socket = (SSLSocket) sslsocketfactory.createSocket(COM_IP, COM_PORT);
			socket.setEnabledCipherSuites(new String[] { "SSL_DH_anon_WITH_RC4_128_MD5" });
			pw = new PrintWriter(socket.getOutputStream(), true);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection() {
		if(pw != null){
			try {
				pw.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void send(String message) {

		messageToServer = message;
		String[] splittedMessage = messageToServer.split(":");

		jsonobject = new JSONObject();
		try {
			jsonobject.put("user", username);
			jsonobject.put("pass", password);

			if (messageToServer.startsWith(ACK_ACCEPTED_EVENT) 
					|| messageToServer.startsWith(ACK_RECIEVED_EVENT)
					|| messageToServer.startsWith(ACK_REJECTED_EVENT)) {
				jsonobject.put("ack", "event");
				jsonobject.put("event", splittedMessage[0]);
				jsonobject.put("eventID", splittedMessage[1]);
			}
			else if (messageToServer.startsWith(ACK_CHOSEN_UNIT)) {
				jsonobject.put("ack", "unit");
				jsonobject.put("unit", splittedMessage[1]);
			}
			else if(messageToServer.startsWith(ACK_STATUS)){
				jsonobject.put("ack", "status");
				jsonobject.put("status", splittedMessage[1]);
			}
			else if(messageToServer.startsWith(REQ_JOURNAL)){
				jsonobject.put("req", "REQ_JOURNAL");
				jsonobject.put("identifier", splittedMessage[1]);
			}
			else {
				jsonobject.put("req", messageToServer);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String jsonString = jsonobject.toString();

		if(NETWORK_STATUS.equals(NetworkManager.NONE)){
			buffer.add(jsonString);
		}
		else {
			if(!buffer.isEmpty()){
				establishConnection();

				for(String str : buffer){
					pw.println(str);
				}
				closeConnection();
			}
			establishConnection();
			pw.println(jsonString);
			closeConnection();
		}
	}

	public static void send(Event ev) throws JSONException {
		jsonobject = new JSONObject();

		jsonobject.put("user", username);
		jsonobject.put("pass", password);
		jsonobject.put("req", UPDATE_MAP_OBJECT);
		jsonobject.put("header", ev.getHeader());
		jsonobject.put("description", ev.getMessage());
		jsonobject.put("tempCoordX", ev.getLatE6());
		jsonobject.put("tempCoordY", ev.getLonE6());
		jsonobject.put("eventID", ev.getID());

		if(ev.getAccidentType() != null){
			jsonobject.put("accidentType",ev.getAccidentType());
			jsonobject.put("numberOfInjured",ev.getNumberOfInjured());
			jsonobject.put("typeOfInjury",ev.getTypeOfInjury());
			jsonobject.put("priority",ev.getPriority());
			jsonobject.put("lastChanged",new SimpleDateFormat("yy:MM:dd:HH:mm:ss").format(new Date()));
			jsonobject.put("req","UPDATE_EVENT");
			jsonobject.put("unitID", ev.getUnitID());
		}	

		String jsonString = jsonobject.toString();

		if(NETWORK_STATUS.equals(NetworkManager.NONE) || NETWORK_STATUS.equals(NetworkManager.MOBILE)
				|| QoSManager.BATTERY_LEVEL.equals(QoSManager.MEDIUM) || QoSManager.BATTERY_LEVEL.equals(QoSManager.HIGH)){
			Log.d("Buffer", jsonString);
			buffer.add(jsonString);
			Log.d("Buffer", ""+buffer.size());
		}
		else {
			establishConnection();
			pw.println(jsonString);
			closeConnection();
		}
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

		if(NETWORK_STATUS.equals(NetworkManager.NONE)){
			buffer.add(jsonString);
		}
		else {
			if(!buffer.isEmpty()){
				establishConnection();

				for(String str : buffer){
					pw.println(str);
				}
				closeConnection();
			}
			establishConnection();
			pw.println(jsonString);
			closeConnection();
		}
	}
	public static void sendContact(String contactName, String contactAddress)
	throws JSONException {
		jsonobject = new JSONObject();
		jsonobject.put("user", username);
		jsonobject.put("pass", password);
		jsonobject.put("req", REQ_CONTACT);
		jsonobject.put("sipaddress", contactAddress);
		jsonobject.put("contactName", contactName);
		String jsonString = jsonobject.toString();

		if(NETWORK_STATUS.equals(NetworkManager.NONE) || NETWORK_STATUS.equals(NetworkManager.MOBILE)
				|| QoSManager.BATTERY_LEVEL.equals(QoSManager.MEDIUM) || QoSManager.BATTERY_LEVEL.equals(QoSManager.HIGH)){
			Log.d("Buffer", jsonString);
			buffer.add(jsonString);
			Log.d("Buffer", ""+buffer.size());
		}
		else {
			establishConnection();
			pw.println(jsonString);
			closeConnection();
		}
	}
	public static void sendReport(Report report) throws JSONException{
		jsonobject = new JSONObject();
		jsonobject.put("ack", "report");
		jsonobject.put("user", username);
		jsonobject.put("pass", password);
		jsonobject.put("eventID",MainView.missionController.getActiveMission().getID());
		jsonobject.put("seriousEvent", report.getSeriousEvent());
		jsonobject.put("typeOfInjury", report.getTypeOfInjury());
		jsonobject.put("threats", report.getThreats());
		jsonobject.put("numberOfInjuries",report.getNumberOfInjuries());
		jsonobject.put("extraResources",report.getExtraResources());

		if(report.getTypeOfReport().equals("WindowReport")){
			jsonobject.put("exactLocation", report.getExactLocation());
			jsonobject.put("report", ACK_WINDOW_REPORT);
		}
		else if(report.getTypeOfReport().equals("VerificationReport")){
			jsonobject.put("areaSearched", report.getAreaSearched());
			jsonobject.put("timeOfDeparture", report.getTimeOfDeparture());
			jsonobject.put("report", ACK_VERIFICATION_REPORT);
		}
		String jsonString = jsonobject.toString();

		if(NETWORK_STATUS.equals(NetworkManager.NONE)){
			Log.d("Buffer", jsonString);
			buffer.add(jsonString);
			Log.d("Buffer", ""+buffer.size());
		}
		else {
			establishConnection();
			pw.println(jsonString);
			closeConnection();
		}
	}
	public static void sendBuffer(){
		if(!buffer.isEmpty()){
			establishConnection();
			for(int i = 0; i < buffer.size(); i++){
				if(!(buffer.get(i) == null)){
					if(pw != null){
						pw.println(buffer.get(i));
						Log.d("Buffer","Skickat från buffern");
						Log.d("Buffer", buffer.get(i));
					}
				}
			}
			buffer.clear();
			closeConnection();
		}
	}
}
