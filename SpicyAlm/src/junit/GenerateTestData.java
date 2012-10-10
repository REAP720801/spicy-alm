/**
 * 
 */
package junit;

import java.util.ArrayList;
import java.util.List;

import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.AssociationDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;

/**Generate test data for junits
 * @author Alexander Börsch
 *
 */
public class GenerateTestData {

		public List<TrackerItemDto> createTrackerItem ()
		{
			//create and add 10 TrackItems
			List<TrackerItemDto>allTrackerItems = new ArrayList<TrackerItemDto>();
			for (int i=0;i<10;i++)
			{
			TrackerItemDto testTrackerItem= new TrackerItemDto();
				int tempValue = 1001+i;
				testTrackerItem.setId(tempValue);
				testTrackerItem.setName("TrackerItem"+ i);
			allTrackerItems.add(testTrackerItem);
			//System.out.println(testTrackerItem);
			}
			return allTrackerItems;
		}
		
		public List<ArtifactDto> createAttachment ()
		{
			//create and add 5 Attachments
			List<ArtifactDto> allAttachments = new ArrayList<ArtifactDto>();
			for (int i=0;i<6;i++)
			{
			ArtifactDto testArtifact= new ArtifactDto();
				int tempValue = 1051+i;
				testArtifact.setId(tempValue);
				testArtifact.setName("Attachment"+ i);
			allAttachments.add(testArtifact);
			//System.out.println(testArtifact);
			}
			return allAttachments;
		}
		
		public List<Object> createAssocation ()
		{
			//create and add 6 Associations (linked TrackerItems-id (1001-1007) to Attachments (1051-1057)
			 List<Object> allAssoc = new ArrayList<Object>();
			 for (int i=0;i<6;i++)
			 {
			 Object[] tempAssoc = new Object [3]; //[0]=assoc-object [1]=trackerItem-object [2]=attachment-object)
				tempAssoc[0] = new AssociationDto ();
				
				//generate equal TrackerItem (as in createTrackerItem())
				TrackerItemDto  tempTrackerItem = new TrackerItemDto();
					int tempValue = 1001+i;
					tempTrackerItem.setId(tempValue);
					tempTrackerItem.setName("TrackerItem"+ i);
				tempAssoc[1] = tempTrackerItem; //trackeritem
				
				//generate equal Artifact (as in createAttachment())
				ArtifactDto testArtifact= new ArtifactDto();
				    tempValue = 1051+i;
					testArtifact.setId(tempValue);
					testArtifact.setName("Attachment"+ i);			
				tempAssoc[2] = testArtifact; 	//artifact
				allAssoc.add(tempAssoc);
				//System.out.println(tempAssoc);
			 }
			 return allAssoc;
		}
		
	}


