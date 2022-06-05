package pack;
import java.io.*;

import javax.swing.JTextArea;

import org.myoggradio.schach.Protokol;
public class Engine extends Thread
{
	private boolean shouldRun = true;
	private Process engine = null;
	private Reader rdr = null;
	private BufferedReader br = null;
	private Writer wrt  = null;
	private static String path = null;
	private JTextArea ta = null;
	private RingPuffer ringPuffer = new RingPuffer(15);
	public Engine(JTextArea ta)
	{
		this.ta = ta;
	}
	public static void setPath(String s)
	{
		path = s;
	}
	public static String getPath()
	{
		return path;
	}
	public void setTextArea(JTextArea ta)
	{
		this.ta = ta;
	}
	public void run()
	{
		try
		{
			Protokol.write("Engine:run:Path:" + path);
			ProcessBuilder pb = new ProcessBuilder(path);
			engine = pb.start();
			InputStream ein = engine.getInputStream();
			OutputStream aus = engine.getOutputStream();
			rdr = new InputStreamReader(ein);
			wrt = new OutputStreamWriter(aus);
			br = new BufferedReader(rdr);
			while (shouldRun)
			{
				String satz = br.readLine();
				String aussatz = filter(satz);
				if (aussatz != null) 
				{
					ringPuffer.addText(aussatz);
					String[] texte = ringPuffer.getText();
					ta.setText("*-----Engine Ausgabe-----*" + "\n");
					for (int i=0;i<texte.length;i++)
					{
						ta.append(texte[i] + "\n");
					}
				}
				Protokol.write(satz);
			}
		}
		catch (Exception e)
		{
			Protokol.write("Engine:start:Exception:");
			Protokol.write(e.toString());
		}
	}
	public void kill()
	{
		sendCommand("stop");
		try
		{
			Thread.sleep(500);
		}
		catch (Exception e)
		{
			
		}
		engine.destroyForcibly();
		shouldRun = false;
	}
	public void sendCommand(String command)
	{
		try 
		{
			wrt.write(command + "\n");
			wrt.flush();
		} 
		catch (IOException e) 
		{
			Protokol.write("Engine:sendCommand:Exception:");
			Protokol.write(e.toString());
		}
	}
	private String filter(String satz)
	{
		boolean ok = false;
		boolean first = true;
		String erg = null;
		try
		{
			String[] worte = satz.split(" ");
			if (worte[0].equals("info"))
			{
				for (int i=1;i<worte.length;i++)
				{
					if (worte[i].equals("score"))
					{
						ok = true;
						if (first)
						{
							first = false;
							erg = worte[i+1] + " " + worte[i+2];
						}
						else
						{
							erg += " " + worte[i+1] + " " + worte[i+2];
						}
					}
					if (worte[i].equals("pv"))
					{
						if (first)
						{
							first = false;
							erg = worte[i+1];
						}
						else
						{
							erg += " " + worte[i+1];
						}
					}
					if (worte[i].equals("depth"))
					{
						if (first)
						{
							first = false;
							erg = worte[i+1];
						}
						else
						{
							erg += " " + worte[i+1];
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			Protokol.write("Engine:filter:Exception:");
			Protokol.write(e.toString());
		}
		if (!ok) erg = null;
		return erg;
	}
}
