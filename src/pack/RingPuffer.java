package pack;
public class RingPuffer 
{
	private int n = 0;
	private String[] puffer = null;
	public RingPuffer(int n)
	{
		this.n = n;
		puffer = new String[n];
		for (int i=0;i<n;i++)
		{
			puffer[i] = "";
		}
	}
	public void addText(String s)
	{
		for (int i=0;i<n-1;i++)
		{
			puffer[i] = puffer[i+1];
			puffer[n-1] = s;
		}
	}
	public String[] getText()
	{
		return puffer;
	}
}
