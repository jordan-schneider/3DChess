//Run dis.
public class Manager {

	public static void main(String[] args) {
		LocalOpponent a=new LocalOpponent(0);
		LocalOpponent b=new LocalOpponent(1);
		//Game g=new Game(a,0,b,1,new Local_GUI(),new TimeControl(5*60,0,0));
		Game g= new Game(a, 0, b, 1, new Local_GUI_3D(), new TimeControl(5*60,0,0));
		g.start();
	}

}