package com.ase0401.device.rest.model;

import msfs_0401.LifeCycleStage;

public class LoggedAspectCopy {
	boolean hasValue;
	LifeCycleStage stage;
	Amount spec;

	public LoggedAspectCopy(boolean hasValue) {
		this.hasValue = hasValue;
	}

	public Amount getSpec() {
		return spec;
	}

	public void setSpec(Amount spec) {
		this.spec = spec;
	}

	public LifeCycleStage getStage() {
		return stage;
	}

	public void setStage(LifeCycleStage stage) {
		this.stage = stage;
	}

	public boolean hasValue() {
		return hasValue;
	}

	public void setHasValue(boolean hasValue) {
		this.hasValue = hasValue;
	}
	
}
