/**
 * 
 */
package com.intland.codebeamer.wiki.plugins.support;

import java.util.ArrayList;
import java.util.List;

import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.TrackerItemDto;
import com.intland.codebeamer.persistence.dto.base.NamedDto;

/**Class combines one TrackerItemDto (Ticket) Object to multiple various Artifacts
 * Required for outpout in VeloCity style
 * @author Alexander Börsch
 *
 */
public class TicketResults {

	private TrackerItemDto ticket;
	private List <ArtifactDto> artifacts;
	
	/**Constructor needs TrackerItemDto
	 * @param ticket Input TrackerItemDto object
	 * It's possible to add multiple Artifacts to various times
	 */
	public TicketResults (TrackerItemDto ticket)
	{
		setTicket(ticket);
		
		if (this.artifacts ==null)
			this.artifacts = new ArrayList <ArtifactDto>();		
	}
	
	/**Returns TrackerItemDto object
	 * @return TrackerItemDto TrackerItemDto Object
	 */
	public TrackerItemDto getTicket() {
		return ticket;
	}
	
	/**Checks if TrackerItemDto-Object already exists in that case no value-change is possible any more
	 * need it because multiple accesses to same object for artifact-adding
	 * @param ticket Input TrackerItemDto Object
	 */
	public void setTicket(TrackerItemDto ticket) {
		if (this.ticket == null)
			this.ticket = ticket;
	}
	/**Returns List of Artifact objects
	 * @return List<ArtifactDto> List of Artifact objects
	 */
	public List<ArtifactDto> getArtifacts() {
		return artifacts;
	}
	
	/**Sets Artifact to internal class List <ArtifactDto>
	 * @param artifact Input ArtifactDto object
	 */
	public void setArtifacts(ArtifactDto artifact) {
			this.artifacts.add((ArtifactDto) artifact);
	}
}
