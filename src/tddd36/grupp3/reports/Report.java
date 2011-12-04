package tddd36.grupp3.reports;

public class Report {

	private String seriousEvent, typeOfInjury, threats, timeOfDeparture,
			numberOfInjuries, extraResources, exactLocation, areaSearched,
			typeOfReport;

	public Report(String seriousEvent, String typeOfInjury, String threats,
			String numberOfInjuries, String extraResources, String typeOfEvent) {
		this.seriousEvent = seriousEvent;
		this.typeOfInjury = typeOfInjury;
		this.threats = threats;
		this.numberOfInjuries = numberOfInjuries;
		this.extraResources = extraResources;
		this.typeOfReport = typeOfEvent;
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

	public void setExtraResources(String extraResources) {
		this.extraResources = extraResources;
	}

	public String getExtraResources() {
		return extraResources;
	}

	public String getExactLocation() {
		return exactLocation;
	}

	public void setExactLocation(String exactLocation) {
		this.exactLocation = exactLocation;
	}

	public String getAreaSearched() {
		return areaSearched;
	}

	public void setAreaSearched(String areaSearched) {
		this.areaSearched = areaSearched;
	}

	public String getTimeOfDeparture() {
		return timeOfDeparture;
	}

	public void setTimeOfDepature(String timeOfDepature) {
		this.timeOfDeparture = timeOfDepature;
	}

	public void setTypeOfReport(String typeOfReport) {
		this.typeOfReport = typeOfReport;
	}

	public String getTypeOfReport() {
		return typeOfReport;
	}

}
