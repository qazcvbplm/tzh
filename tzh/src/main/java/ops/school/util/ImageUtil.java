package ops.school.util;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import ops.school.api.entity.Shop;
import ops.school.api.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Image process examples.
 */
public class ImageUtil {

    /**
     * 水印
     * @param shopImage 店铺图片
     * @param codeImg 店铺二维码图片
     * @param path 店铺图片与二维码合并之后的图片存放的路径
     * @return
     * @throws IOException
     */
    public static Map<String,Object> pictureCombine(String shopImage, String codeImg, String path, Shop shop) {
        /**
         * watermark(位置，水印图，透明度)
         */
        Map<String,Object> map = new HashMap<>();
        String imagePath = "https://www.chuyinkeji.cn/shopbarcode/";
        int randomNumber = (int)(Math.random() * 50 + 1);
        // 店铺图片与二维码合并之后的图片
        String shopCodeImage = path + "yzxy" +shop.getId() + ".jpg";
        // 对店铺图片进行压缩
        String tempCodeImage = path + "temp" + randomNumber + ".jpg";
        // 切圆之后的图片
        InputStream is = QiNiuUtils.downloadHttpFile(shopImage);
        try {
            Thumbnails.of(is).size(190, 190).keepAspectRatio(false).outputQuality(0.8f).toFile(tempCodeImage);
            CircleUtil.circleUtil(tempCodeImage,100,100);
            Thumbnails.of(codeImg).size(430, 430).keepAspectRatio(false).watermark(
                    Positions.CENTER,
                    ImageIO.read(new File(tempCodeImage)), 0.9f)
                    .outputQuality(0.8f).toFile(shopCodeImage);
        } catch (IOException e) {
            e.printStackTrace();
            map.put("code",0);
            return map;
        }
        map.put("code",1);
        map.put("shopCodeImage",imagePath +"yzxy" +shop.getId() + ".jpg");
        return map;
    }


    /**
     * 指定大小进行缩放
     *
     * @throws IOException
     */
    private void test1() throws IOException {
        /*
         * size(width,height) 若图片横比200小，高比300小，不变
         * 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
         * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
         */
        Thumbnails.of("images/test.jpg").size(200, 300).toFile(
                "C:/image_200x300.jpg");
        Thumbnails.of("images/test.jpg").size(2560, 2048).toFile(
                "C:/image_2560x2048.jpg");
    }

    /**
     * 按照比例进行缩放
     *
     * @throws IOException
     */
    private void test2() throws IOException {
        /**
         * scale(比例)
         */
        Thumbnails.of("images/test.jpg").scale(0.25f)
                .toFile("C:/image_25%.jpg");
        Thumbnails.of("images/test.jpg").scale(1.10f).toFile(
                "C:/image_110%.jpg");
    }

    /**
     * 不按照比例，指定大小进行缩放
     *
     * @throws IOException
     */
    private void test3() throws IOException {
        /**
         * keepAspectRatio(false) 默认是按照比例缩放的
         */
        Thumbnails.of("images/test.jpg").size(120, 120).keepAspectRatio(false)
                .toFile("C:/image_120x120.jpg");
    }

    /**
     * 裁剪
     *
     * @throws IOException
     */
    private void test6() throws IOException {
        /**
         * 图片中心400*400的区域
         */
        Thumbnails.of("images/test.jpg").sourceRegion(Positions.CENTER, 400,
                400).size(200, 200).keepAspectRatio(false).toFile(
                "C:/image_region_center.jpg");
        /**
         * 图片右下400*400的区域
         */
        Thumbnails.of("images/test.jpg").sourceRegion(Positions.BOTTOM_RIGHT,
                400, 400).size(200, 200).keepAspectRatio(false).toFile(
                "C:/image_region_bootom_right.jpg");
        /**
         * 指定坐标
         */
        Thumbnails.of("images/test.jpg").sourceRegion(600, 500, 400, 400).size(
                200, 200).keepAspectRatio(false).toFile(
                "C:/image_region_coord.jpg");
    }

    /**
     * 输出到OutputStream
     *
     * @throws IOException
     */
    private void test8() throws IOException {
        /**
         * toOutputStream(流对象)
         */
        OutputStream os = new FileOutputStream(
                "C:/image_1280x1024_OutputStream.png");
        Thumbnails.of("images/test.jpg").size(1280, 1024).toOutputStream(os);
    }

    /**
     * 输出到BufferedImage
     *
     * @throws IOException
     */
    private void test9() throws IOException {
        /**
         * asBufferedImage() 返回BufferedImage
         */
        BufferedImage thumbnail = Thumbnails.of("images/test.jpg").size(1280,
                1024).asBufferedImage();
        ImageIO.write(thumbnail, "jpg", new File(
                "C:/image_1280x1024_BufferedImage.jpg"));
    }

}

