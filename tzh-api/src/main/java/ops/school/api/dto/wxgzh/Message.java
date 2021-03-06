package ops.school.api.dto.wxgzh;

import com.alibaba.fastjson.JSONObject;

public class Message {

    private String appId;

    private String secret;

    private String toUser;

    private String templateId;

    private String formId;

    private String minPath;

    private String dataKeyWord1;

    private String dataKeyWord2;

    private String dataKeyWord3;

    private String dataKeyWord4;

    private String dataKeyWord5;

    private String dataKeyWord6;

    private String dataKeyWord7;

    private String dataKeyWord8;

    private String dataKeyWord9;

    private String emphasisKeyword;

    public Message() {
    }

    public JSONObject toJson() {
        JSONObject output = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject keyword;
        JSONObject minprogram = null;
        output.put("touser", this.toUser);
        output.put("template_id", this.templateId);
        output.put("form_id",formId);
        output.put("emphasis_keyword",emphasisKeyword);
        if (dataKeyWord1 != null) {
            keyword = new JSONObject();
            keyword.put("value", dataKeyWord1);
            data.put("keyword1", keyword);
        }
        if (dataKeyWord2 != null) {
            keyword = new JSONObject();
            keyword.put("value", dataKeyWord2);
            keyword.put("color", "#173177");
            data.put("keyword2", keyword);
        }
        if (dataKeyWord3 != null) {
            keyword = new JSONObject();
            keyword.put("value", dataKeyWord3);
            keyword.put("color", "#173177");
            data.put("keyword3", keyword);
        }
        if (dataKeyWord4 != null) {
            keyword = new JSONObject();
            keyword.put("value", dataKeyWord4);
            keyword.put("color", "#173177");
            data.put("keyword4", keyword);
        }
        if (dataKeyWord5 != null) {
            keyword = new JSONObject();
            keyword.put("value", dataKeyWord5);
            keyword.put("color", "#173177");
            data.put("keyword5", keyword);
        }
        if (dataKeyWord6 != null) {
            keyword = new JSONObject();
            keyword.put("value", dataKeyWord6);
            keyword.put("color", "#173177");
            data.put("keyword6", keyword);
        }
        if (dataKeyWord7 != null) {
            keyword = new JSONObject();
            keyword.put("value", dataKeyWord7);
            keyword.put("color", "#173177");
            data.put("keyword7", keyword);
        }
        if (dataKeyWord8 != null) {
            keyword = new JSONObject();
            keyword.put("value", dataKeyWord8);
            keyword.put("color", "#173177");
            data.put("keyword8", keyword);
        }
        if (dataKeyWord9 != null) {
            keyword = new JSONObject();
            keyword.put("value", dataKeyWord9);
            keyword.put("color", "#173177");
            data.put("keyword9", keyword);
        }
        output.put("data", data);
        if (minPath != null) {
            minprogram = new JSONObject();
            output.put("page", minprogram);
        }
        System.out.println(output);
        return output;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getMinPath() {
        return minPath;
    }

    public void setMinPath(String minPath) {
        this.minPath = minPath;
    }

    public String getDataKeyWord1() {
        return dataKeyWord1;
    }

    public void setDataKeyWord1(String dataKeyWord1) {
        this.dataKeyWord1 = dataKeyWord1;
    }

    public String getDataKeyWord2() {
        return dataKeyWord2;
    }

    public void setDataKeyWord2(String dataKeyWord2) {
        this.dataKeyWord2 = dataKeyWord2;
    }

    public String getDataKeyWord3() {
        return dataKeyWord3;
    }

    public void setDataKeyWord3(String dataKeyWord3) {
        this.dataKeyWord3 = dataKeyWord3;
    }

    public String getDataKeyWord4() {
        return dataKeyWord4;
    }

    public void setDataKeyWord4(String dataKeyWord4) {
        this.dataKeyWord4 = dataKeyWord4;
    }

    public String getDataKeyWord5() {
        return dataKeyWord5;
    }

    public void setDataKeyWord5(String dataKeyWord5) {
        this.dataKeyWord5 = dataKeyWord5;
    }

    public String getDataKeyWord6() {
        return dataKeyWord6;
    }

    public void setDataKeyWord6(String dataKeyWord6) {
        this.dataKeyWord6 = dataKeyWord6;
    }

    public String getDataKeyWord7() {
        return dataKeyWord7;
    }

    public void setDataKeyWord7(String dataKeyWord7) {
        this.dataKeyWord7 = dataKeyWord7;
    }

    public String getDataKeyWord8() {
        return dataKeyWord8;
    }

    public void setDataKeyWord8(String dataKeyWord8) {
        this.dataKeyWord8 = dataKeyWord8;
    }

    public String getDataKeyWord9() {
        return dataKeyWord9;
    }

    public void setDataKeyWord9(String dataKeyWord9) {
        this.dataKeyWord9 = dataKeyWord9;
    }

    public String getEmphasisKeyword() {
        return emphasisKeyword;
    }

    public void setEmphasisKeyword(String emphasisKeyword) {
        this.emphasisKeyword = emphasisKeyword;
    }

    public Message(String toUser, String templateId, String formId, String minPath, String dataKeyWord1, String dataKeyWord2, String dataKeyWord3, String dataKeyWord4, String dataKeyWord5, String dataKeyWord6, String dataKeyWord7, String dataKeyWord8, String dataKeyWord9, String emphasisKeyword) {
        this.toUser = toUser;
        this.templateId = templateId;
        this.formId = formId;
        this.minPath = minPath;
        this.dataKeyWord1 = dataKeyWord1;
        this.dataKeyWord2 = dataKeyWord2;
        this.dataKeyWord3 = dataKeyWord3;
        this.dataKeyWord4 = dataKeyWord4;
        this.dataKeyWord5 = dataKeyWord5;
        this.dataKeyWord6 = dataKeyWord6;
        this.dataKeyWord7 = dataKeyWord7;
        this.dataKeyWord8 = dataKeyWord8;
        this.dataKeyWord9 = dataKeyWord9;
        this.emphasisKeyword = emphasisKeyword;
    }
}
