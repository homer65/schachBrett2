package pack;
import javax.swing.*;
import org.myoggradio.schach.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ImagePanel extends JPanel implements MouseListener
{
	public static final long serialVersionUID = 1;
	private Color color = null;
	private Image img = null;
	private ArrayList<ImagePanelListener> listener = new ArrayList<ImagePanelListener>();
	private ArrayList<Position> felder = new ArrayList<Position>();
	public ImagePanel(Image img)
	{
		this.img = img;
		Dimension dim = new Dimension(48,48);
		setSize(dim);
		setPreferredSize(dim);
		setMinimumSize(dim);
		setMaximumSize(dim);
		setLayout(null);
		this.addMouseListener(this);
	}
	public void setColor(Color color)
	{
		this.color = color;
		this.repaint();
	}
	public void addListener(ImagePanelListener ipl,Position feld)
	{
		listener.add(ipl);
		felder.add(feld);
	}
	public void paintComponent(Graphics g)
	{
		g.drawImage(img,0,0,this);
		if (color != null)
		{
			g.setColor(color);
			g.drawRect(0,0,48,48);
			g.drawRect(1,1,46,46);
		}
	}
	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		for (int i=0;i<felder.size();i++)
		{
			ImagePanelListener ipl = listener.get(i);
			Position feld = felder.get(i);
			ipl.pressedOnImage(feld);
		}
	}
	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub	
	}
	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub	
	}
	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub	
	}
	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		// TODO Auto-generated method stub	
	}
}
