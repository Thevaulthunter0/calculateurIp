/**
 * Titre: IPv6.java;
 * Description: Classe permettant de creer et utiliser les methodes necessaires pour trouver les informations d'une adresse IPv6.
 */
package calculateurIP;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Arrays;

public class IPv6 {

    //Initialisation
    private String ipString;					// l'adresse en string entree par l'utilisateur xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx
    private int prefix;                         //prefix indique le nombre de bits de la portie réseau
    public final int INTERFACE_ID = 64;        //nombre de bits recommandé pour la partie hôtes (interfaceId)
    public final int TOTAL_BITS_IPV6 = 128;    //total nombre de bits d'une adresse IPv6

    //Constructeur
    public IPv6(String ipString, int prefix) {  //une adresse IPv6 doit être construit avec son ip et son préfix indiquant le nombre de bits pour la partie réseau
        this.ipString = ipString;
        this.prefix = prefix;
    }

    public int getPrefix() {
        return this.prefix;
    }

    //Afficher les informations du calculateur
    public void afficherInformation()
    {
        System.out.println(
                "Adresse IP (longue): " + afficheIPv6(this.ipString, this.prefix)
                + "\nAdresse IP (courte) : " + afficheIPv6(getIPv6LongToShort(this), this.prefix)

                + "\n\nNombre d'adresses d'hotes disponibles : " + this.nbrHoteDisponible()
                + "\nNombre d'adresses d'hotes utilisables : " + this.nbrHoteDisponible()

                + "\n\nAdresse du masque : Le terme 'prefix' est utilisé plutôt que 'masque' en IPv6."
                + "\nPréfix : " + this.prefix

                + "\n\nID d'interface (notation bar oblique) : /" + this.INTERFACE_ID + "(defaut)"
                + "\nID d'interface (notation hexadécimal) : " + obtientInterfaceIdHex()

                + "\n\nAdresse reseau (longue) : " + obtientAdresseReseau()
                + "\nAdresse reseau (courte) : " + getIPv6LongToShort(new IPv6(obtientAdresseReseau(), this.prefix))

                + "\n\nPremiere adresse (longue) : " + obtientPremiereAdresse()
                + "\nPremiere adresse (courte) : " + getIPv6LongToShort(new IPv6(obtientPremiereAdresse(), this.prefix))

                + "\n\nDerniere adresse (longue) : " + obtientDerniereAdresse()
                + "\nDerniere adresse (courte) : " + getIPv6LongToShort(new IPv6(obtientDerniereAdresse(), this.prefix))

                + "\n\nAdresse diffusion :  Il n'y a pas en IPv6."
                + "\nAdresse link local (LLA)? : " + estLLA()
                + "\nAdresse unique local (ULA)? : " + estULA()
                + "\nAdresse de bouclage? : " + estBouclage()
                + "\nAdresse est routable (GUA)? : " + estGUA()

                + "\n\nSOUS-RESEAUX"
                + "\nID de sous-reseau: " +  calculerSubnetId()
                + "\nNombre de sous-réseaux disponibles : " + StringIPv6.nbrSousreseauxDisponibleFormatted(nbrSousreseauxDisponible())
                + "\nNombre d'adresses par sous-réseaux disponibles et utilisables : " +  nbrAdresseParSousreseau()

                + "\n\nPremier sous-reseau (longue) : " + getPremierSousReseau()
                + "\nPremier sous-reseau (courte): " + getIPv6LongToShort(new IPv6(getPremierSousReseau(), this.prefix))

                + "\n\nDernier sous-reseau (longue): " + getDernierSousReseau()
                + "\nDernier sous-reseau (courte): " + getIPv6LongToShort(new IPv6(getDernierSousReseau(), this.prefix)));
    }

    public String afficheIPv6(String ipString, int prefix) {
        return ipString + "/" + prefix;
    }

    private int calculerSubnetId() {
        return TOTAL_BITS_IPV6 - INTERFACE_ID - this.prefix;                    //SubnetID est le 128 - 64 - prefix bits
    }

    private String obtientInterfaceIdHex() {                                        //obtient l'interfaceID en hexadécimal
        String ipv6SansPoints = StringIPv6.obtientIPv6SansPoints(this.ipString);    //obtient l'adresseIP sans ':'
        String if_ID = "";                                                          //créer une chaîne de caractère vide
        for (int i = INTERFACE_ID/4; i< TOTAL_BITS_IPV6/4 ; i++) {                  //du position du bit 16 - bit 32, 'append' les hex
            if (i % 4 ==0 && i!=0)                                                  //ajoute ':' à l'interval de 4
                if_ID += ":";
            if_ID += ipv6SansPoints.charAt(i);
        }
        return if_ID;
    }

