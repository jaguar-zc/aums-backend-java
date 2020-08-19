package cn.stackflow.aums.common.image;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 生成圆形头像-根据用户名的首字符
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-05 09:42
 */
public class CreateNamePicture {
    /**
     * @throws IOException
     * @throws
     **/
    public static void main(String[] args) throws IOException {
        List<String> nameList = Arrays.asList("A", "R", "张");
        for (int i = 0; i < nameList.size(); i++) {
            generateImg(nameList.get(i));
        }

    }

    /**
     * @param @param  name
     * @param @throws IOException    设定文件
     * @return void    返回类型
     * @throws
     * @Title: generateImg(生成图片)
     */
    public static InputStream generateImg(String name) throws IOException {
        int width = 100;
        int height = 100;
        int fontSize = 60;

        String first = name.substring(0, 1);
        String filename = "D:/" + name + ".jpg";
        System.out.println(filename);
        File file = new File(filename);
        Font font = new Font("黑体", Font.PLAIN, fontSize);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setBackground(getRandomColor());
        g2.clearRect(0, 0, width, height);
        g2.setFont(font);
        g2.setPaint(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics(font);
        int textWidth = fm.stringWidth(first);

        int widthX = (width - textWidth) / 2;
        // 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
        g2.drawString(first,widthX,height-(fontSize-(fontSize/2)));

//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream()
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
        ImageIO.write(makeRoundedCorner(bi,width), "png", imOut);
        InputStream is = new ByteArrayInputStream(bs.toByteArray());
        return is;
    }


    /**
     * makeRoundedCorner(图片做圆角处理)
     * @Title: makeRoundedCorner
     * @param @param image
     * @param @param cornerRadius
     * @param @return    设定文件
     * @return BufferedImage    返回类型
     * @throws
     */

    public static BufferedImage makeRoundedCorner(BufferedImage image,
                                                  int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h,  BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return output;
    }


    /**
     * drawStar(如果名字为三个字，调用这个方法画五角星)
     *
     * @param @param g2    设定文件
     * @return void    返回类型
     * @throws
     * @Title: drawStar
     */
    private static void drawStar(Graphics2D g2) {
        //定义外切圆和内切圆的半径
        int R = 16;
        int r = (int) (R * Math.sin(Math.PI / 10) / Math.sin(3 * Math.PI / 10));
        //定义两个数组，分别存放10个顶点的X，Y坐标
        int[] x = new int[10];
        int[] y = new int[10];
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                x[i] = 30 + (int) (R * Math.cos(Math.PI / 10 + (i - 1) * Math.PI / 5));
                y[i] = 70 + (int) (R * Math.sin(Math.PI / 10 + (i - 1) * Math.PI / 5));
            } else {
                x[i] = 30 + (int) (r * Math.cos(Math.PI / 10 + (i - 1) * Math.PI / 5));
                y[i] = 70 + (int) (r * Math.sin(Math.PI / 10 + (i - 1) * Math.PI / 5));
            }
        }
        g2.setPaint(Color.white);
        // 调用fillPolygon方法绘制
        g2.fillPolygon(x, y, 10);
    }

    /**
     * getRandomColor(随机产生颜色)
     *
     * @param @return 设定文件
     * @return Color    返回类型
     * @throws
     * @Title: getRandomColor
     */
    private static Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }
}
