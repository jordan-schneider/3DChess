//Run dis.
public class Manager {

	public static void main(String[] args) {
		Opponent a=new LocalOpponent(0);
		Opponent b=new Basic_AI(1,1);
		UI gui = new Local_GUI_3D();
		//UI gui = new Local_GUI();
		Game g=new Game(a,0,b,1,gui,new TimeControl(5,0,0));
		gui.init(new Opponent[]{a,b},new long[]{a.id,b.id},g);
	}
}