package org.chippelius.GitGrab.core;

import java.io.File;
import java.io.FileOutputStream;
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
 *
 */
public class GitGrab {
	/*
	// ******************************
	// Constants
	// ******************************
	private static final String latestVersion = "latest";

	public static final int OWNER_ARGUMENT = 1;
	public static final int REPO_ARGUMENT = 2;
	public static final int ASSET_ARGUMENT = 4;
	public static final int DIRECTORY_ARGUMENT = 8;
	public static final int VERSION_ARGUMENT = 16;
	public static final int ALLOW_OVERRIDE_ARGUMENT = 32;




	// ******************************
	// Fields
	// ******************************
	private GitGrabUI ui;
	private volatile boolean abort;




	// ******************************
	// Constructors
	// ******************************
	 *//**
	 * Creates a GitGrabWorker object for performing the task defined in grab using the given UI.<br>
	 * Note that the GitGrab object not used directly but cloned, so it can't be accessed (and by that also modified) 
	 * from outside after the GitGrabWorker is set up.
	 * 
	 * @param ui the ui used for IO interactions
	 * 
	 * @see GitGrab
	 * @see GitGrabUI
	 *//*
	public GitGrab(GitGrabUI ui) {
		this.ui = ui;
		abort = false;
	}




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




	// ******************************
	// Public methods
	// ******************************
	public void install() {
		install(null, null, null, null, null, false);
	}

	public void install(String owner, String repo, String asset, String directory) {
		install(owner, repo, latestVersion, asset, directory, false);
	}

	public void install(String owner, String repo, String asset, String directory, boolean allowOverride) {
		install(owner, repo, latestVersion, asset, directory, allowOverride);
	}

	public void install(String owner, String repo, String version, String asset, String directory) {
		install(owner, repo, version, asset, directory, false);
	}

	public void install(String owner, String repo, String version, String asset, String directory, boolean allowOverride) {
		abort = false;
		if(abort) return;
		owner = getValidStringArgument(OWNER_ARGUMENT, owner);
		if(abort) return;
		repo = getValidStringArgument(REPO_ARGUMENT, repo);
		if(abort) return;
		version = getValidStringArgument(VERSION_ARGUMENT, version);
		if(abort) return;
		asset = getValidStringArgument(ASSET_ARGUMENT, asset);
		if(abort) return;
		directory = getValidStringArgument(DIRECTORY_ARGUMENT, directory);
		if(abort) return;
		String packageURL;
		try {
			packageURL = getPackageURL(owner, repo, version, asset);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public void abort() {
		abort = true;
	}
	  */



	public static void main(String[] args) {
		try {
			JsonReader reader = Json.createReader(new URL("https://api.github.com/repos/atom/atom/releases/latest").openStream());
			JsonObject root = reader.readObject();
			JsonArray assets = root.getJsonArray("assets");
			for(int i=0; i<assets.size(); ++i) {
				if(assets.getJsonObject(i).getString("name").matches(".*windows.*\\p{Punct}zip")) {
					System.out.println("Downloading "+assets.getJsonObject(i).getString("name"));
					URL downloadUrl = new URL(assets.getJsonObject(i).getString("url"));
					URLConnection downloadConnection = downloadUrl.openConnection();
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
