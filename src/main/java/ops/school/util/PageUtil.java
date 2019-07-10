package ops.school.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class PageUtil {
    private static final Integer page = 0;

    private static final Integer size = 100;

    public static IPage noPage() {
        return new Page(0, 100);
    }
}
