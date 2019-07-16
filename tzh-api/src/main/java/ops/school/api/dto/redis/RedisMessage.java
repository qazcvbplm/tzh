package ops.school.api.dto.redis;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class RedisMessage implements Serializable {

    private String type;

    public RedisMessage(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}
