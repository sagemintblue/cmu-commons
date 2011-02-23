package edu.cmu.commons.data.validation.validators;

import org.hibernate.validator.constraints.URL;

public class URLContainer {
	@URL(protocol = "ftp")
	private java.net.URL value;

	public URLContainer() {}

	public URLContainer(java.net.URL value) {
		this.value = value;
	}

	public java.net.URL getValue() {
		return value;
	}

	public void setValue(java.net.URL value) {
		this.value = value;
	}
}