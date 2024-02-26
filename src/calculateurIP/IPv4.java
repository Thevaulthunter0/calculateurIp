package calculateurIP;

import java.lang.String;
import java.lang.Math;

public class IPv4 {
	
	//Initialisation
	private Masque masque;
	private String ipString;					// l'adresse en string entree pas l'utilisateur xxx.xxx.xxx.xxx
	private int[] ipBin = new int[32];			// les 32 bits de l'adresse entree par l'utilisateur
	private String reseauString;				// l'adresse reseau en string xxx.xxx.xxx.xxx
	private int[] reseauBin = new int[32];		// les 32 bits de l'adresse reseau
	private String[] strListe = new String[4];	// les 4 octets sous forme string
	
	//Constructeur
	public IPv4(String ipString, Masque masque)
	{
		this.ipString = ipString;
		this.masque = masque;
		this.ipBin = convertirStringVersBin();
		this.reseauBin = operationAND(this.ipBin, this.masque.getMasqueBin());
		this.reseauString = convertirBinVersString(this.reseauBin);
	}
	
	//Afficher les informations du calculateur
	public void afficherInformation()
	{
		System.out.println("Nombre d'adresse disponible : " + masque.nbrHoteDisponible()
		+ "\nNombre d'adresse utilisable : " + masque.nbrHoteUtilisable()
		+ "\nAdresse du masque : " + convertirBinVersString(masque.getMasqueBin())
		+ "\nAdresse du reseau : " + this.reseauString
		+ "\nPremiere adresse : " + convertirBinVersString(premiereAdresse())
		+ "\nDerniere adresse : " + convertirBinVersString(derniereAdresse())
		+ "\nAdresse diffusion : " + convertirBinVersString(adresseDiffusion())
		+ "\nAdresse local? : " + estLocal()
		+ "\nAdresse de bouclage? : " + estBouclage()
		+ "\nAdresse est linkLocal? : " + estLinkLocal()); 
	}
	
	//Donne l'adresse de la premiere hote disponible
	//La premiere adresse du reseau, est l'adresse reseau + 0...0001
	public int[] premiereAdresse()
	{
		int[] reponse = new int[32];	//Tableau a retourner
		for(int i = 0; i < this.reseauBin.length - 1; i++)
		{
			reponse[i] = reseauBin[i];
			if(i == 31)
			{
				reponse[i] = 1;		//Ajoute un 1 a la derniere position
			}
		}
		return reponse;
	}
	
	//Donne l'adresse de la derniere hote disponible
	//La derniere adresse est l'adresse reseau + des 1 a partir du masque + 0 a la fin
	public int[] derniereAdresse()	
	{
		int[] reponse = new int[32];	//Tableau a retourner
		int x = this.masque.getIntMasque();		//Position du masque
		for(int i = 0; i < this.reseauBin.length - 1; i++)	
		{
			if(i >= x && i != 32)	//Temps que i est plus grand que la position du masque mais pas la derniere position
			{						//mettre des 1.
				reponse[i] = 1;
			}
			else {					//Sinon copier l'adresse reseau
				reponse[i] = this.reseauBin[i];
			}
		}
		return reponse;
	}
	
	//Donne l'adresse de diffusion du reseau
	//L'adresse de diffusion est l'adresse reseau + des 1 a partir du masque
	public int[] adresseDiffusion()
	{
		int[] reponse = new int[32];	//Tableau a retourner
		int x = this.masque.getIntMasque();		//Position du masque
		for(int i = 0; i < this.reseauBin.length; i++)
		{
			if(i >= x)		//Temps que i est plus grand que la position du masque mettre des 1
			{
				reponse[i] = 1;
			}
			else {			//Sinon copier l'adresse reseau
				reponse[i] = this.reseauBin[i];
			}
		}
		return reponse;
	}
	
