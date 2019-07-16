package ops.school.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public class PageUtil {
    private static final Integer page = 0;

    private static final Integer size = 100;

    public static IPage noPage() {
        return new Page(0, 100);
    }

    public static IPage getPage(Integer page, Integer size) {
        if (page == null || page < 0) {
            page = 0;
        }
        if (size == null || size < 0) {
            size = 10;
        }
        return new Page(page, size);
    }
}
