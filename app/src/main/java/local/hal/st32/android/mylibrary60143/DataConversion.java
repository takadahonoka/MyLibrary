package local.hal.st32.android.mylibrary60143;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataConversion {

    private static final SimpleDateFormat dfDate01 = new SimpleDateFormat("yyyy年MM月dd日");
    private static final SimpleDateFormat dfDate02 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat dfDate03 = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat dfDate04 = new SimpleDateFormat("MM");
    private static final SimpleDateFormat dfDate05 = new SimpleDateFormat("dd");

    /**
     *yyyy年MM年dd日からyyyy-MM-ddに変換。
     * @param date(String)
     * @return
     */
    public String getDataConversion01(String date){
        String strData = "";
        try {
            Date d = dfDate01.parse(date);
            strData = dfDate02.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスのyyyy年MM年dd日からyyyy-MM-ddに変換時。");
        }
        return strData;
    }

    /**
     *yyyy-MM-ddからyyyy年MM年dd日に変換。
     * @param date(String)
     * @return String
     */
    public String getDataConversion02(String date){
        String strData = "";
        try {
            Date d = dfDate02.parse(date);
            strData = dfDate01.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスのyyyy-MM-ddからyyyy年MM年dd日に変換時。");
        }
        return strData;
    }

    /**
     *yyyy-MM-ddからyyyyに変換。
     * @param date(String)
     * @return String
     */
    public String getDataConversion03(String date){
        String strData = "";
        try {
            Date d = dfDate02.parse(date);
            strData = dfDate03.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスのyyyy-MM-ddからyyyyに変換時。");
        }
        return strData;
    }

    /**
     *yyyy-MM-ddからMMに変換。
     * @param date(String)
     * @return String
     */
    public String getDataConversion04(String date){
        String strData = "";
        try {
            Date d = dfDate02.parse(date);
            strData = dfDate04.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスのyyyy-MM-ddからMMに変換時。");
        }
        return strData;
    }

    /**
     *yyyy-MM-ddからddに変換。
     * @param date(String)
     * @return String
     */
    public String getDataConversion05(String date){
        String strData = "";
        try {
            Date d = dfDate02.parse(date);
            strData = dfDate05.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("データ変換失敗", "DataConversionクラスのyyyy-MM-ddからddに変換時。");
        }
        return strData;
    }
}
