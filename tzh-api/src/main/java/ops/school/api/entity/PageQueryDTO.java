package ops.school.api.entity;

import java.io.Serializable;

public class PageQueryDTO implements Serializable {



    private Integer page;

    private Integer size;

    private String orderBy;

    private Integer total;

    private String query;

    private String queryType;
}
