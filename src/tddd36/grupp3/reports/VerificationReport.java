package tddd36.grupp3.reports;

public class VerificationReport extends Report {

	String areaSearched, timeOfDeparture;

	public VerificationReport(String seriousEvent, String typeOfInjury, 
			String threats, String numberOfInjuries, String extraResources, 
			String areaSearched, String timeOfDepature ){

		super( seriousEvent, typeOfInjury, 
				threats, numberOfInjuries,extraResources);

		this.areaSearched = areaSearched;
		this.timeOfDeparture = timeOfDepature;

	}
	@Override
	public String getAreaSearched() {
		return areaSearched;
	}
	@Override
	public void setAreaSearched(String areaSearched) {
		this.areaSearched = areaSearched;
	}
	@Override
	public String getTimeOfDeparture() {
		return timeOfDeparture;
	}
	@Override
	public void setTimeOfDepature(String timeOfDeparture) {
		this.timeOfDeparture = timeOfDeparture;
	}
}
