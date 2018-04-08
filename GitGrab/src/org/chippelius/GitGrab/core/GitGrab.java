package org.chippelius.GitGrab.core;

/**
 * Contains the information needed for GitGrab to perform one action:<br>
 * Download one package from one version of one repository and unpack it into one directory.
 * 
 * @author Leo Köberlein
 *
 */
public class GitGrab implements Cloneable {

	// ******************************
	// Constants
	// ******************************
	/**
	 * Constant representing the action mode 'installation'.<br>
	 * In installation mode, the whole destination directory is cleared (or created, if it doesen't already exist)
	 * before extracting and copying the requested files to ensure a clean installation.
	 */
	public static final String INSTALLATION_MODE = "install";
	/**
	 * Constant representing the action mode 'update'.<br>
	 * In update mode, only the files from the downloaded package are replaced/created. 
	 * Other files remain untouched.
	 */
	public static final String UPDATE_MODE = "update";




	// ******************************
	// Fields
	// ******************************
	private boolean actionModeDefined;
	private String actionMode;
	private boolean ownerDefined;
	private String owner;
	private boolean repoDefined;
	private String repo;
	private boolean useLatestDefined;
	private boolean useLatest;
	/*private boolean allowPrereleaseDefined;
	private boolean allowPrerelease;*/
	private boolean versionDefined;
	private String version;
	private boolean assetDefined;
	private String asset;
	private boolean directoryDefined;
	private String directory;
	private boolean allowOverrideDefined;
	private boolean allowOverride;




	// ******************************
	// Constructors
	// ******************************
	/**
	 * Creates a GitGrab object with empty variables.
	 * GitGrab objects define a task to be performed by the GitGrab worker.
	 * 
	 * @see GitGrabWorker
	 */
	public GitGrab() {
		this(null, null, null, true, null, null, null, false);
		this.useLatestDefined = false;
		this.allowOverrideDefined = false;
	}

	/**
	 * Creates a GitGrab object with the given values for variables.
	 * GitGrab objects define a task to be performed by the GitGrab worker.
	 * 
	 * @param actionMode the type of action to be performed
	 * @param owner the owner of the referred repository
	 * @param repo the name of the referred repository
	 * @param useLatest whether the latest release should be used
	 * @param version the version, that will be used if useLatest == false
	 * @param asset a name or regex to identify the requested package
	 * @param directory the directory, at which the package will be installed
	 * @param allowOverride whether files can be overridden without asking the user for permission
	 * 
	 * @see GitGrabWorker
	 */
	public GitGrab(String actionMode, String owner, String repo, boolean useLatest, String version, String asset, String directory, boolean allowOverride) {
		super();
		setActionMode(actionMode);
		setOwner(owner);
		setRepo(repo);
		setUseLatest(useLatest);
		/*setAllowPrerelease(allowPrerelease);*/ 
		setVersion(version);
		setAsset(asset);
		setDirectory(directory);
		setAllowOverride(allowOverride);
	}




	// ******************************
	// Private methods
	// ******************************
	protected static boolean isValidActionMode(String mode) {
		switch (mode.toLowerCase()) {
		case INSTALLATION_MODE:
		case UPDATE_MODE:
			return true;
		default:
			return false;
		}
	}

	protected static boolean isValidStringArgument(String argument) {
		return (argument != null) && (!argument.equals(""));
	}




	// ******************************
	// Public methods
	// ******************************
	/**
	 * Returns whether the action mode is marked as defined.
	 * 
	 * @return whether the action mode is marked as defined
	 */
	public synchronized boolean isActionModeDefined() {
		return actionModeDefined;
	}
	/**
	 * Marks the action mode as defined/undefined.<br>
	 * Can only be marked as defined if {@link #getActionMode()} is a valid action mode.
	 * 
	 * @param defined whether the action mode should be marked as defined
	 * @return whether the action mode is marked as defined now
	 */
	public synchronized boolean setActionModeDefined(boolean defined) {
		if(!defined) {
			actionModeDefined = false;
		} else {
			if(isValidActionMode(actionMode)) {
				actionModeDefined = true;
			} else {
				actionModeDefined = false;
			}
		}
		return actionModeDefined;
	}
	/**
	 * Returns the type of action to be performed.
	 * 
	 * @return the type of action to be performed
	 */
	public synchronized String getActionMode() {
		return actionMode;
	}
	/**
	 * Sets the type of action to be performed.<br>
	 * The argument must be one of the following: <br>
	 * {@link #INSTALLATION_MODE} or {@link #UPDATE_MODE}
	 * 
	 * @param mode the type of action to be performed
	 * @return whether the argument was valid
	 */
	public synchronized boolean setActionMode(String mode) {
		actionMode = mode;
		return setActionModeDefined(true);
	}


