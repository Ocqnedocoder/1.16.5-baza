package ru.levelup.client.api.utils.render.shaders;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import ru.levelup.client.api.utils.Utils;
import ru.levelup.client.api.utils.misc.MathUtils;

import java.io.*;

import static org.lwjgl.opengl.GL20.*;

public class ShaderUtil extends Utils {

    private final int programID;
    public static Minecraft mc = Minecraft.getInstance();
    private long time;
    private static final IResourceManager RESOURCE_MANAGER = mc.getResourceManager();

    public Framebuffer framebuffer = new Framebuffer(mw.getFramebufferWidth(), mw.getFramebufferHeight(), true);;

    public ShaderUtil(String fragmentShaderLoc) {
        int program = glCreateProgram();
        int fragmentShaderID;
        switch (fragmentShaderLoc) {
            case "CustomRounded":
                fragmentShaderID = createShader(new ByteArrayInputStream(roundedRectCustom.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "glass":
                fragmentShaderID = createShader(new ByteArrayInputStream(glass.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "Glow":
                fragmentShaderID = createShader(new ByteArrayInputStream(Glow.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "Gausin":
                fragmentShaderID = createShader(new ByteArrayInputStream(gausin.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "roundRectTexture":
                fragmentShaderID = createShader(new ByteArrayInputStream(roundRectTexture.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "roundRectOutline":
                fragmentShaderID = createShader(new ByteArrayInputStream(roundRectOutline.getBytes()), GL_FRAGMENT_SHADER);
                break;
            case "roundedRectGradient":
                fragmentShaderID = createShader(new ByteArrayInputStream(roundedRectGradient.getBytes()), GL_FRAGMENT_SHADER);
                break;
            default:
                fragmentShaderID = 0;
                break;
        }
        glAttachShader(program, fragmentShaderID);
        glLinkProgram(program);
        int status = glGetProgrami(program, GL_LINK_STATUS);
        if (status == 0) {
            throw new IllegalStateException("Shader failed to link!");
        }
        this.programID = program;
        time = System.currentTimeMillis();
    }

    public void init() {
        glUseProgram(programID);
    }

    public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != Minecraft.getInstance().getMainWindow().getScaledWidth() || framebuffer.framebufferHeight != Minecraft.getInstance().getMainWindow().getScaledHeight()) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(Minecraft.getInstance().getMainWindow().getScaledWidth(), Minecraft.getInstance().getMainWindow().getScaledHeight(), true);
        }
        return framebuffer;
    }

    public static int createShader(final String fragmentResource, final String vertexResource) {
        final String fragmentSource = getShaderResource(fragmentResource);
        final String vertexSource = getShaderResource(vertexResource);

        if (fragmentResource == null || vertexResource == null) {
            System.out.println("An error occurred whilst creating shader");
            System.out.println("Fragment: " + fragmentSource == null);
            System.out.println("Vertex: " + vertexSource == null);
            return -1;
        }

        final int fragmentId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        final int vertexId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);

        GL20.glShaderSource(fragmentId, fragmentSource);
        GL20.glShaderSource(vertexId, vertexSource);
        GL20.glCompileShader(fragmentId);
        GL20.glCompileShader(vertexId);

        if (!compileShader(fragmentId)) return -1;
        if (!compileShader(vertexId)) return -1;

        final int programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, fragmentId);
        GL20.glAttachShader(programId, vertexId);
        GL20.glValidateProgram(programId);
        GL20.glLinkProgram(programId);
        GL20.glDeleteShader(fragmentId);
        GL20.glDeleteShader(vertexId);

        return programId;
    }

    public static String getShaderResource(final String resource) {
        try {
            final InputStream inputStream = RESOURCE_MANAGER.getResource(new ResourceLocation("nightcore/shader/" + resource)).getInputStream();
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String source = "";

            try {
                for (String s; (s = bufferedReader.readLine()) != null; source += s + System.lineSeparator()) ;
            } catch (final IOException ignored) {
            }

            return source;
        } catch (final IOException | NullPointerException e) {
            System.out.println("An error occurred while getting a shader resource");
            e.printStackTrace();
            return null;
        }
    }

    private static boolean compileShader(final int shaderId) {
        final boolean compiled = GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_TRUE;
        if (compiled) return true;

        final String shaderLog = GL20.glGetShaderInfoLog(shaderId, 8192);
        System.out.println("\nError while compiling shader: ");
        System.out.println("-------------------------------");
        System.out.println(shaderLog);
        return false;
    }

    public static void setupRoundedRectUniforms(float x, float y, float width, float height, float radius, ShaderUtil roundedTexturedShader) {
        MainWindow mainWindow = mc.getMainWindow();
        roundedTexturedShader.setUniformf("location", (float) (x * mainWindow.getGuiScaleFactor()), (float) ((mainWindow.getHeight() - (height * mainWindow.getGuiScaleFactor())) - (y * mainWindow.getGuiScaleFactor())));
        roundedTexturedShader.setUniformf("rectSize", (float) (width * mainWindow.getGuiScaleFactor()), (float) (height * mainWindow.getGuiScaleFactor()));
        roundedTexturedShader.setUniformf("radius", (float) (radius * mainWindow.getGuiScaleFactor()));
    }

    public void setUniformf(String name, float... args) {
        int loc = glGetUniformLocation(programID, name);
        switch (args.length) {
            case 1:
                GL20.glUniform1f(loc, args[0]);
                break;
            case 2:
                GL20.glUniform2f(loc, args[0], args[1]);
                break;
            case 3:
                GL20.glUniform3f(loc, args[0], args[1], args[2]);
                break;
            case 4:
                GL20.glUniform4f(loc, args[0], args[1], args[2], args[3]);
                break;
        }
    }

    public void setUniformi(String name, int... args) {
        int loc = glGetUniformLocation(programID, name);
        if (args.length > 1) glUniform2i(loc, args[0], args[1]);
        else glUniform1i(loc, args[0]);
    }
    
    public static void drawQuads(float x, float y, float width, float height) {
        GL20.glBegin(7);
        GL20.glTexCoord2d(0.0, 0.0);
        GL20.glVertex2d(x, y);
        GL20.glTexCoord2d(0.0, 1.0);
        GL20.glVertex2d(x, y + height);
        GL20.glTexCoord2d(1.0, 1.0);
        GL20.glVertex2d(x + width, y + height);
        GL20.glTexCoord2d(1.0, 0.0);
        GL20.glVertex2d(x + width, y);
        GL20.glEnd();
    }

    public static void drawQuads() {
        MainWindow mainWindow = mc.getMainWindow();
        float width = (float) MathUtils.calc(mainWindow.getScaledWidth());
        float height = (float) MathUtils.calc(mainWindow.getScaledHeight());
        GL20.glBegin(7);
        GL20.glTexCoord2f(0, 1);
        GL20.glVertex2f(0, 0);
        GL20.glTexCoord2f(0, 0);
        GL20.glVertex2f(0, height);
        GL20.glTexCoord2f(1, 0);
        GL20.glVertex2f(width, height);
        GL20.glTexCoord2f(1, 1);
        GL20.glVertex2f(width, 0);
        GL20.glEnd();
    }

    public static void draw3DQuads() {
        MainWindow mainWindow = mc.getMainWindow();
        float width = (float) MathUtils.calc(mainWindow.getScaledWidth());
        float height = (float) MathUtils.calc(mainWindow.getScaledHeight());
        GL20.glBegin(GL20.GL_QUADS);
        GL20.glTexCoord3f(0, 1, 0);
        GL20.glVertex3f(0, 0, 0);
        GL20.glTexCoord3f(0, 0, 0);
        GL20.glVertex3f(0, height, 0);
        GL20.glTexCoord3f(1, 0, 0);
        GL20.glVertex3f(width, height, 0);
        GL20.glTexCoord3f(1, 1, 0);
        GL20.glVertex3f(width, 0, 0);
        GL20.glEnd();
    }

    private int createShader(InputStream inputStream, int shaderType) {
        int shader = glCreateShader(shaderType);
        GL20.glShaderSource(shader, readInputStream(inputStream));
        glCompileShader(shader);


        if (glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
            System.out.println(glGetShaderInfoLog(shader, 4096));
            throw new IllegalStateException(String.format("Shader (%s) failed to compile!", shaderType));
        }

        return shader;
    }

    public static String readInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append('\n');

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private final String roundedRectGradient = "#version 120\n" + "\n" + "uniform vec2 location, rectSize;\n" + "uniform vec4 color1, color2, color3, color4;\n" + "uniform float radius;\n" + "\n" + "#define NOISE .5/255.0\n" + "\n" + "float roundSDF(vec2 p, vec2 b, float r) {\n" + "    return length(max(abs(p) - b , 0.0)) - r;\n" + "}\n" + "\n" + "vec3 createGradient(vec2 coords, vec3 color1, vec3 color2, vec3 color3, vec3 color4){\n" + "    vec3 color = mix(mix(color1.rgb, color2.rgb, coords.y), mix(color3.rgb, color4.rgb, coords.y), coords.x);\n" + "    //Dithering the color\n" + "    // from https://shader-tutorial.dev/advanced/color-banding-dithering/\n" + "    color += mix(NOISE, -NOISE, fract(sin(dot(coords.xy, vec2(12.9898, 78.233))) * 43758.5453));\n" + "    return color;\n" + "}\n" + "\n" + "void main() {\n" + "    vec2 st = gl_TexCoord[0].st;\n" + "    vec2 halfSize = rectSize * .5;\n" + "    \n" + "    float smoothedAlpha =  (1.0-smoothstep(0.0, 2., roundSDF(halfSize - (gl_TexCoord[0].st * rectSize), halfSize - radius - 1., radius))) * color1.a;\n" + "    gl_FragColor = vec4(createGradient(st, color1.rgb, color2.rgb, color3.rgb, color4.rgb), smoothedAlpha);\n" + "}";
    private final String roundRectOutline = " #version 120\n" + "\n" + "            uniform vec2 location, rectSize;\n" + "            uniform vec4 color, outlineColor;\n" + "            uniform float radius, outlineThickness;\n" + "\n" + "            float roundedSDF(vec2 centerPos, vec2 size, float radius) {\n" + "                return length(max(abs(centerPos) - size + radius, 0.0)) - radius;\n" + "            }\n" + "\n" + "            void main() {\n" + "                float distance = roundedSDF(gl_FragCoord.xy - location - (rectSize * .5), (rectSize * .5) + (outlineThickness *.5) - 1.0, radius);\n" + "\n" + "                float blendAmount = smoothstep(0., 2., abs(distance) - (outlineThickness * .5));\n" + "\n" + "                vec4 insideColor = (distance < 0.) ? color : vec4(outlineColor.rgb,  0.0);\n" + "                gl_FragColor = mix(outlineColor, insideColor, blendAmount);\n" + "\n" + "            }";
    private final String roundedRectCustom = "#version 120\n" + "\n" + "uniform vec4 cornerRadius;\n" + "uniform vec2 size;\n" + "uniform vec4 color;\n" + "\n" + "float alpha(vec2 d, vec2 d1, vec4 radii) {\n" + "    vec2 v = abs(d) - d1 + radii.xy;\n" + "    v.x = max(v.x, 0.0);\n" + "    v.y = max(v.y, 0.0);\n" + "    return min(v.x, v.y) + length(max(v, vec2(0.0))) - radii.x;\n" + "}\n" + "\n" + "void main() {\n" + "    vec4 radii;\n" + "    if (gl_TexCoord[0].s < 0.5) {\n" + "        if (gl_TexCoord[0].t < 0.5) {\n" + "            radii = vec4(cornerRadius.x, cornerRadius.x, cornerRadius.x, cornerRadius.x);\n" + "        } else {\n" + "            radii = vec4(cornerRadius.y, cornerRadius.y, cornerRadius.y, cornerRadius.y);\n" + "        }\n" + "    } else {\n" + "        if (gl_TexCoord[0].t < 0.5) {\n" + "            radii = vec4(cornerRadius.z, cornerRadius.z, cornerRadius.z, cornerRadius.z);\n" + "        } else {\n" + "            radii = vec4(cornerRadius.w, cornerRadius.w, cornerRadius.w, cornerRadius.w);\n" + "        }\n" + "    }\n" + "\n" + "    if (radii.x == 0.0 && radii.y == 0.0) {\n" + "        gl_FragColor = vec4(color.rgb, color.a * 1.0);\n" + "    } else {\n" + "        gl_FragColor = vec4(color.rgb, color.a * clamp(smoothstep(1.0, 0.0, length(max((abs(gl_TexCoord[0].st - 0.5) + 0.5) * size - size + radii.xy + 0.5, 0.0)) - radii.xy - 0.5), 0.0, 1.0));\n" + "    }\n" + "}";
    private final String roundRectTexture = "#version 120\n" + "\n" + "uniform vec2 location, rectSize;\n" + "uniform vec4 color;\n" + "uniform float radius;\n" + "uniform bool blur;\n" + "\n" + "float roundSDF(vec2 p, vec2 b, float r) {\n" + "    return length(max(abs(p) - b, 0.0)) - r;\n" + "}\n" + "\n" + "\n" + "void main() {\n" + "    vec2 rectHalf = rectSize * .5;\n" + "    // Smooth the result (free antialiasing).\n" + "    float smoothedAlpha =  (1.0-smoothstep(0.0, 1.0, roundSDF(rectHalf - (gl_TexCoord[0].st * rectSize), rectHalf - radius - 1., radius))) * color.a;\n" + "    gl_FragColor = vec4(color.rgb, smoothedAlpha);// mix(quadColor, shadowColor, 0.0);\n" + "\n" + "}";
    public final String gausin = "#version 120\n" + "\n" + "uniform sampler2D textureIn;\n" + "uniform vec2 texelSize, direction;\n" + "uniform float radius;\n" + "uniform float weights[256];\n" + "\n" + "#define offset texelSize * direction\n" + "\n" + "void main() {\n" + "    vec3 blr = texture2D(textureIn, gl_TexCoord[0].st).rgb * weights[0];\n" + "\n" + "    for (float f = 1.0; f <= radius; f++) {\n" + "        blr += texture2D(textureIn, gl_TexCoord[0].st + f * offset).rgb * (weights[int(abs(f))]);\n" + "        blr += texture2D(textureIn, gl_TexCoord[0].st - f * offset).rgb * (weights[int(abs(f))]);\n" + "    }\n" + "\n" + "    gl_FragColor = vec4(blr, 1.0);\n" + "}\n";
    private final String glass = "#version 120\n" + "\n" + "uniform sampler2D textureIn;\n" + "\n" + "void main() {\n" + "    vec2 zoomedTexCoord = gl_TexCoord[0].st * 0.9; // Увеличиваем текстурные координаты в 2 раза\n" + "\n" + "    vec2 mirroredTexCoord = vec2(1.0 - gl_TexCoord[0].s, 1.0 - gl_TexCoord[0].t);\n" + "    gl_FragColor = texture2D(textureIn, zoomedTexCoord);\n" + "}";
    private final String Glow = "#version 120\n" + "\n" + "uniform sampler2D textureIn;\n" + "uniform bool shouldGlow; // Добавленный параметр для определения, нужно ли применять эффект свечения\n" + "\n" + "void main() {\n" + "    vec2 zoomedTexCoord = gl_TexCoord[0].st * 0.9; // Увеличиваем текстурные координаты в 2 раза\n" + "\n" + "    vec2 mirroredTexCoord = vec2(1.0 - gl_TexCoord[0].s, 1.0 - gl_TexCoord[0].t);\n" + "    \n" + "    vec4 textureColor = texture2D(textureIn, zoomedTexCoord);\n" + "\n" + "    if (shouldGlow) {\n" + "        gl_FragColor = vec4(textureColor.rgb * 1.5, textureColor.a);\n" + "    } else {\n" + "        gl_FragColor = textureColor;\n" + "    }\n" + "}";

    public static void bindTexture(int texture) {
        GlStateManager.bindTexture(texture);
    }

    public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
        return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }

    public void load() {
        GlStateManager.useProgram(programID);
    }

    public void unload() {
        GlStateManager.useProgram(0);
    }

    public int getUniform(String name) {
        return glGetUniformLocation(programID, name);
    }

    public long getTime() {
        return time;
    }

}
