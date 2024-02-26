package calculateurIP;

import java.lang.String;
import java.lang.Math;

public class SousReseauxIPv4 {
	
	//Initialisation
	private int[] reseauBin = new int[32];
	private int[] nbrHotes;
	
	//Constructeur
	public SousReseauxIPv4(int[] reseauBin, int[] nbrHotes)
	{
		this.reseauBin = reseauBin;
		this.nbrHotes = nbrHotes;
	}
	
	//Methode permettant de trouver les sous Reseaux et les stocker dans un tableau[nbrSousReseau][32].
	public int[][] trouverSousReseaux()
	{
		int[][] adresseSousReseau = new int[nbrHotes.length][32];	//Tableau utiliser pour etre retourner contenant les sous reseaux 
		adresseSousReseau[0] = this.reseauBin;		//Le premier plus gros sous reseau utilise l'adresse de base
		for(int i = 1; i < nbrHotes.length; i++)	//Pour chaque sous reseau demande
		{
			//Additionne l'ip du sous reseau precedent au tableau du masque(construireTab) du sous reseau precedent
			adresseSousReseau[i] = additionBin(adresseSousReseau[i - 1], construireTab(trouverMasque(this.nbrHotes[i - 1])));
		}
		return adresseSousReseau;
	}
	
	//Trouver le numero de masque a partir d'un nombre d'hotes
	public int trouverMasque(int nbrHote)
	{																	//Utilise le calcul inverse pour trouver le
		return 32 - (int)(Math.ceil(Math.log(nbrHote)/Math.log(2)));	//nombre d'hote a partir d'un masque.
	}
	
	//Construire le tableau[32] du masque pour etre additionner au sous reseau d'avant
	//ex:00000000.00000000.00000001.000000000 pour /25, indique la derniere position avant le masque(masque - 1)
	public int[] construireTab(int intMasque)
	{
		int[] reponse = new int[32];	//Tableau utilise pour etre retourner
		for(int i = 0; i < 32 ; i++)	//On parcour le tableau[32]
		{
			if(i == intMasque - 1)		//indique la derniere position avant le masque
			{							
				reponse[i] = 1;			
			}
			else
			{
				reponse[i] = 0;
			}
		}
		return reponse;
	}
	
	//Methode permettant d'effectuer une addition binaire entre deux tableau[] 
	public int[] additionBin(int[] bin1, int[] bin2)
	{
		int retenue = 0;
		int[] reponse = new int[32];		//Tableau utilise pour etre retourne
		
		for(int i = 31; i >= 0; i--)		//On effectue l'addition sur les 32 positions
		{
			int total = bin1[i] + bin2[i] + retenue;	//Le total est l'addition des deux bits plus la retenue du calcul d'avant

	        if (total == 2) 	//Si le total est de deux, il y a une retenue et la reponse est 0
	        {
	            reponse[i] = 0;
	            retenue = 1;
	        }

	        else if (total == 3) 	//Si le total est de trois, il y a une retenue et la reponse est 1
	        {
	            reponse[i] = 1;
	            retenue = 1;
	        }

	        else 				//Si la reponse est de 1 ou 0, il n'y a pas de retenue.
	        {
	            reponse[i] = total;
	            retenue = 0;
	        }
		}
		return reponse;
	}
}
