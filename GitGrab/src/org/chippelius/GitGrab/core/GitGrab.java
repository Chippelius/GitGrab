package org.chippelius.GitGrab.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.json.*;

import org.chippelius.GitGrab.ui.GitGrabUI;

import net.lingala.zip4j.core.ZipFile;

/**
 * 
 * 
 * @author Leo Köberlein
 */
public class GitGrab {

	// ******************************
	// Constants
	// ******************************
	private static final String latestVersion = "latest";




	// ******************************
	// Fields
	// ******************************
	protected GitGrabUI ui;
	//	protected volatile boolean abort;
	protected String repoOwner, repoName, version, asset, destination = null;
	protected Boolean override = null;



	// ******************************
	// Constructors
	// ******************************
	/**
	 * Creates a GitGrab object that will use the given UI for displaying progress 
	 * and requesting missing information.<br>
	 * The UI is necessary and therefore must not be null!
	 * 
	 * @param ui the ui used for IO interactions with the user
	 * 
	 * @see GitGrabUI
	 */
	public GitGrab(GitGrabUI ui) {
		if(ui == null) {
			throw new IllegalArgumentException("The GitGrabUI must not be null!");
		}
		this.ui = ui;
	}




	// ******************************
	// Protected methods
	// ******************************
	protected static URLConnection getURLConnection(String url) throws IOException {
		URL u = new URL(url);
		URLConnection uc = u.openConnection();
		uc.setRequestProperty("User-Agent", "Chippelius/GitGrab");
		uc.setRequestProperty("Accept", "application/vnd.github.v3+json");
		return uc;
	}

	protected static void download(String url, File assetFile) throws IOException {
		URLConnection downloadConnection = getURLConnection(url);
		downloadConnection.setRequestProperty("Accept", "application/octet-stream");
		InputStream in = downloadConnection.getInputStream();
		FileOutputStream fos = new FileOutputStream(assetFile, false);
		byte[] buffer = new byte[1024];
		for(int count=0;(count=in.read(buffer)) > -1; fos.write(buffer, 0, count));
		in.close();
		fos.close();
	}

	protected static void delete(File file) {
		if(file.exists()) {
			if(file.isDirectory()) {
				for(File f : file.listFiles()) {
					delete(f);
				}
			}
			file.delete();
		}
	}

	protected static void unzip(File assetFile, String destinationDirectory) throws Exception {
		//TODO: redo without dependency
		ZipFile zipfile = new ZipFile(assetFile);
		zipfile.extractAll(destinationDirectory);
	}






	// ******************************
	// Getter and Setter methods
	// ******************************
	public synchronized String getRepoOwner() {
		return repoOwner;
	}
	public synchronized GitGrab setRepoOwner(String repoOwner) {
		this.repoOwner = repoOwner;
		return this;
	}

	public synchronized String getRepoName() {
		return repoName;
	}
	public synchronized GitGrab setRepoName(String repoName) {
		this.repoName = repoName;
		return this;
	}

	public synchronized String getVersion() {
		return version;
	}
	public synchronized GitGrab setVersion(String version) {
		this.version = version;
		return this;
	}

	public synchronized String getAsset() {
		return asset;
	}
	public synchronized GitGrab setAsset(String asset) {
		this.asset = asset;
		return this;
	}

	public synchronized String getDestination() {
		return destination;
	}
	public synchronized GitGrab setDestination(String destination) {
		if(!(destination.charAt(destination.length()-1) == '\\' || 
				destination.charAt(destination.length()) == '/'))
			destination = destination + '/';
		this.destination = destination;
		return this;
	}

	public synchronized boolean isOverride() {
		return override;
	}
	public synchronized GitGrab setOverride(boolean override) {
		this.override = override;
		return this;
	}




	// ******************************
	// Public methods
	// ******************************
	public synchronized void install() throws Exception {
		if(ui.init() == null)
			return;

		/* 
		 * Gather necessary information
		 */
		if(repoOwner == null) {
			repoOwner = ui.requestRepoOwner();
			if(repoOwner == null)
				return;
		}
		if(repoName == null) {
			repoName = ui.requestRepoName();
			if(repoName == null)
				return;
		}
		if(version == null) {
			version = ui.requestVersion();
			if(version == null)
				return;
		}
		if(asset == null) {
			asset = ui.requestAsset();
			if(asset == null)
				return;
		}
		if(destination == null) {
			destination = ui.requestDestination();
			if(destination == null)
				return;
		}

		/*
		 * Gather assets from GitHub
		 */
		JsonArray assets;
		try {
			JsonReader reader = Json.createReader(getURLConnection(
					"https://api.github.com/repos/"+repoOwner+"/"+repoName+"/releases/"+version)
					.getInputStream());
			JsonObject root = reader.readObject();
			assets = root.getJsonArray("assets");
		} catch(Exception e) {
			ui.showException("An error occurred while gathering necessary data from GitHub", e);
			throw e;
		}

		/* 
		 * Find asset in assets
		 */
		JsonObject assetObject = null;
		try {
			for(int i=0; i<assets.size()+1; ++i) {
				if(assets.getJsonObject(i).getString("name").equals(asset)) {
					assetObject = assets.getJsonObject(i);
					break;
				}
			}
		} catch (Exception e) {
			ui.showException("Asset with name \""+asset+"\" could not be found.", e);
			throw e;
		}
		
		/*
		 * Wait for user to confirm installation
		 */
		if(ui.confirmInstallation() == null)
			return;

		/* 
		 * Prepare destination directory
		 */
		try {
			File destinationFolder = new File(destination);
			if(destinationFolder.exists()) {
				if(override == null) {
					override = ui.confirmOverride();
					if(override == null)
						return;
				}
				if(override) {
					delete(destinationFolder);
					destinationFolder.mkdir();
				} else {
					throw new IOException("Deleting file not allowed by user");
				}
			} else {
				destinationFolder.mkdirs();
			}
		} catch (Exception e) {
			ui.showException("An error occurred while preparing the destination path", e);
			throw e;
		}

		/* 
		 * Download asset
		 */
		File assetFile = new File(destination+assetObject.getString("name"));
		System.out.println("Downloading "+assetObject.getString("name"));
		download(assetObject.getString("url"), assetFile);
		System.out.println("Download complete.");

		/*
		 * Extract asset
		 */
		System.out.println("Extracting "+assetObject.getString("name"));
		unzip(assetFile, destination);
		System.out.println("Extracting complete.");

		/*
		 * Clean up
		 */
		System.out.println("Cleaning up.");
		assetFile.delete();
		System.out.println("Cleanup done.");

	}

	//	public void abort() {
	//		abort = true;
	//	}




	// ******************************
	// Public static methods
	// ******************************

	public static void main(String[] args) {
		try {
			new GitGrab(new GitGrabUI() {
			})
			.setRepoOwner("atom")
			.setRepoName("atom")
			.setVersion(latestVersion)
			.setAsset("atom-windows.zip")
			.setDestination("C:\\Users\\leo\\Downloads\\atom\\")
			.setOverride(false)
			.install();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
