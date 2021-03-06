package uk.fls.main.util.plugins;

public class Version {

	
	private int major;
	private int minor;
	private int patch;
	
	private boolean beta;
	private boolean alpha;
	
	public Version(int maj, int min, int pat){
		this.major = maj;
		this.minor = min;
		this.patch = pat;
	}
	
	public boolean isGreater(Version other){
		if(this.major > other.major){
			return true;
		}else if(this.major < other.major){
			return false;
		}else{
			if(this.minor > other.minor){
				return true;
			}else if(this.minor < other.minor){
				return false;
			}else{
				if(this.patch > other.patch){
					return true;
				}else if(this.patch < other.patch){
					return false;
				}else{
					if(this.beta && !other.beta){
						return true;
					}else if(!this.beta && other.beta){
						return false;
					}else{
						if(this.alpha && !other.alpha){
							return true;
						}else if(!this.alpha && other.alpha){
							return false;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public Version setBeta(){
		this.beta = true;
		this.alpha = false;
		return this;
	}
	
	public Version setAlpha(){
		this.alpha = true;
		this.beta = false;
		return this;
	}
	
	public boolean isLower(Version other){
		return !isGreater(other);
	}
	
	public static Version parse(String sv){
		boolean dots = false;
		boolean dash = false;
		boolean slash = false;
		
		if(sv == null)return null;
		
		sv = sv.trim();
		if(sv.indexOf(".") != -1)dots = true;
		if(sv.indexOf("-") != -1)dash = true;
		if(sv.indexOf("/") != -1)slash = true;
		
		
		
		int maj = 0;
		int min = 0;
		int pat = 0;
		if(dots){
			String[] parts = sv.split("\\.");
			if(parts.length > 0)maj = Integer.parseInt(parts[0]);
			if(parts.length > 1)min = Integer.parseInt(parts[1]);
			if(parts.length > 2)pat = Integer.parseInt(parts[2]);
			return new Version(maj,min,pat);
		}else if(dash){
			String[] parts = sv.split("-");
			if(parts.length > 0)maj = Integer.parseInt(parts[0]);
			if(parts.length > 1)min = Integer.parseInt(parts[1]);
			if(parts.length > 2)pat = Integer.parseInt(parts[2]);
			return new Version(maj,min,pat);
		}else if(slash){
			String[] parts = sv.split("/");
			if(parts.length > 0)maj = Integer.parseInt(parts[0]);
			if(parts.length > 1)min = Integer.parseInt(parts[1]);
			if(parts.length > 2)pat = Integer.parseInt(parts[2]);
			return new Version(maj,min,pat);
		}else{
			return new Version(maj,min,pat);
		}
	}
	
	public String asString(){
		String ending = "";
		if(alpha){
			ending = " - Alpha";
		}else if(beta){
			ending = " - Beta";
		}
		return this.major + "." + this.minor + "." + this.patch + ending;
	}
}
