public class EntryInfo
{
	private String info;
	private long size;
	
	public EntryInfo() {
		info = "";
		size = 0;
	}
	
	public EntryInfo(String info, long size) {
		super();
		this.info = info;
		this.size = size;
	}

	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
}