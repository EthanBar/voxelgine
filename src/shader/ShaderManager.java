package shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public abstract class ShaderManager {

    private int programID;
    private int vertexID;
    private int fragID;

    private Map<String, Integer> uniforms;

    ShaderManager(String vertexFile, String fragFile) {
        uniforms = new HashMap<>();
        vertexID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragID = loadShader(fragFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexID);
        GL20.glAttachShader(programID, fragID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
    }

    public void createUniform(String uniformName){
        int uniformLocation = glGetUniformLocation(programID,
                                                   uniformName);
        if (uniformLocation < 0) {
            System.out.println("Could not find uniform:" +
                                        uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        // Dump the matrix into a float buffer
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        }
    }

    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    int getUniformLocation(String uniformName) {
        return glGetUniformLocation(programID, uniformName);
    }

    abstract void getAllUniformLocations();

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void clean() {
        stop();
        GL20.glDetachShader(programID, vertexID);
        GL20.glDetachShader(programID, fragID);
        GL20.glDeleteShader(vertexID);
        GL20.glDeleteShader(fragID);
        GL20.glDeleteProgram(programID);
    }

    abstract void bindAttributes();

    void bindAttribute(int index, CharSequence name) {
        GL20.glBindAttribLocation(programID, index, name);
    }

    void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    void loadVector(int location, Vector3f vector) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    void loadInt(int location, int value) {
        GL20.glUniform1i(location, value);
    }

    void loadBoolean(int location, boolean value) {
        float toLoad = 0;
        if (value) { toLoad = 1;}
        GL20.glUniform1f(location, toLoad);
    }

    void loadArray(int location, int x, int y, int z) {
        GL20.glUniform3f(location, x, y, z);
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine())!=null){
                shaderSource.append(line).append("\n");
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        return shaderID;
    }
}
