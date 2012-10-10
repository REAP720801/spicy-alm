/**
 * 
 */
package com.intland.codebeamer.wiki.plugins.support;

/**Class for Attachment output it's usable in VeloCity template
 * @author Alexander Börsch
 *
 */
public class AttachmentTable {

	private String name;
	private String url;
	
	public AttachmentTable (String name, String url)
	{
		setName(name);
		setUrl(url);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
