package ops.school.api.dto;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/9/6
 * 16:42
 * #
 */
public class TimeEveryDTO {

    private Integer year;

    private Integer month;

    private Integer day;

    private Integer hour;

    private Integer minutes;

    private Integer seconds;


    public TimeEveryDTO(Integer month, Integer day) {
        this.month = month;
        this.day = day;
        this.year = 2019;
        this.hour = 0;
        this.minutes = 0;
        this.seconds= 0;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }
}
