
public class Manager {

	public static void main(String[] args) {
		LocalOpponent a=new LocalOpponent(0);
		LocalOpponent b=new LocalOpponent(1);
		//Game g=new Game(a,0,b,1,new Text_UI());
		Game g=new Game(a,0,b,1,new Local_GUI_3D());
		g.start();
	}

}