package ops.school.controller;

import com.github.qcloudsms.httpclient.HTTPException;
import ops.school.api.util.Util;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class TestController {

    @RequestMapping("test")
    public void test(){
        try {
            Util.qqsms(1400169549, "0eb188f83ef4b2dc8976b5e76c70581e", "13958933693", 372755, "123123,ghuighiu", null);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
