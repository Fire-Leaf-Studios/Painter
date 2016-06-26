package uk.fls.main.util.plugins;

public class Version {

	
	public int major;
	public int minor;
	public int patch;
	
	public Version(int maj, int min, int pat){
		this.major = maj;
		this.minor = min;
		this.patch = pat;
	}
	
	public boolean isGreater(Version other){
		if(this.major > other.major){
			return true;
		}else{
			if(this.minor > other.minor){
				return true;
			}else{
				if(this.patch > other.patch){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static Version parse(String sv){
		boolean dots = false;
		boolean dash = false;
		boolean slash = false;
		
		sv = sv.trim();
		if(sv.indexOf(".") != -1)dots = true;
		if(sv.indexOf("-") != -1)dash = true;
		if(sv.indexOf("/") != -1)slash = true;
		
		
		
		int maj = 0;
		int min = 0;
		int pat = 0;
		if(dots){
			String[] parts = sv.split(".");
			if(parts.length > 0)maj = Integer.parseInt(parts[0]);
			if(parts.length > 1)min = Integer.parseInt(parts[1]);
			if(parts.length > 2)pat = Integer.parseInt(parts[2]);
		}else if(dash){
			String[] parts = sv.split("-");
			if(parts.length > 0)maj = Integer.parseInt(parts[0]);
			if(parts.length > 1)min = Integer.parseInt(parts[1]);
			if(parts.length > 2)pat = Integer.parseInt(parts[2]);
		}else if(slash){
			String[] parts = sv.split("/");
			if(parts.length > 0)maj = Integer.parseInt(parts[0]);
			if(parts.length > 1)min = Integer.parseInt(parts[1]);
			if(parts.length > 2)pat = Integer.parseInt(parts[2]);
		}
		

		return new Version(maj,min,pat);
	}
}
