package com.intland.codebeamer.wiki.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;


import com.ecyrd.jspwiki.WikiContext;

import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.persistence.dto.UserDto;
import com.intland.codebeamer.persistence.dto.WikiPageDto;
import com.intland.codebeamer.wiki.plugins.base.AbstractCodeBeamerWikiPlugin;
import com.intland.codebeamer.wiki.plugins.core.Logic;
import com.intland.codebeamer.wiki.plugins.core.Printer;
import com.intland.codebeamer.wiki.plugins.core.Reader;
import com.intland.codebeamer.wiki.plugins.support.GlobalVariable;
import com.intland.codebeamer.wiki.plugins.support.VelocitySupport;
import com.intland.codebeamer.wiki.plugins.support.VelocityTable;



/**SpicyAlm Plugin
 * Main class 
 * Provides Code which is executable on Codebeamer server (version 6.0.2) and runs within JSPWiki
 * @author Alexander Börsch
 * September 2012
 *
 * Velocity templates are available in project folder /velocityTemplate
 * both have to be copied into codebeamer folder ...\tomcat\webapps\cb\config\templates\wiki-plugin
 */
public class SpicyAlm extends AbstractCodeBeamerWikiPlugin {
	
	
	private Set<TrackerItemDto.Flag> flag = null;
	
	public String execute(WikiContext context, Map params) {

		UserDto user = getUserFromContext(context);
		//WikiPageDto page = getPageFromContext(context);
		
		//object instantiation 
		Logic logic = new Logic ();
		GlobalVariable globalVariable = new GlobalVariable();
		VelocitySupport velocitySupport = new VelocitySupport();
		Reader readerObject = new Reader(globalVariable, velocitySupport);
		SLReader slreader = new SLReader (user, readerObject);
		Printer printer = new Printer(globalVariable, velocitySupport);
		
		HttpServletRequest httpRequest = context.getHttpRequest();	//required for address creating
		List<List<Object>>  results = null;
		
		//wikiPage id from context 
		readerObject.setWikiID(context.getName());
		
		//configuration
		globalVariable.setUrl("http://"+httpRequest.getServerName() + ":" + httpRequest.getServerPort() + "/cb" ); //set Address
		
		//read user input as plugin parameters
		readerObject.readParameter(params);	

		//**********************reading*************
		
		List<WikiPageDto> wiki = null;
		List<TrackerItemDto> allTrackerItems =null;
		List<Object> allAssoc = null;
		  
	    if (readerObject.getWikiContext())	//check if user chooses wikicontext or tracker 
		{
			//read WikiPage
			wiki = slreader.readWikiPage(readerObject.getWikiID());	//TODO: statisch
			
			//find all Associations related to all wikipages in this specific project
			allAssoc = slreader.readAllAssociationsWiki();	
		}
		else
		{
			//read TrackerItems
			allTrackerItems = slreader.readAllTrackerItems(readerObject.getTrackerID(), flag); 
		
			//find all Associations in this specific project
		    allAssoc = slreader.readAllAssociationsTracker();	
		}

		//**********************logic*************	
		//if user chooses wikicontext, checks all artifacts linked to WikiPage
		if (readerObject.getWikiContext())	
		{
			results = logic.checkWikiPages(wiki, null, allAssoc);
		}
		//check whether Attachments or TrackerItems have to check
		else if (readerObject.getProjectId()==null) //checks TrackerItem
		{
			//check if every TrackerItem has minimum one association
			results = logic.checkTrackerItems(allTrackerItems, null, allAssoc);
		}
			
		//**********************Print*************
		String output ="";
		if (readerObject.getDisplay() == "chart") //"chart"-parameter
		{
			if (readerObject.getProjectId()!=null)
				output = printer.printPluginPattern(results, true);	//Attachment specific output //TODO: in Process
			else
				output = printer.printPluginPattern(results, false); //Tracker specific output
		}
		else	//"table"-parameter
		{
			// set up Velocity context
			VelocityContext velocityContext = getDefaultVelocityContextFromContext(context);
			
			List<VelocityTable> table = null;
			
			//print wikicontext 
			if (readerObject.getWikiContext())
			{
				velocityContext.put("table", printer.printWikPagesAscii(results, false));
				velocityContext.put("support", velocitySupport );
				return renderPluginTemplate("spicyAlm withArtifacts.vm", velocityContext);
			}
			
			//check whether Attachments or TrackerItems have to print
			else if (readerObject.getProjectId()!=null)
			{
				velocityContext.put("table", printer.printAttachmentsAscii(results));	 //TODO: in Process
				velocityContext.put("support", velocitySupport );	 
				
			}
			else{
				if (readerObject.getNoLinked())	//print just the "notLinked"-TrackerItems
				{
					velocityContext.put("table", printer.printTrackerItemsAscii(results,readerObject.getNoLinked()));
					velocityContext.put("support", velocitySupport);	
					return renderPluginTemplate("spicyAlm noArtifacts.vm", velocityContext);	
				}
				else	//print just the "Linked"-TrackerItems
				{
					velocityContext.put("table", printer.printTrackerItemsAscii(results,readerObject.getNoLinked()));
					velocityContext.put("support", velocitySupport);	
					return renderPluginTemplate("spicyAlm withArtifacts.vm", velocityContext);
				}
			}
		}

		return output;
		
	}
	
	/**
	 * Add flag value 
	 */
	private void initTrackerItemFlag()
	{
		//TODO: only still open marked interesting?
		flag.add(TrackerItemDto.Flag.Aggregated);
		//set.add(TrackerItemDto.Flag.Closed);
		//set.add(TrackerItemDto.Flag.Deleted);
		//set.add(TrackerItemDto.Flag.Resolved);
		//set.add(TrackerItemDto.Flag.Successful);
	}
	
	/**
	 * @param deleteFlag
	 * @param addFlag
	 */
	public void setTrackerItemFlag (TrackerItemDto.Flag deleteFlag, TrackerItemDto.Flag addFlag)
	{
		flag.remove(deleteFlag);
		flag.add(addFlag);
	}
	
	
	
}
