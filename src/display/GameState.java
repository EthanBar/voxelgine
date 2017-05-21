package display;

import com.sun.prism.Texture;
import de.matthiasmann.twl.utils.PNGDecoder;
import objects.GameItem;
import objects.Mesh;
import org.joml.Matrix4f;
import render.GLRender;
import render.Transformation;
import shader.BlockShader;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

class GameState {

    static private long window;
    static private GLRender render;
    static private BlockShader shader;

    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    private static Matrix4f projectionMatrix;

    private static Window win;


    static void load(Window wind) {
        win = wind;
        window = Window.windowHandle;
        shader = new BlockShader();
        loop();
    }

    private static void loop() {
        Camera camera = new Camera(window);
        float[] positions = new float[] {
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,

                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,

                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,

                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,

                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f,
        };
        float[] textCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,

                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,

                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,

                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,

                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,

                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };
        int[] indices = new int[]{
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7,};
        float aspectRatio = (float) win.getWidth() / win.getHeight();
        projectionMatrix = new Matrix4f().perspective(FOV, aspectRatio,
                                                      Z_NEAR, Z_FAR);
        projectionMatrix.translate(1, 1, 0);
        GLRender.init(shader);
        Transformation transformation = new Transformation();
        display.Texture texture = new display.Texture("/res/grassblock.png");
        texture.bind();

        Mesh mesh = new Mesh(positions, indices, textCoords, texture);
        GameItem gameItem = new GameItem(mesh);
        GameItem gameItem2 = new GameItem(mesh);

        // Draw the mesh
        // Main loop
        float rot = 0;
        gameItem.setRotation(0, 0, 90);
        while (!glfwWindowShouldClose(window) ) {
            camera.takeInput(win, window);
            GLRender.prepare(window);
            shader.start();
            shader.setUniform("texture_sampler", 0);
            shader.setUniform("projectionMatrix", projectionMatrix);
            rot -= 0.01f;
            gameItem.setPosition(0, 1, rot);
            gameItem2.setPosition(0, 1, -rot);
            gameItem.setRotation(0, 30, 0);
            // Set world matrix for this item
            // Update projection Matrix
            Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, win.getWidth(), win.getHeight(), Z_NEAR, Z_FAR);
            shader.setUniform("projectionMatrix", projectionMatrix);

// Update view Matrix
            Matrix4f viewMatrix = transformation.getViewMatrix(camera);
            // Render the mes for this game item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
            shader.setUniform("modelViewMatrix", modelViewMatrix);
            gameItem.getMesh().render();

            glfwSwapBuffers(window); // swap the color buffers
            shader.stop();
        }
        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        clean();
    }

    private static void clean() {
        shader.clean();
        glDisableVertexAttribArray(0);
        GLRender.clean();
    }

//    static private float[] vertices = {
//            -1,1,-1, // 0
//            -1,-1,-1, // 1
//            1,-1,-1, // 2
//            1,1,-1, // 3
//
//            -1,1,1, // 4
//            -1,-1,1, // 5
//            1,-1,1,
//            1,1,1,
//
//            1,1,-1,
//            1,-1,-1,
//            1,-1,1,
//            1,1,1,
//
//            -1,1,-1, // 0
//            -1,-1,-1,
//            -1,-1,1,
//            -1,1,1,
//
//            -1,1,1,
//            -1,1,-1,
//            1,1,-1,
//            1,1,1,
//
//            -1,-1,1, // 20
//            -1,-1,-1,
//            1,-1,-1,
//            1,-1,1 // 23
//    };
//
//    static private float[] textureCoords = {
//            0.5f, 0, // Back side
//            0.5f, 0.5f,
//            0, 0.5f,
//            0, 0,
//            0.5f, 0, // Front side
//            0.5f, 0.5f,
//            0, 0.5f,
//            0, 0,
//            0.5f, 0, // Right side
//            0.5f, 0.5f,
//            0, 0.5f,
//            0, 0,
//            0.5f, 0, // Left side
//            0.5f, 0.5f,
//            0, 0.5f,
//            0, 0,
//            1, 0, // Top side
//            1, 0.5f,
//            0.5f, 0.5f,
//            0.5f, 0,
//            0.5f, 0.5f, // Bottom side
//            0.5f, 1,
//            0, 1,
//            0, 0.5f
//    };
//
//    static private float[] textureCoordsSingle = {
//            1, 0, // Back side
//            1, 1,
//            0, 1,
//            0, 0,
//            1, 0, // Back side
//            1, 1,
//            0, 1,
//            0, 0,
//            1, 0, // Back side
//            1, 1,
//            0, 1,
//            0, 0,
//            1, 0, // Back side
//            1, 1,
//            0, 1,
//            0, 0,
//            1, 0, // Back side
//            1, 1,
//            0, 1,
//            0, 0,
//            1, 0, // Back side
//            1, 1,
//            0, 1,
//            0, 0
//    };
//
//    static private int[] indices = {
//            0,3,1, // Back Counter
//            3,2,1,
//            4,5,7, // Front Normal
//            7,5,6,
//            8,11,9, // Right Counter
//            11,10,9,
//            12,13,15, // Left Normal
//            15,13,14,
//            16,19,17, // Top Counter
//            19,18,17,
//            20,21,23, // Bottom Normal
//            23,21,22
//    };


    /*
//        Vao vao = new Vao();
//        vao.bind();
//        Vbo vbo = new Vbo();
//        vbo.bind();
//        vbo.addBufferData(vertices, 0, 3, false, 0, 0);
//        vbo.unbind();
//        vao.unbind();
     */

}
