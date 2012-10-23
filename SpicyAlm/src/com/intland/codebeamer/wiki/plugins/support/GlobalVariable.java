package com.intland.codebeamer.wiki.plugins.support;

/**Global Variable Class
 * most are static values
 * only dynamic value is url
 * @author Alexander Börsch
 *
 */
public class GlobalVariable {

	public static int assocationType_depends = 1;
	public static int assocationType_parent = 2;
	public static int assocationType_child = 3;
	public static int assocationType_related = 4;
	public static int assocationType_derived = 5;
	public static int assocationType_violates = 6;
	public static int assocationType_excludes = 7;
	public static int assocationType_invalidates = 8;
	
	public static String trackerType_Bug = "Bug"; 
	public static String trackerType_ChangeRequest = "Change Request";
	public static String trackerType_Requirement = "Requirement"; 
	public static String trackerType_Task = "Task"; 
	
	public static int attachmentType_externalFile = 1; 
	public static int attachmentType_Folder = 2; 
	public static int attachmentType_WikiPage = 8;
	// do there exist more values (e.g.6)?
	
	//variable value
	public String url ="";

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public static int limitation = 100; //standard value
}
