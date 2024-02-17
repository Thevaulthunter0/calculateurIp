package calculateurIP;

public class IPv4 {
	
	//Initialisation
	private Masque masque;
	private String ipString;
	private int[] ipBin = new int[32];
	private String reseauString;
	private int[] reseauBin = new int[32];
	
	//Constructeur
	public IPv4(String ipString, Masque masque)
	{
		this.ipString = ipString;
		this.masque = masque;
		this.ipBin = convertirStringVersBin();
		this.reseauBin = operationAND(this.ipBin, this.masque.getMasqueBin());
		this.reseauString = convertirBinVersString(this.reseauBin);
	}
	
	//Donne l'adresse de la premiere hote disponible
	public int[] premiereAdresse()
	{
		int[] reponse = new int[32];
		reponse = this.reseauBin;
		reponse[31] = 1;
		return reponse;
	}
	
	//Donne l'adresse de la derniere hote disponible
	public int[] derniereAdresse()
	{
		int[] reponse = new int[32];
		int x = this.masque.getIntMasque();
		for(int i = 0; i < this.reseauBin.length - 1; i++)
		{
			if(i >= x && i != 32)
			{
				reponse[i] = 1;
			}
			else {
				reponse[i] = this.reseauBin[i];
			}
		}
		return reponse;
	}
	
	//Donne l'adresse de diffusion du reseau
	public int[] adresseDiffusion()
	{
		int[] reponse = new int[32];
		int x = this.masque.getIntMasque();
		for(int i = 0; i < this.reseauBin.length; i++)
		{
			if(i >= x)
			{
				reponse[i] = 1;
			}
			else {
				reponse[i] = this.reseauBin[i];
			}
		}
		return reponse;
	}
	
	//Faire une operation AND entre deux tableau[32]. Utiliser dans le constructeur
	//pour avoir l'ip reseau en binaire.
	private int[] operationAND(int[] bin1, int[] bin2)
	{
		int[] reponse = new int[32];
		for(int i = 0; i < reponse.length; i++)
		{
			if(bin1[i] == 1 && bin2[i] == 1)
			{
				reponse[i] = 1;
			}
		}
		return reponse;
	}
	
	//Convertir IP(ipString) vers un tableau[32] binaire.
	private int[] convertirStringVersBin()
	{
		//Initialisation
		String[] strListe = new String[4];
		double[] dListe = new double[4];
		
		//Transforme le string en tableau de double
		strListe = this.ipString.split("\\.");
		for(int i = 0; i < dListe.length; i++)
		{
			dListe[i] = Double.parseDouble(strListe[i]);
		}
		
		//Convertir chaque double en chiffre binaire
		//et construit le chiffre binaire total.
		int[] ipBin = new int[32];
		int binPosition = 0;
		for(int i = 0; i < dListe.length; i++)
		{
			int[] listeTemp = convertirDoubleVersBin(dListe[i]);
			for(int j = 0; j < listeTemp.length; j++)
			{
				ipBin[binPosition] = listeTemp[j];
				binPosition++;
			}
		}
		return ipBin;
	}
	
	//Convertir un chiffre double vers un binaire(Utiliser dans convertStringToBin)
	private int[] convertirDoubleVersBin(double chiffre)
	{
		//Calculer le binaire en utilisant la divsion succesive
		double[] bitListe = new double[8];	//Tableau pour stocke les bits
		for(int i = bitListe.length - 1; i >= 0; i--)	//Boucle de division succesive
		{
			if(chiffre % 2 == 0)	//Si la division du chiffre par 2 resulte a un chiffre x.0,
			{						//le binaire a cette position est de 0
				bitListe[i] = 0.0;
			}
			if(chiffre % 2 != 0)	//Si la division du chiffre par 2 ne resulte pas a un chiffre x.0
			{						//le binaire a cette position est de 1
				bitListe[i] = 1.0;
			}
			chiffre = chiffre / 2;	//Divise le chiffre en 2
			chiffre = Math.floor(chiffre);	//Trouve le chiffre le plus petit pret.
			if(chiffre == 0)
			{
				break;		//Si le chiffre est egal a 0, la division succesive est fini.
			}
		}
		
		//Convertir la forme de double vers int
		int[] intListe = new int[8];
		for(int i = 0; i < intListe.length; i++)
		{
			intListe[i] = (int) bitListe[i];
		}
		
		return intListe;
	}
	
	//Convertir un tableau[32] en binaire vers IP decimal xxx.xxx.xxx.xxx
	public String convertirBinVersString(int[] binaire)
	{
		String texte = "";
		int nbrOctet = 0;
		for(int i = 0; i < binaire.length; i += 8)	//Un octet etant 8 bits, on fait des bons de 8
		{
			int octet = 0;
			for(int j = 0; j < 8; j++)	//On se promene dans un range de 8 bits(octets)
			{
				if(binaire[i + j] == 1)		//Si la position de l'octet + la position dans l'octet == 1
				{
					octet += Math.pow(2, 7 - j);	//Calculer la valeur en decimal de cette position(dans l'octet).
				}
			}
			texte += octet;		//Ajouter l'octet dans le string
			nbrOctet++;
			if(nbrOctet < 4)	//A la derniere octet ne pas ajouter de point
			{
				texte += ".";
			}
		}
		return texte;
	}
	
	//Afficher un tableau[32] en binaire sous la forme xxxxxxxx.xxxxxxxx.xxxxxxxx.xxxxxxxx
	public String afficherBin(int[] binaire)
	{
		String texte = "";
		for(int i = 0; i < binaire.length; i++)
		{
			if(i != 0 && i % 8 == 0)
			{
				texte += ".";
			}
			texte += binaire[i];
		}
		return texte;
	}

	//Getter
	public String getIpString()
	{
		return this.ipString;
	}
	
	public int[] getIpBin()
	{
		return this.ipBin;
	}
	
	public int[] getReseauBin()
	{
		return this.reseauBin;
	}
	
	public String getReseauString()
	{
		return this.reseauString;
	}
}
