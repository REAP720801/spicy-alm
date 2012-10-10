package com.intland.codebeamer.wiki.plugins.core;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;

import com.intland.codebeamer.wiki.plugins.support.GlobalVariable;


/**Centralized Reader Class for SpicyAlm Plugin and StandAloneApplication
 * Enables to read user input values  and set various parameters
 * @author Alexander Börsch
 *
 */
public class Reader {

	private String url ="noValue";
	private String display ="table"; 
	private String projectName;		
	private int[] projectId =null;
	private int[] trackerID =null;	
	private String user ="noValue";
	private String password ="noValue";
	
	private Boolean notLinked = false;
	private Boolean tracker = false;

	private Boolean positive = false;
	private Boolean negative = false;
	private Boolean all = false;
	
	Reader readerObject = null;
	
	GlobalVariable globalVariable = null;
	
	/**Returns Instance of GlobalVariable object
	 * useful for "url" which is used in Printer-class
	 * @return GlobalVariable Returns Instance of GlobalVariable object
	 */
	public GlobalVariable getGlobalVariable() {
		return this.globalVariable;
	}
	
	public Reader (GlobalVariable globalVariable)
	{
		this.globalVariable = globalVariable;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
				if (!url.isEmpty())		//TODO: testcases schreiben, noch fehlerhaft
						{
					//'' ja
					//'http://asdfPc:8080/cb' ja
					//' 'nein 
					//(com.ecyrd.jspwiki.plugin.PluginException:Plugin failed, java.lang.StringIndexOutOfBoundsException:String index out of range: -2)
					
					int start = 0;
					int end = 0;
						try{	
							 start = url.lastIndexOf("//");
							 end = url.indexOf(":8080");
							 
							 String tempVar = url.substring(start+2, end);
								if (start!=0 && end !=0)	//prüfe ob Eingabe 
								{
									url ="http://"+ tempVar + ":8080/cb/remote-api";
									globalVariable.setUrl("http://"+ tempVar + ":8080/cb");
								}
						}
						catch (StringIndexOutOfBoundsException e)
						{
							
						}
					}
					else 
					{
						url ="urlLeer";
					}
		this.url = url;
	}

	public String display() {
		return display;
	}
	
	private boolean checkBoolean (String temp)
	{
		if (temp == "true")
			return true;
		else 
			return false;	
	}
	
	/**Standard value is "table", changeable through user input to "chart"
	 * @param display
	 */
	public void setDisplay(String display) {
		if (display.contains("chart"))
			this.display ="chart";
		else 
			this.display ="table";
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectId=null; //set default-value, not both are allowed
		this.projectName = projectName;
	}

	public int[] getTrackerID() {
		return trackerID;
	}

