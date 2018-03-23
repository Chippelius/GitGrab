package org.chippelius.GitGrab.core;

/**
 * Contains the information needed for GitGrab to perform an installation/update.
 * 
 * @author Leo Köberlein
 *
 */
public class GitGrabConfig implements Cloneable {
	
	// ******************************
	// Constants
	// ******************************
	public static final int INSTALLATION_MODE = 0;
	public static final int UPDATE_MODE = 1;
	
	
	// ******************************
	// Fields
	// ******************************
	private int actionMode;
	
	
	// ******************************
	// Constructors
	// ******************************
	/**
	 * Creates a GitGrab config defining a task to be performed by a GitGrab worker.
	 * 
	 * @see GitGrab
	 */
	public GitGrabConfig() {
		
	}
	
	
	// ******************************
	// Private methods
	// ******************************
	
	
	// ******************************
	// Public methods
	// ******************************
	/**
	 * Sets the type of action to be performed. 
	 * 
	 * @param mode the type of action to be performed
	 */
	public synchronized void setActionMode(int mode) {
		actionMode = mode;
	}
	/**
	 * Returns the type of action to be performed.
	 * 
	 * @return the type of action to be performed
	 */
	public synchronized int getActionMode() {
		return actionMode;
	}

}