	/**
	 * Returns whether the repository's owner is defined.
	 * 
	 * @return whether the repository's owner is defined
	 */
	public synchronized boolean isOwnerDefined() {
		return ownerDefined;
	}
	/**
	 * Marks the repository's owner as defined/undefined.<br>
	 * Can only be marked as defined if {@link #getOwner()} is not null or empty.
	 * 
	 * @param defined whether the repository's owner should be marked as defined
	 * @return whether the repository's owner is marked as defined now
	 */
	public synchronized boolean setOwnerDefined(boolean defined) {
		if(!defined) {
			ownerDefined = false;
		} else {
			if(isValidStringArgument(owner)) {
				ownerDefined = true;
			} else {
				ownerDefined = false;
			}
		}
		return ownerDefined;
	}
	/**
	 * Returns the owner of the referred repository.
	 * 
	 * @return the owner of the referred repository
	 */
	public synchronized String getOwner() {
		return owner;
	}
	/**
	 * Sets the owner of the referred repository.<br>
	 * If the argument is not null or empty, the repository owner will be set accordingly and marked as defined, 
	 * otherwise and marked as undefined.
	 * 
	 * @param owner the owner to set
	 * @return whether the argument was valid
	 */
	public synchronized boolean setOwner(String owner) {
		this.owner = owner;
		return setOwnerDefined(true);
	}


	/**
	 * Returns whether the repository's name is defined.
	 * 
	 * @return whether the repository's name is defined
	 */
	public synchronized boolean isRepoDefined() {
		return repoDefined;
	}
	/**
	 * Marks the repository's name as defined/undefined.<br>
	 * Can only be marked as defined if {@link #getRepo()} is not null or empty.
	 * 
	 * @param defined whether the repository's name should be marked as defined
	 */
	public synchronized void setRepoDefined(boolean defined) {
		if(!defined) {
			repoDefined = false;
		} else {
			if(repo != null && !repo.equals("")) {
				repoDefined = true;
			} else {
				repoDefined = false;
			}
		}
	}
	/**
	 * Returns the name of the referred repository.
	 * 
	 * @return the name of the referred repository
	 */
	public synchronized String getRepo() {
		return repo;
	}
	/**
	 * Sets the name of the referred repository.<br>
	 * If the argument is not null or empty, the repository name will be set accordingly and marked as defined, 
	 * otherwise set to null and marked as undefined.
	 * 
	 * @param name the repository name to set
	 */
	public synchronized void setRepo(String name) {
		if(name != null && !name.equals("")) {
			this.repo = name;
			repoDefined = true;
		} else {
			this.repo = null;
			repoDefined = false;
		}
	}


	/**
	 * Returns whether useLatest is defined.
	 * 
	 * @return whether useLatest is defined
	 */
	public synchronized boolean isUseLatestDefined() {
		return useLatestDefined;
	}
	/**
	 * Marks useLatest as defined/undefined.
	 * 
	 * @param defined whether useLatest should be marked as defined
	 */
	public synchronized void setUseLatestDefined(boolean defined) {
		useLatestDefined = defined;
	}
	/**
	 * Returns whether the latest release or a specific one should be used.
	 * 
	 * @return whether the latest release should be used
	 */
	public synchronized boolean getUseLatest() {
		return useLatest;
	}
	/**
	 * Sets whether the latest release or a specific one should be used.<br>
	 * If false, {@link #getVersion()} will be used, otherwise the latest version.<br>
	 * Marks useLatest as defined.
	 * 
	 * @param useLatest whether the latest release should be used
	 */
	public synchronized void setUseLatest(boolean useLatest) {
		this.useLatest = useLatest;
		useLatestDefined = true;
	}

	/*
	 *//**
	 * Returns whether allowPrerelease is defined.
	 * 
	 * @return whether allowPrerelease is defined
	 *//*
	public synchronized boolean isAllowPrereleaseDefined() {
		return allowPrereleaseDefined;
	}
	  *//**
	  * Marks allowPrerelease as defined/undefined.
	  * 
	  * @param defined whether allowPrerelease should be marked as defined
	  *//*
	public synchronized void setAllowPrereleaseDefined(boolean defined) {
		allowPrereleaseDefined = defined;
	}
	   *//**
	   * Returns whether draft releases and prereleases should be considered. (Only relevant if {@link #getUseLatest()} == true.)
	   * 
	   * @return whether draft releases and prereleases should be considered
	   *//*
	public boolean getAllowPrerelease() {
		return allowPrerelease;
	}
	    *//**
	    * Sets whether draft releases and prereleases should be considered. (Only relevant if {@link #getUseLatest()} == true.)<br>
	    * Marks allowPrerelease as defined.
	    * 
	    * @param allowPrerelease whether draft releases and prereleases should be considered
	    *//*
	public void setAllowPrerelease(boolean allowPrerelease) {
		this.allowPrerelease = allowPrerelease;
		allowPrereleaseDefined = true;
	}*/


