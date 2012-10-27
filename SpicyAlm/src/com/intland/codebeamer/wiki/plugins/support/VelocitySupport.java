package com.intland.codebeamer.wiki.plugins.support;

/**Class for Velocity output
 * Provides user and standard limitation values for caution part in velocity template
 * @author Alexander Börsch
 *
 */
public class VelocitySupport {
	
    private int userLimitation;
	private int standardLimitation;
	
	public VelocitySupport (int userLimitation, int standardLimitation)
	{
		setUserLimitation(userLimitation);
		setStandardLimitation(standardLimitation);
	}
	
	public VelocitySupport ()
	{
	}

	 /**Returns User limitation value for velocity output 
     * @return int 
     */
    public int getUserLimitation() {
		return userLimitation;
	}


	public void setUserLimitation(int userLimitation) {
		this.userLimitation = userLimitation;
	}

    /**Returns standard limitation value for velocity output 
     * @return int 
     */
	public int getStandardLimitation() {
		return standardLimitation;
	}


	public void setStandardLimitation(int standardLimitation) {
		this.standardLimitation = standardLimitation;
	}
}
