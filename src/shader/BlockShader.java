package shader;

import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;


public class BlockShader extends ShaderManager {

    private static final String VERTEX_FILE = System.getProperty("user.dir") + "/src/shader/vertex.glsl";
    private static final String FRAG_FILE = System.getProperty("user.dir") + "/src/shader/fragment.glsl";
    private final Map<String, Integer> uniforms = new HashMap<>();

    public BlockShader() {
        super(VERTEX_FILE, FRAG_FILE);
    }

    @Override
    void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "texCoord");
    }

    @Override
    void getAllUniformLocations() {

    }

    void updateUni(String name, Matrix4f value) {
        super.setUniform(name, value);
    }

    void createUni(String name) {
        super.createUniform("projectionMatrix");
    }

    public void createUniform(String uniformName, int value) {
        super.setUniform(uniformName, value);
    }

}
