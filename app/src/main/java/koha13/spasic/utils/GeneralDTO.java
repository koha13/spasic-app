package koha13.spasic.utils;

public class GeneralDTO {
    public static String secondToMinute(int length) {
        String hour = String.valueOf(length / 3600);
        String min = String.valueOf(length % 3600 / 60);
        String sec = String.valueOf(length % 3600 % 60);
        String rs = null;
        if (Integer.parseInt(sec) < 10) sec = "0" + sec;
        if (Integer.parseInt(hour) > 0) {
            min = Integer.parseInt(min) < 10 ? "0" + min : min;
            rs = hour + ":" + min + ":" + sec;
        } else {
            rs = min + ":" + sec;
        }
        return rs;
    }
}
