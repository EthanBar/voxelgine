package render;

import display.Window;
import shader.BlockShader;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL30.*;

public class GLRender {

    private static ArrayList<Integer> vaos = new ArrayList<>();
    private static ArrayList<Integer> vbos = new ArrayList<>();
    private static Transformation transformation;


    public static void prepare(long window) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer


        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }

    public static void init(BlockShader shaderProgram) {
        transformation = new Transformation();

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
    }

    public static void clean() {
        glBindVertexArray(0);
        for (int vbo : vbos) {
            glDeleteBuffers(vbo);
        }
        for (int vao : vaos) {
            glDeleteVertexArrays(vao);
        }
    }

    public static void addVao(int vao) {
        vaos.add(vao);
    }

    public static void addVbo(int vbo) {
        vbos.add(vbo);
    }

}
