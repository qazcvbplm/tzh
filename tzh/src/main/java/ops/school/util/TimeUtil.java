package ops.school.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeUtil {

    // 获取当前时间与订单支付的时间差
    public static Long dateDiff(String startTime, String endTime) {
        // 按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long nh = 1000 * 60 * 60;// 一小时的毫秒数
        long nm = 1000 * 60;// 一分钟的毫秒数
        long diff = 2L;
        long day = 0;
        long min = 0;
        try {
            diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
            min = diff % nd % nh / nm + day * 24 * 60;// 计算差多少分钟
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 输出结果
        return min;
    }

}

