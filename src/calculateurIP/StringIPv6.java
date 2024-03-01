package calculateurIP;

import java.math.BigInteger;
import java.text.DecimalFormat;

public class StringIPv6 {

    public static String obtientIPv6SansPoints (String ipStr) {                                   //fonction privée pour obtenir le IPv6 en chaîne de caractères sans ':'
        String IPv6SansPoints = "";                                             //déclare une chaîne de caratères vide
        for (int i = 0; i<ipStr.length(); i++) {                        //pour tous les caractères, si le caractère est ':', enlève le.
            if (ipStr.charAt(i)!=':')                                   //sauvegarde dans la nouvelle chaîne de caractères.
                IPv6SansPoints += ipStr.charAt(i);
        }
        return IPv6SansPoints;
    }

    public static String addDeuxPoints(String ipStr) { //fonction pour ajouter des ':'
        String ipStrAvecPoints = "";
        for (int i=0; i<ipStr.length(); i++) {
            if (i%4==0 & i!=0) {
                ipStrAvecPoints += ':';
            }
            ipStrAvecPoints += ipStr.charAt(i);
        }
        return ipStrAvecPoints;
    }

        public static String hexSubstring(String ipSansPoints, int start, int end) {
        String ipNew = "";
            for (int i=start; i<end;i++) {
            ipNew += ipSansPoints.charAt(i);                                //garder la partie à gauche du global routing adresse
        }
        return ipNew;
    }

    public static String nbrSousreseauxDisponibleFormatted(BigInteger bi) {            //formattage pour BigInteger
        DecimalFormat df = new DecimalFormat("###,###,###");
        return df.format(bi);
    }
}
