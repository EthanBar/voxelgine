package objects;

import org.lwjgl.opengl.GL30.*;
import render.GLRender;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;

public class Vao {

    private int ID;

    private ArrayList<Vbo> vbos = new ArrayList<>();

    public Vao() {
        ID = glGenVertexArrays();
        GLRender.addVao(ID);
    }

    public int addVBO () {
        Vbo vbo = new Vbo();
        return vbo.getID();
    }

    public void bind() {
        glBindVertexArray(ID);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    public int getID() {
        return ID;
    }


}
