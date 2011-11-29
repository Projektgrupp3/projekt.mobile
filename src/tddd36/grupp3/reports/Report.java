package tddd36.grupp3.reports;

public class Report {
	
	private String seriousEvent, typeOfInjury, threats, numberOfInjuries, extraResources;
	
	public Report (String seriousEvent, String typeOfInjury, String threats, String numberOfInjuries, String extraResources){
		this.seriousEvent = seriousEvent;
		this.typeOfInjury = typeOfInjury;
		this.threats = threats;
		this.numberOfInjuries=numberOfInjuries;
		this.extraResources = extraResources;
	}

	public String getSeriousEvent() {
		return seriousEvent;
	}

	public void setSeriousEvent(String seriousEvent) {
		this.seriousEvent = seriousEvent;
	}

	public String getTypeOfInjury() {
		return typeOfInjury;
	}

	public void setTypeOfInjury(String typeOfInjury) {
		this.typeOfInjury = typeOfInjury;
	}

	public String getThreats() {
		return threats;
	}

	public void setThreats(String threats) {
		this.threats = threats;
	}

	public String getNumberOfInjuries() {
		return numberOfInjuries;
	}

	public void setNumberOfInjuries(String numberOfInjuries) {
		this.numberOfInjuries = numberOfInjuries;
	}
	

}
