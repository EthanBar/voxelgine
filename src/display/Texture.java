package display;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class Texture {
    private int ID;
    private int width;
    private int height;

//    public Texture(String filename) {
//        BufferedImage bufferedImage;
//        try {
//            File file = new File(System.getProperty("user.dir") + filename);
//            System.out.println(file.getAbsolutePath());
//            bufferedImage = ImageIO.read(file);
//            width = bufferedImage.getWidth();
//            height = bufferedImage.getHeight();
//
//            int[] pixels_raw = new int[width * height * 4];
//            pixels_raw = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
//
//            ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
//
//            for (int i = 0; i < width; i++) {
//                for (int j = 0; j < height; j++) {
//                    int pixel = pixels_raw[i * width + j];
//                    pixels.put((byte) ((pixel >> 16) & 0xFF)); // RED
//                    pixels.put((byte) ((pixel >> 8) & 0xFF));  // GREEN
//                    pixels.put((byte) (pixel & 0xFF));		  // BLUE
//                    pixels.put((byte) ((pixel >> 24) & 0xFF)); // ALPHA
//                }
//            }
//
//            pixels.flip();
//
//            textureObject = glGenTextures();
//
//            glBindTexture(GL_TEXTURE_2D, textureObject);
//
//            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
//            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
//
//            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
//
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public Texture(String filename) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer comp = BufferUtils.createIntBuffer(1);

        ByteBuffer data = stbi_load(System.getProperty("user.dir") + filename, width, height, comp, 4);
        data.flip();

        ID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, ID);

        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        this.width = width.get();
        this.height = height.get();

//        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
//        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        glGenerateMipmap(GL_TEXTURE_2D);
//        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
        stbi_image_free(data);
    }


    public int getID() {
        return ID;
    }

    public void bind() {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, ID);
    }

    public void unbind() {
        glActiveTexture(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}