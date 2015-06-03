package com.drogueria.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by a983060 on 02/06/2015.
 */
public class DateUtils {

    public static Date getDateFrom(int daysAgo) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, daysAgo);
        date.setTime(c.getTime().getTime());
        return date;
    }
}
