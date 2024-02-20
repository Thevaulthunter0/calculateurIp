package calculateurIP;

public class Main {

	public static void main(String[] args) 
	{
		String str = "209.225.161.3";
		Masque msq = new Masque(23);
		IPv4 ip = new IPv4(str,msq);
		int[] reseauBin = ip.getReseauBin();
		//System.out.println(ip.estLinkLocal());
		//System.out.println("---\nIP:" + ip.getIpString() + "\n" + ip.afficherBin(ip.getIpBin()));
		//System.out.println("---\nMasque" + msq.getStrMasque() + "\n" + ip.convertirBinVersString(msq.getMasqueBin())); 
		//System.out.println(msq.afficherBin());
		//System.out.println("---\nNbr hote disponible:" + msq.nbrHoteDisponible());
		//System.out.println("---\nNbr hote utilisable:" +msq.nbrHoteUtilisable());
		//System.out.println("---\nReseau:\n" + ip.afficherBin(ip.getReseauBin()) + "\n" + ip.convertirBinVersString(ip.getReseauBin()));
		//System.out.println("---\nPremier hote:\n" + ip.afficherBin(ip.premiereAdresse()) + "\n" + ip.convertirBinVersString(ip.premiereAdresse()));
		//System.out.println("---\nDerniere hote:\n" + ip.afficherBin(ip.derniereAdresse()) + "\n" + ip.convertirBinVersString(ip.derniereAdresse()));
		//System.out.println("---\nAddresse diffusion:\n" + ip.afficherBin(ip.adresseDiffusion()) + "\n" + ip.convertirBinVersString(ip.adresseDiffusion()));
		int[] nbrHotesDemande = {120,90,60,30,24,24,20};
		int[] nbrHotes = convertirNombreHote(nbrHotesDemande);
		SousReseauxIPv4 sousReseau = valideSousReseauIPv4(msq, reseauBin,nbrHotes);
		//for(int i = 0; i < nbrHotes.length; i++)
		//{
		//	System.out.println(nbrHotes[i]);
		//}
		int[][] lesSousReseau = sousReseau.trouverSousReseaux();
		for(int i = 0; i < lesSousReseau.length; i++)
		{
			System.out.println(nbrHotesDemande[i] + " : " + ip.convertirBinVersString(lesSousReseau[i]));
		}
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
