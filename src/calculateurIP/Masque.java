package calculateurIP;

public class Masque {
	
	//Initialisation
	private int intMasque;
	private String strMasque;
	private int[] masqueBin = new int[32];
	
	//Constructeur
	public Masque(int masque)
	{
		this.intMasque = masque;
		this.strMasque = "/" + masque;
		masqueBin = convertMasqueToBin();
	}
	
	//Convertir le numero du masque vers un tableau[32] binaire
	private int[] convertMasqueToBin()
	{	
		int[] masqueBin = new int[32];
		for(int i = 0; i < masqueBin.length; i++)
		{	
			if(i > this.intMasque - 1)
			{
				masqueBin[i] = 0;
				continue;
			}
			masqueBin[i] = 1;
		}
		
		return masqueBin;
	}
	
	//Afficher le masque binaire sous la forme xxxxxxxx.xxxxxxxx.xxxxxxxx.xxxxxxxx
	public String afficherBin()
	{
		String txt = "";
		for(int i = 0; i < this.masqueBin.length; i++)
		{
			if(i != 0 && i % 8 == 0)
			{
				txt += ".";
			}
			txt += this.masqueBin[i];
		}
		return txt;
	}
	
	//Calculer le nombre d'hote disponible
	public int nbrHoteDisponible()
	{
		return (int) Math.pow(2,(32 - intMasque));	//= 2^(32 - masque)
	}
	
	//Calculer le nombre d'hote utilisable
	public int nbrHoteUtilisable()
	{
		return (int) nbrHoteDisponible() - 2;	//nbrHoteDisponible - 2
	}
	
	//Getter 
	public String getStrMasque()
	{
		return this.strMasque;
	}
	
	public int getIntMasque()
	{
		return this.intMasque;
	}
	
	public int[] getMasqueBin()
	{
		return this.masqueBin;
	}
}
