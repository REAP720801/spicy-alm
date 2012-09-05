package com.intland.codebeamer.wiki.plugins;

import java.text.NumberFormat;
import java.text.ParsePosition;

public class Reader {
	
	//TODO: lösche useMethoden
	private String url =null;
	private String format =null;
	private String projectName;	//by name
	private int trackerID =-1;	//standart value in readAllTrackerItemsByTracker() for all Trackers
	private String user =null;
	private String password =null;
	
	//TODO: prüfe ob auch wirklich false bzw true steht
	private Boolean attachment = false;
	private Boolean tracker = false;
	private Boolean number = false;
	private Boolean name = false;
	
	private Boolean positive = false;
	private Boolean negative = false;
	private Boolean all = false;
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		//TODO: getURL()
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
									url ="http://"+ tempVar + ":8080/cb/remote-api";
								
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

	public String getFormat() {
		return format;
	}
	
	private boolean checkBoolean (String temp)
	{
		if (temp == "true")
			return true;
		else 
			return false;
		
	}

	public void setFormat(String format) {
		
		if (format == "pieChart" || isNumeric(format))
		{
			this.format = format;
			this.number = true;
		}
		else 
		{
			this.number =false;
		}
		
		if (format =="ascii" || format.isEmpty() ||format == " ")	//direkter Befehl oder leer 
		{
			//''
			//' '
			//ascii
			format = "ascii";
			this.format = format;
			this.name =true;
		}
		else 
		{
			this.name = false;
		}
		
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getTrackerID() {
		return trackerID;
	}

	//TODO: abfangen: java.lang.IllegalArgumentException
	public void setTrackerID(String trackerID) {
		if (isNumeric(trackerID))
		{
			try {
				  int i = Integer.parseInt(trackerID);
				  if (i>=1)
				  {
					  this.trackerID = i;
				  }
				} catch (NumberFormatException e) {
				  System.out.println("i ist keine Zahl");
				}
		}				
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

	
	public Boolean getAttachment() {
		return attachment;
	}

	/**Standartvalue is false
	 * @param attachment
	 */
	public void setAttachment(String attachment) {
		if (attachment.indexOf("true")>-1)
		{
			this.attachment = true;
		}
	}

	public Boolean getTracker() {
		return tracker;
	}

	/**Standartvalue is false
	 * @param tracker
	 */
	public void setTracker(String tracker) {
		if (tracker.indexOf("true")>-1)
		{
			this.tracker = true;
		}

	}

	public Boolean getNumber() {
		return number;
	}
	
	/**Standartvalue is false
	 * @param Number
	 */
	public void setNumber(String number) {
		if (number.indexOf("true")>-1)
		{
			this.number = true;
		}
	}

	public Boolean getName() {
		return name;
	}

	/**Standartvalue is false
	 * @param Number
	 */
	public void setName(String name) {
		if (name.indexOf("true")>-1)
		{
			this.name = true;
		}
	}

	public Boolean getPositve() {
		return positive;
	}

	/**Standartvalue is false
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

	/**Standartvalue is false
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

	/**Standartvalue is false
	 * @param all
	 */
	public void setAll(String all) {
		if (all.indexOf("true")>-1)
		{
			this.all = true;
		}
	}

	private static boolean isNumeric(String str)
	{
	  NumberFormat formatter = NumberFormat.getInstance();
	  ParsePosition pos = new ParsePosition(0);
	  formatter.parse(str, pos);
	  return str.length() == pos.getIndex();
	}

}