    private String nbrHoteDisponible() {                                        //nbrHoteDisponible et nbrHoteUtilisable est pareils pour IPv6
        DecimalFormat df = new DecimalFormat("###,###,###");            //formattage pour avoir des virgules entre 3 chiffres
        BigInteger nbrHote = new BigInteger("2").pow(TOTAL_BITS_IPV6 - this.prefix); //2^(128-prefix)
        return df.format(nbrHote);
    }

    public BigInteger nbrSousreseauxDisponible() {
        return new BigInteger("2").pow(calculerSubnetId());                 //nbrSousréseaux : 2^SubnetId
    }

    public String nbrAdresseParSousreseau() {
        DecimalFormat df = new DecimalFormat("###,###,###");
        BigInteger nbrAdresseParSousreseau = new BigInteger("2").pow(INTERFACE_ID);
        return df.format(nbrAdresseParSousreseau);                                              //2^(nbrBitsDesubnetID)
    }

    public String obtientAdresseReseauSansPoints() {
        String newIPv6SansPoints = obtientPartieGaucheIP("0");
        newIPv6SansPoints += Calcul.creerHexString(this.prefix/4 + 1, TOTAL_BITS_IPV6/4, "0");  //append des '0's pour le reste de l'adresse
        return newIPv6SansPoints;
    }

    public String obtientAdresseReseau() {                                 //cette fonction ajoute des points à l'adresse du réseau sans points obtenu à la fonction obtientAdresseReseauSansPoints()
        String adresseReseauSansPoints = obtientAdresseReseauSansPoints();
        String IPv6AvecPoints = StringIPv6.addDeuxPoints(adresseReseauSansPoints);
        return IPv6AvecPoints;
    }

    public String obtientPremiereAdresse() {                                        //cette fonction obtient la première adresse du réseau
        String adresseReseauSansPoints = obtientAdresseReseauSansPoints();          //obtient l'adresse réseau en hex sans ':'
        String premiereAdresseSansPoints = StringIPv6.hexSubstring(adresseReseauSansPoints, 0, adresseReseauSansPoints.length()-1); //append tous les hexadécimaux de l'adresse réseau sauf le dernier
        premiereAdresseSansPoints += "1";                                                                                       //append un '1' pour indiquer premiere adresse
        String premiereAdresseAvecPoints = StringIPv6.addDeuxPoints(premiereAdresseSansPoints);                                 //**ON ASSUME QUE L'ADRESSE À POSITION '0' EST POUR LE RÉSEAU, DONC PREMIÈRE ADRESSE EST LA PREMIÈRE ADRESSE DISPONIBLE POUR UN HÔTE
        return premiereAdresseAvecPoints;
    }

    public String obtientDerniereAdresse() {                                        //cette fonction obtient la dernière adresse du réseau
        String adresseDernier = obtientPartieGaucheIP("1");         //append nouveau hex à la chaîne de caractère
        adresseDernier += Calcul.creerHexString(this.prefix/4+1, TOTAL_BITS_IPV6/4, "f"); //append  "f" pour le reste de l'adresse
        String adresseDernierAvecPoints = StringIPv6.addDeuxPoints(adresseDernier);                         //ajouter les ':'
        return adresseDernierAvecPoints;
    }


    public boolean estLLA() {                                                               //fe80::/3 - febx::/3 dans un LAN
        String IPv6SansPoints = StringIPv6.obtientIPv6SansPoints(this.ipString);            //obtient l'adresse IP sans ':'
        return (IPv6SansPoints.charAt(0) == 'f' &&                                          //vérifie que les chars à 0,1, et 2 sont dans le bon format
                IPv6SansPoints.charAt(1) == 'e' &&
                (IPv6SansPoints.charAt(2) == '8' ||
                        IPv6SansPoints.charAt(2) == '9' ||
                        IPv6SansPoints.charAt(2) == 'a' ||
                        IPv6SansPoints.charAt(2) == 'b'));
    }

    public boolean estULA() {                                       //fc00::/7 to fdff::/7 possible d'être routable à l'interne d'une organisation mais pas globalement routable
        return (this.ipString.charAt(0) == 'f' &&
                this.ipString.charAt(1) == 'c'  ||
                this.ipString.charAt(1) == 'd');
    }

    public boolean estBouclage() {                                  //loopback adresse ::1
        return (this.ipString == "0:0:0:0:0:0:0:1" ||
                this.ipString == "::1" ||
                this.ipString == "0000:0000:0000:0000:0000:0000:0000:0001");
    }

    private boolean estAdresseNonspecifie() {                  //adresse non-spécifié, peu être utilisé dans les cas d'erreur ou adresse temporaire
        return (this.ipString== "0:0:0:0:0:0:0:0" ||
                this.ipString == "::0" ||
                this.ipString == "::" ||
                this.ipString == "0000:0000:0000:0000:0000:0000:0000:0000");
    }

