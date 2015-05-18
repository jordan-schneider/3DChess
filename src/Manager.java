//Run dis.
public class Manager {

	public static void main(String[] args) {
		Opponent a=new LocalOpponent(0);
		Opponent b=new Basic_AI(1,1);
		UI gui = new Local_GUI_3D();
		Game g=new Game(a,b,gui,new TimeControl(5,0,0));
		g.init();
		gui.init(new Opponent[]{a,b}, new long[]{0,1}, g);
	}
}
