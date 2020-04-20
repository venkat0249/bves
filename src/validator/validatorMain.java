package validator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import appConfigurationClasses.Clientconfig;
import appConfigurationClasses.Clientconfig.Endpoints.Endpoint;
import appConfigurationClasses.Clientconfig.Endpoints.Endpoint.Attributes.Attribute;


public class validatorMain {
	private static Clientconfig config=null;
	
	public static void main(String arrg[]) {
		System.out.println("###### WELCOME TO REST API Validator ######");
		HttpURLConnection con;
		try {  
			//Read Config XML file and parse it to Config object
	        File file = new File("config.xml");    
	        JAXBContext jaxbContext = JAXBContext.newInstance(Clientconfig.class);    
	        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();    
	        config=(Clientconfig) jaxbUnmarshaller.unmarshal(file);
	        
	        System.out.println(config.getRootURL()); 
	        
	        //access endpoints
	        for(Endpoint endpoint : config.getEndpoints().getEndpoint())
	    	{
	        	
	        	URL url = new URL(config.getRootURL()+endpoint.getEndpointPath());
	        	System.out.println("Accessing Endpoint:"+ url);
		        
		        String inputLine=new String();
				con = (HttpURLConnection) url.openConnection();
				con.setRequestProperty("Content-Type", "application/json");
				if(endpoint.isAuthRequired())
				con.setRequestProperty("api_token", config.getToken());
				if(endpoint.getOperation().equalsIgnoreCase("GET"))
				{
		        con.setRequestMethod("GET");

		        int responsecode = con.getResponseCode();
		        if(responsecode != 200)
		        	System.out.println("Endpoint not found"); 
		        	else
		        	{
		        		Scanner sc = new Scanner(url.openStream());
		        		while(sc.hasNext())
		        		{
		        			inputLine+=sc.nextLine();
		        		}
		        		sc.close();
		        con.disconnect();
		        System.out.println(inputLine); 
		        
		        
		        JSONObject jobj = new JSONObject();
		        JSONParser parse = new JSONParser();
				try {
					jobj = (JSONObject)parse.parse(inputLine);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					System.out.println("JSON parse exception");
					e1.printStackTrace();
				}
		        // checking for each attribute
		        for(Attribute attribute : endpoint.getAttributes().getAttribute())
		        {
		        	System.out.println("---------------------------------------");
		        	if(attribute.getDatatype().equalsIgnoreCase("STRING"))
		        	{
		        		try {
		        				System.out.println("Reading Attribute: "+attribute.getName());
		        				String jsonarr_1 = (String) jobj.get(attribute.getName());
		        				
		        				if(jsonarr_1!=null)
		        				{
		        				System.out.println(attribute.getName()+ " attribute exists");	
		        				System.out.println("Value: "+jsonarr_1); 
		        				}
		        				else
		        					System.out.println("Attention: Attribute not present or arribute value is null!");
		        		}
		        		catch (ClassCastException e) {
		        			System.out.println("Invalid Datatype or Datatype miss match");
		        			System.out.println(e.getMessage());
	        			}
		        		catch (Exception e) {
		        			System.out.println(e.getMessage());
		        			e.printStackTrace();
	        			}
		        	}
		        	
		        	else if(attribute.getDatatype().equalsIgnoreCase("DOUBLE"))
		        	{
		        		try {
		        				System.out.println("Reading Attribute: "+attribute.getName());	
		        				Double jsonarr_1 = (Double) jobj.get(attribute.getName());

		        				if(jsonarr_1!=null)
		        				{
		        				System.out.println(attribute.getName()+ " attribute exists");	
		        				System.out.println("Value: "+jsonarr_1); 
		        				}
		        				else
		        					System.out.println("Attention: Attribute not present or arribute value is null!");
		        		}
		        		catch (ClassCastException e) {
		        			System.out.println("Invalid Datatype or Datatype miss match");
		        			System.out.println(e.getMessage());
	        			}
		        		catch (Exception e) {
		        			System.out.println(e.getMessage());
		        			e.printStackTrace();
	        			}
		        	}
		        	
		        	else if(attribute.getDatatype().equalsIgnoreCase("INT"))
		        	{
		        		try {
	        					System.out.println("Reading Attribute: "+attribute.getName());
		        				Long jsonarr_1 = (Long) jobj.get(attribute.getName());

		        				if(jsonarr_1!=null)
		        				{
		        				System.out.println(attribute.getName()+ " attribute exists");	
		        				System.out.println("Value: "+jsonarr_1); 
		        				}
		        				else
		        					System.out.println("Attention: Attribute not present or arribute value is null!");
		        		}
		        		catch (ClassCastException e) {
		        			System.out.println("Invalid Datatype or Datatype miss match");
		        			System.out.println(e.getMessage());
	        			}
		        		catch (Exception e) {
		        			System.out.println(e.getMessage());
		        			e.printStackTrace();
	        			}
		        	}
		        	
		        	
		        }
		        	}
	        }
	        else
	        	postRequest( con, endpoint);
	    	}
	        
	      //access individulal attributes
	        for(Endpoint endpoint : config.getEndpoints().getEndpoint())
	    	{
	        	System.out.println("Accessing individual attribute Endpoints...");
	        	if(endpoint.getOperation().equalsIgnoreCase("GET"))
	        	{
	        	
		        for(Attribute attribute : endpoint.getAttributes().getAttribute())
		        {
		        	System.out.println("---------------------------------------");
		        	URL url = new URL(config.getRootURL()+attribute.getName());
		        	System.out.println("Accessing Endpoint:"+ url);
			        //HttpURLConnection con;
			        String inputLine=new String();
					con = (HttpURLConnection) url.openConnection();
					con.setRequestProperty("Content-Type", "application/json");
					if(endpoint.isAuthRequired())
					con.setRequestProperty("api_token",config.getToken());
					if(endpoint.getOperation().equalsIgnoreCase("GET"))
					{
			        con.setRequestMethod("GET");
					
			        int responsecode = con.getResponseCode();
			        if(responsecode != 200)
			        	System.out.println("Endpoint not found or endpoint error! Http Response code:"+ responsecode); 
			        	else
			        	{
			        		Scanner sc = new Scanner(url.openStream());
			        		while(sc.hasNext())
			        		{
			        			inputLine+=sc.nextLine();
			        		}
			        		sc.close();
			        con.disconnect();
			        System.out.println(inputLine); 
			        
			        JSONObject jobj;
					try {
						JSONParser parse = new JSONParser();
						jobj = (JSONObject)parse.parse(inputLine);
					
			        // checking for each attribute
		        	if(attribute.getDatatype().equalsIgnoreCase("STRING"))
		        	{
		        		try {
		        				System.out.println("Reading Attribute: "+attribute.getName());
		        				String jsonarr_1 = (String) jobj.get(attribute.getName());
		        				
		        				if(jsonarr_1!=null)
		        				{
		        				System.out.println(attribute.getName()+ " attribute exists");	
		        				System.out.println("Value: "+jsonarr_1); 
		        				}
		        				else
		        					System.out.println("Attention: Attribute not present or arribute value is null!");
		        		}
		        		catch (ClassCastException e) {
		        			System.out.println("Invalid Datatype or Datatype miss match");
		        			System.out.println(e.getMessage());
	        			}
		        		catch (Exception e) {
		        			System.out.println(e.getMessage());
		        			e.printStackTrace();
	        			}
		        	}
		        	
		        	else if(attribute.getDatatype().equalsIgnoreCase("DOUBLE"))
		        	{
		        		try {
		        				System.out.println("Reading Attribute: "+attribute.getName());	
		        				Double jsonarr_1 = (Double) jobj.get(attribute.getName());

		        				if(jsonarr_1!=null)
		        				{
		        				System.out.println(attribute.getName()+ " attribute exists");	
		        				System.out.println("Value: "+jsonarr_1); 
		        				}
		        				else
		        					System.out.println("Attention: Attribute not present or arribute value is null!");
		        		}
		        		catch (ClassCastException e) {
		        			System.out.println("Invalid Datatype or Datatype miss match");
		        			System.out.println(e.getMessage());
	        			}
		        		catch (Exception e) {
		        			System.out.println(e.getMessage());
		        			e.printStackTrace();
	        			}
		        	}
		        	
		        	else if(attribute.getDatatype().equalsIgnoreCase("INT"))
		        	{
		        		try {
	        					System.out.println("Reading Attribute: "+attribute.getName());
		        				Long jsonarr_1 = (Long) jobj.get(attribute.getName());

		        				if(jsonarr_1!=null)
		        				{
		        				System.out.println(attribute.getName()+ " attribute exists");	
		        				System.out.println("Value: "+jsonarr_1); 
		        				}
		        				else
		        					System.out.println("Attention: Attribute not present or arribute value is null!");
		        		}
		        		catch (ClassCastException e) {
		        			System.out.println("Invalid Datatype or Datatype miss match");
		        			System.out.println(e.getMessage());
	        			}
		        		catch (Exception e) {
		        			System.out.println(e.getMessage());
		        			e.printStackTrace();
	        			}
		        	}
				} catch (ParseException e1) {
						
						System.out.print("Invalid JSON response!"); 
						e1.printStackTrace();
					}
		        	
		        }

		        
		        	}
	    	}
	    	}
	        
	    	}
	      } 
		catch (JAXBException e1) {e1.printStackTrace(); } 
		catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.print("#### Validation Complete ####"); 
}
	
