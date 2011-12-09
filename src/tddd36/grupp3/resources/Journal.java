package tddd36.grupp3.resources;

import org.json.JSONException;
import org.json.JSONObject;

public class Journal {
	
	public String ssn;
	public String name;
	public String address;
	public String blodType;
	public String allergies;
	public String other;
	
	public Journal(String jsonInput) throws JSONException{
		JSONObject journal = new JSONObject(jsonInput);
		
		this.ssn 		= journal.getString("SSN");
		this.name		= journal.getString("Name");
		this.address 	= journal.getString("Address");
		this.blodType 	= journal.getString("BlodType");
		this.allergies 	= journal.getString("Allergies");
		this.other		= journal.getString("Notes");

	}
	
	public String getSSN(String a){
		return ssn;
	}
	
	public String getName(String a){
		return name;
	}
	
	public String getAddress(String a){
		return address;
	}
	
	public String getBlodType(String a){
		return blodType;
	}
	
	public String getAllergies(String a){
		return allergies;
	}
	
	public String getOther(String a){
		return other;
	}
	

}
