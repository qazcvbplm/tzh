package ops.school.api.dto.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import ops.school.api.entity.WxUser;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


public class WxUserDTO extends BaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 微信用户唯一标识
     */
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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
}