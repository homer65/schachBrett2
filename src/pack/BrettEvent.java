package pack;
import org.myoggradio.schach.*;
public class BrettEvent 
{
	private Position feld = null;
	public BrettEvent(Position feld)
	{
		this.feld = feld;
	}
	public Position getFeld()
	{
		return feld;
	}
}
