package com.intland.codebeamer.wiki.plugins.support;

import java.util.ArrayList;
import java.util.List;

import com.intland.codebeamer.persistence.dto.ArtifactDto;
import com.intland.codebeamer.persistence.dto.WikiPageDto;

/**Class combines one WikiPageDto (wiki) Object to multiple various Artifacts
 * Required for outpout in VeloCity style
 * Similar to "TicketResults" class
 * @author Alexander Börsch
 *
 */
public class WikiResults {

	private WikiPageDto wiki;
	private List <ArtifactDto> artifacts;
	
	/**Constructor needs WikiPageDto
	 * @param ticket Input WikiPageDto object
	 * It's possible to add multiple Artifacts to various times
	 */
	public WikiResults (WikiPageDto wiki)
	{
		setObject(wiki);
		
		if (this.artifacts ==null)
			this.artifacts = new ArrayList <ArtifactDto>();		
	}
	
	/**Returns WikiPageDto object
	 * @return WikiPageDto WikiPageDto Object
	 */
	public WikiPageDto getObject() {
		return wiki;
	}
	
	/**Checks if WikiPageDto-Object already exists in that case no value-change is possible any more
	 * need it because multiple accesses to same object for artifact-adding
	 * @param object Input WikiPageDto Object
	 */
	public void setObject(WikiPageDto object) {
		if (this.wiki == null)
			this.wiki = object;
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
