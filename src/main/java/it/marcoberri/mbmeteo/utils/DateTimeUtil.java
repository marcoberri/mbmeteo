/**
 *  Copyright 2011 Marco Berri - marcoberri@gmail.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 **/
package it.marcoberri.mbmeteo.utils;

import java.sql.*;
import java.util.*;
import java.text.*;

/**
 *
 * @author Marco Berri marcoberri@gmail.com
 */
public final class DateTimeUtil {

    /**
     *
     */
    public static final String tsDefaultFormats[] = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss", "HH:mm", "yyyy-MM"};

    /**
     * Return a String value of Now() in a specify format
     * @param lang lingua di default
     * @param format formato della data vedi esempi tsDefaultFormats
     * @return String value of Now() in a specify format
     */
    public static final String getNowWithFormat(String format, String lang) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        String DATE_FORMAT = format;
        SimpleDateFormat sdf = null;

        if (lang != null) {
            sdf = new SimpleDateFormat(DATE_FORMAT, new Locale(lang));
        } else {
            sdf = new SimpleDateFormat(DATE_FORMAT);
        }

        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(cal.getTime());
    }//getNowWithFormat()

    /**
     *
     * @param month
     * @param year
     * @return
     */
    public static final int getlastDateofThisMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        if (year > -1) {
            calendar.set(Calendar.YEAR, year);
        }

        if (month > -1) {
            month--;
            calendar.set(Calendar.MONTH, month);
        }

        return calendar.getActualMaximum(Calendar.DATE);
    }//lastDateofThisMonth()

    /**
     *
     * @return
     */
    public static final int getlastDateofThisMonth() {
        return getlastDateofThisMonth(-1, -1);
    }//getlastDateofThisMonth()

    /**
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static final int getDayOfWeek(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();

        if (year > -1) {
            calendar.set(Calendar.YEAR, year);
        }

        if (month > -1) {
            month--;
            calendar.set(Calendar.MONTH, month);
        }

        if (day > -1) {
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }

        return calendar.get(Calendar.DAY_OF_WEEK);
    }//getDayOfWeek()

    /**
     *
     * @param format
     * @return
     */
    public static final String getNowWithFormat(String format) {
        return getNowWithFormat(format, null);
    }////getNowWithFormat()

    /**
     * Return a String value of Now()
     *
     * @return a java.sql.Timestamp representing current timestamp
     */
    public static final Timestamp getNow() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        return new Timestamp(cal.getTimeInMillis());
    }//getNow()

    /**
     * Return a String value of Now() with an offset
     * es: -1d per il giorno prima
     * es: +3h per tre ore dopo
     * es: 5m tra 5 minuti
     * es: 5M tra 5 mesi (mese standard = 30 giorni)
     *
     * @param offset
     * @return a java.sql.Timestamp representing current timestamp with an offset
     */
    public static final Timestamp getNowWithOffset(String offset) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        long now = cal.getTimeInMillis();
        if (offset != null && offset.length() > 0) {
            char u = offset.charAt(offset.length() - 1);
            offset = offset.substring(0, offset.length() - 1);
            int n = Default.toInt(offset, 0);
            long U = 0;
            switch (u) {
                case 'm':
                    U = 60000L;
                    break;
                case 'h':
                    U = 3600000L;
                    break;
                case 'd':
                    U = 86400000L;
                    break;
                case 'M':
                    U = 2592000000L;
                    break;
            }
            now += (n * U);
        }
        return new Timestamp(now);
    }//getNow()

    /**
     * Return a String value of Now() with an offset in a specify format
     *
     * @param offset
     * @param format
     * @return a java.sql.Timestamp representing current timestamp with an offset in a specify format
     */
    public static final String getNowWithOffsetAndFormat(String offset, String format) {
        return dateFormat(format, getNowWithOffset(offset));
    }//getNow()

    /**
     * Return a String value of Now() with an offset in a specify format
     *
     * @param offset 
     * @param format
     * @param locale
     * @return a java.sql.Timestamp representing current timestamp with an offset in a specify format
     */
    public static final String getNowWithOffsetAndFormat(String offset, String format, String locale) {
        return dateFormat(format, getNowWithOffset(offset), null, new Locale(locale));
    }//getNow()

    /**
     *
     * @param format
     * @param date
     * @param tz
     * @param locale
     * @return
     */
    public static final String dateFormat(String format, java.util.Date date, String tz, Locale locale) {
        if (date == null) {
            return null;
        }

        if (format == null) {
            for (int i = 0; i < tsDefaultFormats.length; i++) {
                String dFormat = tsDefaultFormats[i];
                try {
                    String df = dateFormat(dFormat, date, tz, locale);
                    if (df != null) {
                        return df;
                    }
                } catch (Exception x) {
                }
            }//for
        }//if

        if (format == null) {
            return null;
        }

        SimpleDateFormat sdf = null;
        if (locale == null) {
            sdf = new SimpleDateFormat(format);
        } else {
            sdf = new SimpleDateFormat(format, locale);
        }
        if (tz != null) {
            sdf.setTimeZone(TimeZone.getTimeZone(tz));
        }
        return sdf.format(date);
    }//dateFormat()

    /**
     *
     * @param format
     * @param date
     * @return
     */
    public static final String dateFormat(String format, java.util.Date date) {
        return dateFormat(format, date, null, null);
    }//dateFormat()

    /**
     *
     * @param format
     * @param date
     * @param language
     * @return
     */
    public static final String dateFormat(String format, java.util.Date date, String language) {
        Locale loc = new Locale(language);
        return dateFormat(format, date, null, loc);
    }//dateFormat()

    /**
     *
     * @param format
     * @param date
     * @param locale
     * @return
     */
    public static final String dateFormat(String format, java.util.Date date, Locale locale) {
        return dateFormat(format, date, null, locale);
    }//dateFormat()

    /**
     *
     * @param format
     * @param millis
     * @param tz
     * @param locale
     * @return
     */
    public static final String dateFormat(String format, long millis, String tz, Locale locale) {
        return dateFormat(format, new java.util.Date(millis), tz, locale);
    }//dateFormat()

    /**
     *
     * @param format
     * @param millis
     * @return
     */
    public static final String dateFormat(String format, long millis) {
        return dateFormat(format, millis, null, null);
    }//dateFormat()

    /**
     *
     * @param format
     * @param date
     * @param tz
     * @param locale
     * @return
     */
    public static final Timestamp getTimestamp(String format, String date, String tz, Locale locale) {
        java.util.Date d = getDate(format, date, tz, locale);
        return (d == null ? null : new Timestamp(d.getTime()));
    }//getTimestamp()

    /**
     *
     * @param format
     * @param date
     * @return
     */
    public static final Timestamp getTimestamp(String format, String date) {
        return getTimestamp(format, date, null, null);
    }//getTimestamp()

    /**
     *
     * @param format
     * @param date
     * @param tz
     * @param locale
     * @return
     */
    public static final java.util.Date getDate(String format, String date, String tz, Locale locale) {
        String s = date;
        if (StringUtil.isEmpty(s)) {
            return null;
        }

        if (format == null) {
            for (int i = 0; i < tsDefaultFormats.length; i++) {
                String dFormat = tsDefaultFormats[i];
                Timestamp ts = getTimestamp(dFormat, date, tz, locale);
                if (ts != null) {
                    return ts;
                }
            }//for
        }//if

        if (format == null) {
            return null;
        }

        SimpleDateFormat sdf = null;	//"yyyy-MM-dd HH:mm:ss"
        if (locale == null) {
            sdf = new SimpleDateFormat(format);
        } else {
            sdf = new SimpleDateFormat(format, locale);
        }
        if (tz != null) {
            sdf.setTimeZone(TimeZone.getTimeZone(tz));
        }
        try {
            return new java.util.Date(sdf.parse(s).getTime());
        } catch (ParseException e) {
        }
        return null;
    }//getTimestamp()

    /**
     *
     * @param format
     * @param date
     * @return
     */
    public static final java.util.Date getDate(String format, String date) {
        return getDate(format, date, null, null);
    }

    /**
     *
     * @param format
     * @param date
     * @param tz
     * @param locale
     * @return
     */
    public static final long getTime(String format, String date, String tz, Locale locale) {
        java.util.Date d = getDate(format, date, tz, locale);
        return (d == null ? 0 : d.getTime());
    }//getTime()

    /**
     *
     * @param format
     * @param date
     * @return
     */
    public static final long getTime(String format, String date) {
        return getTime(format, date, null, null);
    }

    /**
     *
     * @param format
     * @param date
     * @param offset
     * @return
     */
    public static final Timestamp getTimeWithOffset(String format, String date, String offset) {
        long time = getTime(format, date);
        if (offset != null && offset.length() > 0) {
            char u = offset.charAt(offset.length() - 1);
            offset = offset.substring(0, offset.length() - 1);
            int n = Default.toInt(offset, 0);
            long U = 0;
            switch (u) {
                case 'm':
                    U = 60000L;
                    break;
                case 'h':
                    U = 3600000L;
                    break;
                case 'd':
                    U = 86400000L;
                    break;
                case 'M':
                    U = 2592000000L;
                    break;
            }
            time += (n * U);
        }
        return new Timestamp(time);
    }//getTimeWithOffset()

    /**
     *
     * @param date
     * @param informat
     * @param outformat
     * @param tz
     * @param locale
     * @return
     */
    public static final String changeDateFormat(String date, String informat, String outformat, String tz, Locale locale) {

        if (StringUtil.isEmpty(date) || StringUtil.isEmpty(informat) || StringUtil.isEmpty(outformat)) {
            return null;
        }

        java.util.Date d = getDate(informat, date, tz, locale);

        if (d == null) {
            return null;
        }

        return dateFormat(outformat, d, tz, locale);
    }

    /**
     *
     * @param date
     * @param informat
     * @param outformat
     * @return
     */
    public static final String changeDateFormat(String date, String informat, String outformat) {
        return changeDateFormat(date, informat, outformat, null, null);
    }

    /**
     *
     * @param date
     * @param format
     * @param field
     * @return
     */
    public static final String getMax(String date, String format, String field) {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTimeInMillis(getTime(format, date));
        if (field.equalsIgnoreCase("DAY_OF_MONTH")) {
            return Default.toString("" + cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        } else if (field.equalsIgnoreCase("DAY_OF_WEEK")) {
            return Default.toString("" + cal.getActualMaximum(Calendar.DAY_OF_WEEK));
        } else if (field.equalsIgnoreCase("DAY_OF_YEAR")) {
            return Default.toString("" + cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        } else {
            return date;
        }
    }

    /**
     *
     * @param offsetField
     * @param offsetValue
     * @param format
     * @return
     */
    public static final String getNowWithOffset(String offsetField, int offsetValue, String format) {

        Calendar rightNow = Calendar.getInstance(TimeZone.getDefault());
        if (offsetField.equalsIgnoreCase("YEAR")) {
            rightNow.add(Calendar.YEAR, offsetValue);
        } else if (offsetField.equalsIgnoreCase("MONTH")) {
            rightNow.add(Calendar.MONTH, offsetValue);
        } else if (offsetField.equalsIgnoreCase("DAY")) {
            rightNow.add(Calendar.DATE, offsetValue);
        }
        return dateFormat(format, rightNow.getTime(), "it_IT", new Locale("it"));
    }
}//DateTimeUtil

