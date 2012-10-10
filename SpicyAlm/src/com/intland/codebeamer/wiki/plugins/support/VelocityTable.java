package com.intland.codebeamer.wiki.plugins.support;

import java.util.List;

/**Class for VeloCity template output
 * Provides various entities (TicketID, TicketLink, TicketName, List of Attachments) which were  
 * calculated during entire Process
 * Entities can be positive linked TrackerItems with Attachments or not linked TrackerItems
 * @author Alexander Börsch
 * 
 */

public class VelocityTable {
	
	private int ticketID;
    private String ticketLink;
    private String ticketName;
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


	public String getTicketName() {
		return ticketName;
	}


	public void setTicketName(String ticketName) {
		this.ticketName = ticketName;
	}


	/**Constructor sets new elements out of a TrackerItem with related Attachments
	 * @param ticketID Integer
	 * @param ticketName String
	 * @param ticketLink String 
	 * @param attachment List<AttachmentTable> List of AttachmentTable objects
	 */
	public VelocityTable (int ticketID, String ticketName, String ticketLink, List<AttachmentTable> attachment ) 
	{
		setTicketID (ticketID);
		setTicketLink(ticketLink);	
		setTicketName(ticketName);
		setAttachment(attachment);
	}


	public String getTicketID() {
		return Integer.toString(ticketID) ;
		//Cast integer to string because Velocity template cant handle int-values
	}


	public void setTicketID(int ticketID) {
		this.ticketID = ticketID;
	}


	public String getTicketLink() {
		return ticketLink;
	}


	public void setTicketLink(String ticketLink) {
		this.ticketLink = ticketLink;
	}



}
