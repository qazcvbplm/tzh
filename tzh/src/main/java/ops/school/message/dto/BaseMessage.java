package ops.school.message.dto;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class BaseMessage implements Serializable {

    private String type;

    public BaseMessage(String type) {
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
