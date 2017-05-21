package objects;

import org.lwjgl.system.MemoryUtil;
import render.GLRender;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Vbo {

    private int ID;

    public Vbo() {
        ID = glGenBuffers();
        GLRender.addVbo(ID);
    }

    public void addBufferData(float[] vertices, int index, int size, boolean normalized, int stride, int offset) {
        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(index, size, GL_FLOAT, normalized, stride, offset);
//        MemoryUtil.memFree(verticesBuffer);
    }

    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, ID);
    }

    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public int getID() {
        return ID;
    }
}
