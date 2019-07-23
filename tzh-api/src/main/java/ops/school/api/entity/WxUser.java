package ops.school.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * wx_user
 * @author 
 */
@Table(name="wx_user")
public class WxUser extends Base implements Serializable {
    /**
     * 微信用户唯一标识
     */
    @TableId(type = IdType.INPUT)
    private String openId;

    /**
     * 主键id
     */
    @NotNull
    private Long id;

    /**
     * 微信昵称
     */
    @NotNull
    private String nickName;

    /**
     * 微信头像
     */
    @NotNull
    private String avatarUrl;

    /**
     * 性别
     */
    @NotNull
    private String gender;

    /**
     * 省
     */
    @NotNull
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 手机号码
     */
    @NotNull
    private String phone;

    /**
     * 来源
     */
    @NotNull
    private String client;

    /**
     * 学校id
     */
    @NotNull
    private Integer schoolId;

    /**
     * 主体id
     */
    @NotNull
    private Integer appId;

    /**
     * 公众号openid
     */
    private String gzhOpenId;

    private WxUserBell bell;

    public WxUserBell getBell() {
        return bell;
    }

    public void setBell(WxUserBell bell) {
        this.bell = bell;
    }

    public WxUser(String openId, String client) {
        super();
        this.openId = openId;
        this.client = client;
    }

    public WxUser() {
        super();
    }

    private static final long serialVersionUID = 1L;

    public String getGzhOpenId() {
        return gzhOpenId;
    }

    public void setGzhOpenId(String gzhOpenId) {
        this.gzhOpenId = gzhOpenId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        WxUser other = (WxUser) that;
        return (this.getOpenId() == null ? other.getOpenId() == null : this.getOpenId().equals(other.getOpenId()))
            && (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getNickName() == null ? other.getNickName() == null : this.getNickName().equals(other.getNickName()))
            && (this.getAvatarUrl() == null ? other.getAvatarUrl() == null : this.getAvatarUrl().equals(other.getAvatarUrl()))
            && (this.getGender() == null ? other.getGender() == null : this.getGender().equals(other.getGender()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getPhone() == null ? other.getPhone() == null : this.getPhone().equals(other.getPhone()))
            && (this.getClient() == null ? other.getClient() == null : this.getClient().equals(other.getClient()))
            && (this.getSchoolId() == null ? other.getSchoolId() == null : this.getSchoolId().equals(other.getSchoolId()))
            && (this.getAppId() == null ? other.getAppId() == null : this.getAppId().equals(other.getAppId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getOpenId() == null) ? 0 : getOpenId().hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getNickName() == null) ? 0 : getNickName().hashCode());
        result = prime * result + ((getAvatarUrl() == null) ? 0 : getAvatarUrl().hashCode());
        result = prime * result + ((getGender() == null) ? 0 : getGender().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getPhone() == null) ? 0 : getPhone().hashCode());
        result = prime * result + ((getClient() == null) ? 0 : getClient().hashCode());
        result = prime * result + ((getSchoolId() == null) ? 0 : getSchoolId().hashCode());
        result = prime * result + ((getAppId() == null) ? 0 : getAppId().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", openId=").append(openId);
        sb.append(", id=").append(id);
        sb.append(", nickName=").append(nickName);
        sb.append(", avatarUrl=").append(avatarUrl);
        sb.append(", gender=").append(gender);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", phone=").append(phone);
        sb.append(", client=").append(client);
        sb.append(", schoolId=").append(schoolId);
        sb.append(", appId=").append(appId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}