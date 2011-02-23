package edu.cmu.commons.data.validation.validators;

import org.hibernate.validator.constraints.URL;

public class URIContainer {
	@URL(protocol = "ftp")
	private java.net.URI value;

	public URIContainer() {}

	public URIContainer(java.net.URI value) {
		this.value = value;
	}

	public java.net.URI getValue() {
		return value;
	}

	public void setValue(java.net.URI value) {
		this.value = value;
	}
}