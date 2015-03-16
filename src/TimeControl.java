
public class TimeControl {
	long[] sTime;
	long[] incr;
	long[] delay;
	public TimeControl(long sTime,long incr,long delay){
		this(new long[]{sTime,sTime},new long[]{incr,incr},new long[]{delay,delay});
	}
	public TimeControl(long[] sTime,long[] incr,long[] delay){
		this.sTime=sTime;
		this.incr=incr;
		this.delay=delay;
	}
	public static String timeToString(long t){
		if(t<60000)
			return ""+(t/1000)+"_"+(t/10);
		else if(t<3600000)
			return ""+(t/60000)+"-"+((t%60000)/1000);
		else
			return ""+(t/3600000)+":"+((t%3600000)/60000)+"-"+((t%60000)/1000);
	}
}
