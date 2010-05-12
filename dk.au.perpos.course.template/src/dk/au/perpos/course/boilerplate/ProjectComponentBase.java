package dk.au.perpos.course.boilerplate;

import dk.au.perpos.sensing.SensingService;
import dk.au.perpos.spatialsupport.locationmodel.LocationModelService;

public abstract class ProjectComponentBase {

	protected SensingService sensingService;
	protected LocationModelService locationModelService;

	protected abstract void activate();
	protected abstract void deactivate();

	final protected SensingService getSensingService() {
		return sensingService;
	}

	final protected LocationModelService getLocationModelService() {
		return locationModelService;
	}

	final protected void setSensingService(SensingService sensingService) {
		this.sensingService = sensingService;
	}

	final protected void removeSensingService(SensingService sensingService) {
		this.sensingService = null;
	}

	final protected void setLocationModelService(LocationModelService locationModelService) {
		this.locationModelService = locationModelService;
	}

	final protected void removeLocationModelService(LocationModelService locationModelService) {
		this.locationModelService = null;
	}


}
