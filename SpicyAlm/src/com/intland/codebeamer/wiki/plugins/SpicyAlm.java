package com.intland.codebeamer.wiki.plugins;



import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;


import com.ecyrd.jspwiki.WikiContext;

import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.persistence.dto.UserDto;
import com.intland.codebeamer.persistence.dto.WikiPageDto;
import com.intland.codebeamer.wiki.plugins.*;
import com.intland.codebeamer.wiki.plugins.base.AbstractCodeBeamerWikiPlugin;
import com.intland.codebeamer.wiki.plugins.core.Logic;
import com.intland.codebeamer.wiki.plugins.core.Printer;
import com.intland.codebeamer.wiki.plugins.core.Reader;
import com.intland.codebeamer.wiki.plugins.support.GlobalVariable;


/**SpicyAlm Plugin
 * Main class 
 * Provides Code which is executable on Codebeamer server (version 6.0.2) and runs within JSPWiki
 * @author Alexander Börsch
 * September 2012
 *
 */
public class SpicyAlm extends AbstractCodeBeamerWikiPlugin {
	
	
	private Set<TrackerItemDto.Flag> flag = null;
	
	public String execute(WikiContext context, Map params) {

		UserDto user = getUserFromContext(context);
		//WikiPageDto page = getPageFromContext(context);
		
		//object instantiation 
		SLReader slreader = new SLReader (user);
		Logic logic = new Logic ();
		GlobalVariable globalVariable = new GlobalVariable();
		Printer printer = new Printer(globalVariable);
		Reader readerObject = new Reader(globalVariable);
		HttpServletRequest httpRequest = context.getHttpRequest();	//required for address creating
		List<List<Object>>  results = null;
		
		//configuration
		globalVariable.setUrl("http://"+httpRequest.getServerName() + ":" + httpRequest.getServerPort() + "/cb" ); //set Address
		
		//read user input as plugin parameters
		readerObject.readParameter(params);	

		//**********************reading*************
		//read TrackerItems
		List<TrackerItemDto> allTrackerItems = slreader.readAllTrackerItems (readerObject.getTrackerID(), flag); 

		//find all Associations in this specific project
		List<Object> allAssoc = slreader.readAllAssociations();	//TODO: still Dummy values
		
		//check if every TrackerItem has minimum one association
	    results = logic.checkTrackerItems(allTrackerItems, null, allAssoc);

		//**********************logic*************
		//check if every TrackerItem has minimum one association
		results = logic.checkTrackerItems(allTrackerItems, null, allAssoc);
		
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
			
			//check whether Attachments or TrackerItems have to print
			if (readerObject.getProjectId()!=null)
				velocityContext.put("table", printer.printAttachmentsAscii(results));	 //TODO: in Process
			else{
				if (readerObject.getNoLinked())	//print just the "notLinked"-TrackerItems
				{
					velocityContext.put("table", printer.printTrackerItemsAscii(results,readerObject.getNoLinked()));
					return renderPluginTemplate("spicyAlm noArtifacts.vm", velocityContext);	
				}
				else	//print just the "Linked"-TrackerItems
				{
					velocityContext.put("table", printer.printTrackerItemsAscii(results,readerObject.getNoLinked()));
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
