package uk.fls.main.util.pallet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fls.engine.main.io.FileIO;

public class PalletManager {

	private List<Pallet> pallets;
	private List<Pallet> validPallets;
	
	public static PalletManager instance = new PalletManager();
	
	public PalletManager(){
	}
	
	public void init(){
		File folder = new File(FileIO.path+"/pallets");
		folder.mkdirs();
		
		this.pallets = new ArrayList<Pallet>();
		this.pallets.add(Pallet.def);
		File[] files = folder.listFiles();
		for(File f : files){
			this.pallets.add(new Pallet(f.getAbsolutePath()));
		}
		
		
		this.validPallets = new ArrayList<Pallet>();
		for(Pallet p : this.pallets){
			if(p.valid){
				this.validPallets.add(p);
			}
		}
		this.pallets.clear();
		log("Loaded " + this.validPallets.size() + " pallet(s)");
	}
	
	public List<Pallet> getPallets(){
		return this.validPallets;
	}
	
	public void log(String msg){
		System.out.println("[Pallet Manager] " + msg);
	}
	
	public void err(String msg){
		System.err.println("[Pallet Manager] " + msg);
	}
}