    private String estGUA() {                               //globalement routable. Couramment seulement adresses des 3 premiers bits ont été assigné 001 or 2000::/3
        if (this.ipString.charAt(0) == '2') {
            return "true";
        } else if (!estBouclage() || !estULA() || !estLLA() || !estAdresseNonspecifie()) {
            return "Possible adresse GUA mais cette adresse n'a pas été assigné.";
        } else {
            return "false";
        }
    }


    private String[] obtientIPv6LongEnArray(IPv6 ipv6) {                           //cette fonction 'split' une adresseIP en tableau par hextets
        return ipv6.ipString.split(":");
    }

    public String getIPv6LongToShort(IPv6 ipv6)  {                                  //cette fonction convertit un IPv6 version longue en version courte

        String[] IPv6Array = obtientIPv6LongEnArray(ipv6);                          //obtient un tableau de ses hextets
        String[] IPv6ArrayClone = new String[8];                                    //créer un autre tableau de la même taille mais vide

        for (int i=0; i<7; i++) {                                                   //pour chaque hextet sauf le dernier
            String str = "";                                                        //créer une chaîne de caractère vide
            for (int j = 0; j < 4; j++) {                                           //pour chaque caractère du hextet
                if (IPv6Array[i].charAt(j)!='0') {                                  //commençant à gauche, si ce n'est pas '0', sauvegarde tous le hextet; si c'est '0' continue de regarder prochain caractère dans le hextet
                        str += (IPv6Array[i].substring(j));                         //concatène à la chaîne de caractère
                        IPv6ArrayClone[i] = str;                                    //ajoute au nouveau tableau à la même indice/position
                        break;                                                      //continue avec le prochain hextet
                }
            }
            str += (IPv6Array[i].equals("0000")) ? "0:" : ":";                      //si hextet contient 4 "0"s, concatène "0:" sinon ":"
            IPv6ArrayClone[i] = str;                                                //ajoute au nouveau tableau à la même indice/position
        }
        String str = "";                                                            //pour le dernier hextet
        for (int i=0; i < 4; i++) {                                                 //faire pareil mais ne pas afficher ':' à la fin
            if (IPv6Array[IPv6Array.length-1].charAt(i)!='0') {
                str += (IPv6Array[IPv6Array.length-1].substring(i));
                IPv6ArrayClone[IPv6Array.length-1] = str;
                break;
            }
        }
        if (IPv6Array[IPv6Array.length-1].equals("0000")) {
            str += "0";
            IPv6ArrayClone[IPv6ArrayClone.length-1] = str;
        }

        String IPv6ShortString = "";                                                   //déclare une nouvelle chaîne de caractère (IP modifié)
        for (String s : IPv6ArrayClone) {                                              //lier tous les hextets du tableau
            IPv6ShortString += s;
        }

        String[] patterns = {":0:0:0:0:0:0:0:0", ":0:0:0:0:0:0:0", ":0:0:0:0:0:0", ":0:0:0:0:0", ":0:0:0:0", ":0:0:0", ":0:0"};    //déclare et initialiser un tableau avec les chaînes de '0' concatené avec ':'
        for (int i=0; i< patterns.length; i++) {                                                                            //commençant par la chaîne le plus longue
            if (IPv6ShortString.indexOf(patterns[i]) != -1) {                                                               //si l'adress IP modifié contient la chaîne de '0' et ':'
                IPv6ShortString = IPv6ShortString.replace(patterns[i], ":");                                     //remplace les '0' et ':' entre avec une chaîne de caractère vide
            }
        }

        if (IPv6ShortString.charAt(IPv6ShortString.length()-1)==':') {                                                      //ajoute ':' à la fin si c'était un '0' qui a été remplacé
            IPv6ShortString += ':';
        }

        if (IPv6ShortString.indexOf(":0:") != -1 && IPv6ShortString.indexOf("::")==-1) {                                     //Pour les hextets '0' seuls,
            IPv6ShortString = IPv6ShortString.replace(":0:", "::");                                       //remplacer par :: . C'est pour éviter que les '0' dans n'importe quel hextet soit éffacé.
        }

        return IPv6ShortString;
    }

