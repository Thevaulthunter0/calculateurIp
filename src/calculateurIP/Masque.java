/**
 * Titre: Masque.java;
 * Description: Classe permettant de creer et utiliser les methodes necessaires pour trouver les informations d'un masque d'IPv4.
 */
package calculateurIP;

import java.lang.String;
import java.lang.Math;

public class Masque {
	
	//Initialisation
	private int intMasque;					//Chiffre du masque
	private String strMasque;				//Representation du masque /masque
	private int[] masqueBin = new int[32];	//Adresse du masque
	
	//Constructeur
	public Masque(int masque)
	{
		this.intMasque = masque;
		this.strMasque = "/" + masque;
		masqueBin = convertMasqueToBin();
	}
	
	//Convertir du numero du masque vers un binaire
	//Entree: Rien
	//Sortie: tableau de 1 et 0 representant le masque
	private int[] convertMasqueToBin()
	{	
		int[] masqueBin = new int[32];
		for(int i = 0; i < masqueBin.length; i++)
		{	
			if(i > this.intMasque - 1)		//A partir du numero de masque mettre des 0
			{
				masqueBin[i] = 0;
				continue;
			}
			masqueBin[i] = 1;			//Avant la position du numero de masque mettre des 1
		}
		
		return masqueBin;
	}
	
	//Afficher le masque binaire sous la forme xxxxxxxx.xxxxxxxx.xxxxxxxx.xxxxxxxx
	//Entree: Rien
	//Sortie: string du tableau de 1 et 0 representant le masque
	public String afficherBin()
	{
		String txt = "";
		for(int i = 0; i < this.masqueBin.length; i++)
		{
			if(i != 0 && i % 8 == 0)	//Ajouter un . a chaque 8
			{
				txt += ".";
			}
			txt += this.masqueBin[i];
		}
		return txt;
	}
	
	//Calculer le nombre d'hote disponible
	//Entree: Rien
	//Sortie: Nombre d'hote disponible
	public int nbrHoteDisponible()
	{
		return (int) Math.pow(2,(32 - intMasque));	//= 2^(32 - masque)
	}
	
	//Calculer le nombre d'hote utilisable
	//Entree: Rien
	//Sortie: Nombre d'hote utilisable
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
