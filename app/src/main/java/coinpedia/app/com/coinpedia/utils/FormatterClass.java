package coinpedia.app.com.coinpedia.utils;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringJoiner;
import java.util.TimeZone;

/**
 * Created by senzec on 24/7/17.
 */

public class FormatterClass {

    private static final String TAG = FormatterClass.class.getSimpleName();
    public String convertDecimalPerc(String value)
    {
        DecimalFormat df = new DecimalFormat("####0.00");
        return df.format(Double.parseDouble(value));
    }
    public String formatPrecisionDetail(Double value)
    {
        if(value<1) {
            DecimalFormat df = new DecimalFormat("####0.0000000");
            return df.format(value);
        }else {
            DecimalFormat df = new DecimalFormat("####0.00");
            return df.format(value);
        }
    }

    public String precisionTwo(String value)
    {
        DecimalFormat df = new DecimalFormat("####0.00");
        return df.format(Double.parseDouble(value));
    }
    public String precisionPerc(Double value)
    {
        DecimalFormat df = new DecimalFormat("####0.00");
        return df.format(value);
    }

    public String precisionSeven(String value)
    {
        DecimalFormat df = new DecimalFormat("####0.0000000");
        return df.format(Double.parseDouble(value));
    }
    public String simpleDate()
    {
     //   Calendar c = Calendar.getInstance();
    //    System.out.println("Current time => " + c.getTime());

        SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mm:ss a dd MMM yy");
        return fmtOut.format(new Date());
    }
    public String getUTCDate(long dateStr) {
        String actualDate = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss a ddMMM,yy");
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            simpleDateFormat.setTimeZone(utcZone);
            Date myDate = simpleDateFormat.parse(String.valueOf(dateStr));
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            String formattedDate = simpleDateFormat.format(myDate);
            actualDate = formattedDate;
        }catch (ParseException pe)
        {
            Log.e(TAG, "Error : "+pe, pe);
            actualDate = simpleDate();
        }
        return actualDate;
    }
}
