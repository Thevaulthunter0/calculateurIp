package calculateurIP;

import java.math.BigInteger;

public class Calcul {

    public static String creerHexString(int start, int end, String hexDigit) { //cette fonction créer une chaîne de caractère hexDigit en spécifiant le début et la fin
        String hexStr = "";
        for (int i = start; i < end; i++) {
            hexStr += hexDigit;
        }
        return hexStr;
    }

    public static String obtientBinEnQuatreBits(String oldBinary) { //ex. binaire "11" à binaire "0011"
        String quatreBitBinaryStr = "";
        for (int i = 0; i < 4 - oldBinary.length(); i++) {
            quatreBitBinaryStr += "0";
        }
        quatreBitBinaryStr += oldBinary;
        return quatreBitBinaryStr;
    }

    public static String obtientBinEnNBits(String oldBinary, int subnetId) { //ex. pour /32, si oldBinary == "11" et subnetId == 32 => retourne binaire "0000 0011"
        String nBitBinaryStr = "";
        for (int i = 0; i < subnetId - oldBinary.length(); i++) {
            nBitBinaryStr += "0";
        }
        nBitBinaryStr += oldBinary;
        return nBitBinaryStr;
    }

    public static String manipulerQuatreBitBin(String oldQuatreBitStr, String bit, int prefix) {
        String newQuatreBitStr = "";
        int restant = prefix % 4; //1

        for (int i=0; i<restant; i++) {                                           //garder les bits à gauche
            newQuatreBitStr += oldQuatreBitStr.charAt(i);
        }
        for (int i=restant; i<4; i++) {                                           //mettre les bits à droite à '0'
            newQuatreBitStr += bit; //1 au lieu de 0 pour dernier adresse (comparé à la première adresse)
        }
        return newQuatreBitStr;
    }

    public static String convertirDecABin(int num) {                            //cette fonction convertit un entier en binaire
        String str = "";
        int nbDeDigitBin = (int)(Math.floor(Math.log(num)/Math.log(2)));        //donner le nb de caractères binaires
        for (int i=nbDeDigitBin; i>= 0; i--) {                                  //pour chaque position binaire,
            if (num - (int)Math.pow(2,i) >= 0) {                                //si le nombre ou restant est plus grand que 2 ^ i,
                str += "1";                                                     //mettre binaire '1'
                num -= (int)Math.pow(2,i);                                      //soustraire pour avoir le restant
            } else {
                str += "0";                                                     //mettre binaire '0' si le nombre ou restant est plus petit que 2 ^ i
            }
        }
        return str;
    }
    public static String convertirDecABin(BigInteger bi) {                  //cette fonction convertit un Big Integer en binaire
        String str = "";
        int nbDeDigitBin = bi.bitLength();                                  //obtient le longueur de bits du big integer
        for (int i=nbDeDigitBin-1; i>= 0; i--) {                            //pour chaque bit, en order déscendant
            BigInteger pow2 = BigInteger.ONE.shiftLeft(i);                  //2^i; BigInteger.ONE est le chiffre '1' et i est le nb de bits où '1'' est positionné dans la chaîne binaire, essentiellement donnant le valeur 2^î
            if (bi.compareTo(pow2)>=0) {                                    //si bi >= pow2 => binaire '1'
                str += "1";
                bi = bi.subtract(pow2);                                     //soustraire pow2 de bi pour obtenir le restant
            } else {
                str += "0";                                                 //sinon bi < pow2 => binaire '0'
            }
        }
        return str;
    }
    public static String convertirBinAHex(String bin) {
        String str = "";
        int accumulator = 0;

        for (int i = bin.length() - 1; i >= 0; i -= 4) {                //en groupes de 4 bits, on itère les caractères de la chaîne binaire. Après chaque itération, i = i - 4;
            int power = 0;
            for (int j = 0; j < 4; j++) {
                if (i - j >= 0 && bin.charAt(i - j) == '1') {           //Si c'est un '1' à la position binaire,
                    accumulator += Math.pow(2, power);                  //accumule 2 ^ power selon la position
                }
                power++;
            }

            if (accumulator >= 10 && accumulator <= 15) {
                str = (char) ('a' + accumulator - 10) + str;                //Si c'est un valeur entre 10-15, soustraire 10 et additionne 'a'(ASCII) pour avoir le bon caractère a-f. Append le caractère
            } else {
                str = accumulator + str;                                    //sinon append le chiffre 0-9
            }
            accumulator = 0;                                                //reset accumulator pour la prochaine itération
        }
        return str;
    }

    public static int convertirHexDigitADec(char hex) {                      //convertir un hexDigit en décimal
        return Character.digit(hex, 16);
    }


}
