package display;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    private final Vector3f position;


    private float yaw = 0;
    private float pitch = 0;

    private final float SPEED = 0.1f;

    private double newX = 400;
    private double newY = 300;


    private double prevX = 0;
    private double prevY = 0;

    public Camera(long window) {
        this(new Vector3f(0, 0, 0), window);
    }

    public Camera(Vector3f position, long window) {
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if ( offsetZ != 0 ) {
            position.x += (float)Math.sin(Math.toRadians(yaw)) * -1.0f * offsetZ;
            position.z += (float)Math.cos(Math.toRadians(yaw)) * offsetZ;
        }
        if ( offsetX != 0) {
            position.x += (float)Math.sin(Math.toRadians(yaw - 90)) * -1.0f * offsetX;
            position.z += (float)Math.cos(Math.toRadians(yaw - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    public Vector3f getRotation() {
        return new Vector3f(pitch, yaw %= 360, 0);
    }

    public void setRotation(float x, float y, float z) {
        yaw = x;
        pitch = y;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        pitch+= offsetX;
        yaw += offsetY;
        if (pitch > 90) pitch= 90;
        if (pitch < -90) pitch = -90;
    }

    public void takeInput(Window window, long win) {
        if (window.isKeyPressed(GLFW_KEY_S)) { // D
            double xo = Math.sin(Math.toRadians(yaw));
            double zo = Math.cos(Math.toRadians(yaw));
            System.out.println(zo);
            position.x -= SPEED * xo;
            position.z += SPEED * zo;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            double xo = Math.sin(Math.toRadians(yaw + 90));
            double zo = Math.cos(Math.toRadians(yaw + 90));
            position.x -= SPEED * xo;
            position.z += SPEED * zo;
        }
        if (window.isKeyPressed(GLFW_KEY_D)) {
            double xo = Math.sin(Math.toRadians(yaw + 90));
            double zo = Math.cos(Math.toRadians(yaw + 90));
            position.x += SPEED * xo;
            position.z -= SPEED * zo;
        }
        if (window.isKeyPressed(GLFW_KEY_W)) {
            double xo = Math.sin(Math.toRadians(yaw));
            double zo = Math.cos(Math.toRadians(yaw));
            position.x += SPEED * xo;
            position.z -= SPEED * zo;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)) {
            position.y = position.y - SPEED;
        }
        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            position.y = position.y + SPEED;
        }
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(win, x, y);

        newX = x.get(0);
        newY = y.get(0);

        double deltaX = newX - 400;
        double deltaY = newY - 300;

        prevX = newX;
        prevY = newY;


        glfwSetCursorPos(win, 800/2, 600/2);
        this.moveRotation((float)deltaY / 10, (float)deltaX / 10, 0);
    }
}