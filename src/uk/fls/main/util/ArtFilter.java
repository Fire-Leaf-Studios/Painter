package uk.fls.main.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ArtFilter extends FileFilter {
	
	
	private String ext, desc;
	
	public ArtFilter(String ext, String desc){
		this.ext = ext;
		this.desc = desc;
	}

	@Override
	public boolean accept(File f) {
		if(f.isDirectory())return true;
		String name = f.getName();
		String ext = name.substring(name.indexOf("."));
		ext = ext.toLowerCase();
		if(this.ext.equals(ext))return true;
		return false;
	}

	@Override
	public String getDescription() {
		return this.desc==null?"Custom filter":this.desc;
	}

}
