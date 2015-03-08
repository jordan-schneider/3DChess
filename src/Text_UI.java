import java.util.Scanner;


public class Text_UI implements UI{
	private Opponent[] opps;
	private long[] ids;
	private Game g;
	private Scanner s=new Scanner(System.in);
	@Override
	public void init(Opponent[] opps,long[] ids,Game g) {
		this.opps=opps;
		this.g=g;
		this.ids=ids;
	}
	public void getMove(long id){
		if(id!=ids[g.cPlayer])
			return;
		if(opps[g.cPlayer].isHuman()){
			boolean first=true;
			int[] f,t;
			do{
				if(!first)
					System.out.println("\nLooks like you made an illegal move! Try again.");
				System.out.println("It is "+((g.cPlayer==RaumschachBoard.WHITE)?"white's":"black's")+" turn.");
				first=false;
				System.out.println(g.board.toString());
				System.out.print("Where do you want to move from? (e.g. Dc4) ");
				String from=s.nextLine();
				System.out.print("Where do you want to move to? ");
				String to=s.nextLine();
				System.out.println();
				f=new int[]{from.charAt(1)-'a',from.charAt(2)-'1',from.charAt(0)-'A'};
				t=new int[]{to.charAt(1)-'a',to.charAt(2)-'1',to.charAt(0)-'A'};
			}while(!opps[g.cPlayer].makeMove(f, t));
		}else
			opps[g.cPlayer].makeMove();
	}

}
