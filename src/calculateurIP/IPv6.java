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

                + "\n\nAdresse reseau (longue) : " + getIPv6AddressReseauLong()
                + "\nAdresse reseau (courte) : " + getIPv6LongToShort(new IPv6(getIPv6AddressReseauLong(), this.prefix))

                + "\n\nPremiere adresse (longue) : " + getIPv6AddressReseauLong()//trouverPremierAdresse()
                + "\nPremiere adresse (courte) : " + getIPv6LongToShort(new IPv6(getIPv6AddressReseauLong(), this.prefix))

                + "\n\nDerniere adresse (longue) : " + trouverDerniereAdresse()
                + "\nDerniere adresse (courte) : " + getIPv6LongToShort(new IPv6(trouverDerniereAdresse(), this.prefix))

                + "\n\nAdresse diffusion :  Il n'y a pas en IPv6."
                + "\nAdresse link local (LLA)? : " + estLLA()
                + "\nAdresse unique local (ULA)? : " + estULA()
                + "\nAdresse de bouclage? : " + estBouclage()
                + "\nAdresse est routable (GUA)? : " + estGUA()

                + "\n\nSOUS-ReSEAUX"
                + "\nID de sous-reseau: " +  calculerSubnetId()
                + "\nNombre de sous-réseaux disponibles : " + nbrSousreseauxDisponibleFormatted(nbrSousreseauxDisponible())
                + "\nNombre d'adresses par sous-réseaux disponibles et utilisables : " +  nbrAdresseParSousreseau()

                + "\n\nPremier sous-reseau (longue) : " + getPremierSousReseau()
                + "\nPremier sous-reseau (courte): " + getIPv6LongToShort(new IPv6(getPremierSousReseau(), this.prefix))

                + "\n\nDernier sous-reseau (longue): " + adresseDernierSousReseau()
                + "\nDernier sous-reseau (courte): " + getIPv6LongToShort(new IPv6(adresseDernierSousReseau(), this.prefix)));
    }

    public String afficheIPv6(String ipString, int prefix) {
        return ipString + "/" + prefix;
    }

    private int calculerSubnetId() {
        return TOTAL_BITS_IPV6 - INTERFACE_ID - this.prefix;                    //SubnetID est le 128 - 64 - prefix bits
    }

    private String obtientInterfaceIdHex() {
        String ipv6SansPoints = obtientIPv6SansPoints(this.ipString);
        String if_ID = "";
        for (int i = INTERFACE_ID/4; i< TOTAL_BITS_IPV6/4 ; i++) {
            if (i % 4 ==0 && i!=0)
                if_ID += ":";
            if_ID += ipv6SansPoints.charAt(i);
        }
        return if_ID;
    }

    public String nbrHoteDisponible() {                                     //nbrHoteDisponible et nbrHoteUtilisable est pareils pour IPv6
        DecimalFormat df = new DecimalFormat("###,###,###");
        BigInteger nbrHote = new BigInteger("2").pow(TOTAL_BITS_IPV6 - this.prefix); //2^(128-prefix)
        return df.format(nbrHote);
    }

    public BigInteger nbrSousreseauxDisponible() {
        return new BigInteger("2").pow(calculerSubnetId());                 //nbrSousréseaux : 2^SubnetId
    }

    public String nbrSousreseauxDisponibleFormatted(BigInteger bi) {
        DecimalFormat df = new DecimalFormat("###,###,###");
        return df.format(bi);
    }

    public String nbrAdresseParSousreseau() {
        DecimalFormat df = new DecimalFormat("###,###,###");
        BigInteger nbrAdresseParSousreseau = new BigInteger("2").pow(INTERFACE_ID);
        return df.format(nbrAdresseParSousreseau);                            //2^(nbrBitsDesubnetID)
    }

    public String getPremierSousReseau() {
        String IPv6SansPoints = obtientIPv6SansPoints(this.ipString);                        //obtient l'adresse IP sans ':'
        String IPv6String = "";                                                 //déclare une chaîne de caratères vide

        for (int i = 0; i<this.prefix/4; i++) {                                 //Garder les bits de la partie réseau (préfix) dans la partie à gauche
            IPv6String += IPv6SansPoints.charAt(i);
        }

        String newQuatreBitStr = "";
        char c = IPv6SansPoints.charAt(this.prefix/4);                          //caractère au bit qui est changeable selon prefix
        int num = convertirHexDigitADec(c);                                     //convertir ce hexDigit en entier

        if (IPv6SansPoints.charAt(this.prefix/4)!='0') {
            String quatreBitString = convertirDecABin(num);                     //obtient la forme binaire de ce hexDigit
            int restant = this.prefix % 4;

            for (int i=0; i<restant; i++) {                                           //garder les bits à gauche
                newQuatreBitStr += quatreBitString.charAt(i);
            }
            for (int i=restant; i<4; i++) {                                           //mettre les bits à droite à '0'
                newQuatreBitStr += "0";
            }
        } else {
            newQuatreBitStr = "0000";                                               //si caractère à la position du hexDigit est 0, mettre la représentation binaire est 0000
        }

        String newHex = convertirBinAHex(newQuatreBitStr);                          //convertir du binaire en hexadécimal

        IPv6String += newHex;                                                       //ajouter le hexDigit converi

//        for (int i = this.prefix/4; i<INTERFACE_ID/4; i++) {                  //Le reste sont des '0'
////            for (int i = this.prefix/4+1; i<INTERFACE_ID/4-1; i++) {                  //Le reste sont des '0'
//
//                IPv6String += "0";
//        }
//        IPv6String += "1";                                                        //***SI LE PREMIER SOUS-RÉSEAU EST 1 SINON SI PREMIER SOUS-RÉSEAU EST L'ADRESSE RÉSEAU c'EST 0
        for (int i = this.prefix/4+1; i<TOTAL_BITS_IPV6/4; i++) {                  //Le reste sont des '0'
            IPv6String += "0";
        }

        String IPv6StrAvecPoints = "";                                              //mettre des ':'
        for (int i=0; i<IPv6String.length(); i++) {
            if (i%4==0 & i!=0) {
                IPv6StrAvecPoints += ':';
            }
            IPv6StrAvecPoints += IPv6String.charAt(i);
        }

        return IPv6StrAvecPoints;
    }

    private String trouverHexDigit(int num) {
        if (num % 4 == 0)  return "f";
        if (num % 4 == 1)  return "7";
        if (num % 4 == 2)  return "3";
        if (num % 4 == 3)  return "1";
        else return null;
    }

    private String convertirDecABin(int num) {
        String str = "";
        int nbDeDigitBin = (int)(Math.floor(Math.log(num)/Math.log(2)));
        for (int i=nbDeDigitBin; i>= 0; i--) {
            if (num - (int)Math.pow(2,i) >= 0) {
                str += "1";
                num -= (int)Math.pow(2,i);
            } else {
                str += "0";
            }
        }
        return str;
    }
    private String convertirDecABin(BigInteger bi) {                  //obtient la forme binaire des BigInteger
        String str = "";
        int nbDeDigitBin = bi.bitLength();
            for (int i=nbDeDigitBin-1; i>= 0; i--) {
                BigInteger pow2 = BigInteger.ONE.shiftLeft(i);
            if (bi.compareTo(pow2)>=0) {
                str += "1";
                bi = bi.subtract(pow2);
            } else {
                str += "0";
            }
        }
        return str;
    }
    private String convertirBinAHex(String bin) {
        String str = "";
        int accumulator = 0;

        for (int i = bin.length() - 1; i >= 0; i -= 4) {                //en groupes de 4 bits, le valeur du hexDigit est accumulé par accumulator
            int power = 0;
            for (int j = 0; j < 4; j++) {
                if (i - j >= 0 && bin.charAt(i - j) == '1') {
                    accumulator += Math.pow(2, power);
                }
                power++;
            }

            if (accumulator >= 10 && accumulator <= 15) {
                str = (char) ('a' + accumulator - 10) + str;                //convertir accumlateur en hexadécimal
            } else {
                str = accumulator + str;
            }

            accumulator = 0;                                                //reset accumulator pour prochain itération
        }
        return str;
    }

    private int convertirHexDigitADec(char hexDigit) {                      //convertir un hexDigit en décimal
        switch (hexDigit) {
            case 'a': return 10;
            case 'b': return 11;
            case 'c': return 12;
            case 'd': return 13;
            case 'e': return 14;
            case 'f': return 15;
            default: return Character.digit(hexDigit, 16);             //retourner le caractère en entier
        }
    }

    public String adresseDernierSousReseau() {
        BigInteger zero = new BigInteger("0");                                  //obtient le valeur '0' en BigInteger (pour comparaison)
        String IPv6SansPoints = obtientIPv6SansPoints(this.ipString);                            //obtient l'adresse IP en hexadécimal sans ':'
        String adresseDernierSousReseau = "";

        if (nbrSousreseauxDisponible().compareTo(zero)==1) {                      //S'il y a des sous-réseaux
            for (int i=0; i<this.prefix/4;i++) {
                adresseDernierSousReseau += IPv6SansPoints.charAt(i);              //garder la partie à gauche du global routing adresse
            }
            String nbSousReseauBin = convertirDecABin(nbrSousreseauxDisponible().subtract(new BigInteger("1")));
            String nbSousReseauHex = convertirBinAHex(nbSousReseauBin);

            adresseDernierSousReseau += nbSousReseauHex;

            for (int i=INTERFACE_ID/4; i<IPv6SansPoints.length();i++) {            //mettre '0's jusqu'à la fin de la fin
                adresseDernierSousReseau += "0";
            }

            String adresseDernierSousReseauAvecPoints = "";                         //ajouter des ':'
            for (int i=0; i<adresseDernierSousReseau.length(); i++) {
                if (i%4==0 & i!=0) {
                    adresseDernierSousReseauAvecPoints += ':';
                }
                adresseDernierSousReseauAvecPoints += adresseDernierSousReseau.charAt(i);
            }
            return adresseDernierSousReseauAvecPoints;
        }
        else return "Pas de sous-réseaux";

    }


    public String obtientIPv6SansPoints (String ipStr) {                                   //fonction privée pour obtenir le IPv6 en chaîne de caractères sans ':'
        String IPv6SansPoints = "";                                             //déclare une chaîne de caratères vide
        for (int i = 0; i<ipStr.length(); i++) {                        //pour tous les caractères, si le caractère est ':', enlève le.
            if (ipStr.charAt(i)!=':')                                   //sauvegarde dans la nouvelle chaîne de caractères.
                IPv6SansPoints += ipStr.charAt(i);
        }
        return IPv6SansPoints;
    }

    public String getIPv6AddressReseauLong() {                                  //fonction pour obtenir l'adresse du réseau à partir d'une adresse IPv6

        String IPv6SansPoints = obtientIPv6SansPoints(this.ipString);                        //obtient l'adresse IP sans ':'
        String IPv6String = "";                                                 //déclare une chaîne de caratères vide

        for (int i = 0; i<this.prefix/4; i++) {                                 //Garder les bits de la partie réseau (préfix) dans la partie à gauche
            IPv6String += IPv6SansPoints.charAt(i);
        }

        String newQuatreBitStr = "";
        char c = IPv6SansPoints.charAt(this.prefix/4);                          //caractère au bit qui est changeable selon prefix
        int num = convertirHexDigitADec(c);                                     //convertir ce hexDigit en entier

        if (IPv6SansPoints.charAt(this.prefix/4)!='0') {
            String quatreBitString = convertirDecABin(num);                     //obtient la forme binaire de ce hexDigit
            int restant = this.prefix % 4;

            for (int i=0; i<restant; i++) {                                           //garder les bits à gauche
                newQuatreBitStr += quatreBitString.charAt(i);
            }
            for (int i=restant; i<4; i++) {                                           //mettre les bits à droite à '0'
                newQuatreBitStr += "0";
            }
        } else {
            newQuatreBitStr = "0000";                                               //si caractère à la position du hexDigit est 0, mettre la représentation binaire est 0000
        }

        String newHex = convertirBinAHex(newQuatreBitStr);                          //convertir du binaire en hexadécimal

        IPv6String += newHex;                                                       //ajouter le hexDigit converi

        for (int i = this.prefix/4 + 1; i<TOTAL_BITS_IPV6/4; i++) {                  //Le reste sont des '0'
            IPv6String += "0";
        }

        String IPv6StrAvecPoints = "";                                              //mettre des ':'
        for (int i=0; i<IPv6String.length(); i++) {
            if (i%4==0 & i!=0) {
                IPv6StrAvecPoints += ':';
            }
            IPv6StrAvecPoints += IPv6String.charAt(i);
        }

        return IPv6StrAvecPoints;

    }

    private String[] obtientIPv6LongEnArray(IPv6 ipv6) {                           //fonction privée pour mettre des hextets du l'adresse IP en tableau
        return ipv6.ipString.split(":");
    }

    public String getIPv6LongToShort(IPv6 ipv6)  {                                  //fonction pour convertir un IPv6 version longue en version courte

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

        if (IPv6ShortString.indexOf(":0:") != -1 && IPv6ShortString.indexOf("::")==-1) {                                                                         //Pour les hextets '0' seuls,
            IPv6ShortString = IPv6ShortString.replace(":0:", "::");                                       //remplacer par :: . C'est pour éviter que les '0' dans n'importe quel hextet soit éffacé.
        }

        return IPv6ShortString;
    }

    public String trouverPremierAdresse() {
        String IPv6AddressReseauLong = getIPv6AddressReseauLong();                                  //obtient l'adresse IP du réseau
        String premiereAdresse = "";                                                                //déclare une chaîne de caractère vide
        for (int i=0; i<IPv6AddressReseauLong.length()-1; i++) {                                    //garder tous les caractères du réseau sauf le dernier
            premiereAdresse += IPv6AddressReseauLong.charAt(i);
        }
        premiereAdresse += "1";                                                                     //mettre '1' pour le dernier
        return premiereAdresse;
    }

    private String addLesDeuxPoints(String str) {                                                   //fonction privée pour remettre ':' dans une adresse IPv6 dont les ':' ont été enlevé
        String IPv6AvecDeuxPoints = "";                                                             //déclare une chaîne de caractère vide
        for (int i=0; i<str.length(); i++) {                                                        //concatène la chaîne mais
            IPv6AvecDeuxPoints += (i % 4 == 0 && i!=0) ? ":" + str.charAt(i) : str.charAt(i);       //à tous les positions qui sont des facteurs de 4, ajoute ':'
        }
        return IPv6AvecDeuxPoints;
    }

    public String trouverDerniereAdresse() {
        String IPv6SansPoints = obtientIPv6SansPoints(this.ipString);                            //obtient l'adresse IP en hexadécimal sans ':'
        String adresseDernier = "";

        for (int i=0; i<this.prefix/4-1;i++) {
            adresseDernier += IPv6SansPoints.charAt(i);              //garder la partie à gauche du global routing adresse
        }

        String newQuatreBitStr = "";
        char c = IPv6SansPoints.charAt(this.prefix/4-1);                          //caractère au bit qui est changeable selon prefix
        int num = convertirHexDigitADec(c);                                     //convertir ce hexDigit en entier

        if (IPv6SansPoints.charAt(this.prefix/4-1)!='0') {
            String quatreBitString = convertirDecABin(num);                     //obtient la forme binaire de ce hexDigit
            int restant = this.prefix % 4;
            for (int i=0; i<restant; i++) {                                           //garder les bits à gauche
                newQuatreBitStr += quatreBitString.charAt(i);
            }
            for (int i=restant; i<4; i++) {                                           //mettre les bits à droite à '1'
                newQuatreBitStr += "1";
            }
        } else {
            newQuatreBitStr = "0000";
        }

        String newHex = convertirBinAHex(newQuatreBitStr);

        adresseDernier += newHex;

        for (int i=this.prefix/4; i<TOTAL_BITS_IPV6/4; i++) {
            adresseDernier += "f";
        }

        String adresseDernierAvecPoints = "";
        for (int i=0; i<adresseDernier.length(); i++) {
            if (i%4==0 & i!=0) {
                adresseDernierAvecPoints += ':';
            }
            adresseDernierAvecPoints += adresseDernier.charAt(i);
        }
        return adresseDernierAvecPoints;
    }

    public boolean estLLA() {                                       //fe80::/3 - febx::/3 dans un LAN
        String IPv6SansPoints = obtientIPv6SansPoints(this.ipString);            //obtient l'adresse IP sans ':'
        return (IPv6SansPoints.charAt(0) == 'f' &&
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

    public void afficherSousReseauxIPv6(int nbSousReseaux) {

        //pour savoir combien de hexdigits pour la partie reseau
        int nbDeBitsSousReseau = (int)(Math.ceil(Math.log(nbSousReseaux)/Math.log(2)))+ 1; //trouver nb de bits ça prend
        System.out.println("NbBitsSousReseau: " + nbDeBitsSousReseau);
        String nbTotalSousReseauBin = convertirDecABin(nbSousReseaux); //nbSousReseau binaire
        String nbTotalSousReseauHex = convertirBinAHex(nbTotalSousReseauBin); //change to hex

        //savoir combien de hexdigits chaque sous-reseau, ex. 1, 2, 3...
        for (int i=0; i<nbSousReseaux; i++) {
            String ipSousReseau = "";
            String ipReseau = getIPv6AddressReseauLong();
            String ipReseauSansPoints = obtientIPv6SansPoints(ipReseau);

            String nbSousReseauBin = convertirDecABin(i); //nbSousReseau binaire
            String nbSousReseauHex = convertirBinAHex(nbSousReseauBin); //change to hex
            String hex = "";
            for (int j=0; j<nbTotalSousReseauHex.length() - nbSousReseauHex.length(); j++) {
                hex += '0';
            }
            hex += nbSousReseauHex;
//            System.out.println("nbSousReseauBin: " + nbSousReseauBin);
//            System.out.println("nbSousReseauHex: " + nbSousReseauHex);
            for (int j=0; j<16 - nbTotalSousReseauHex.length(); j++) {
                ipSousReseau += ipReseauSansPoints.charAt(j);
            }
            ipSousReseau += hex;
            for (int j=INTERFACE_ID/4; j<TOTAL_BITS_IPV6/4; j++) {
                ipSousReseau += "0";
            }

            String IPv6StrAvecPoints = "";                                              //mettre des ':'
            for (int j=0; j<ipSousReseau.length(); j++) {
                if (j%4==0 & j!=0) {
                    IPv6StrAvecPoints += ':';
                }
                IPv6StrAvecPoints += ipSousReseau.charAt(j);
            }

            System.out.println("\n" + (i+1) + "e sous-réseau (longue) : " + IPv6StrAvecPoints + "\n" + (i+1) + "e sous-réseau (courte) : " + getIPv6LongToShort(new IPv6(IPv6StrAvecPoints, this.prefix)));
        }
    }

}
