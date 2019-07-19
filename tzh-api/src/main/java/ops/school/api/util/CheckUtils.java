package ops.school.api.util;

import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.util.StringUtils;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/7/19
 * 18:03
 * #
 */
public class CheckUtils {


    public static String checkEmoji(String checkString){
        if (StringUtils.hasText(checkString)){
            return null;
        }
        if(EmojiManager.isEmoji(checkString)){
            return EmojiParser.removeAllEmojis(checkString);
        }
        return checkString;
    }
}
