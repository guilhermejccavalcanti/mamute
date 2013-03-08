package br.com.caelum.brutal.auth;

import br.com.caelum.brutal.model.MethodType;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class SignupInfo {

	private final MethodType method;
	private final String email;
	private final String name;
	private final String location;

	public SignupInfo(MethodType method, String email, String name,
			String location) {
				this.method = method;
				this.email = email;
				this.name = name;
				this.location = location;
	}

	public static SignupInfo fromFacebook(String body) {
		JsonObject jsonObj = new JsonParser().parse(body).getAsJsonObject();
		String email = jsonObj.get("email").getAsString();
		String name = jsonObj.get("name").getAsString();
		String location = jsonObj.getAsJsonObject("location").get("name").getAsString();
		
		return new SignupInfo(MethodType.FACEBOOK, email, name, location);
	}

	public MethodType getMethod() {
		return method;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getLocation() {
		return location;
	}

}
