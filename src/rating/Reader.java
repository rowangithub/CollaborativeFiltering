package rating;


public abstract class Reader {
	public abstract int getUserNum();
	public abstract int getItemNum();
	public abstract double getMinPref();
	public abstract double getMaxPref();
	
	public abstract boolean hasNext();	
	public abstract Record getNext();
	public abstract void reset();
}
