package calculateurIP;

import java.util.Scanner;

public class GUI {
	
	static Scanner scan = new Scanner(System.in);
	
	static void debutGUI()
	{
		//Initialisation
		int choix = 0;
		boolean choixValide = false;
		
		//Debut GUI
		do {
			while(!choixValide)
			{
				System.out.println("***Calculateur IP***\n"
						+ "Qu'elle est la version de l'IP?\n"
						+ "1-IPv4\n"
						+ "2-IPv6\n"
						+ "0-Quitter");
				try {
					choix = scan.nextInt();
					if(choix < 0 || choix > 2)
					{
						System.out.println("Erreur, selectionner un choix valide.");
					}
					else choixValide = true;
				} catch (Exception e)
				{
					System.out.println("Erreur, selectionner un choix valide.");
					scan.next();
				}
			}
		switch(choix)
		{
		//Adresse IPv4
		case 1:
			IPv4GUI();
			choixValide = false;
			break;
		//Adresse IPv6
		case 2:
			IPv6GUI();
			choixValide = false;
			break;
		//Quitter l'application
		case 0 :
			break;
		default:
			System.out.println("Comment es-tu arrive ici?");
			choixValide = false;
			break;
		}
		} while(choix != 0);
		System.out.println("Application ferme");
	}
	
	static void IPv4GUI()
	{
		//Initialisation
		int choix = 0;
		String ip = "";
		int masque = 0;
		boolean choixValide = false;
		
		//Debut
		do {
			//Demande de IP
			while(!choixValide)
			{
				System.out.println("Entrer votre addresse IPv4(xxx.xxx.xxx.xxx)");
				try {
					ip = scan.next();
					//
					//Ajouter une verification avec un regex pour que l'ip est la bonne forme
					//
					choixValide = true;
				}catch(Exception e)
				{
					System.out.println("Erreur: " + e);
					scan.next();
				}
			}
			
			//Demande du masque
			choixValide = false;
			while(!choixValide)
			{
				System.out.println("Entrer votre masque. Un chiffre entre 23 et 30.");
				try {
					masque = scan.nextInt();
					if(masque < 23 || masque > 30)
					{
						System.out.println("Erreur, le masque n'est pas valide.");
					}
					else choixValide = true;
				}catch(Exception e)
				{
					System.out.println("Erreur: " + e);
					scan.next();
				}
			}
			Masque msq = new Masque(masque);
			IPv4 ipv4 = new IPv4(ip, msq);
			ipv4.afficherInformation();
			
			//
			//Fonction de sous-reseau
			//
			//sousReseauIPv4GUI();
			
		} while(choix != 0);
	}
	
	static void sousReseauIPv4GUI()
	{
		boolean choixValide = false;
		int nbrSousReseau = 1;
		//Nombre de sous reseau
		while(!choixValide)
		{
			System.out.println("Combien de sous reseau voulez-vous faire?");
			try {
				nbrSousReseau = scan.nextInt();
				if(nbrSousReseau < 1)
				{
					System.out.println("Erreur, il vous faut au moins 2 sous reseaux.");
				}
				else choixValide = true;
			} catch(Exception e)
			{
				System.out.println("Erreur," + e);
				scan.next();
			}
		}
		choixValide = false;
		int[] nbrHotesDemande = new int[nbrSousReseau];
		for(int i = 0; i < nbrSousReseau; i++)
		{
			while(!choixValide)
			{
				System.out.println("Qu'elle est le nombre d'hote pour le sous reseau " + i);
				try {
					nbrHotesDemande[i]= scan.nextInt();
					if(nbrHotesDemande[i] >= 1)
					{
						System.out.println("Il doit avoir au moins 1 hote dans un sous reseau.");
					}
					else choixValide = true;
				} catch(Exception e)
				{
					System.out.println("Erreur," + e);
					scan.next();
				}
			}
			choixValide = false;
		}
		//
		// ordonne(plus grand au plus petit) ,valider, convertir, creer objet sousReseauIPv4, afficher 
		//
	}
	
	static void IPv6GUI()
	{
		//Initialisation
		int choix = 0;
		boolean choixValide = false;
		do {
			while(!choixValide)
			{
				
			}
		} while(choix != 0);
	}
	
	//Valide la demande du nombre d'hotes avec le masque et creer un objet SousReseauxIPv4.
	public static SousReseauxIPv4 valideSousReseauIPv4(Masque masque,int[] reseauBin, int[] nbrHotes)
	{
		int nbrHoteDemande = 0;
		for(int i = 0; i < nbrHotes.length; i++)			//Calculer le nombre total hote
		{													//demander par l'utilisateur
			nbrHoteDemande += nbrHotes[i];		
		}
		
		int nbrHoteDisponible = masque.nbrHoteDisponible();	//A partir du masque initial,
		if(nbrHoteDisponible >= nbrHoteDemande)				//on peut determiner si la demande
		{													//est trop haute.
			SousReseauxIPv4 sousReseau = new SousReseauxIPv4(reseauBin,nbrHotes);
			System.out.println("Nombre d'hote demande valide.");
			return sousReseau;
		}
		else
		{
			System.out.println("Vous demander trop d'hote pour le nombre hote disponible avec ce masque.");
			return null;
		}
	}
	
	//Permet de trouver le nombre d'hote qui sera rellement utiliser dans les sous-reseaux
	//en utilisant l'arrondissement a la puissance 2
	public static int[] convertirNombreHote(int[] nbrHoteDemande)
	{
		int[] nbrHote = new int[nbrHoteDemande.length];
		for(int i = 0; i < nbrHoteDemande.length; i++)
		{
			//Trouver la logarithme en base 2 du chiffre voulu. (Faire l'opération inverse d'un exposant)
			double log = Math.log(nbrHoteDemande[i])/Math.log(2);
			//Arrondir au plus grand pour trouver le chiffre supperieur
			int hoteAvantPow = (int) Math.ceil(log);
			//2 a l'exposant "chiffre arrondit"
			nbrHote[i] = (int) Math.pow(2,hoteAvantPow);
		}
		return nbrHote;
	}
}
