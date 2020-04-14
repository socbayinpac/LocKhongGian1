import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class LoKhongGian {

    private static int RONG;
    private static int CAO;
    private static int ALPHA;

    public static void main(String[] args) {
        BufferedImage img = null;
        File f = null;
        //lay anh bulldog.jpg
        try {
            f = new File("bulldog.jpg");
            img = ImageIO.read(f);
        } catch (IOException e) {
            System.out.println(e);
        }
        //lay chieu cao chieu rong
        RONG = img.getWidth();
        CAO = img.getHeight();


        // duyet tung pixel de xu ly diem
        for (int y = 0; y < CAO; y++) {
            for (int x = 0; x < RONG; x++) {
                int[][] mauKhongGian = layMauKhongGian(img, x, y);

                // Bai 1
                // int[] locKhongGian = locMin(mauKhongGian);

                //Bai 2
                // int[] locKhongGian = locMax(mauKhongGian);

                //Bai 3
                // int[] locKhongGian = locTrungBinh(mauKhongGian);

                //Bai 4
                // int[] locKhongGian = locTrungVi(mauKhongGian);

                //Bai 5
                int[] locKhongGian = locTrungBinhTrongSo(mauKhongGian);

                //Bai 6: Ca 5 bai deu ap dung nhan ban duong bien

                //gan pixel tro lai
                int p = (ALPHA << 24) | (locKhongGian[0] << 16) | (locKhongGian[1] << 8) | locKhongGian[2];
                img.setRGB(x, y, p);
            }
        }


        //luu anh
        try {
            f = new File("output.jpg");
            ImageIO.write(img, "jpg", f);
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    private static int[][] layMauKhongGian(BufferedImage img, int x, int y) { // nap vao mang 2 chieu 8 pt trong do moi pt la 1 mang 3pt (rgb)
        // lay cac mau cua pixel ke ben
        int[][] pixelsRGB = new int[9][3];

        pixelsRGB[0] = pixelToRGB(img, x - 1, y - 1);
        pixelsRGB[1] = pixelToRGB(img, x, y - 1);
        pixelsRGB[2] = pixelToRGB(img, x + 1, y - 1);
        pixelsRGB[3] = pixelToRGB(img, x - 1, y);
        pixelsRGB[5] = pixelToRGB(img, x + 1, y);
        pixelsRGB[6] = pixelToRGB(img, x - 1, y + 1);
        pixelsRGB[7] = pixelToRGB(img, x, y + 1);
        pixelsRGB[8] = pixelToRGB(img, x + 1, y + 1);
        pixelsRGB[4] = pixelToRGB(img, x, y);

        return pixelsRGB;
    }

    private static int[] pixelToRGB(BufferedImage img, int x, int y) {// tach pixel thanh 3 mau
        int p = 0;
        // xu ly pixel nam o duong bien
        // -> nhan ban pixel (su dung pixel truoc do)
        if (x >= RONG) {
            x--;
        }
        if (x < 0) {
            x++;
        }
        if (y >= CAO) {
            y--;
        }
        if (y < 0) {
            y++;
        }

        // lay pixel
        p = img.getRGB(x, y);

        // lay tung loai mau Alpha Red Green Blue
        ALPHA = (p >> 24) & 0xff; // luu lai alpha
        int r = (p >> 16) & 0xff;
        int g = (p >> 8) & 0xff;
        int b = p & 0xff;
        return new int[]{r, g, b};
    }

    private static int[] locMin(int[][] mauKhongGian) {
        int[] rgb = new int[3];
        int min;
        for (int i = 0; i < rgb.length; i++) {
            min = 256;
            for (int j = 0; j < 8; j++) {
                if (mauKhongGian[j][i] < min) {
                    min = mauKhongGian[j][i];
                }
            }
            rgb[i] = min;
        }
        return rgb;
    }

    private static int[] locMax(int[][] mauKhongGian) {
        int[] rgb = new int[3];
        int max;
        for (int i = 0; i < rgb.length; i++) {
            max = -1;
            for (int j = 0; j < 8; j++) {
                if (mauKhongGian[j][i] > max) {
                    max = mauKhongGian[j][i];
                }
            }
            rgb[i] = max;
        }
        return rgb;
    }

    private static int[] locTrungBinh(int[][] mauKhongGian) {
        int[] rgb = new int[3];
        int tb;
        for (int i = 0; i < rgb.length; i++) {
            int sum = 0;
            for (int j = 0; j < 8; j++) {
                sum += mauKhongGian[j][i];
            }
            rgb[i] = sum / 9;
        }
        return rgb;
    }

    private static int[] locTrungVi(int[][] mauKhongGian) {
        int[] rgb = new int[3];
        int tv;
        for (int i = 0; i < rgb.length; i++) {
            ArrayList<Integer> arr = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                arr.add(mauKhongGian[j][i]);
            }
            Collections.sort(arr);
            rgb[i] = arr.get(4); // lay vi tri trung vi
        }
        return rgb;
    }

    private static int[] locTrungBinhTrongSo(int[][] mauKhongGian) {
        int[] rgb = new int[3];
        int tb;
        int[] trongSo = {1, 2, 1, 2, 4, 2, 1, 2, 1};
        for (int i = 0; i < rgb.length; i++) {
            int sum = 0;
            for (int j = 0; j < 8; j++) {
                sum += trongSo[j] * mauKhongGian[j][i]; // nhan voi trong so
            }
            rgb[i] = sum / 16;
        }
        return rgb;
    }

}
