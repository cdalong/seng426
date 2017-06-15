package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServerConfig {
	public static void Setup(String baseUrl) throws Exception
	{
		Setup(baseUrl, 25);
	}
	
	public static void Setup(String baseUrl, int numEntries) throws Exception
	{
		URL url = new URL(baseUrl);
		
		String token = GetXsrfToken(url);
		System.out.println("Registering test account...");
		RegisterTestAccount(url, token);
		System.out.println("Logging into test account...");
		String sessionId = LoginToTestAccount(url, token);
		
		System.out.println("Confirming account contents...");
		JsonArray entries = GetEntries(url, token, sessionId);
		System.out.println("Existing test entries: " + entries.size());
		if (entries.size() != numEntries)
			System.out.println(String.format("Updating account to %d entries...", numEntries));
			
		for (int i = entries.size(); i < numEntries; ++i)
			AddTestEntry(url, token, sessionId);
		for (int i = entries.size(); i > numEntries; --i) {
			int id = entries.get(i - 1).getAsJsonObject().get("id").getAsInt();
			DeleteEntry(url, token, sessionId, id);
		}
		
		System.out.println("Test account ready for testing");
	}
	
	public static String GetXsrfToken(URL baseUrl) throws Exception
	{
		HttpURLConnection connection = (HttpURLConnection) baseUrl.openConnection();
		connection.setRequestMethod("GET");
		
		List<String> cookies = connection.getHeaderFields().get("Set-Cookie");
		String xsrfToken = "";
		if (cookies != null)
			for (String cookie : cookies)
				if (cookie.startsWith("XSRF-TOKEN"))
					xsrfToken = cookie.split(";")[0].split("=", 2)[1];
		
		return xsrfToken;
	}
	
	public static void RegisterTestAccount(URL baseUrl, String token) throws Exception
	{
		URL url = new URL(baseUrl, "api/register");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Cookie", "XSRF-TOKEN="+token);
		connection.setRequestProperty("X-XSRF-TOKEN", token);
		
		JsonObject obj = new JsonObject();
		obj.addProperty("email", "test@acme.com");
		obj.addProperty("langKey", "en");
		obj.addProperty("login", "test@acme.com");
		obj.addProperty("password", "test");
		
		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(obj.toString());
		writer.flush();
		writer.close();

		int code = connection.getResponseCode();
		if (code == 200) {
			System.out.println("Registered new account: test@acme.com");
		} else {
			String response = GetResponseError(connection);
			if (response.toString().startsWith("login already in use")) {
				System.out.println("Account already registered...");
			} else {
				System.out.println("Registration error...");
				System.out.println("code: " + code);
			}
		}
	}
	
	public static String LoginToTestAccount(URL baseUrl, String token) throws Exception
	{
		URL url = new URL(baseUrl, "api/authentication");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Cookie", "XSRF-TOKEN="+token);
		connection.setRequestProperty("X-XSRF-TOKEN", token);
		
		ArrayList<String> parameters = new ArrayList<String>();
		parameters.add("j_username=test@acme.com");
		parameters.add("j_password=test");
		parameters.add("remember-me=true");
		parameters.add("submit=Login");
		String parameterList = StringUtils.join(parameters, '&');

		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(parameterList);
		writer.flush();
		writer.close();
		
		int code = connection.getResponseCode();
		if (code == 200) {
			System.out.println("Login OK");
			
			List<String> cookies = connection.getHeaderFields().get("Set-Cookie");
			String sessionId = "";
			if (cookies != null)
				for (String cookie : cookies)
					if (cookie.startsWith("JSESSIONID"))
						sessionId = cookie.split(";")[0].split("=", 2)[1];
			
			return sessionId;
		} else {
			System.out.println("Login error...");
			System.out.println("code: " + code);
			return "";
		}
	}
	
	public static JsonArray GetEntries(URL baseUrl, String token, String sessionId) throws Exception
	{
		URL url = new URL(baseUrl, "api/acme-passes?page=0&size=999");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Cookie", "XSRF-TOKEN="+token + "; JSESSIONID=" + sessionId);
		connection.setRequestProperty("X-XSRF-TOKEN", token);

		int code = connection.getResponseCode();
		if (code == 200) {
			String content = GetResponseContent(connection);
			JsonParser parser = new JsonParser();
			return parser.parse(content).getAsJsonArray();
		} else {
			System.out.println("Connection failure...");
			System.out.println("code: " + code);
			return null;
		}
	}
	
	public static void AddTestEntry(URL baseUrl, String token, String sessionId) throws Exception
	{
		URL url = new URL(baseUrl, "api/acme-passes?page=0&size=999");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Cookie", "XSRF-TOKEN="+token + "; JSESSIONID=" + sessionId);
		connection.setRequestProperty("X-XSRF-TOKEN", token);
		
		int num = new Random().nextInt(1000);
		String key = String.format("test_%03d", num);
		JsonObject obj = new JsonObject();
		obj.addProperty("login", key);
		obj.addProperty("password", key);
		obj.addProperty("site", key);
		
		OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(obj.toString());
		writer.flush();
		writer.close();

		int code = connection.getResponseCode();
		if (code == 200 || code == 201) {
			//
		} else {
			System.out.println("Connection failure...");
			System.out.println("code: " + code);
		}
	}
	
	public static void DeleteEntry(URL baseUrl, String token, String sessionId, int id) throws Exception
	{
		URL url = new URL(baseUrl, "api/acme-passes/" + id);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("DELETE");
		connection.setRequestProperty("Cookie", "XSRF-TOKEN="+token + "; JSESSIONID=" + sessionId);
		connection.setRequestProperty("X-XSRF-TOKEN", token);

		int code = connection.getResponseCode();
		if (code == 200 || code == 204) {
			//
		} else {
			System.out.println("Connection failure...");
			System.out.println("code: " + code);
		}
	}
	
	public static String GetResponseContent(HttpURLConnection connection) throws Exception
	{
		String line;
		StringBuffer response = new StringBuffer();
		InputStreamReader reader = new InputStreamReader(connection.getInputStream());
		BufferedReader bufferedReader = new BufferedReader(reader);
		while ((line = bufferedReader.readLine()) != null) {
		    response.append(line);
		    response.append('\r');
		}
		bufferedReader.close();
		
		return response.toString();
	}
	
	public static String GetResponseError(HttpURLConnection connection) throws Exception
	{
		String line;
		StringBuffer response = new StringBuffer();
		InputStreamReader reader = new InputStreamReader(connection.getErrorStream());
		BufferedReader bufferedReader = new BufferedReader(reader);
		while ((line = bufferedReader.readLine()) != null) {
		    response.append(line);
		    response.append('\r');
		}
		bufferedReader.close();
		
		return response.toString();
	}
}
