package com.andressantibanez.sarapp;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by Andrés on 2/26/16.
 */
public class Utils {

    public static String asMoneyString(double value) {
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormatSymbols.setGroupingSeparator(',');
        return "$ " + new DecimalFormat("####.##", decimalFormatSymbols).format(value);
    }

}
