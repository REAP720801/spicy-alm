package remoteApi;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.persistence.dto.WikiPageDto;
import com.intland.codebeamer.wiki.plugins.core.Logic;
import com.intland.codebeamer.wiki.plugins.core.Printer;
import com.intland.codebeamer.wiki.plugins.core.Reader;
import com.intland.codebeamer.wiki.plugins.support.AttachmentTable;
import com.intland.codebeamer.wiki.plugins.support.GlobalVariable;
import com.intland.codebeamer.wiki.plugins.support.VelocitySupport;
import com.intland.codebeamer.wiki.plugins.support.VelocityTable;



/**SpicyAlm StandAloneApplication
 * Provides code for a stand alone application which is working with remote api
 * @author Alexander Börsch
 *
 */
public class StandAloneApplication {

	public static void main(String[] args) {

		//object instantiation 

		GlobalVariable globalVariable = new GlobalVariable();
		VelocitySupport velocitySupport = new VelocitySupport();
		Reader readerObject = new Reader(globalVariable, velocitySupport);
		RAReader raReader =new RAReader(readerObject);
		Printer printer = new Printer(globalVariable, velocitySupport);
		Logic logic = new Logic ();
		List<List<Object>>  results = null;
		
		//configuration parameters
		String serviceUrl = "http://localhost:8080/cb/remote-api";
		String login ="bond";
		String password = "007";
		
		//String serviceUrl ="http://wifo1-54.bwl.uni-mannheim.de:8080/cb/remote-api";
		//String login ="IS613_D_Boersch";
		//String password = "a123456";		
		
		//String serviceUrl="https://codebeamer.com/cb/remote-api";
		//String login ="aboersch";
		//String password = "a123456";	
		
		//configuration user simulated parameters
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", serviceUrl);
		map.put("user", login);
		map.put("password", password);
		//map.put("projectId", "2");	 //TODO: in process
		map.put("display", "chart");	//input: chart or table
		map.put("trackerId", "5,6,7");	    //Pattern: specific values as int,int,int,... or for a full search use: "" -->alle
		map.put("notLinked", "false");
		map.put("outputLimit", "100");
		map.put("artifactLimit", "file");
		map.put("wikiContext", "false");	
		
		//read user parameters
		readerObject.readParameterSAloneApp(map);

	//codebeamer login with configuration parameters
	raReader.login(readerObject.getUrl(), readerObject.getUser(), readerObject.getPassword());
	
	
	//**********************reading*************

	List<WikiPageDto> wiki = null;
	List<TrackerItemDto> allTrackerItems =null;
	List<Object> allAssoc = null;
	
	if (readerObject.getWikiContext())	//check if user chooses wikicontext or tracker 
	{
		//read WikiPage
		wiki = raReader.readWikiPage("1039");	//TODO: statisch
		
		//find all Associations related to all wikipages in this specific project
		allAssoc = raReader.readAllAssociationsWiki();	
	}
	else
	{
		//read TrackerItems
		allTrackerItems = raReader.readAllTrackerItems(readerObject.getTrackerID()); 
	
		//find all Associations in this specific project
	    allAssoc = raReader.readAllAssociationsTracker();	
	}
	
	//**********************logic*************
	
	//if user chooses wikicontext, checks all artifacts linked to WikiPage
	if (readerObject.getWikiContext())	
	{
		results = logic.checkWikiPages(wiki, null, allAssoc);
		System.out.println("A");
	}
	//check whether Attachments or TrackerItems have to check
	else if (readerObject.getProjectId()!=null)
	{
		System.out.println("3");
		//find all Attachments from project
		//List<ArtifactDto>  allAttachments = raReader.readAllAttachmentsByProject(projects);	 //TODO: in process
		
		//check if every Attachment has minimum one association
	   // results = logic.checkAttachments(null, allAttachments, allAssoc);		//TODO: in process
	}
	else	//checks TrackerItem
	{
		//check if every TrackerItem has minimum one association
		results = logic.checkTrackerItems(allTrackerItems, null, allAssoc);
	}
	//**********************Print*************
	
	//print "chart" 
	if (readerObject.getDisplay() == "chart")
	{
		if (readerObject.getProjectId()!=null)
			System.out.println (printer.printPluginPattern(results, true));	//Attachment specific output
		else
			System.out.println (printer.printPluginPattern(results, false)); //Tracker specific output
	}
	//print "table"
	else
	{
		List<VelocityTable> table = null;
		
		//print wikicontext 
		if (readerObject.getWikiContext())
		{
			table = printer.printWikPagesAscii(results, false);	
		}
		
		//check whether Attachments or TrackerItems have to print
		else if (readerObject.getProjectId()!=null)
		{
			table = printer.printAttachmentsAscii(results);
		}
		else
		{
			   Iterator<List<Object>> f= results.iterator();
			   	f.hasNext(); 	//runs just once, but can goes twice: [0]=posTrackerItems(Class TicketResults) [1]=negTrackerItems (Class TrackerItemsDto)
			   		List<Object> a = f.next();
			
			table = printer.printTrackerItemsAscii(results, readerObject.getNoLinked());
		}	
			//Print simulation
			Iterator<VelocityTable> itrResults= table.iterator();
		   	while(itrResults.hasNext()) {	//runs just once, later //VelocityTable-Object 
		   		VelocityTable tempResult =  itrResults.next();
		   			System.out.println (tempResult.getID() + " "+ tempResult.getName()+ " " + tempResult.getLink()); 
		   			
		   			List<AttachmentTable> attResults = tempResult.getAttachment();
		   			if (!readerObject.getNoLinked()) //needs it, otherwise nullpointerException of calling not existing Artifacts
			   			{
			   			Iterator<AttachmentTable> itrAttResults= attResults.iterator();
					   	while(itrAttResults.hasNext())
					   	{
					   		AttachmentTable tempAttResult =  itrAttResults.next();
					   		System.out.println("--" + tempAttResult.getName() + " " + tempAttResult.getUrl() );
					   	}
		   			}
		}
		
	}
				
			}
			   	
	
	}
	

/* //TODO: in process
int[] projects=null;
//check if user put in projectName or ProjectId
if (readerObject.getProjectId()!=null || readerObject.getProjectName() !=null )
{
	if (readerObject.getProjectId().length >0)
	{
		if (readerObject.getProjectId()[0] ==-1)
		{
			
			projects = raReader.readAllProjects();	//all Projects	
		}
		else
		{
			projects = readerObject.getProjectId(); //spezifische projects
		}
	}
	else 	
		projects = raReader.findProject(projects, readerObject.getProjectName()); //find a specific project in codebeamer
}
*/



	
	
