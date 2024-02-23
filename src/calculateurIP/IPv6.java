package calculateurIP;

import java.math.BigInteger;

public class IPv6 {

    //Initialisation
    private String ipString;					// l'adresse en string entree par l'utilisateur xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx:xxxx
    private int prefix;                         //prefix indique le nombre de bits de la portie réseau
    private final int INTERFACE_ID = 64;        //nombre de bits recommandé pour la partie hôtes (interfaceId)
    private final int TOTAL_BITS_IPV6 = 128;    //total nombre de bits d'une adresse IPv6

    //Constructeur
    public IPv6(String ipString, int prefix) {  //une adresse IPv6 doit être construit avec son ip et son préfix indiquant le nombre de bits pour la partie réseau
        this.ipString = ipString;
        this.prefix = prefix;
    }

    //Afficher les informations du calculateur
    public void afficherInformation()
    {
        System.out.println(
                "Nombre d'adresses d'hôtes disponibles : " + this.nbrHoteDisponible()
                + "\nNombre d'adresses d'hôtes utilisables : " + this.nbrHoteDisponible()
                + "\n\nAdresse du masque : Le terme 'préfix' est utilisé plutôt que 'masque' en IPv6."
                + "\nPréfix : " + this.prefix
                + "\nID d'interface : " + this.INTERFACE_ID + "(defaut)"
                + "\nID de sous-réseau: " +  calculerSubnetId()
                + "\nNombre de sous-réseaux disponibles : " + this.nbrSousreseauxDisponible()
                + "\n\nAdresse reseau (longue) : " + getIPv6AddressReseauLong()
                + "\nAdresse reseau (courte) : " + getIPv6LongToShort(new IPv6(getIPv6AddressReseauLong(), this.prefix))
                + "\n\nPremiere adresse (longue) : " + trouverPremierAdresse()
                + "\nPremiere adresse (courte) : " + getIPv6LongToShort(new IPv6(trouverPremierAdresse(), this.prefix))
                + "\n\nDerniere adresse (longue) : " + trouverDerniereAdresse()
                + "\nDerniere adresse (courte) : " + getIPv6LongToShort(new IPv6(trouverDerniereAdresse(), this.prefix))
                + "\n\nAdresse diffusion :  Il n'y a pas en IPv6."
                + "\nAdresse link local (LLA)? : " + estLLA()
                + "\nAdresse unique local (ULA)? : " + estULA()
                + "\nAdresse de bouclage? : " + estBouclage()
                + "\nAdresse est routable (GUA)? : " + estGUA());
    }

    private int calculerSubnetId() {
        return TOTAL_BITS_IPV6 - INTERFACE_ID - this.prefix;                    //SubnetID est le 128 - 64 - prefix bits
    }

    public BigInteger nbrHoteDisponible() {                                     //nbrHoteDisponible et nbrHoteUtilisable est pareils pour IPv6
        return new BigInteger("2").pow(TOTAL_BITS_IPV6 - this.prefix);      //2^(128-prefix)
    }

    public BigInteger nbrSousreseauxDisponible() {
        return new BigInteger("2").pow(calculerSubnetId());                 //nbrSousréseaux : 2^SubnetId
    }

    private String obtientIPv6SansPoints () {                                   //fonction privée pour obtenir le IPv6 en chaîne de caractères sans ':'
        String IPv6SansPoints = "";                                             //déclare une chaîne de caratères vide
        for (int i = 0; i<this.ipString.length(); i++) {                        //pour tous les caractères, si le caractère est ':', enlève le.
            if (this.ipString.charAt(i)!=':')                                   //sauvegarde dans la nouvelle chaîne de caractères.
                IPv6SansPoints += this.ipString.charAt(i);
        }
        return IPv6SansPoints;
    }

    public String getIPv6AddressReseauLong() {                                  //fonction pour obtenir l'adresse du réseau à partir d'une adresse IPv6

        String IPv6SansPoints = obtientIPv6SansPoints();                        //obtient l'adresse IP sans ':'
        String IPv6String = "";                                                 //déclare une chaîne de caratères vide

        for (int i = 0; i<this.prefix/4; i++) {                                 //Garder les bits de la partie réseau (préfix) dans la partie à gauche
            if (i % 4 ==0 && i!=0)
                IPv6String += ":";
            IPv6String += IPv6SansPoints.charAt(i);
        }

        for (int i = this.prefix/4; i<TOTAL_BITS_IPV6/4; i++) {                  //Le reste sont des '0'
            if (i % 4 ==0)
                IPv6String += ":";
            IPv6String += "0";
        }
        return IPv6String;
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


        String[] patterns = {"0:0:0:0:0:0:0:0", "0:0:0:0:0:0:0", "0:0:0:0:0:0", "0:0:0:0:0", "0:0:0:0", "0:0:0", "0:0"};    //déclare et initialiser un tableau avec les chaînes de '0' concatené avec ':'
        for (int i=0; i< patterns.length; i++) {                                                                            //commençant par la chaîne le plus longue
            if (IPv6ShortString.indexOf(patterns[i]) != -1) {                                                               //si l'adress IP modifié contient la chaîne de '0' et ':'
                IPv6ShortString = IPv6ShortString.replace(patterns[i], "");                                     //remplace les '0' et ':' entre avec une chaîne de caractère vide
            }
        }

        if (IPv6ShortString.indexOf(":0:") != -1) {                                                                         //Pour les hextets '0' seuls,
            IPv6ShortString = IPv6ShortString.replace(":0:", "::");                                       //remplacer par :: . C'est pour éviter que les '0' dans n'importe quel hextet soit éffacé.
        }

        if (IPv6ShortString.charAt(IPv6ShortString.length()-1)==':') {                                                      //ajoute ':' à la fin si c'était un '0' qui a été remplacé
            IPv6ShortString += ':';
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
        String IPv6SansPoints = obtientIPv6SansPoints();                             //obtient l'adresse IP sans ':'
        String derniereAdresse = "";                                                 //déclare une chaîne de caractère vide
        for (int i=0; i<this.prefix/4; i++) {                                        // prefix/4 parce que chaque caractère du hextet est 4 bits
            derniereAdresse += IPv6SansPoints.charAt(i);
        }
        for (int i=this.prefix/4; i<IPv6SansPoints.length(); i++) {
            derniereAdresse += "f";                                                  //après les bits du réseau à gauche, ajoute tous les 'f' pour signifier le dernier/plafond de chaque bit
        }
        return addLesDeuxPoints(derniereAdresse);
    }

    public boolean estLLA() {                                       //fe80::/3 - febx::/3 dans un LAN
        String IPv6SansPoints = obtientIPv6SansPoints();            //obtient l'adresse IP sans ':'
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

}
