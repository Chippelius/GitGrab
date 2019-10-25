package org.chippelius.GitGrab.ui;

/**
 * An interface defining the methods required for the core library to input and output data.
 * 
 * @author Leo Köberlein
 *
 */
public interface GitGrabUI {

	public Object init();

	public String requestRepoOwner();

	public String requestRepoName();

	public String requestVersion();

	public String requestAsset();

	public String requestDestination();

	public Object confirmInstallation();
	
	public Boolean confirmOverride();

	public void showException(String string, Exception e);

}
