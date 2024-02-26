package calculateurIP;

import java.util.Scanner;
import java.lang.String;
import calculateurIP.Masque;

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

					if (!ip.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}")) {  //ip doit avoir 3 places de nombres suivi par ':' et un dernier groupe de 3 places de nombres
						continue;

						//si ip est dans le bon format, vérifie si les chiffres sont entre 0-255
					} else {
						String[] ipSplit = ip.split("\\.");
						int[] ipSplitInt = new int[ipSplit.length];
						for (int i=0; i< ipSplit.length; i++) {
							ipSplitInt[i] = Integer.parseInt(ipSplit[i]);
						}

						for (int i=0; i< ipSplitInt.length; i++) {
							if (ipSplitInt[i] < 0 || ipSplitInt[i] > 255) {
								continue;
							} else  choixValide = true;
						}
					}

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
				System.out.println("Entrer votre masque.");
				try {
					masque = scan.nextInt();
					if(masque < 1 || masque > 31)
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
			sousReseauIPv4GUI(ipv4, msq);
			
		} while(choix != 0);
	}
	
	static void sousReseauIPv4GUI(IPv4 ip, Masque masque)
	{
		boolean choixValide = false;
		int nbrSousReseau = 1;
		//Nombre de sous reseau
		while(!choixValide)
		{
			System.out.println("Combien de sous reseaux voulez-vous faire?");
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
				System.out.println("Qu'elle est le nombre d'hote pour le sous reseau " + i + "?");
				try {
					nbrHotesDemande[i]= scan.nextInt();
					if(nbrHotesDemande[i] < 1)
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
		// ordonne(plus grand au plus petit) 
		//
		int[] nbrHotes = convertirNombreHote(nbrHotesDemande);
		SousReseauxIPv4 sousReseau = valideSousReseauIPv4(masque, ip.getReseauBin(), nbrHotes);
		int[][] lesSousReseau = sousReseau.trouverSousReseaux();
		for(int i = 0; i < lesSousReseau.length; i++)
		{
			System.out.println(nbrHotesDemande[i] + " : " + ip.convertirBinVersString(lesSousReseau[i]));
		}
	}
	
	static void IPv6GUI()
	{
		//Initialisation
		int choix = 0;
		int prefix = 64;
		String ip = "";
		boolean choixValide = false;

		//Debut
		do {
			//Demande de IP
			while(!choixValide)
			{
				System.out.println("Entrer votre addresse IPv6(xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx)");
				try {
					ip = "2001:2002:0fed:ffff:0000:0000:0000:0001"; //pour testing
//					ip = scan.next();

					if (!ip.matches("^(?:[0-9a-fA-F$]{1,4}:){7}[0-9a-fA-F$]{1,4}")) {  //ip doit avoir
						continue;
					} else choixValide = true;

				}catch(Exception e)
				{
					System.out.println("Erreur: " + e);
					scan.next();
				}
			}

			//Demande du préfixe
			choixValide = false;
			while(!choixValide)
			{
				System.out.println("Entrer votre préfixe. Un chiffre entre 1 et 128.");
				try {
					prefix = scan.nextInt();
					if(prefix < 1 || prefix > 128)
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
			IPv6 ipv6 = new IPv6(ip, prefix);
			ipv6.afficherInformation();

			//
			//Fonction de sous-reseau
			//
			//sousReseauIPv4GUI();

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
