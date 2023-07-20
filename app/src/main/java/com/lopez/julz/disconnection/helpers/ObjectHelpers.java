package com.lopez.julz.disconnection.helpers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.core.app.ActivityCompat;

import com.lopez.julz.disconnection.dao.DisconnectionList;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ObjectHelpers {
    public static String dbName() {
        return "ReadAndBill";
    }


    public static String getCurrentTimestamp() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            return sdf.format(new Date());
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCurrentTime() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

            return sdf.format(new Date());
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCurrentDate() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            return sdf.format(new Date());
        } catch (Exception e) {
            return null;
        }
    }

    public static String generateRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static String getTimeInMillis() {
        try {
            return new Date().getTime() + "";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatShortDateWithoutYear(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            sdf = new SimpleDateFormat("MMM");
            return sdf.format(d);
        } catch (Exception e) {
            Log.e("ERR_FORMAT_DATE", e.getMessage());
            return "";
        }
    }

    public static String formatShortDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            sdf = new SimpleDateFormat("MMM yyyy");
            return sdf.format(d);
        } catch (Exception e) {
            Log.e("ERR_FORMAT_DATE", e.getMessage());
            return "";
        }
    }

    public static String formatShortDateWithDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            sdf = new SimpleDateFormat("MMM dd, yyyy");
            return sdf.format(d);
        } catch (Exception e) {
            Log.e("ERR_FORMAT_DATE", e.getMessage());
            return "";
        }
    }

    public static String formatNumericDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            sdf = new SimpleDateFormat("MM-dd-yyyy");
            return sdf.format(d);
        } catch (Exception e) {
            Log.e("ERR_FORMAT_DATE", e.getMessage());
            return "";
        }
    }

    public static String formatSqlDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(date);
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(d);
        } catch (Exception e) {
            Log.e("ERR_FORMAT_DATE", e.getMessage());
            return "";
        }
    }

    public static String roundFour(Double doubleX) {
        try {
            DecimalFormat df = new DecimalFormat("#,###,###.####");
            return df.format(doubleX);
        } catch (Exception e) {
            return "";
        }
    }

    public static String roundFourNoComma(Double doubleX) {
        try {
            DecimalFormat df = new DecimalFormat("#.####");
            return df.format(doubleX);
        } catch (Exception e) {
            return "";
        }
    }

    public static String roundTwo(Double doubleX) {
        try {
            DecimalFormat df = new DecimalFormat("#,###,###.##");
            return df.format(doubleX);
        } catch (Exception e) {
            return "";
        }
    }

    public static String roundTwoNoComma(Double doubleX) {
        try {
            DecimalFormat df = new DecimalFormat("#.##");
            return df.format(doubleX);
        } catch (Exception e) {
            return "";
        }
    }

    public static Double doubleStringNull(String regex) {
        try {
            if (regex.equals(null)) {
                return 0.0;
            } else {
                return Double.valueOf(regex);
            }
        } catch (Exception e) {
            Log.e("ERR_DBL_STRNG_NLL", e.getMessage());
            return 0.0;
        }
    }

    public static String getSelectedTextFromRadioGroup(RadioGroup rg, View view) {
        try {
            int selectedId = rg.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) view.findViewById(selectedId);
            return radioButton.getText().toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String[] getPreviousMonths(int numberOfMonths) {
        try {
            String[] months = new String[numberOfMonths];

            for (int i=0; i<months.length; i++) {
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.MONTH, -i);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

                months[i] =  sdf.format(c.getTime()) + "-01";
            }

            return months;
        } catch (Exception e) {
            return new String[]{};
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static double getTotalAmount(List<DisconnectionList> disconnectionLists) {
        try {
            double sum = 0;
            for (DisconnectionList disco:
                 disconnectionLists) {
                double amnt = 0;
                if (disco.isSelected()) {
                    amnt = Double.valueOf(disco.getNetAmount());
                } else {
                    amnt = 0;
                }
                sum += amnt;
            }

            return sum;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public static double getTotalSucharges(List<DisconnectionList> disconnectionLists) {
        try {
            double sum = 0;
            for (DisconnectionList disco:
                    disconnectionLists) {
                double amnt = 0;
                if (disco.isSelected()) {
                    amnt = Double.valueOf(disco.getSurcharge());
                } else {
                    amnt = 0;
                }
                sum += amnt;
            }

            return sum;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public static double getTotalAmountPaid(List<DisconnectionList> disconnectionLists) {
        try {
            double sum = 0;
            for (DisconnectionList disco:
                    disconnectionLists) {
                double amnt = 0;
                if (disco.isSelected()) {
                    amnt = Double.valueOf(disco.getPaidAmount());
                } else {
                    amnt = 0;
                }
                sum += amnt;
            }

            return sum;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public static double getTotalPaidAmount(DisconnectionList disconnectionList) {
        try {
            double netAmount = Double.valueOf(disconnectionList.getNetAmount());
            double surcharges = Double.valueOf(disconnectionList.getSurcharge());

            return netAmount + surcharges;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
