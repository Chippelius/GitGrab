package org.chippelius.GitGrab.core;

import org.chippelius.GitGrab.ui.GitGrabUI;

/**
 * Main worker class performing {@link GitGrab}s.
 * 
 * @author Leo K�berlein
 * @see GitGrab
 *
 */
public class GitGrabWorker {
	
	// ******************************
	// Constants
	// ******************************
	
	
	
	
	// ******************************
	// Fields
	// ******************************
	private GitGrab grab;
	private GitGrabUI ui;
	
	
	
	
	// ******************************
	// Constructors
	// ******************************
	/**
	 * Creates a GitGrabWorker object for performing the task defined in grab using the given UI.<br>
	 * Note that the GitGrab object not used directly but cloned, so it can't be accessed (and by that also modified) 
	 * from outside after the GitGrabWorker is set up.
	 * 
	 * @param config defines the task and its parameters
	 * @param ui the ui used for IO interactions
	 * 
	 * @see GitGrab
	 * @see GitGrabUI
	 */
	public GitGrabWorker(GitGrab grab, GitGrabUI ui) {
		this.grab = grab.clone();
		this.ui = ui;
	}
	
	
	
	
	// ******************************
	// Private methods
	// ******************************
	
	
	
	
	// ******************************
	// Public methods
	// ******************************
	public void start() {
		ui.init();
		if(grab.isActionModeDefined()) {
			
		}
		//TODO
	}
	
	public static void main(String[] args) {
		//TODO
	}

}