package org.duchessfrance.duchessdroid;

public class TechnicalException extends Exception {

	private TechnicalException() {
	}

	public TechnicalException(String tag, String detailMessage) {
		super("[" + tag + "] " + detailMessage);
	}

	public TechnicalException(String tag, Throwable throwable) {
		super(throwable);
	}

	public TechnicalException(String tag, String detailMessage, Throwable throwable) {
		super("[" + tag + "] " + detailMessage, throwable);
	}

}
