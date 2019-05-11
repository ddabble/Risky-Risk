package no.ntnu.idi.tdt4240.util.gl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.HashMap;
import java.util.Map;

import no.ntnu.idi.tdt4240.util.Utils;

import static com.badlogic.gdx.graphics.GL20.*;

public class GLSLshaders {
    private static class Shader {
        int type;
        String source;

        Shader(int type) {
            this.type = type;
        }
    }

    public static Map<Integer, String> parseShadersInFile(String filePath) {
        FileHandle shaderFileHandle = Gdx.files.internal(filePath);
        Map<Integer, String> shaders = new HashMap<>(2); // 2 is the number of different shader types supported

        String source = shaderFileHandle.readString();
        int shaderTypeIndex = source.indexOf("/***");
        if (shaderTypeIndex == -1)
            throw new GLSLshaderParseException("Could not find any shader type declarations."
                                               + "\n\tat " + Utils.getLinkToCharInFile(shaderFileHandle.file(), source, 0));
        do {
            int endIndex = source.indexOf("*/", shaderTypeIndex + "/***".length());
            if (endIndex == -1)
                throw new GLSLshaderParseException("Missing comment end token */ for the shader type declaration."
                                                   + "\n\tat " + Utils.getLinkToCharInFile(shaderFileHandle.file(), source, shaderTypeIndex));

            String shaderTypeDeclaration = source.substring(shaderTypeIndex + "/***".length(), endIndex);

            Shader shader = parseShaderTypeDeclaration(shaderTypeDeclaration);
            if (shader == null)
                throw new GLSLshaderParseException("Could not find a valid shader type in the shader type declaration."
                                                   + "\n\tat " + Utils.getLinkToCharInFile(shaderFileHandle.file(), source, shaderTypeIndex));

            int nextShaderTypeIndex = source.indexOf("/***", shaderTypeIndex + "/***".length());
            if (nextShaderTypeIndex != -1)
                shader.source = source.substring(endIndex + "*/".length(), nextShaderTypeIndex);
            else
                shader.source = source.substring(endIndex + "*/".length());

            shaders.put(shader.type, shader.source);

            shaderTypeIndex = nextShaderTypeIndex;
        } while (shaderTypeIndex != -1);

        if (shaders.size() == 1) {
            int existingShaderType = shaders.keySet().iterator().next();
            String missingShaderType = null;
            if (existingShaderType == GL_VERTEX_SHADER)
                missingShaderType = "fragment shader";
            else if (existingShaderType == GL_FRAGMENT_SHADER)
                missingShaderType = "vertex shader";

            throw new GLSLshaderParseException("Could not find any " + missingShaderType + "."
                                               + "\n\tat " + Utils.getLinkToCharInFile(shaderFileHandle.file(), source, source.length()));
        }

        return shaders;
    }

    private static Shader parseShaderTypeDeclaration(String shaderTypeDeclaration) {
        Shader shader = null;

        String[] tokens = shaderTypeDeclaration.split("[^a-zA-Z]");
loop:
        for (String token : tokens) {
            if (token.length() < 4)
                continue;

            switch (token.substring(0, 4).toLowerCase()) {
                case "vert":
                    shader = new Shader(GL_VERTEX_SHADER);
                    break loop;

                case "frag":
                    shader = new Shader(GL_FRAGMENT_SHADER);
                    break loop;
            }
        }

        return shader;
    }

    private static class GLSLshaderParseException extends RuntimeException {
        public GLSLshaderParseException(String s) {
            super(s);
        }
    }
}
