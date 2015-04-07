
public class Principle {
	static final int EXACT=1;
	static final int ALPHA=2;
	static final int BETA=3;
	float value;
	int[] source;
	int[] dest;
	int type;
	public Principle(int[] source,int[] dest,float value,int type) {
		this.source=source;
		this.dest=dest;
		this.value=value;
		this.type=type;
	}
}