    private String obtientPartieGaucheIP(String bit) {
        String IPv6SansPoints = StringIPv6.obtientIPv6SansPoints(this.ipString);                        //obtient l'adresse IP sans ':'
        String newIPv6SansPoints = StringIPv6.hexSubstring(IPv6SansPoints,0, this.prefix/4);     //copier les hexDigits de la partie réseau

        char c = IPv6SansPoints.charAt(this.prefix/4);                          //au bit où prefix arrête, détermine le bon caractère en hex
        int num = Calcul.convertirHexDigitADec(c);                              //convertir ce hex en entier

        String bitString = Calcul.convertirDecABin(num);                        //convertir l'entier en binaire ex. 3 = "11"
        String quatreBitStr = Calcul.obtientBinEnQuatreBits(bitString);         //concatène le 4-bit chaîne de caractère binaire ex. 3 = "0011"
        String newQuatreBitStr = Calcul.manipulerQuatreBitBin(quatreBitStr,bit, this.prefix); //changer le valeur du binaire selon le préfix (à droit du binaire du position du prefix, mettre tous '0')

        String newHex = Calcul.convertirBinAHex(newQuatreBitStr);                          //convertir le nouveau chaîne binaire en hex
        newIPv6SansPoints += newHex;
        return newIPv6SansPoints;
    }


    //############################### MÉTHODES SOUS-RÉSEAUX IPv6 ####################################


    public String getPremierSousReseau() {
        String IPv6String = obtientPartieGaucheIP("0");                                                 //obtient partie gauche
        IPv6String += Calcul.creerHexString(this.prefix/4+1, TOTAL_BITS_IPV6/4, "0");     //après, append les "0"
        String IPv6StrAvecPoints = StringIPv6.addDeuxPoints(IPv6String);                                     //ajouter des ':'
        return IPv6StrAvecPoints;
    }

    public String getDernierSousReseau() {
        String IPv6SansPoints = StringIPv6.obtientIPv6SansPoints(this.ipString);                        //obtient l'adresse IP sans ':'
        String IPv6String = StringIPv6.hexSubstring(IPv6SansPoints,0, this.prefix/4);         //obtient la partie réseau

        if (this.prefix!=64) {                                                              //pour éviter que ça ajoute un 'f' de trop dans le cas où prefix est 64
            char c = IPv6SansPoints.charAt(this.prefix/4);                                  //caractère au bit qui est changeable selon prefix
            int num = Calcul.convertirHexDigitADec(c);                                     //convertir ce hexDigit en entier
            String bitString = Calcul.convertirDecABin(num); //11                          //obtient la forme binaire de ce hexDigit
            String quatreBitStr = Calcul.obtientBinEnQuatreBits(bitString); //0011
            String newQuatreBitStr = Calcul.manipulerQuatreBitBin(quatreBitStr, "1", this.prefix);
            String newHex = Calcul.convertirBinAHex(newQuatreBitStr);                          //convertir du binaire en hexadécimal
            IPv6String += newHex;
        }

        IPv6String += Calcul.creerHexString((this.prefix/4)+1,INTERFACE_ID/4,  "f");
        IPv6String += Calcul.creerHexString(INTERFACE_ID/4,TOTAL_BITS_IPV6/4,  "0");

        String IPv6StrAvecPoints = StringIPv6.addDeuxPoints(IPv6String) ;                     //mettre des ':'
        return IPv6StrAvecPoints;
    }

    public String[] obtientSousReseauxIPv6(int nbSousReseaux) { //nbSousReseaux = nb demandé par client

        //pour savoir combien de hexdigits pour la partie reseau
        String nbTotalSousReseauBin = Calcul.convertirDecABin(nbSousReseaux);               //nbSousReseaux en binaire
        String nbTotalSousReseauHex = Calcul.convertirBinAHex(nbTotalSousReseauBin);        //nbSousReseaux en hex
        String[] sousReseaux = new String[nbSousReseaux];                                   //creer un tableau contenant les nb de sous réseaux demandé

        //savoir combien de hexdigits chaque sous-reseau, ex. 1, 2, 3...
        for (int i=0; i<nbSousReseaux; i++) {
            String IPv6SansPoints = StringIPv6.obtientIPv6SansPoints(this.ipString);        //obtient l'adresse IP sans ':'
            String sousReseau = StringIPv6.hexSubstring(IPv6SansPoints,0, this.prefix/4);

            String sousReseauBin = Calcul.convertirDecABin(i);                              //obtient i en binaire ex. 1

            int subnetId = calculerSubnetId();                                              //il faut savoir combien de bits partie subnetId
            String sousReseauLongBin = Calcul.obtientBinEnNBits(sousReseauBin, subnetId);   //obtient i en binaire ex. 0000 0001
            String sousReseauHex = Calcul.convertirBinAHex(sousReseauLongBin);              //convertir en hex

            sousReseau += sousReseauHex;                                                    //append à l'adresse
            sousReseau += Calcul.creerHexString(INTERFACE_ID/4,TOTAL_BITS_IPV6/4,  "0");

            String sousReseauAvecPoints = StringIPv6.addDeuxPoints(sousReseau) ;            //mettre des ':'
            sousReseaux[i] = sousReseauAvecPoints;
        }
        return sousReseaux;
    }

}
