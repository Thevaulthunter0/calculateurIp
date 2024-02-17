package calculateurIP;

public class Main {

	public static void main(String[] args) 
	{
		String str = "172.16.22.117";
		Masque msq = new Masque(24);
		IPv4 ip = new IPv4(str,msq);
		System.out.println("---\nIP:" + ip.getIpString() + "\n" + ip.afficherBin(ip.getIpBin()));
		System.out.println("---\nMasque" + msq.getStrMasque() + "\n" + ip.convertirBinVersString(msq.getMasqueBin())); 
		System.out.println(msq.afficherBin());
		System.out.println("---\nNbr hote disponible:" + msq.nbrHoteDisponible());
		System.out.println("---\nNbr hote utilisable:" +msq.nbrHoteUtilisable());
		System.out.println("---\nReseau:\n" + ip.afficherBin(ip.getReseauBin()) + "\n" + ip.convertirBinVersString(ip.getReseauBin()));
		System.out.println("---\nPremier hote:\n" + ip.afficherBin(ip.premiereAdresse()) + "\n" + ip.convertirBinVersString(ip.premiereAdresse()));
		System.out.println("---\nDerniere hote:\n" + ip.afficherBin(ip.derniereAdresse()) + "\n" + ip.convertirBinVersString(ip.derniereAdresse()));
		System.out.println("---\nAddresse diffusion:\n" + ip.afficherBin(ip.adresseDiffusion()) + "\n" + ip.convertirBinVersString(ip.adresseDiffusion()));
	}
}
