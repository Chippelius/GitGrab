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
	
	/*
	public static final int OWNER_ARGUMENT = 1;
	public static final int REPO_ARGUMENT = 2;
	public static final int ASSET_ARGUMENT = 4;
	public static final int DIRECTORY_ARGUMENT = 8;
	public static final int VERSION_ARGUMENT = 16;
	public static final int ALLOW_OVERRIDE_ARGUMENT = 32;
	*/



	// ******************************
	// Fields
	// ******************************
	protected GitGrabUI ui;
//	protected volatile boolean abort;
	protected String repoOwner, repoName, version, assetRegex;



	// ******************************
	// Constructors
	// ******************************
	
	/**
	 * Creates a GitGrab object that will use the given UI for displaying progress and requesting missing information.<br>
	 * The UI is necessary and therefor must not be null!
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




/*
	// ******************************
	// Private methods
	// ******************************
	private String getValidStringArgument(int argrument, String currentValue) {
		while((currentValue == null || currentValue.equals("")) && !abort) {
			currentValue = ui.requestStringArgument(argrument, currentValue);
		}
		return currentValue;
	}

	private String getPackageURL(String owner, String repo, String version, String asset) throws Exception {
		StringBuilder sb = new StringBuilder();
		URL url = new URL("https://api.github.com/repos/"+owner+"/"+repo+"/releases/"+version);
		URLConnection uc = url.openConnection();
		uc.connect();
		BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
		for(String s; (s = br.readLine()) != null; sb.append(s)) {System.out.print(s);}
		String res = sb.toString();
		int assetsIndex = res.indexOf("\"assets\"");
		if(assetsIndex == -1)
			return null;

		return res;
	}

	  */



	
	
	
	
	
	protected static URLConnection getURLConnection(String url) throws IOException {
		URL u = new URL(url);
		URLConnection uc = u.openConnection();
		uc.setRequestProperty("User-Agent", "Chippelius/GitGrab");
		uc.setRequestProperty("Accept", "application/vnd.github.v3+json");
		return uc;
	}
	
	
	
	
	
	
	// ******************************
	// Public methods
	// ******************************

//	public void abort() {
//		abort = true;
//	}

	/**
	 * @return the repoOwner
	 */
	public synchronized String getRepoOwner() {
		return repoOwner;
	}
	/**
	 * @param repoOwner the repoOwner to set
	 */
	public synchronized GitGrab setRepoOwner(String repoOwner) {
		this.repoOwner = repoOwner;
		return this;
	}

	/**
	 * @return the repoName
	 */
	public synchronized String getRepoName() {
		return repoName;
	}
	/**
	 * @param repoName the repoName to set
	 */
	public synchronized GitGrab setRepoName(String repoName) {
		this.repoName = repoName;
		return this;
	}

	/**
	 * @return the version
	 */
	public synchronized String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public synchronized GitGrab setVersion(String version) {
		this.version = version;
		return this;
	}

	/**
	 * @return the assetRegex
	 */
	public synchronized String getAssetRegex() {
		return assetRegex;
	}
	/**
	 * @param assetRegex the assetRegex to set
	 */
	public synchronized GitGrab setAssetRegex(String assetRegex) {
		this.assetRegex = assetRegex;
		return this;
	}




	public synchronized void install() throws Exception {
		JsonReader reader = Json.createReader(getURLConnection("https://api.github.com/repos/"+repoOwner+"/"+repoName+"/releases/"+version).getInputStream());
		JsonObject root = reader.readObject();
		JsonArray assets = root.getJsonArray("assets");
		for(int i=0; i<assets.size(); ++i) {
			if(assets.getJsonObject(i).getString("name").matches(assetRegex)) {
				System.out.println("Downloading "+assets.getJsonObject(i).getString("name"));
				URLConnection downloadConnection = getURLConnection(assets.getJsonObject(i).getString("url"));
				downloadConnection.setRequestProperty("Accept", "application/octet-stream");
				InputStream in = downloadConnection.getInputStream();
				FileOutputStream fos = new FileOutputStream("C:\\Users\\Leo\\Downloads\\"+assets.getJsonObject(i).getString("name"), false);
				byte[] buffer = new byte[1024];
				for(int count=0;(count=in.read(buffer)) > -1; fos.write(buffer, 0, count));
				in.close();
				fos.close();
				System.out.println("Download complete.");
				
				System.out.println("Extracting "+assets.getJsonObject(i).getString("name"));
				ZipFile zipfile = new ZipFile("C:\\Users\\Leo\\Downloads\\"+assets.getJsonObject(i).getString("name"));
				zipfile.extractAll("C:\\Users\\Leo\\Downloads\\");
				System.out.println("Extracting complete.");
				
				new File("C:\\Users\\Leo\\Downloads\\"+assets.getJsonObject(i).getString("name")).delete();
				break;
			}
		}
	}
	
	
	

	// ******************************
	// Public static methods
	// ******************************

	public static void main(String[] args) {
		try {
			new GitGrab(new GitGrabUI() {
				@Override
				public void init() {
					// TODO Auto-generated method stub
				}
			}).setRepoOwner("atom").setRepoName("atom").setVersion(latestVersion).setAssetRegex(".*windows.*\\p{Punct}zip").install();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
