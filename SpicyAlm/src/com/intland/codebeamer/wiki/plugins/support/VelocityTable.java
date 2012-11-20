package com.intland.codebeamer.wiki.plugins.support;

import java.util.List;

/**Class for VeloCity template output
 * Provides various entities (ID, Link, Name, List of Attachments) which were  
 * calculated during entire Process
 * Entities can be positive linked TrackerItems/WikiPages with Attachments or not linked TrackerItems/WikiPages
 * @author Alexander Börsch
 * 
 */

public class VelocityTable {
	
	private int ID;
    private String Link;
    private String Name;
    private List<AttachmentTable> attachment;

	/**Gets List of AttachmentTable Objects
	 * @return List <AttachmentTable> List of AttachmentTable objects with getName() and getUrl()
	 */
	public List<AttachmentTable> getAttachment() {
		return attachment;
	}


	public void setAttachment(List<AttachmentTable> attachment) {
		this.attachment = attachment;
	}


	public String getName() {
		return Name;
	}


	public void setName(String Name) {
		this.Name = Name;
	}


	/**Constructor sets new elements out of a TrackerItem with related Attachments
	 * @param ticketID Integer
	 * @param ticketName String
	 * @param ticketLink String 
	 * @param attachment List<AttachmentTable> List of AttachmentTable objects
	 */
	public VelocityTable (int ID, String Name, String Link, List<AttachmentTable> attachment ) 
	{
		setID (ID);
		setLink(Link);	
		setName(Name);
		setAttachment(attachment);
	}


	public String getID() {
		return Integer.toString(ID) ;
		//Cast integer to string because Velocity template cant handle int-values
	}


	public void setID(int ID) {
		this.ID = ID;
	}


	public String getLink() {
		return Link;
	}


	public void setLink(String ticketLink) {
		this.Link = ticketLink;
	}



}