	//Determine si l'adresse ip est un adresse locale ou publique
	public boolean estLocal()
	{
		//10.0.0.0
		if(this.strListe[0].equals("10"))
		{	
			return true;
		}
		//172.16.0.0
		else if(this.strListe[0].equals("172") && this.strListe[1].equals("16"))
		{
			return true;
		}
		//192.168.0.0
		else if(this.strListe[0].equals("192") && this.strListe[1].equals("168"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	//Determine si l'adresse est un adresse de bouclage
	public boolean estBouclage()
	{
		//127.x.x.x
		if(this.strListe[0].equals("127"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//Determine si l'adresse est une adresse Link-local(APIPA)
	public boolean estLinkLocal()
	{
		//169.254.x.x
		if(this.strListe[0].equals("169") && this.strListe[1].equals("254"))
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}
	
	//Faire une operation AND entre deux tableau[32]. Utiliser dans le constructeur
	//pour avoir l'ip reseau en binaire.
	private int[] operationAND(int[] bin1, int[] bin2)
	{
		int[] reponse = new int[32];	//Tableau a retourner
		for(int i = 0; i < reponse.length; i++)		//Pour chaque position du tableau de reponse
		{
			if(bin1[i] == 1 && bin2[i] == 1)		// 1 AND 1 = 1 
			{										// Toute autre combinaison = 0
				reponse[i] = 1;						
			}
		}
		return reponse;
	}
	
	//Convertir IP(ipString) xxx.xxx.xxx.xxx vers un tableau[32] binaire.
	private int[] convertirStringVersBin()
	{
		double[] dListe = new double[4];	//Tableau de double pour separer les 4 octets
		
		//Transforme le string en tableau de double
		this.strListe = this.ipString.split("\\.");		//Spliter dans le tableau de string les 4 octets avec les '.'
		for(int i = 0; i < dListe.length; i++)
		{
			dListe[i] = Double.parseDouble(strListe[i]);	//Transformer les 4 octets en double pour pouvoir les
		}													//utiliser dans des calculs.
		
		//Convertir chaque double en chiffre binaire
		//et construit le chiffre binaire total.
		int[] ipBin = new int[32];	//Tableau pour retourner
		int binPosition = 0;	//Indique la position dans notre tableau binaire
		for(int i = 0; i < dListe.length; i++)		//Pour chaque octet
		{
			int[] listeTemp = convertirDoubleVersBin(dListe[i]);	//Creer un tableau temporaire pour accueilir 8 chiffres binaires
			for(int j = 0; j < listeTemp.length; j++)	//Pour chaque byte dans le tableau temporaire
			{
				ipBin[binPosition] = listeTemp[j];	//Inserer dans notre tableau binaire les bytes du tableau temporaire
				binPosition++;
			}
		}
		return ipBin;
	}
	
	//Convertir un octet double vers un binaire(Utiliser dans convertStringVersBin)
	private int[] convertirDoubleVersBin(double octet)
	{
		//Calculer le binaire d'un octet en utilisant la divsion succesive
		double[] bitListe = new double[8];	//Tableau pour stocke les bits
		for(int i = bitListe.length - 1; i >= 0; i--)	//Boucle de division succesive
		{
			if(octet % 2 == 0)	//Si la division du chiffre par 2 resulte a un chiffre x.0,
			{						//le binaire a cette position est de 0
				bitListe[i] = 0.0;
			}
			if(octet % 2 != 0)	//Si la division du chiffre par 2 ne resulte pas a un chiffre x.0
			{						//le binaire a cette position est de 1
				bitListe[i] = 1.0;
			}
			octet = octet / 2;	//Divise le chiffre en 2
			octet = Math.floor(octet);	//Trouve le chiffre le plus petit pret.
			if(octet == 0)
			{
				break;		//Si le chiffre est egal a 0, la division succesive est fini.
			}
		}
		
		//Convertir la forme de double vers int pour que l'affichage soit propre
		int[] intListe = new int[8];	//Tableau qui sera retourner
		for(int i = 0; i < intListe.length; i++)	//Boucle dans le tableau de int
		{
			intListe[i] = (int) bitListe[i];	//Cast de double vers int
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
		for(int i = 0; i < binaire.length; i++)	//Parcours le tableau[32]
		{
			if(i != 0 && i % 8 == 0)	//A chaque 8 position on met un '.'
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
