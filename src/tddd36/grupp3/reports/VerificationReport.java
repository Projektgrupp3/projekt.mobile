package tddd36.grupp3.reports;

public class VerificationReport extends Report {

	String areaSearched, timeOfDepature;
	
	public VerificationReport(String seriousEvent, String typeOfInjury, 
			String threats, String numberOfInjuries, String extraResources, 
			String areaSearched, String timeOfDepature ){

		super( seriousEvent, typeOfInjury, 
			 threats, numberOfInjuries,extraResources);
	
	this.areaSearched = areaSearched;
	this.timeOfDepature = timeOfDepature;
	
	}

	public String getAreaSearched() {
		return areaSearched;
	}

	public void setAreaSearched(String areaSearched) {
		this.areaSearched = areaSearched;
	}

	public String getTimeOfDepature() {
		return timeOfDepature;
	}

	public void setTimeOfDepature(String timeOfDepature) {
		this.timeOfDepature = timeOfDepature;
	}
}
