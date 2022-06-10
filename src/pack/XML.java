package pack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.myoggradio.schach.Factory;
import org.myoggradio.schach.Figur;
import org.myoggradio.schach.Position;
import org.myoggradio.schach.Protokol;
import org.myoggradio.schach.Spiel;
import org.myoggradio.schach.Zug;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XML 
{
	public void save(File file,Spiel partie)
	{
		try
		{
			OutputStream os = new FileOutputStream(file);
			Writer writer = new OutputStreamWriter(os,"UTF-8");
			XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
			XMLStreamWriter out = outputFactory.createXMLStreamWriter(writer);
			out.writeStartDocument();
			out.writeStartElement("partie");
			ArrayList<Zug> zuege = partie.getZuege();
			for (int i=0;i<zuege.size();i++)
			{
				Zug zug = zuege.get(i);
				Position von = zug.getVon();
				Position nach = zug.getNach();
				Figur neueFigur = zug.getNeueFigur();
				String svon = von.getNotation();
				String snach = nach.getNotation();
				String sfarbe = "-10";
				String styp = "-10";
				if (neueFigur != null)
				{
					sfarbe = "" + neueFigur.getFarbe();
					styp = "" + neueFigur.getTyp();
				}
				out.writeStartElement("halbzug");
				out.writeAttribute("von",svon);
				out.writeAttribute("nach",snach);
				out.writeAttribute("farbe",sfarbe);
				out.writeAttribute("typ",styp);
				out.writeEndElement();
			}
			out.writeEndElement();
			out.writeEndDocument();
			out.close();
		}
		catch (Exception e)
		{
			Protokol.write("XML:save:Exception:");
			Protokol.write(e.toString());
		}
	}
	public Spiel read(File file)
	{
		Spiel erg = Factory.getSpiel();
		try
		{
			InputStream fin = new FileInputStream(file);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(fin));
			Node partie = doc.getFirstChild();
			NodeList list = partie.getChildNodes();
			for (int i=0;i<list.getLength();i++)
			{
				Node node = list.item(i);
				if (node instanceof Element)
				{
					Element halbzug = (Element) node;
					String name = halbzug.getTagName();
					if (name.equals("halbzug"))
					{
						String svon = halbzug.getAttribute("von");
						String snach = halbzug.getAttribute("nach");
						String sfarbe = halbzug.getAttribute("farbe");
						String styp = halbzug.getAttribute("typ");
						Position von = Factory.getPosition();
						Position nach = Factory.getPosition();
						Figur neueFigur = Factory.getFigur();
						von.getFromNotation(svon);
						nach.getFromNotation(snach);
						int farbe = Integer.parseInt(sfarbe);
						int typ = Integer.parseInt(styp);
						if (farbe == -10)
						{
							neueFigur = null;
						}
						else
						{
							neueFigur.setFarbe(farbe);
							neueFigur.setTyp(typ);
						}
						Zug zug = Factory.getZug();
						zug.setVon(von);
						zug.setNach(nach);
						zug.setNeueFigur(neueFigur);
						erg.ziehe(zug);
					}
				}
			}
		}
		catch (Exception e)
		{
			Protokol.write("XML:read:Exception:");
			Protokol.write(e.toString());
		}
		return erg; 
	}
}
