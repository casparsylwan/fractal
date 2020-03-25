package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpcomputeIterations extends Thread {

	private URL url;
	private HttpURLConnection connection;
	private int responseCode, index=0;
	private BufferedReader buffer;
	private String strResponse;

	public Integer urlCall(String adress)
	{
		try
		{
			url = new URL(adress);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();

			connection.setRequestMethod("GET");
			connection.setConnectTimeout(1000);
			connection.setReadTimeout(1000);
			responseCode = connection.getResponseCode();
			System.out.println(responseCode);

			if (responseCode>299) {
				System.out.println(responseCode);
				strResponse= Integer.toString(responseCode);
				connection.disconnect();
			}
			else
			{

				buffer = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
				strResponse = buffer.readLine();
				

				buffer.close();
				connection.disconnect();
			}

		}
		catch(MalformedURLException e)
		{

		}
		catch(IOException e)
		{

		}		
		return Integer.parseInt(strResponse) ;
	}


}