	public static void postRequest(HttpURLConnection con,Endpoint endpoint)
	{
		System.out.println("Doing Post Request.."); 
		String inputLine=new String();
		try {
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		DataOutputStream out;
		Map<String, String> parameters = new HashMap<>();
		Map<String, Double> parametersDouble = new HashMap<>();
		Map<String, Integer> parametersInt = new HashMap<>();
		for(Attribute attribute : endpoint.getAttributes().getAttribute())
        {
        	if(attribute.getDatatype().equalsIgnoreCase("STRING"))
        	{
        		parameters.put(attribute.getName(), "RestAPITest");
        		


        			out = new DataOutputStream(con.getOutputStream());
        			out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        			out.flush();
        			out.close();
        	}
        	else if(attribute.getDatatype().equalsIgnoreCase("DOUBLE"))
        	{
        		parametersDouble.put(attribute.getName(), 1.0);
        		

        			out = new DataOutputStream(con.getOutputStream());
        			out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        			out.flush();
        			out.close();
        	}
        	else if(attribute.getDatatype().equalsIgnoreCase("INT"))
        	{
        		parametersInt.put(attribute.getName(), 10);
        		

        			out = new DataOutputStream(con.getOutputStream());
        			out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
        			out.flush();
        			out.close();
        	}

        	
        }
		int responsecode = con.getResponseCode();
        if(responsecode >= 300)
        	System.out.println("Endpoint not found or endpoint error! Http Response code:"+ responsecode); 
        	else
        	{
        		Scanner sc = new Scanner(con.getInputStream());
        		while(sc.hasNext())
        		{
        			inputLine+=sc.nextLine();
        		}
        		sc.close();
				con.disconnect();
				System.out.println("Request Response: "+ inputLine); 
        	}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}