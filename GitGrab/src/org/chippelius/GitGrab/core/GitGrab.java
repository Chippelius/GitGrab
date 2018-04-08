package org.chippelius.GitGrab.core;

import org.chippelius.GitGrab.ui.GitGrabUI;

/**
 * Contains the information needed for GitGrab to perform one action:<br>
 * Download one package from one version of one repository and unpack it into one directory.
 * 
 * @author Leo Köberlein
 *
 */
public class GitGrab {

	// ******************************
	// Constants
	// ******************************




	// ******************************
	// Fields
	// ******************************
	private String owner;
	private String repo;
	private boolean useLatest;
	private String version;
	private String asset;
	private String directory;
	private boolean allowOverride;
	
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
	public GitGrab(GitGrabUI ui) {
		this.ui = ui;
	}
	
	
	
	
	// ******************************
	// Private methods
	// ******************************
	
	
	
	
	// ******************************
	// Public methods
	// ******************************
	public void install(String owner, String repo, String asset, String directory) {
		
	}

	public void install(String owner, String repo, String asset, String directory, boolean allowOverride) {
		
	}
	
	public void install(String owner, String repo, String version, String asset, String directory) {
		
	}

	public void install(String owner, String repo, String version, String asset, String directory, boolean allowOverride) {
		
	}
	
	public static void main(String[] args) {
		//TODO
	}

}