	/**
	 * Returns whether the version, that will be used if {@link #getUseLatest()} == false, is defined.
	 * 
	 * @return whether the version is defined
	 */
	public synchronized boolean isVersionDefined() {
		return versionDefined;
	}
	/**
	 * Marks the version as defined/undefined.<br>
	 * Can only be marked as defined if {@link #getVersion()} is not null or empty.
	 * 
	 * @param defined whether the version should be marked as defined
	 */
	public synchronized void setVersionDefined(boolean defined) {
		if(!defined) {
			versionDefined = false;
		} else {
			if(version != null && !version.equals("")) {
				versionDefined = true;
			} else {
				versionDefined = false;
			}
		}
	}
	/**
	 * Returns the version, that will be used if {@link #getUseLatest()} == false.
	 * 
	 * @return the version, that will be used if {@link #getUseLatest()} == false
	 */
	public synchronized String getVersion() {
		return version;
	}
	/**
	 * Sets the version that will be used if {@link #getUseLatest()} == false.
	 * If the argument is not null or empty, the version will be set accordingly and marked as defined, 
	 * otherwise set to null and marked as undefined.
	 * 
	 * @param version the version that will be used if {@link #getUseLatest()} == false
	 */
	public synchronized void setVersion(String version) {
		if(version != null && !version.equals("")) {
			this.version = version;
			versionDefined = true;
		} else {
			this.version = null;
			versionDefined = false;
		}
	}


	/**
	 * Returns whether the name of the asset, that represents the requested package, is defined. 
	 * 
	 * @return whether the name of the asset is defined
	 */
	public synchronized boolean isAssetDefined() {
		return assetDefined;
	}
	/**
	 * Marks the asset name as defined/undefined.<br>
	 * Can only be marked as defined if {@link #getAsset()} is not null or empty.
	 * 
	 * @param assetDefined whether the asset should be marked as defined
	 */
	public synchronized void setAssetDefined(boolean defined) {
		if(!defined) {
			assetDefined = false;
		} else {
			if(asset != null && !asset.equals("")) {
				assetDefined = true;
			} else {
				assetDefined = false;
			}
		}
	}
	/**
	 * Returns a name or regex to identify the requested package among the assets of the desired release.
	 * 
	 * @return a name or regex to identify the requested package
	 */
	public synchronized String getAsset() {
		return asset;
	}
	/**
	 * Sets a name or regex to identify the requested package among the assets of the desired release.
	 * If the argument is not null or empty, the asset name or regex will be set accordingly and marked as defined, 
	 * otherwise set to null and marked as undefined.
	 * 
	 * @param asset a name or regex to identify the requested package
	 */
	public synchronized void setAsset(String asset) {
		if(asset != null && !asset.equals("")) {
			this.asset = asset;
			assetDefined = true;
		} else {
			this.asset = null;
			assetDefined = false;
		}
	}


	/**
	 * Returns whether the destination directory is defined.
	 * 
	 * @return whether the destination directory is defined
	 */
	public synchronized boolean isDirectoryDefined() {
		return directoryDefined;
	}
	/**
	 * Marks the destination directory as defined/undefined.<br>
	 * Can only be marked as defined if {@link #getDirectory()} is not null or empty.
	 * 
	 * @param directoryDefined whether the directory should be marked as defined
	 */
	public synchronized void setDirectoryDefined(boolean defined) {
		if(!defined) {
			directoryDefined = false;
		} else {
			if(directory != null && !directory.equals("")) {
				directoryDefined = true;
			} else {
				directoryDefined = false;
			}
		}
	}
	/**
	 * Returns the directory, at which the package will be installed.
	 * 
	 * @return the directory, at which the package will be installed
	 */
	public synchronized String getDirectory() {
		return directory;
	}
	/**
	 * Sets the destination directory, at which the package will be installed.
	 * If the argument is not null or empty, the directory will be set accordingly and marked as defined, 
	 * otherwise set to null and marked as undefined.
	 * 
	 * @param directory the directory, at which the package will be installed
	 */
	public synchronized void setDirectory(String directory) {
		if(directory != null && !directory.equals("")) {
			this.directory = directory;
			directoryDefined = true;
		} else {
			this.directory = null;
			directoryDefined = false;
		}
	}


	/**
	 * Returns whether allowOverride is defined.
	 * 
	 * @return whether allowOverride is defined
	 */
	public boolean isAllowOverrideDefined() {
		return allowOverrideDefined;
	}
	/**
	 * Marks allowOverride as defined/undefined.
	 * 
	 * @param allowOverrideDefined whether allowOverride should be marked as defined
	 */
	public void setAllowOverrideDefined(boolean defined) {
		allowOverrideDefined = defined;
	}
	/**
	 * Returns whether files, that already exist in the destination folder, can be deleted/altered without asking the user for permission.<br>
	 * This is only relevant in installation mode, as the sole purpose of update mode IS to alter existing files (and create new ones).
	 * 
	 * @return whether files can be overridden without asking the user for permission
	 */
	public boolean isAllowOverride() {
		return allowOverride;
	}
	/**
	 * Returns whether files, that already exist in the destination folder, can be deleted/altered without asking the user for permission.<br>
	 * This is only relevant in installation mode, as the sole purpose of update mode IS to alter existing files (and create new ones).
	 * 
	 * @param allowOverride whether files should be overridden without asking the user for permission
	 */
	public void setAllowOverride(boolean allowOverride) {
		this.allowOverride = allowOverride;
	}


	@Override
	public synchronized GitGrab clone() {
		try {
			return (GitGrab) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}

}
