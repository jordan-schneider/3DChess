import java.util.LinkedList;


public class Basic_AI extends Opponent {
	Board board;
	byte[][][] b=new byte[5][5][5];	//game state (board).
	int player; 					//game state (player).
	int act_player;
	public Basic_AI(long id,int player) {
		super(id);
		this.player=0;
		this.act_player=player;
		board=new RaumschachBoard(new Opponent(0l), new Opponent(1l));
		for(int i=0;i<5;i++)
			for(int j=0;j<2;j++){
				b[i][1][j]='P';
				b[i][3][4-j]='p';
			}
		b[2][4][4]='k';
		b[0][4][4]='r';
		b[4][4][4]='r';
		b[1][4][4]='n';
		b[3][4][4]='n';
		b[2][4][3]='q';
		b[0][4][3]='b';
		b[4][4][3]='b';
		b[1][4][3]='u';
		b[3][4][3]='u';

		b[2][0][0]='K';
		b[0][0][0]='R';
		b[4][0][0]='R';
		b[1][0][0]='N';
		b[3][0][0]='N';
		b[2][0][1]='Q';
		b[0][0][1]='B';
		b[4][0][1]='B';
		b[1][0][1]='U';
		b[3][0][1]='U';
	}
	final static int[][] king={{1,0,0},{-1,0,0},{0,1,0},{0,-1,0},{0,0,1},{0,0,-1}};
	final static int[][] knight={
		{0,1,2},{0,-1,2},{0,1,-2},{0,-1,-2},
		{0,2,1},{0,-2,1},{0,2,-1},{0,-2,-1},
		{1,0,2},{-1,0,2},{1,0,-2},{-1,0,-2},
		{1,2,0},{-1,2,0},{1,-2,0},{-1,-2,0},
		{2,0,1},{2,0,-1},{-2,0,1},{-2,0,-1},
		{2,1,0},{2,-1,0},{-2,1,0},{-2,-1,0}
	};
	public LinkedList<int[]> genMove(int x,int y,int z){
		LinkedList<int[]> li=new LinkedList<int[]>();
		switch(b[x][y][z]){
		case 'k':
		case 'K':
			for(int[] p:king){
				int[] d=new int[]{x+p[0],y+p[1],z+p[2]};
				if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&(b[d[0]][d[1]][d[2]]==0||(b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0)))
					li.add(d);
			}
			break;
		case 'n':
		case 'N':
			for(int[] p:knight){
				int[] d=new int[]{x+p[0],y+p[1],z+p[2]};
				if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&(b[d[0]][d[1]][d[2]]==0||(b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0)))
					li.add(d);
			}
			break;
		case 'r':
		case 'R':
			for(int i=0;i<3;i++)
				for(int j=-1;j<2;j+=2){
					int[] d=new int[]{x,y,z};
					d[i]+=j;
					while((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&b[d[0]][d[1]][d[2]]==0){
						li.add(d.clone());
						d[i]+=j;
					}
					if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&(b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0))
						li.add(d);				
				}
			break;
		case 'b':
		case 'B':
			for(int i=0;i<2;i++)
				for(int j=i+1;j<3;j++)
					for(int k=-1;k<2;k+=2)
						for(int l=-1;l<2;l+=2){
							int[] d=new int[]{x,y,z};
							d[i]+=k;
							d[j]+=l;
							while((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&b[d[0]][d[1]][d[2]]==0){
								li.add(d.clone());
								d[i]+=k;
								d[j]+=l;
							}
							if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&((b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0))){

								li.add(d);	
							}
						}
			break;
		case 'u':
		case 'U':
			for(int i=-1;i<2;i+=2)
				for(int j=-1;j<2;j+=2)
					for(int k=-1;k<2;k+=2){
						int[] d=new int[]{x,y,z};
						d[0]+=i;
						d[1]+=j;
						d[2]+=k;
						while((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&b[d[0]][d[1]][d[2]]==0){
							li.add(d.clone());
							d[0]+=i;
							d[1]+=j;
							d[2]+=k;
						}
						if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&(b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0))
							li.add(d);	
					}
			break;
		case 'q':
		case 'Q':
			for(int i=0;i<3;i++)
				for(int j=-1;j<2;j+=2){
					int[] d=new int[]{x,y,z};
					d[i]+=j;
					while((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&b[d[0]][d[1]][d[2]]==0){
						li.add(d.clone());
						d[i]+=j;
					}
					if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&(b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0))
						li.add(d);				
				}
			for(int i=0;i<2;i++)
				for(int j=i+1;j<3;j++)
					for(int k=-1;k<2;k+=2)
						for(int l=-1;l<2;l+=2){
							int[] d=new int[]{x,y,z};
							d[i]+=k;
							d[j]+=l;
							while((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&b[d[0]][d[1]][d[2]]==0){
								li.add(d.clone());
								d[i]+=k;
								d[j]+=l;
							}
							if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&(b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0))
								li.add(d);	
						}
			for(int i=-1;i<2;i+=2)
				for(int j=-1;j<2;j+=2)
					for(int k=-1;k<2;k+=2){
						int[] d=new int[]{x,y,z};
						d[0]+=i;
						d[1]+=j;
						d[2]+=k;
						while((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&b[d[0]][d[1]][d[2]]==0){
							li.add(d.clone());
							d[0]+=i;
							d[1]+=j;
							d[2]+=k;
						}
						if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&(b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0))
							li.add(d);	
					}
			break;
		case 'p':
			if(x-1>=0&&y-1>=0&&b[x-1][y-1][z]!=0&&(b[x-1][y-1][z]-'a'<0))
				li.add(new int[]{x-1,y-1,z});
			if(x-1>=0&&z-1>=0&&b[x-1][y][z-1]!=0&&(b[x-1][y][z-1]-'a'<0))
				li.add(new int[]{x-1,y,z-1});
			if(x+1<5&&y-1>=0&&b[x+1][y-1][z]!=0&&(b[x+1][y-1][z]-'a'<0))
				li.add(new int[]{x+1,y-1,z});
			if(x+1<5&&z-1>=0&&b[x+1][y][z-1]!=0&&(b[x+1][y][z-1]-'a'<0))
				li.add(new int[]{x+1,y,z-1});
			if(y-1>0&&b[x][y-1][z]==0)
				li.add(new int[]{x,y-1,z});
			if(z-1>0&&b[x][y][z-1]==0)
				li.add(new int[]{x,y,z-1});
			break;
		case 'P':
			if(x-1>=0&&y+1<5&&b[x-1][y+1][z]!=0&&(b[x-1][y+1][z]-'a'>=0))
				li.add(new int[]{x-1,y+1,z});
			if(x-1>=0&&z+1<5&&b[x-1][y][z+1]!=0&&(b[x-1][y][z+1]-'a'>=0))
				li.add(new int[]{x-1,y,z+1});
			if(x+1<5&&y+1<5&&b[x+1][y+1][z]!=0&&(b[x+1][y+1][z]-'a'>=0))
				li.add(new int[]{x+1,y+1,z});
			if(x+1<5&&z+1<5&&b[x+1][y][z+1]!=0&&(b[x+1][y][z+1]-'a'>=0))
				li.add(new int[]{x+1,y,z+1});
			if(y+1<5&&b[x][y+1][z]==0)
				li.add(new int[]{x,y+1,z});
			if(z+1<5&&b[x][y][z+1]==0)
				li.add(new int[]{x,y,z+1});
			break;
		default:
		}
		return li;
	}
	public LinkedList<int[]> genQuiMove(int x,int y,int z){
		LinkedList<int[]> li=new LinkedList<int[]>();
		switch(b[x][y][z]){
		case 'k':
		case 'K':
			for(int[] p:king){
				int[] d=new int[]{x+p[0],y+p[1],z+p[2]};
				if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&((b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0)))
					li.add(d);
			}
			break;
		case 'n':
		case 'N':
			for(int[] p:knight){
				int[] d=new int[]{x+p[0],y+p[1],z+p[2]};
				if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&(b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0))
					li.add(d);
			}
			break;
		case 'r':
		case 'R':
			for(int i=0;i<3;i++)
				for(int j=-1;j<2;j+=2){
					int[] d=new int[]{x,y,z};
					d[i]+=j;
					while((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&b[d[0]][d[1]][d[2]]==0){
						d[i]+=j;
					}
					if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&(b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0))
						li.add(d);				
				}
			break;
		case 'b':
		case 'B':
			for(int i=0;i<2;i++)
				for(int j=i+1;j<3;j++)
					for(int k=-1;k<2;k+=2)
						for(int l=-1;l<2;l+=2){
							int[] d=new int[]{x,y,z};
							d[i]+=k;
							d[j]+=l;
							while((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&b[d[0]][d[1]][d[2]]==0){
								d[i]+=k;
								d[j]+=l;
							}
							if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&(b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0))
								li.add(d);	
						}
			break;
		case 'u':
		case 'U':
			for(int i=-1;i<2;i+=2)
				for(int j=-1;j<2;j+=2)
					for(int k=-1;k<2;k+=2){
						int[] d=new int[]{x,y,z};
						d[0]+=i;
						d[1]+=j;
						d[2]+=k;
						while((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&b[d[0]][d[1]][d[2]]==0){
							d[0]+=i;
							d[1]+=j;
							d[2]+=k;
						}
						if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&(b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0))
							li.add(d);	
					}
			break;
		case 'q':
		case 'Q':
			for(int i=0;i<3;i++)
				for(int j=-1;j<2;j+=2){
					int[] d=new int[]{x,y,z};
					d[i]+=j;
					while((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&b[d[0]][d[1]][d[2]]==0){
						d[i]+=j;
					}
					if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&(b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0))
						li.add(d);				
				}
			for(int i=0;i<2;i++)
				for(int j=i+1;j<3;j++)
					for(int k=-1;k<2;k+=2)
						for(int l=-1;l<2;l+=2){
							int[] d=new int[]{x,y,z};
							d[i]+=k;
							d[j]+=l;
							while((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&b[d[0]][d[1]][d[2]]==0){
								d[i]+=k;
								d[j]+=l;
							}
							if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&(b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0))
								li.add(d);	
						}
			for(int i=-1;i<2;i+=2)
				for(int j=-1;j<2;j+=2)
					for(int k=-1;k<2;k+=2){
						int[] d=new int[]{x,y,z};
						d[0]+=i;
						d[1]+=j;
						d[2]+=k;
						while((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&b[d[0]][d[1]][d[2]]==0){
							d[0]+=i;
							d[1]+=j;
							d[2]+=k;
						}
						if((d[0]|d[1]|d[2])>=0&&(d[0]<5&&d[1]<5&&d[2]<5)&&(b[x][y][z]-'a'>=0)^(b[d[0]][d[1]][d[2]]-'a'>=0))
							li.add(d);	
					}
			break;
		case 'p':
			if(x-1>=0&&y-1>=0&&b[x-1][y-1][z]!=0&&(b[x-1][y-1][z]-'a'<0))
				li.add(new int[]{x-1,y-1,z});
			if(x-1>=0&&z-1>=0&&b[x-1][y][z-1]!=0&&(b[x-1][y][z-1]-'a'<0))
				li.add(new int[]{x-1,y,z-1});
			if(x+1<5&&y-1>=0&&b[x-1][y-1][z]!=0&&(b[x+1][y-1][z]-'a'<0))
				li.add(new int[]{x-1,y-1,z});
			if(x+1<5&&z-1>=0&&b[x-1][y][z-1]!=0&&(b[x+1][y][z-1]-'a'<0))
				li.add(new int[]{x-1,y,z-1});
			break;
		case 'P':
			if(x-1>=0&&y+1<5&&b[x-1][y+1][z]!=0&&(b[x-1][y+1][z]-'a'>=0))
				li.add(new int[]{x-1,y+1,z});
			if(x-1>=0&&z+1<5&&b[x-1][y][z+1]!=0&&(b[x-1][y][z+1]-'a'>=0))
				li.add(new int[]{x-1,y,z+1});
			if(x+1<5&&y+1<5&&b[x-1][y+1][z]!=0&&(b[x+1][y+1][z]-'a'>=0))
				li.add(new int[]{x-1,y+1,z});
			if(x+1<5&&z+1<5&&b[x-1][y][z+1]!=0&&(b[x+1][y][z+1]-'a'>=0))
				li.add(new int[]{x-1,y,z+1});
			break;
		default:
		}
		return li;
	}
	public float eval(float alpha,float beta){
		float material=0;
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
				for(int k=0;k<5;k++)
					switch(b[i][j][k]){
					case 'p':
						material+=1;
						break;
					case 'P':
						material-=1;
						break;
					case 'b':
						material+=4;
						break;
					case 'B':
						material-=4;
						break;
					case 'u':
						material+=2;
						break;
					case 'U':
						material-=2;
						break;
					case 'r':
						material+=4;
						break;
					case 'R':
						material-=4;
						break;
					case 'n':
						material+=3;
						break;
					case 'N':
						material-=3;
						break;
					case 'q':
						material+=9;
						break;
					case 'Q':
						material-=9;
						break;
					case 'k':
						material+=10000;
						break;
					case 'K':
						material-=10000;
						break;
					}

		if(player!=1)
			material*=-1;
		//System.out.println("material "+material+","+alpha+","+beta);
		return material;
	}
	public float qui(float alpha,float beta){
		float best=Float.MIN_VALUE;
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
				for(int k=0;k<5;k++){
					for(int[] d:genQuiMove(i,j,k)){
						byte t1=b[i][j][k];
						byte t2=b[d[0]][d[1]][d[2]];
						b[i][j][k]=0;
						b[d[0]][d[1]][d[2]]=t1;
						player=1-player;
						float v=qui(-beta,-alpha);
						if(v>best)
							best=alpha;
						player=1-player;
						b[i][j][k]=t1;
						b[d[0]][d[1]][d[2]]=t2;
						alpha=Math.max(alpha,v);
						if(beta<=alpha)
							return alpha;
					}
				}
		if(best==Float.MIN_VALUE)
			return eval(alpha,beta);
		return best;
	}
	Principle variation=null;
	int max_iterl=1;
	public float search(float alpha,float beta,int iterl,int maxim){
		if(iterl==0)
			return eval(-beta,-alpha);
			//return qui(alpha,beta);
		float best=-10000000;
		for(int i=0;i<5;i++)
			for(int j=0;j<5;j++)
				for(int k=0;k<5;k++){
					if(b[i][j][k]>='a'&&player==1||b[i][j][k]<'a'&&player==0)
					for(int[] d:genMove(i,j,k)){
					//	System.out.println(RaumschachBoard.moveToString(new int[]{i,j,k})+" "+RaumschachBoard.moveToString(d)+" "+d[0]+","+d[1]+","+d[2]);
						byte t1=b[i][j][k];
						byte t2=b[d[0]][d[1]][d[2]];
						b[i][j][k]=0;
						b[d[0]][d[1]][d[2]]=t1;
						player=1-player;
						float v=-search(-beta,-alpha,iterl-1,maxim);
						//System.out.println("Has value "+v+","+best);
						if(v>best){
						//	System.out.println("best?");
							if(max_iterl==iterl){
								variation=new Principle(new int[]{i,j,k},d,v,Principle.EXACT);
								System.out.println("updated variation");
							}
								best=v;
						}
						player=1-player;
						b[i][j][k]=t1;
						b[d[0]][d[1]][d[2]]=t2;
						alpha=Math.max(alpha,v);
						if(beta<=alpha)
							return alpha;
					}
				}
		return best;
	}

	@Override
	void requestMove() {
		new Thread()
		{
			public void run() {
				assert player==act_player;
				System.out.println("Player: "+player);
				/*for(Piece p:game.cPlayer==Board.WHITE?board.whitepiece:board.blackpiece)
					for(int[] move:p.getMoves())
						if(board.isValidMove(p, move))
							if(game.ui.informMove(p.location,move,id)){
								Basic_AI.this.informMove(p.location, move);
								return;
							}

			}*/
				max_iterl=4;
				System.out.println("In Action!!!!!!!!!!!!!");
				float f=Basic_AI.this.search(-1000000, 1000000, max_iterl, act_player);
				System.out.println(f+" OUT OF ACTION!?");
				Principle p=variation;
				
				if(board.isValidMove(board.getAt(p.source),p.dest)){
					if(game.ui.informMove(p.source,p.dest,id)){
						System.out.println("Move accepted");
						Basic_AI.this.informMove(p.source, p.dest);
						return;
					}
				}else
					System.out.println("Illegal move...");
			}
		}.start();
	}
	

	@Override
	void informMove(int[] pieceSpot, int[] moveSpot) {
		/*for(int x=0;x<5;x++)
			for(int y=0;y<5;y++)
				for(int z=0;z<5;z++)
					if(b[x][y][z]!=0&&(player==1?(b[x][y][z]-'a'>=0):(b[x][y][z]-'a'<0)))
						for(int[] i:genMove(x, y, z))
							System.out.println(new String(new byte[]{b[x][y][z]})+""+RaumschachBoard.moveToString(new int[]{x,y,z})+" to "+RaumschachBoard.moveToString(i));
		*/if(board.getAt(moveSpot)!=null)
			if(board.getAt(moveSpot).owner==Board.WHITE)
				board.whitepiece.remove(board.getAt(moveSpot));
			else
				board.blackpiece.remove(board.getAt(moveSpot));
		board.setAt(board.getAt(pieceSpot),moveSpot);
		board.getAt(moveSpot).move(moveSpot);
		board.setAt(null,pieceSpot);
		b[moveSpot[0]][moveSpot[1]][moveSpot[2]]=b[pieceSpot[0]][pieceSpot[1]][pieceSpot[2]];
		b[pieceSpot[0]][pieceSpot[1]][pieceSpot[2]]=0;
		player=1-player;
	}

	@Override
	public boolean isHuman() {
		return false;
	}

	@Override
	public boolean makeMove() {
		return false;
	}

	@Override
	public boolean makeMove(int[] from, int[] to) {
		return false;
	}

}
