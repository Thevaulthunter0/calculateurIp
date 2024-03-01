package calculateurIP;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;
import java.lang.String;
import java.lang.Math;

import static java.lang.String.valueOf;

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
						System.out.println("Votre adresse doit avoir 4 octets en entiers, séparés par un point.");
						continue;

						//si ip est dans le bon format, vérifie si les chiffres sont entre 0-255
					} else {
						String[] ipSplit = ip.split("\\.");			//obtient un array de 4 octets en chaîne de caractères
						int[] ipSplitInt = new int[ipSplit.length];			//obtient un array de 4 octets en entier
						for (int i=0; i< ipSplit.length; i++) {
							ipSplitInt[i] = Integer.parseInt(ipSplit[i]);
						}

						for (int i : ipSplitInt) {
							if (i < 0 || i > 255) {							//vérifier que chaque octet est entre 0 et 255
								System.out.println("Chaque octet doit être entre 0 et 255.");
								break;
							} else  {
								choixValide = true;
							}
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
			
			//Demande si l'utilisateur veut creer des sous-reseau
			choixValide = false;
			String reponse = "";
			while(!choixValide)
			{
				System.out.println("Voulez-vous faire des sous-reseau?(o-oui, n-non)");
				try {
					reponse = scan.next();
					if(reponse.equals("o") && reponse.equals("n"))
					{
						System.out.println("Erreur, repondre avec o(oui) ou n(non).");
					}
					else choixValide = true;
				}catch(Exception e)
				{
					System.out.println("Erreur: " + e);
					scan.next();
				}
			}
			if(reponse.equals("o"))
			{
				sousReseauIPv4GUI(ipv4, msq);
			}
			if(reponse.equals("n"))
			{
				break;
			}
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
		
		//Demande du nombre d'hotes par sous-reseau
		choixValide = false;
		int[] nbrHotesDemande = new int[nbrSousReseau];
		System.out.println("ne pas inclure l'adresse et diffusion");
		for(int i = 0; i < nbrSousReseau; i++)
		{
			while(!choixValide)
			{
				System.out.println("Qu'elle est le nombre d'hote pour le sous reseau " + i + "?");
				try {
					nbrHotesDemande[i]= scan.nextInt() + 2;
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
		
		nbrHotesDemande = triBulleInverse(nbrHotesDemande);		//Ordonne pour le calcul du VSML
		
		int[] nbrHotes = convertirNombreHote(nbrHotesDemande);	//Convertir le nombre hote demande vers le nombre hote reel
		SousReseauxIPv4 sousReseau = valideSousReseauIPv4(masque, ip.getReseauBin(), nbrHotes);	//Valider avant de creer l'objet
		int[][] lesSousReseau = sousReseau.trouverSousReseaux();	//Matrice representant binairement chaque adresse de chaque sous-reseau
		for(int i = 0; i < lesSousReseau.length; i++)
		{
			System.out.println(nbrHotesDemande[i] + " : " + ip.convertirBinVersString(lesSousReseau[i]));	//Afficher chaque sous-reseau
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
//					ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334"; //pour testing
					ip = scan.next();

					if (!ip.matches("^(?:[0-9a-fA-F$]{1,4}:){7}[0-9a-fA-F$]{1,4}")) {
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
			sousReseauIPv6GUI(ipv6);

		} while(choix != 0);
	}

	static void sousReseauIPv6GUI(IPv6 ipv6)
	{
		boolean choixValide = false;
		int nbrSousReseau = 1;
		//Nombre de sous reseau
		while(!choixValide)
		{
			System.out.println("Combien de sous reseaux voulez-vous faire?");
//			try {
				nbrSousReseau = scan.nextInt();

				if(nbrSousReseau < 1) {
					System.out.println("Erreur, il vous faut au moins 2 sous reseaux.");
				}

				else {

					BigInteger nbSousReseauDispo = ipv6.nbrSousreseauxDisponible();
					BigInteger nbSousReseauDispoBi = new BigInteger(String.valueOf(nbrSousReseau));
					if (ipv6.nbrSousreseauxDisponible().compareTo(nbSousReseauDispoBi)==-1) {//si client demande plus que nbr de sous-réseaux disponible
						System.out.println("Il y a seulement " + ipv6.nbrSousreseauxDisponibleFormatted(nbSousReseauDispo) + " disponibles");
						continue;
					} else {
						System.out.println("I AM HERE");
						ipv6.afficherSousReseauxIPv6(nbrSousReseau);
						choixValide = true;
					}
				}

//			} catch(Exception e)
//			{
//				System.out.println("Erreur," + e);
//				scan.next();
//			}
		}

	}


	//Valide la demande du nombre d'hotes avec le masque et creer un objet SousReseauxIPv4.
	private static SousReseauxIPv4 valideSousReseauIPv4(Masque masque,int[] reseauBin, int[] nbrHotes)
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
	
	//Permet de trouver le nombre d'hote qui sera reellement utiliser dans les sous-reseaux
	//en utilisant l'arrondissement a la puissance 2 ex:200 hotes demandes = 256 hotes reellements utilises
	private static int[] convertirNombreHote(int[] nbrHoteDemande)
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
	
	//Permet de trier du plus grand au plus petit les inputs de l'utilisateurs avec la methode du tri par bulle
	private static int[] triBulleInverse(int[] nbrHotesDemande)
	{
		int n = nbrHotesDemande.length;
		for(int i = 0; i < n - 1; i++)
		{
			for(int j = 0; j < n - 1 - i; j++)
			{
				if(nbrHotesDemande[j] < nbrHotesDemande[j+1])		//Si la valeur de gauche est plus petite que la valeur de droite
				{
					int temporaire = nbrHotesDemande[j];			//Garder en memoire temporaire la valeur
					nbrHotesDemande[j] = nbrHotesDemande[j + 1];	//Echanger la position des deux valeurs
					nbrHotesDemande[j + 1] = temporaire;			//Donner la valeur initiale de gauche a la position de droite
				}
			}

		}
		return nbrHotesDemande;
	}
}
