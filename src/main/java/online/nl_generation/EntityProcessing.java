/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package online.nl_generation;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import settings.Settings;

/**
 *
 * @author aorogat
 */
public class EntityProcessing {

    public static String decide_quotes(String O, String type) {
        type = Settings.explorer.removePrefix(type);
        String O_with_quetes;
        
        System.out.println("O:" + O);
        System.out.println("type:" + type);
        
        
        if(O.length()>24){
            O_with_quetes = "the " + type + " \"" + O + "\"";
            return O_with_quetes;
        }
        
        if(type.equals(Settings.Number) || type.equals(Settings.Date))
                return O;
        
        
        
        
        StringTokenizer st = new StringTokenizer(O);
        if (st.countTokens() >= 4) {
            O_with_quetes = "the " + type + " \"" + O + "\"";
            return O_with_quetes;
        } else {
            O_with_quetes = O;
        }

        if (st.countTokens() == 1) {
            Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(O);
            boolean hasSpecialCharacter = m.find();

            if (hasSpecialCharacter) {
                O_with_quetes = "the " + type + " \"" + O + "\"";
                return O_with_quetes;
            }
        }

        return O_with_quetes;
    }

    private static Pattern NumberPattern = Pattern.compile("-?\\d+(\\.\\d+)?([eE][-\\+]?\\d+(\\.\\d+)?)?");

    public static boolean isNumeric(String strNum) {
        if(strNum == null) {
            return false;
        }
        return NumberPattern.matcher(strNum).matches();
    }
    private static Pattern DATE_PATTERN = Pattern.compile(
      "^((2000|2400|2800|(19|2[0-9])(0[48]|[2468][048]|[13579][26]))-02-29)$" 
      + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
      + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$" 
      + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$");

    public static boolean isDate(String date) {
        return DATE_PATTERN.matcher(date).matches();
    }

}
