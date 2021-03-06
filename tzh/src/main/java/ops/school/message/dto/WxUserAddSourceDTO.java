package ops.school.message.dto;

public class WxUserAddSourceDTO extends BaseMessage {

    private String openId;

    private Integer source;

    public WxUserAddSourceDTO(String openId, Integer source) {
        super("addsource");
        this.openId = openId;
        this.source = source;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }
}
