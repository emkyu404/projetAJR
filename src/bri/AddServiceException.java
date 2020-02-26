package bri;

public class AddServiceException extends Exception {
	
	private String message;
	
	public AddServiceException(String message) {
		this.message = "Erreur : " + message;
	}
	
	public String toString() {
		return message;
	}

}
