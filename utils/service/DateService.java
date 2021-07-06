package tz.go.tcra.lims.utils.service;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Service
public class DateService {

    Date date = new Date();
    Calendar calendar = Calendar.getInstance();

    LocalDate today = LocalDate.now();

    //Return current / today's date
    public Date currentDate() throws ParseException {
        SimpleDateFormat dt = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date date = dt.parse(new Date().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateFormat.format(date));
    }

    //Return date of the first day of the current week.
    public Date thisWeekStartDate() throws ParseException {
        calendar.setTime(date);
        int i = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
        calendar.add(Calendar.DATE, -i);
        SimpleDateFormat dt = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date date = dt.parse(calendar.getTime().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateFormat.format(date));
    }

    //Return date of the last day of the current week.
    public Date thisWeekEndDate() {
        calendar.add(Calendar.DATE, 6);
        return calendar.getTime();
    }

    //Return first Date of this month
    public Date thisMonthStartDate() throws ParseException {
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat dt = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
        Date date = dt.parse(calendar.getTime().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return new SimpleDateFormat("yyyy-MM-dd").parse(dateFormat.format(date));
    }

    //Returns the date of the specified number of n previous days
    public LocalDate dateOfLastnDays(long nDays) {
        LocalDate requiredDate = today.minusDays(nDays);
        return requiredDate;
    }

    //Returns the date of the specified number of n coming days
    public LocalDate dateOfNextnDays(int nDays) {
        LocalDate requiredDate = today.plusDays(nDays);
        return requiredDate;
    }
}
