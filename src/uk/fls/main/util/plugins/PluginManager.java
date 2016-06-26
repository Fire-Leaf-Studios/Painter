package uk.fls.main.util.plugins;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import fls.engine.main.io.FileIO;
import uk.fls.main.util.tools.Downloader;
import uk.fls.main.util.tools.Tool;

public class PluginManager {

	private File pluginsFolder;
	private List<Plugin> plugins;
	private List<File> pluginFiles;
	private HashMap<String, PluginResource> infoFiles;
	private Downloader dl;
	
	public PluginManager(){
		this.pluginsFolder = new File(FileIO.path+"/plugins");
		if(!this.pluginsFolder.exists()){
			log("No Plugin folder");
			this.pluginsFolder.mkdir();
			log("Created a new one");
		}else{
			log("Plugin folder found");
		}
		
		this.dl = new Downloader();
		
		reload();
	}
	
	private void findPlugins(){
		this.plugins = new ArrayList<Plugin>();
		this.pluginFiles = new ArrayList<File>();
		this.infoFiles = new HashMap<String, PluginResource>();
		File[] files = this.pluginsFolder.listFiles();
		for(int i = 0; i < files.length; i++){// Looks for jars int the plugins directory
			if(files[i].getName().endsWith(".jar")){// Found a potential plugin
				this.pluginFiles.add(files[i]);
			}
		}
		
		URL[] urls = new URL[this.pluginFiles.size()];
		for(int i = 0; i < urls.length; i++){// Makes URLs of all the jars that we found
			try {
				urls[i] = this.pluginFiles.get(i).toURI().toURL();
			} catch (MalformedURLException e) {
				log("Error loading plugin file: " + this.pluginFiles.get(i));
			}
		}
		
		for(int i = 0; i < this.pluginFiles.size(); i++){// Looking for info files within jars for later
				File f = new File(urls[i].getFile());
				this.infoFiles.put(this.pluginFiles.get(i).getName(), loadPluginResources(f));
		}
		
		@SuppressWarnings("resource")
		ClassLoader cl = new URLClassLoader(urls, this.getClass().getClassLoader());
		
		for(int i = 0; i < this.pluginFiles.size(); i++){// Looking for classes that extend 'Plugin' and then instaciating them
			MetaData md = this.infoFiles.get(this.pluginFiles.get(i).getName()).getInfo();
			try {
				@SuppressWarnings("unchecked")
				Class<? extends Plugin> pluginClass = (Class<? extends Plugin>) cl.loadClass(md.getMain());
				Plugin p = pluginClass.newInstance();
				p.setResources(this.infoFiles.get(this.pluginFiles.get(i).getName()));
				this.plugins.add(p);
				continue;
				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			log("Error creating plugin instace for " + md.getMain());
		}
		
		log("Found " + this.plugins.size() + " plugins");
	}
	
	private void orderAndValidatePlugins(){
		int corePos = -1;
		for(int i = 0; i < this.plugins.size(); i++){
			if(this.plugins.get(i).getInfo().getName().equals("Core plugin")){
				corePos = i;
				break;
			}
		}
		
		if(corePos == -1){// No core plugin
			log("Unable to find or load Core plugin");
			log("Going to download the latest version for you");
			dl.downloadFile("http://fireleafstudios.uk/dl/painter/CorePainter.jar");
			reload();
			return;
		}
		
		if(corePos != 0){
			Plugin first = this.plugins.get(0);
			Plugin core = this.plugins.get(corePos);
			this.plugins.set(0, core);
			this.plugins.set(corePos, first);
			
			List<Plugin> invalid = new ArrayList<Plugin>();
			for(int i = 0; i < this.infoFiles.size(); i++){
				if(this.infoFiles.get(this.plugins.get(i).getInfo()) == null){
					invalid.add(this.plugins.get(i));
				}
			}
			this.plugins.removeAll(invalid);
			log("Removed invalid plugins");
		}
		log("Running with " + this.plugins.size() + " plugin(s) loaded");
	}
	
	private void log(String s){
		System.out.println("[Plugin Manager] " + s);
	}
	
	private PluginResource loadPluginResources(File f){
		PluginResource pr = new PluginResource();
		try(JarFile jar = new JarFile(f)){
			JarEntry entry = jar.getJarEntry("plugin.info");
			if(entry == null){
				log("Plugin file " + f + " is missing it's 'plugin.info' file");
				return null;
			}
			
			
			
			BufferedReader in = new BufferedReader(new InputStreamReader(jar.getInputStream(entry)));
			String out = "";
			String l = "";
			while((l = in.readLine()) != null){
				out += l + "\n"; 
			}
			MetaData md = new MetaData(out);
			pr.setInfo(md);
			
			entry = jar.getJarEntry(md.getSprites());
			if(entry == null){
				log("Couldn't find "+ md.getName() + "'s sprite resource: " + md.getSprites());
				log("Skipng over this");
			}else{
				in = new BufferedReader(new InputStreamReader(jar.getInputStream(entry)));
				l = "";
				while((l = in.readLine()) != null){
					out += l + "\n"; 
				}
				pr.setIconData(out);
			}
			
			return pr;
		}catch(Exception e){
			e.printStackTrace();
			log("Error loading plugin file: " + f);
		}
		return null;
	}
	
	public void loadTools(List<Tool> tools){
		for(int i = 0; i < this.plugins.size(); i++){
			Plugin p = this.plugins.get(i);
			for(int j = 0; j < p.getTools().size(); j++){
				Tool tool = p.getTools().get(j);
				tool.setFromPlugin();
				if(p.getResources().getIconData() != null){
					tool.setSpriteHolder(p.getResources().getIconData());
				}
				tool.init();
				tools.add(tool);
			}
		}
	}
	
	private void reload(){
		findPlugins();
		orderAndValidatePlugins();
	}
}
