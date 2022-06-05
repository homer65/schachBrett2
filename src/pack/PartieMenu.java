package pack;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import org.myoggradio.schach.*;
public class PartieMenu extends Menu implements ActionListener,BrettListener
{
	static final long serialVersionUID = 1;
	private JLabel lab1 = new JLabel("spiel beginnt");
	private JButton butt1 = new JButton("<");
	private JButton butt2 = new JButton("Matt?");
	private JButton butt3 = new JButton("Patt?");
	private JButton butt4 = new JButton("Drehen");
	private JButton butt5 = new JButton("Kernel");
	private JButton butt6 = new JButton("Engine");
	private JPanel cpan = null;
	private JTextArea ta = null;
	private JTextArea taengine = null;
	private JScrollPane scroll = null;
	private JPanel main = null;
	private JPanel npan = null;
	private boolean ok = false;
	private Position von = null;
	private Position nach = null;
	private Zug zug = null;
	private Spiel partie = null;
	private BrettPanel bpan = null;
	private boolean viewWeiss = true;
	private JMenuBar menubar = new JMenuBar();
	private JMenu menu1 = new JMenu("File");
	private JMenuItem item11 = new JMenuItem("backup");
	private JMenuItem item12 = new JMenuItem("restore");
	private JMenuItem item13 = new JMenuItem("print to HTML");
	private JMenu menu2 = new JMenu("Engine");
	private JMenuItem item21 = new JMenuItem("Choose Engine");
	private JMenu menu3 = new JMenu("Info");
	private JMenuItem item31 = new JMenuItem("Version");
	private boolean engineOn = false;
	private Engine engine = null;
	private Preferences prefs = Preferences.userRoot();
	public PartieMenu(Spiel partie)
	{
		menubar.add(menu1);
		menubar.add(menu2);
		menubar.add(menu3);
		menu1.add(item11);
		menu1.add(item12);
		menu1.add(item13);
		menu2.add(item21);
		menu3.add(item31);
		this.partie = partie;
		butt1.addActionListener(this);
		butt2.addActionListener(this);
		butt3.addActionListener(this);
		butt4.addActionListener(this);
		butt5.addActionListener(this);
		butt6.addActionListener(this);
		item11.addActionListener(this);
		item12.addActionListener(this);
		item13.addActionListener(this);
		item21.addActionListener(this);
		item31.addActionListener(this);
		init();
	}
	public void init()
	{
		bpan = new BrettPanel(partie.getStellung(),viewWeiss);
		bpan.addListener(this);
		main = new JPanel();
		main.setLayout(new BorderLayout());
		main.add(butt1,BorderLayout.WEST);
		main.add(lab1,BorderLayout.CENTER);
		npan = new JPanel();
		npan.setLayout(new GridLayout(1,5));
		npan.add(butt2);
		npan.add(butt3);
		npan.add(butt4);
		npan.add(butt5);
		npan.add(butt6);
		ta = new JTextArea();
		taengine = new JTextArea();
		String notation = partie.getNotation();
		ta.setText(notation);
		taengine.setText("*-----Engine Ausgabe-----*" + "\n");
		if (engineOn) engine.setTextArea(taengine);
		scroll = new JScrollPane(ta);
		JSplitPane split = new JSplitPane();
		split.setLeftComponent(scroll);
		split.setRightComponent(taengine);
		cpan = new JPanel();
		cpan.setLayout(new BorderLayout());
		cpan.add(bpan,BorderLayout.CENTER);
		cpan.add(main,BorderLayout.SOUTH);
		cpan.add(npan,BorderLayout.NORTH);
		cpan.add(split,BorderLayout.EAST);
		setContentPane(cpan);
		setJMenuBar(menubar);
		validate();
	}
	public void getEnginePath()
	{
		JFileChooser chooser = new JFileChooser();
		int rc = chooser.showOpenDialog(null);
		if (rc == JFileChooser.APPROVE_OPTION)
		{
			File file = chooser.getSelectedFile();
			Engine.setPath(file.getAbsolutePath());
			prefs.put("schachbrett2_path",file.getAbsolutePath());
		}
	}
	@Override
	public void actionPerformed(ActionEvent ae)
	{
		Object source = ae.getSource();
		if (source == item11)
		{
			JFileChooser chooser = new JFileChooser();
			int rc = chooser.showOpenDialog(null);
			if (rc == JFileChooser.APPROVE_OPTION)
			{
				try
				{
					File file = chooser.getSelectedFile();
					OutputStream os = new FileOutputStream(file);
					ObjectOutputStream oos = new ObjectOutputStream(os);
					oos.writeObject(partie);
					oos.close();
				}
				catch (Exception e)
				{
					System.out.println("PartieMenu:actionPerformed:item11:Exception:");
					System.out.println(e.toString());
				}
			}
		}
		if (source == item12)
		{
			JFileChooser chooser = new JFileChooser();
			int rc = chooser.showOpenDialog(null);
			if (rc == JFileChooser.APPROVE_OPTION)
			{
				try
				{
					File file = chooser.getSelectedFile();
					InputStream is = new FileInputStream(file);
					ObjectInputStream ois = new ObjectInputStream(is);
					partie = (Spiel)ois.readObject();
					ois.close();
				}
				catch (Exception e)
				{
					System.out.println("PartieMenu:actionPerformed:item12:Exception:");
					System.out.println(e.toString());
				}
			}
			init();
		}
		if (source == item13)
		{
			JFileChooser chooser = new JFileChooser();
			int rc = chooser.showOpenDialog(null);
			if (rc == JFileChooser.APPROVE_OPTION)
			{
				try
				{
					File file = chooser.getSelectedFile();
					Writer wrt = new FileWriter(file);
					wrt.write("<!doctype html>" + "\n");
					wrt.write("<html lang=\"de\">" + "\n");
					wrt.write("<head>" + "\n");
					wrt.write("<meta hhtp-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">" + "\n");
					wrt.write("</head>" + "\n");
					wrt.write("<body>" + "\n");
					String notation = partie.getNotationsHTML();
					wrt.write(notation);
					wrt.write("</body>" + "\n");
					wrt.write("</html>" + "\n");
					wrt.close();
				}
				catch (Exception e)
				{
					System.out.println("PartieMenu:actionPerformed:item13:Exception:");
					System.out.println(e.toString());
				}
			}
		}
		if (source == item21)
		{
			getEnginePath();
		}
		if (source == item31)
		{
			Protokol.write("Version 04.06.2022");
			Version version = new Version();
			String info = version.getVersion();
			Protokol.write("Kernel Version:");
			Protokol.write(info);
		}
		if (source == butt1)
		{
			partie = partie.einZugZurueck();
			if (engineOn)
			{
				String command = "stop";
				engine.sendCommand(command);
				wait(500);
				command = "position startpos moves";
				ArrayList<Zug> zuege = partie.getZuege();
				for (int i=0;i<zuege.size();i++)
				{
					Zug zug = zuege.get(i);
					Position von = zug.getVon();
					Position nach = zug.getNach();
					String notation = von.getNotation() + nach.getNotation();
					command += " " + notation;
				}
				engine.sendCommand(command);
				wait(100);
				command = "go infinite";
				engine.sendCommand(command);
			}
			init();
		}
		if (source == butt2)
		{
			boolean matt = partie.istMatt();
			if (matt) System.out.println("Partie ist matt");
			else System.out.println("Partie ist nicht matt");
		}
		if (source == butt3)
		{
			boolean patt = partie.istPatt();
			if (patt) System.out.println("Partie ist patt");
			else System.out.println("Partie ist nicht patt");
		}
		if (source == butt4)
		{
			if (viewWeiss)
			{
				viewWeiss = false;
			}
			else
			{
				viewWeiss = true;
			}
			init();
		}
		if (source == butt5)
		{
			Version version = new Version();
			String info = version.getVersion();
			System.out.println(info);
		}
		if (source == butt6)
		{
			if (engineOn)
			{
				engineOn = false;
				engine.kill();
				engine = null;
			}
			else
			{
				if (Engine.getPath() == null)
				{
					getEnginePath();
				}
				if (Engine.getPath() != null)
				{
					engineOn = true;
					engine = new Engine(taengine);
					engine.start();
					wait(100);
					String command = "ucinewgame";
					engine.sendCommand(command);
					wait(100);
					command = "position startpos moves";
					ArrayList<Zug> zuege = partie.getZuege();
					for (int i=0;i<zuege.size();i++)
					{
						Zug zug = zuege.get(i);
						Position von = zug.getVon();
						Position nach = zug.getNach();
						String notation = von.getNotation() + nach.getNotation();
						command += " " + notation;
					}
					engine.sendCommand(command);
					wait(100);
					command = "go infinite";
					engine.sendCommand(command);
				}
			}
		}
	}
	public void wait(int millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (Exception e)
		{
			
		}
	}
	@Override
	public void event(BrettEvent be) 
	{
		Figur nf = null;
		Position feld = be.getFeld();
		int x = feld.getX();
		int y = feld.getY();
		if (ok)
		{
			int vx = von.getX();
			int vy = von.getY();
			if (x == vx && y == vy)
			{
				String text = "von anulliert";
				lab1.setText(text);	
			}
			else
			{	
				nach = feld;
				if (partie.istWeissAmZug())
				{
					if (partie.getStellung().getFigur(von).istBauer())
					{
						if (partie.getStellung().getFigur(von).istWeiss())
						{
							if (nach.getY() == 7)
							{
								Figur weisseDame = Factory.getFigur();
								weisseDame.setWeiss();
								weisseDame.setDame();
								String[] values = {"Dame","Turm","Läufer","Springer"};
								Object selected = JOptionPane.showInputDialog(
										null,
										"Bitte Figur auswählen",
										"?",
										JOptionPane.DEFAULT_OPTION,
										null,
										values,"Dame"
										);
								if (selected != null)
								{
									String s = selected.toString();
									System.out.println("PartieMenu:event:Neue Figur:" + s);
									if (s.equals("Turm")) weisseDame.setTurm();
									if (s.equals("Läufer")) weisseDame.setLaeufer();
									if (s.equals("Springer")) weisseDame.setSpringer();
								}
								nf = weisseDame;
							}
						}
					}
				}
				else
				{
					if (partie.getStellung().getFigur(von).istBauer())
					{
						if (partie.getStellung().getFigur(von).istSchwarz())
						{
							if (nach.getY() == 0)
							{
								Figur schwarzeDame = Factory.getFigur();
								schwarzeDame.setSchwarz();
								schwarzeDame.setDame();
								String[] values = {"Dame","Turm","Läufer","Springer"};
								Object selected = JOptionPane.showInputDialog(
										null,
										"Bitte Figur auswÃ¤hlen",
										"?",
										JOptionPane.DEFAULT_OPTION,
										null,
										values,"Dame"
										);
								if (selected != null)
								{
									String s = selected.toString();
									System.out.println("PartieMenu:event:Neue Figur:" + s);
									if (s.equals("Turm")) schwarzeDame.setTurm();
									if (s.equals("Läufer")) schwarzeDame.setLaeufer();
									if (s.equals("Springer")) schwarzeDame.setSpringer();
								}
								nf = schwarzeDame;
							}
						}
					}
				}
				zug = Factory.getZug();
				zug.setVon(von);
				zug.setNach(nach);
				zug.setNeueFigur(nf);
				if (nf != null)
				{
					Position v1 = zug.getVon();
					Position n1 = zug.getNach();
					int v1x = v1.getX();
					int v1y = v1.getY();
					int n1x = n1.getX();
					int n1y = n1.getY();
					System.out.println("PartieMenu:event:" + v1x + ":" + v1y + " # " + n1x + ":" + n1y);
				}
				boolean gueltig = partie.istGueltig(zug);
				if (gueltig)
				{
					partie.ziehe(zug);
					if (engineOn)
					{
						String command = "stop";
						engine.sendCommand(command);
						wait(500);
						command = "position startpos moves";
						ArrayList<Zug> zuege = partie.getZuege();
						for (int i=0;i<zuege.size();i++)
						{
							Zug zug = zuege.get(i);
							Position von = zug.getVon();
							Position nach = zug.getNach();
							String notation = von.getNotation() + nach.getNotation();
							command += " " + notation;
						}
						engine.sendCommand(command);
						wait(100);
						command = "go infinite";
						engine.sendCommand(command);
					}
					String text = zug.getNotation(" ");
					lab1.setText(text);
				}
				else
				{
					lab1.setText("ungueltig");	
				}
			}
			ok = false;
			init();
		}
		else
		{
			von = feld;
			int vonx = von.getX();
			int vony = von.getY();
			lab1.setText(von.getNotation());
			ok = true;
			ArrayList<Zug> mzuege = partie.gueltigeZuege();
			for (int i=0;i<mzuege.size();i++)
			{
				Zug mzug = mzuege.get(i);
				Position vfeld = mzug.getVon();
				int vx = vfeld.getX();
				int vy = vfeld.getY();
				if (vx == vonx && vy == vony)
				{
					Position nfeld = mzug.getNach();
					int nx = nfeld.getX();
					int ny = nfeld.getY();
					bpan.setColor(nx,ny,Color.blue);
				}
			}
		}
	}
}
