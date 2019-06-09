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
	protected String repoOwner, repoName, version, asset, destination;
	protected boolean override;



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




	// ******************************
	// Private methods
	// ******************************
	protected static URLConnection getURLConnection(String url) throws IOException {
		URL u = new URL(url);
		URLConnection uc = u.openConnection();
		uc.setRequestProperty("User-Agent", "Chippelius/GitGrab");
		uc.setRequestProperty("Accept", "application/vnd.github.v3+json");
		return uc;
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
	
/*
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

	protected static void unzip(String zipFile, String destinationDirectory) throws Exception {
		//TODO: redo without dependency
		ZipFile zipfile = new ZipFile(zipFile);
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
		JsonReader reader = Json.createReader(getURLConnection("https://api.github.com/repos/"+repoOwner+"/"+repoName+"/releases/"+version).getInputStream());
		JsonObject root = reader.readObject();
		JsonArray assets = root.getJsonArray("assets");
		for(int i=0; i<assets.size(); ++i) {
			if(assets.getJsonObject(i).getString("name").equals(asset)) {
				System.out.println("Downloading "+assets.getJsonObject(i).getString("name"));
				URLConnection downloadConnection = getURLConnection(assets.getJsonObject(i).getString("url"));
				downloadConnection.setRequestProperty("Accept", "application/octet-stream");
				InputStream in = downloadConnection.getInputStream();
				FileOutputStream fos = new FileOutputStream(destination+assets.getJsonObject(i).getString("name"), false);
				byte[] buffer = new byte[1024];
				for(int count=0;(count=in.read(buffer)) > -1; fos.write(buffer, 0, count));
				in.close();
				fos.close();
				System.out.println("Download complete.");
				
				File destinationFolder = new File(destination);
				if(destinationFolder.exists()) {
					if(override) {
						delete(destinationFolder);
						destinationFolder.mkdir();
					} else {
						System.out.println("Destination already exists!");
						return;
					}
				} else {
					destinationFolder.mkdirs();
				}
				
				System.out.println("Extracting "+assets.getJsonObject(i).getString("name"));
				unzip(destination+assets.getJsonObject(i).getString("name"), destination);
				System.out.println("Extracting complete.");
				
				new File(destination+assets.getJsonObject(i).getString("name")).delete();
				break;
			}
		}
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
				@Override
				public void init() {
					// TODO Auto-generated method stub
				}
			})
			.setRepoOwner("atom")
			.setRepoName("atom")
			.setVersion(latestVersion)
			.setAsset("atom-windows.zip")
			.setDestination("C:\\Users\\leo\\Downloads\\atom\\")
			.setOverride(false)
			.install();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
