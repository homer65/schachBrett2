package pack;

import java.util.prefs.Preferences;

import org.myoggradio.schach.*;

public class Main 
{
	public static void main(String[] args) 
	{
		Preferences prefs = Preferences.userRoot();
		String path = prefs.get("schachbrett2_path",null);
		Engine.setPath(path);
		Spiel partie = Factory.getSpiel();
		PartieMenu pmenu = new PartieMenu(partie);
		pmenu.anzeigen();
	}
}
