package pack;
import javax.swing.*;
import org.myoggradio.schach.*;
import java.awt.*;
import java.awt.event.*;

public class BrettMenu extends Menu implements ActionListener,BrettListener
{
	static final long serialVersionUID = 1;
	private JPanel cpan = null;
	public BrettMenu(BrettPanel brett)
	{
		brett.addListener(this);
		cpan = new JPanel();
		cpan.setLayout(new BorderLayout());
		cpan.add(brett,BorderLayout.NORTH);
		setContentPane(cpan);
		validate();
	}
	@Override
	public void event(BrettEvent be) 
	{
		Position feld = be.getFeld();
		int x = feld.getX();
		int y = feld.getY();
		System.out.println("BrettMenu:event:" + x + ":" + y);
	}
}