	public void setTrackerID(String trackerID) {				
				List<Integer> tempArray = new ArrayList<Integer>();
				String[] tempString = trackerID.split(",");
				
				for (int i = 0; i<tempString.length; i++ )
				{
					try {
						int temp = Integer.parseInt(tempString[i]);
						tempArray.add(temp);
					} 
					catch (NumberFormatException e) {
						  System.out.println("i ist keine Zahl. " + e.getMessage());
					}
				}
				
				this.trackerID =ArrayUtils.toPrimitive(tempArray.toArray(new Integer[tempArray.size()]));		
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getNoLinked() {
		return notLinked;
	}

	/**Standard value is false
	 * @param attachment
	 */
	public void setNoLinked(String noLinked) {
		if (noLinked.contains("true"))
		{
			this.notLinked = true;
		}
		else
			this.notLinked = false;
	}

	public Boolean getTracker() {
		return tracker;
	}

	/**Standardvalue is false
	 * @param tracker
	 */
	public void setTracker(String tracker) {
		if (tracker.indexOf("true")>-1)
		{
			this.tracker = true;
		}

	}

	public Boolean getPositve() {
		return positive;
	}

	/**Standardvalue is false
	 * @param positive
	 */
	public void setPositve(String positive) {
		if (positive.indexOf("true")>-1)
		{
			this.positive = true;
		}
	}

	public Boolean getNegative() {
		return negative;
	}

	/**Standardvalue is false
	 * @param negative
	 */
	public void setNegative(String negative) {
		if (negative.indexOf("true")>-1)
		{
			this.negative = true;
		}
	}

	public Boolean getAll() {
		return all;
	}

	/**Standardvalue is false
	 * @param all
	 */
	public void setAll(String all) {
		if (all.indexOf("true")>-1)
		{
			this.all = true;
		}
	}

	/**Checks if String value is numeric
	 * @param str
	 * @return boolean True or False
	 */
	private static boolean isNumeric(String str)
	{
	  NumberFormat formatter = NumberFormat.getInstance();
	  ParsePosition pos = new ParsePosition(0);
	  formatter.parse(str, pos);
	  return str.length() == pos.getIndex();
	}
	
	/**Reads User input parameters
	 * This method is only used for SpicyAlm Plugin.
	 * It has less parameter (e.g. excluded url, login, pw) than the StandAloneApplication version
	 * @param params Expected Map<String, String> map = new HashMap<String, String>();
	 */
	public void readParameter(Map params)
	{
		String display = (String)params.get("display");	
		
		//String projectName = (String)params.get("projectName");	//String
		String projectId = (String)params.get("projectId");		//number
		String trackerId = (String)params.get("trackerId");		//String
		String noLinked = (String)params.get("notLinked");
		
		//required to avoid "nullpointerexceptions" when user missed these parameters 		
		//necessary parameters
		try 
		{
			if(!display.isEmpty())
				this.setDisplay(display);
		}
		catch (NullPointerException e)
		{
			System.out.println("Necessary Parameters are missing. " + e.getMessage());
		}
		
		try 
		{
			if(!trackerId.isEmpty())
				this.setTrackerID(trackerId);
		}
		catch (NullPointerException e)
		{
			System.out.println("Necessary Parameters are missing. " + e.getMessage());
			this.setTrackerID("1");	
		}
		
		try 
		{
			if(!noLinked.isEmpty())
				this.setNoLinked(noLinked);
		}
		catch (NullPointerException e)
		{
			System.out.println("Necessary Parameters are missing. " + e.getMessage());
		}
			
	}

	//TODO: nullpointerexception if parameters are not used 
	/**Reads User input parameters
	 * This method is only used for StandAloneApplication.
	 * It has more parameter (e.g. url, login, pw) than the SpicyAlm Plugin version
	 * @param params Expected Map<String, String> map = new HashMap<String, String>();
	 * @param params
	 */
	public void readParameterSAloneApp(Map params)
	{
		//mandatory Parameters
		String loginName = (String)params.get("user");			
		String loginPw = (String)params.get("password");		
		String url = (String)params.get("url");	
				
		//necessary parameters
		String display = (String)params.get("display");	
		String noLinked = (String)params.get("notLinked");
		String trackerId = (String)params.get("trackerId");		
		
		//String projectName = (String)params.get("projectName");	//String
		String projectId = (String)params.get("projectId");		//number
		
		//required to avoid "nullpointerexceptions" when user missed these parameters 	
		//mandatory parameters
		try 
		{
			if(!loginPw.isEmpty())
				this.setPassword(loginPw);
			if(!loginName.isEmpty())
				this.setUser(loginName);
			if(!url.isEmpty())
				this.setUrl(url);
		}
		catch (NullPointerException e)
		{
			System.out.println("Mandatory Parameters are missing. " +e.getMessage());
		}
		
		//necessary parameters
		try 
		{
			if(!display.isEmpty())
				this.setDisplay(display);
		}
		catch (NullPointerException e)
		{
			System.out.println("Necessary Parameters are missing. " + e.getMessage());
		}
		
		try 
		{
			if(!trackerId.isEmpty())
				this.setTrackerID(trackerId);
		}
		catch (NullPointerException e)
		{
			System.out.println("Necessary Parameters are missing. " + e.getMessage());
			this.setTrackerID("1");	
		}
		
		try 
		{
			if(!noLinked.isEmpty())
				this.setNoLinked(noLinked);
		}
		catch (NullPointerException e)
		{
			System.out.println("Necessary Parameters are missing. " + e.getMessage());
		}
	
		//not required values
		try //TODO: in process
		{
			//if (!projectId.isEmpty())
				//this.setProjectId(projectId);	
			//if (!projectName.isEmpty())
				//this.setProjectName(projectName);	//TODO: nicht zeitgleich mit projectID			
		}
		catch (NullPointerException e)
		{
			System.out.println(e.getMessage());
		}
		
	}
	
	public int[] getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectName = null; //both are not allowed simultaneously
		
		List<Integer> tempArray = new ArrayList<Integer>();
		String[] tempString = projectId.split(",");
		
		for (int i = 0; i<tempString.length; i++ )
		{
			try {
				int temp = Integer.parseInt(tempString[i]);
				tempArray.add(temp);
			} 
			catch (NumberFormatException e) {
				  System.out.println("i ist keine Zahl. " + e.getMessage());
			}
		}
		
		this.projectId =ArrayUtils.toPrimitive(tempArray.toArray(new Integer[tempArray.size()]));		
	}

	public String getDisplay() {
		return display;
	}

}
