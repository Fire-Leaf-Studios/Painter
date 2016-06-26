package uk.fls.main.util.tools;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import fls.engine.main.io.FileIO;

public class Downloader {

	public void downloadFile(String address){
		URL url;
		try {
			url = new URL(address);
			HttpURLConnection httpcont = (HttpURLConnection)url.openConnection();
			int responceCode = httpcont.getResponseCode();
			
			if(responceCode == HttpURLConnection.HTTP_NOT_FOUND){
				System.err.println("Nothing found at URL: " + address);
				return;
			}
			
			if(responceCode == HttpURLConnection.HTTP_OK){
				String fileName = "";
				fileName = address.substring(address.lastIndexOf("/")+1,address.length());
				
				InputStream in = httpcont.getInputStream();
				String filePos = FileIO.path +"/plugins/"+fileName;
				FileOutputStream out = new FileOutputStream(filePos);
				
				int bytesRead = -1;
				byte[] buffer = new byte[4096];
				while((bytesRead = in.read(buffer)) != -1){
					out.write(buffer, 0, bytesRead);
				}
				
				in.close();
				out.close();
				log("File downloaded");
			}else{
				err("Unable to download, url returned error code: " + responceCode);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void log(String s){
		System.out.println("[Downloader] " + s);
	}
	
	private void err(String s){
		System.err.println("[Downloader] " + s);
	}
}
