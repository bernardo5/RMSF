package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.Authenticator;
import java.net.URLConnection;
import java.nio.charset.Charset;
 

 
public class GET {
	
	public static void main(String[] args) {
		System.out.println("\nOutput: \n" + callURL("https://backend.sigfox.com/api/devicetypes/56bdd1da9336b182b106d3b0/id-pac"));
	}
 
	public static String callURL(String myURL) {
		System.out.println("Requested URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			Authenticator.setDefault (new Authenticator() {
			    protected PasswordAuthentication getPasswordAuthentication() {
			        return new PasswordAuthentication ("56c47b4c9336adb5ba39c9b6", "dd6bd147da1dcc9e34b4674b0f0be948".toCharArray());
			    }
			});
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
}