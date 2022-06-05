package pack;
import javax.swing.*;

import org.myoggradio.schach.*;
import java.awt.*;
import java.net.URL;
import java.util.*;

import schachfiguren.*;
public class BrettPanel extends JPanel implements ImagePanelListener
{
	public static final long serialVersionUID = 1;
	private ImagePanel[][] feldPanel = new ImagePanel[8][8];
	private Locator locator = new Locator();
	private ArrayList<BrettListener> listener = new ArrayList<BrettListener>();
	public BrettPanel(Stellung brett,boolean viewWeiss)
	{
		this.setLayout(new GridLayout(8,8));
		for (int x=0;x<8;x++)
		{
			for (int y=0;y<8;y++)
			{
				int k = y;
				int l = 7-x;
				Position feld = Factory.getPosition();
				feld.setX(k);
				feld.setY(l);
				Figur figur = brett.getFigur(feld);
				String name = feld.getImageName(figur);
				URL url = locator.getURL(name + ".gif");
				Image img = Toolkit.getDefaultToolkit().getImage(url); 
				feldPanel[k][l] = new ImagePanel(img);
				feldPanel[k][l].addListener(this,feld);
			}
		}
		for (int x=0;x<8;x++)
		{
			for (int y=0;y<8;y++)
			{
				int k = y;
				int l = 7-x;
				if (viewWeiss)
				{
					this.add(feldPanel[k][l]);
				}
				else
				{
					this.add(feldPanel[7-k][7-l]);
				}
			}
		}
	}
	public void setColor(int x,int y,Color color)
	{
		feldPanel[x][y].setColor(color);
	}
	public void addListener(BrettListener bl)
	{
		listener.add(bl);
	}
	@Override
	public void pressedOnImage(Position feld) 
	{
		System.out.println("BrettPanel:pressedOnImage:" + feld.getNotation());
		BrettEvent be = new BrettEvent(feld);
		for (int i=0;i<listener.size();i++)
		{
			BrettListener bl = listener.get(i);
			bl.event(be);
		}
	}
}
