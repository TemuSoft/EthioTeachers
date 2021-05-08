package ethioteacher.dejen.ethioteachers;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hp on 8/25/2017.
 */

public class FirstOrSecondEmester {

    SimpleDateFormat dateFormat;
    Date date;


    public String semester() {
        dateFormat = new SimpleDateFormat("MM");
        date = new Date();
        String currentMonth = dateFormat.format(date).toString();
        String semester = "";
        if (Integer.parseInt(currentMonth) >= 3 && Integer.parseInt(currentMonth) <= 8)
            semester = "Second";
        else if (Integer.parseInt(currentMonth) >= 10 && Integer.parseInt(currentMonth) <= 2)
            semester = "First";
        return semester;
    }
}
