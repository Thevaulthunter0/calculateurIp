/**
 * Titre: GUI.java;
 * Description: Classe definissant les methodes necessaires pour l'affichage des menus.
 */
package calculateurIP;

import java.math.BigInteger;
import java.util.Scanner;
import java.lang.String;
import java.lang.Math;

public class GUI {
	
	static Scanner scan = new Scanner(System.in);
	
	//Premiere methode de menu appele dans le main. Elle appelle les autres methodes de menu.
	//Entree:Rien
	//Sortie:Rien
	public static void debutGUI()
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
	
	//Methode de menu pour la section IPv4. Elle est appelee dans la methode debutGUI().
	//Entree:Rien
	//Sortie:Rien
	static void IPv4GUI()
	{
		//Initialisation
		int choix = 0;
		String ip = "";
		int masque = 0;
		boolean choixValide = false;
		
		//Debut
		do {
			//Demande d'IP
			while(!choixValide)
			{
				System.out.println("Entrer votre adresse IPv4(xxx.xxx.xxx.xxx)");
				try {
					//ip = "172.16.0.0"	
					ip = scan.next();

					if (!ip.matches("^(?:[0-9]{3}\\.){3}[0-9]{3}")) {  //ip doit avoir 3 places de nombres suivi par ':' et un dernier groupe de 3 places de nombres
						System.out.println("Votre adresse doit avoir 4 octets en entiers, separes par un point.");
						continue;

					//si l'ip a le bon format, vérifie si les chiffres sont entre 0-255
					} else {
						String[] ipSplit = ip.split("\\.");			//obtient un array de 4 octets en chaîne de caractères
						int[] ipSplitInt = new int[ipSplit.length];			//Creer un array de 4 octets en entier
						for (int i=0; i< ipSplit.length; i++) {
							ipSplitInt[i] = Integer.parseInt(ipSplit[i]);
						}

						for (int i : ipSplitInt) {
							if (i < 0 || i > 255) {							//vérifier que chaque octet est entre 0 et 255
								System.out.println("Chaque octet doit etre entre 0 et 255.");
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
					if(masque < 1 || masque > 31)	//Verification si le masque est entre 1 et 31
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
			Masque msq = new Masque(masque);	//Creation de l'objet masque
			IPv4 ipv4 = new IPv4(ip, msq);		//Creation de l'objet IPv4
			ipv4.afficherInformation();			//Afficher les informations demandes
			
			//Demande si l'utilisateur veut creer des sous-reseaux
			choixValide = false;
			String reponse = "";
			while(!choixValide)
			{
				System.out.println("Voulez-vous faire des sous-reseaux?(o-oui, n-non)");
				try {
					reponse = scan.next();
					if(reponse.equals("o") && reponse.equals("n"))		//Verification que l'utilisateur entre o ou n.
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
				sousReseauIPv4GUI(ipv4, msq);		//Appel de la methode permettant de creer des sous-reseaux
			}
			if(reponse.equals("n"))
			{
				break;		//Sinon retourne menu principal
			}
		} while(choix != 0);
	}
	
	//Methode de menu pour la sous-section IPv4 de creation de sous-reseau. Elle est appelee dans la methode IPv4GUI()
	//Entree:IPv4 et Masque
	//Sortie:Rien
	static void sousReseauIPv4GUI(IPv4 ip, Masque masque) {
		boolean choixValide = false;
		int nbrSousReseau = 1;

		//Nombre de sous-reseau
		while (!choixValide) {
			System.out.println("Combien de sous-reseaux voulez-vous faire?");
			try {
				nbrSousReseau = scan.nextInt();
				if (nbrSousReseau < 1)            //Verification que l'utilisateur demande plus qu'un sous-reseau
				{
					System.out.println("Erreur, il vous faut au moins 2 sous-reseaux.");
				} else choixValide = true;
			} catch (Exception e) {
				System.out.println("Erreur," + e);
				scan.next();
			}
		}

		//Demande du nombre d'hotes par sous-reseau
		choixValide = false;
		int[] nbrHotesDemande = new int[nbrSousReseau];
		System.out.println("ne pas inclure l'adresse reseau et l'adresse de diffusion");
		for (int i = 0; i < nbrSousReseau; i++) {
			while (!choixValide) {
				System.out.println("Qu'elle est le nombre d'hote pour le sous reseau " + (i + 1) + " ?");
				try {
					nbrHotesDemande[i] = scan.nextInt() + 2;
					if (nbrHotesDemande[i] < 1) {
						System.out.println("Il doit avoir au moins 1 hote dans un sous reseau.");
					} else choixValide = true;
				} catch (Exception e) {
					System.out.println("Erreur," + e);
					scan.next();
				}
			}
			choixValide = false;
		}

		nbrHotesDemande = triBulleInverse(nbrHotesDemande);		//Ordonne pour le calcul du VSML
		
		int[] nbrHotes = convertirNombreHote(nbrHotesDemande);	//Convertir le nombre hote demande vers le nombre hote reel
		SousReseauxIPv4 sousReseau = valideSousReseauIPv4(masque, ip.getReseauBin(), nbrHotes);	//Valider et creer l'objet sousReseauIPv4
		if(sousReseau != null)	//Si le nombre d'hote demande n'est pas valide, l'objet n'est pas creer donc retour au menu principal.
		{
			int[][] lesSousReseau = sousReseau.trouverSousReseaux();	//Matrice representant binairement chaque adresse de chaque sous-reseau
			for(int i = 0; i < lesSousReseau.length; i++)	//Pour chaque sous-reseau dans la matrice
			{
				System.out.println(nbrHotesDemande[i] - 2 + " : " + ip.convertirBinVersString(lesSousReseau[i]) + "\\" + sousReseau.trouverMasque(nbrHotes[i]));	//Afficher information
			}
		}
	}
	
	//Methode de menu pour la section IPv6. Elle est appelee dans la methode debutGUI().
	//Entree:Rien
	//Sortie:Rien
	static void IPv6GUI()
	{
		//Initialisation
		int choix = 0;
		int prefix = 64;
		String ip = "";
		boolean choixValide = false;

		//Debut
		do {
			//Demande de l'IP
			while(!choixValide)
			{
				System.out.println("Entrer votre adresse IPv6(xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx)");
				try {
					//ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334"; //pour testing
					ip = scan.next();

					if (!ip.matches("^(?:[0-9a-fA-F$]{4}:){7}[0-9a-fA-F$]{4}")) {
						System.out.println("Votre adresse doit avoir 8 hextets, separes par \':\'");
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
				System.out.println("Entrer votre prefixe. Un chiffre entre 1 et 64.");
				try {
					prefix = scan.nextInt();
					if(prefix < 1 || prefix > 64)
					{
						System.out.println("Erreur, la longueur du prefixe n'est pas valide.");
					}
					else choixValide = true;
				}catch(Exception e)
				{
					System.out.println("Erreur: " + e);
					scan.next();
				}
			}
			IPv6 ipv6 = new IPv6(ip, prefix);	//Creation de l'objet IPv6
			ipv6.afficherInformation();			//Afficher les informations
			sousReseauIPv6GUI(ipv6);			//Methode pour calculer les sous-reseaux

		} while(choix != 0);
	}

	//Methode de menu pour la sous-section IPv6 de creation de sous-reseau. Elle est appelee dans la methode IPv6GUI()
	//Entree:IPv4 et Masque
	//Sortie:Rien
	static void sousReseauIPv6GUI(IPv6 ipv6)
	{
		boolean choixValide = false;
		int nbrSousReseau = 1;
		//Nombre de sous reseau
		while(!choixValide && ipv6.getPrefix()!=64)
		{
			System.out.println("Combien de sous-reseaux voulez-vous faire?");
				nbrSousReseau = scan.nextInt();

				try {
					if (nbrSousReseau < 1) {
						System.out.println("Erreur, il vous faut au moins 2 sous-reseaux.");
					} else {
						BigInteger nbSousReseauDispo = ipv6.nbrSousreseauxDisponible();
						BigInteger nbSousReseauDispoBi = new BigInteger(String.valueOf(nbrSousReseau));
						if (ipv6.nbrSousreseauxDisponible().compareTo(nbSousReseauDispoBi) == -1) {//si client demande plus que nbr de sous-réseaux disponible
							System.out.println("Il y a seulement " + StringIPv6.nbrSousreseauxDisponibleFormatted(nbSousReseauDispo) + ((nbSousReseauDispo.compareTo(new BigInteger("1")) == 0) ? " disponibles" : "disponible"));
							continue;
						} else {
							String[] sousReseauxArray = ipv6.obtientSousReseauxIPv6(nbrSousReseau);
							if (sousReseauxArray.length != 0) {
								for (int i = 0; i < sousReseauxArray.length; i++) {
									System.out.println("\n" + (i + 1) + "e sous-réseau (longue) : " + sousReseauxArray[i] + "\n" + (i + 1)
											+ "e sous-réseau (courte) : " + ipv6.getIPv6LongToShort(new IPv6(sousReseauxArray[i], ipv6.getPrefix())));
								}
							} else {
								System.out.println("Aucuns sous-réseaux disponibles.");
							}
							choixValide = true;
						}
					}
				} catch (Exception e) {
					System.out.println("Erreur: " + e);
					scan.next();
				}
		}

	}

	//Valide la demande du nombre d'hotes avec le masque et creez un objet SousReseauxIPv4.
	//Entree: Masque, l'ip reseau en binaire[32], nombre d'hotes[nombre sous-reseau]
	//Sortie: Creation d'un objet SousReseauxIPv4 ou null
	private static SousReseauxIPv4 valideSousReseauIPv4(Masque masque,int[] reseauBin, int[] nbrHotes)
	{
		int nbrHoteDemande = 0;
		for(int i = 0; i < nbrHotes.length; i++)			//Calculer le nombre total d'hote
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
			System.out.println("Vous demander trop d'hotes pour le nombre hote disponible avec ce masque.");
			return null;
		}
	}
	
	//Permet de trouver le nombre d'hote qui sera reellement utiliser dans les sous-reseaux
	//en utilisant l'arrondissement a la puissance 2 ex:200 hotes demandes = 256 hotes reellements utilises
	//Entree: nombre d'hotes demande[nombre sous-reseau]
	//Sortie: tableau du nombre d'hote[nombre sous-reseau]
	private static int[] convertirNombreHote(int[] nbrHoteDemande)
	{
		int[] nbrHote = new int[nbrHoteDemande.length];
		for(int i = 0; i < nbrHoteDemande.length; i++)
		{
			//Trouver la logarithme en base 2 du chiffre voulu. (Faire l'opération inverse d'un exposant)
			double log = Math.log(nbrHoteDemande[i])/Math.log(2);
			//Arrondir au plus grand pour trouver le chiffre supperieur
			int hoteAvantPow = (int) Math.ceil(log);
			//2 a l'exposant du "chiffre arrondit"
			nbrHote[i] = (int) Math.pow(2,hoteAvantPow);
		}
		return nbrHote;
	}
	
	//Permet de trier du plus grand au plus petit les entrees de l'utilisateur avec la methode du tri par bulle
	//Entree: nombre d'hotes demande[nombre sous-reseau]
	//Sortie: tableau du nombre d'hote[nombre sous-reseau]
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
