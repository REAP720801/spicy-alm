package remoteApi;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.wiki.plugins.core.Logic;
import com.intland.codebeamer.wiki.plugins.core.Printer;
import com.intland.codebeamer.wiki.plugins.core.Reader;
import com.intland.codebeamer.wiki.plugins.support.AttachmentTable;
import com.intland.codebeamer.wiki.plugins.support.GlobalVariable;
import com.intland.codebeamer.wiki.plugins.support.VelocityTable;



/**SpicyAlm StandAloneApplication
 * Provides code for a stand alone application which is working with remote api
 * @author Alexander Börsch
 *
 */
public class StandAloneApplication {

	public static void main(String[] args) {

		//object instantiation 
		RAReader raReader =new RAReader();
		GlobalVariable globalVariable = new GlobalVariable();
		Reader readerObject = new Reader(globalVariable);
		Printer printer = new Printer(globalVariable);
		Logic logic = new Logic ();
		List<List<Object>>  results = null;
		
		//configuration parameters
		String serviceUrl = "http://localhost:8080/cb/remote-api";
		String login ="bond";
		String password = "007";	
		
		//configuration user simulated parameters
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", serviceUrl);
		map.put("user", login);
		map.put("password", password);
		//map.put("projectId", "2");	 //TODO: in process
		map.put("display", "chart");	//input: chart or table
		map.put("trackerId", "5");	    //Pattern: specific values as int,int,int,... or for a full search use: "" -->alle
		map.put("notLinked", "true");
		
		//read user parameters
		readerObject.readParameterSAloneApp(map);

	//codebeamer login with configuration parameters
	raReader.login(readerObject.getUrl(), readerObject.getUser(), readerObject.getPassword());
	
	
	//**********************reading*************

	//read TrackerItems
	List<TrackerItemDto> allTrackerItems = raReader.readAllTrackerItems(readerObject.getTrackerID()); 
	//System.out.println(allTrackerItems.size());

	//find all Associations in this specific project
	List<Object> allAssoc = raReader.readAllAssociations();	
	
	//**********************logic*************
	//check whether Attachments or TrackerItems have to check
	if (readerObject.getProjectId()!=null)
	{
		//find all Attachments from project
		//List<ArtifactDto>  allAttachments = raReader.readAllAttachmentsByProject(projects);	 //TODO: in process
		
		//check if every Attachment has minimum one association
	   // results = logic.checkAttachments(null, allAttachments, allAssoc);		//TODO: in process
	}
	else	//check TrackerItem
	{
		//check if every TrackerItem has minimum one association
		results = logic.checkTrackerItems(allTrackerItems, null, allAssoc);
	}
				
	//**********************Print*************
	if (readerObject.getDisplay() == "chart")
	{
		if (readerObject.getProjectId()!=null)
			System.out.println (printer.printPluginPattern(results, true));	//Attachment specific output
		else
			System.out.println (printer.printPluginPattern(results, false)); //Tracker specific output
	}
	else
	{
		//check whether Attachments or TrackerItems have to print
		if (readerObject.getProjectId()!=null){
		//System.out.println(printer.printAttachmentsAscii(results));
			List<VelocityTable> table = printer.printAttachmentsAscii(results);
		}
		else
		{
			   Iterator<List<Object>> f= results.iterator();
			   	f.hasNext(); 	//runs just once, but can goes twice: [0]=posTrackerItems(Class TicketResults) [1]=negTrackerItems (Class TrackerItemsDto)
			   		List<Object> a = f.next();
			
			List<VelocityTable> table = printer.printTrackerItemsAscii(results, readerObject.getNoLinked());
			
			//Print simulation
			Iterator<VelocityTable> itrResults= table.iterator();
		   	while(itrResults.hasNext()) {	//runs just once, later //VelocityTable-Object 
		   		VelocityTable tempResult =  itrResults.next();
		   			System.out.println (tempResult.getTicketID() + " " + tempResult.getTicketLink()); 
		   			
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



	
	
