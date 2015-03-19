//Run dis.
public class Manager {

	public static void main(String[] args) {
		LocalOpponent a=new LocalOpponent(0);
		LocalOpponent b=new LocalOpponent(1);
		//UI gui=new Local_GUI();
		UI gui = new Local_GUI_3D();
		Game g=new Game(a,0,b,1,gui,new TimeControl(5*60,0,0));
		gui.init(new LocalOpponent[]{a,b},new long[]{a.id,b.id},g);
	}

}