package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.util.SharedConstants;
import net.optifine.Config;
import net.optifine.reflect.Reflector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.system.MemoryUtil;

public class TextureUtil
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static int[] dataArray = new int[4194304];
    private static final IntBuffer DATA_BUFFER = GLAllocation.createDirectIntBuffer(4194304);


    public static int generateTextureId()
    {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);

        if (SharedConstants.developmentMode)
        {
            int[] aint = new int[ThreadLocalRandom.current().nextInt(15) + 1];
            GlStateManager.genTextures(aint);
            int i = GlStateManager.genTexture();
            GlStateManager.deleteTextures(aint);
            return i;
        }
        else
        {
            return GlStateManager.genTexture();
        }
    }

    public static void releaseTextureId(int textureId)
    {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        GlStateManager.deleteTexture(textureId);
    }

    public static void prepareImage(int textureId, int width, int height)
    {
        prepareImage(NativeImage.PixelFormatGLCode.RGBA, textureId, 0, width, height);
    }

    public static void prepareImage(NativeImage.PixelFormatGLCode pixelFormat, int textureId, int width, int height)
    {
        prepareImage(pixelFormat, textureId, 0, width, height);
    }

    public static void prepareImage(int textureId, int mipmapLevel, int width, int height)
    {
        prepareImage(NativeImage.PixelFormatGLCode.RGBA, textureId, mipmapLevel, width, height);
    }

    public static void prepareImage(NativeImage.PixelFormatGLCode pixelFormat, int textureId, int mipmapLevel, int width, int height)
    {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        bindTexture(textureId);

        if (mipmapLevel >= 0)
        {
            GlStateManager.texParameter(3553, 33085, mipmapLevel);
            GlStateManager.texParameter(3553, 33082, 0);
            GlStateManager.texParameter(3553, 33083, mipmapLevel);
            GlStateManager.texParameter(3553, 34049, 0.0F);
        }

        for (int i = 0; i <= mipmapLevel; ++i)
        {
            GlStateManager.texImage2D(3553, i, pixelFormat.getGlFormat(), width >> i, height >> i, 0, 6408, 5121, (IntBuffer)null);
        }
    }

    private static void bindTexture(int textureId)
    {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        GlStateManager.bindTexture(textureId);
    }

    public static ByteBuffer readToBuffer(InputStream inputStreamIn) throws IOException
    {
        ByteBuffer bytebuffer;

        if (inputStreamIn instanceof FileInputStream)
        {
            FileInputStream fileinputstream = (FileInputStream)inputStreamIn;
            FileChannel filechannel = fileinputstream.getChannel();
            bytebuffer = MemoryUtil.memAlloc((int)filechannel.size() + 1);

            while (filechannel.read(bytebuffer) != -1)
            {
            }
        }
        else
        {
            bytebuffer = MemoryUtil.memAlloc(8192);
            ReadableByteChannel readablebytechannel = Channels.newChannel(inputStreamIn);

            while (readablebytechannel.read(bytebuffer) != -1)
            {
                if (bytebuffer.remaining() == 0)
                {
                    bytebuffer = MemoryUtil.memRealloc(bytebuffer, bytebuffer.capacity() * 2);
                }
            }
        }

        return bytebuffer;
    }

    public static String readResourceAsString(InputStream inputStreamIn)
    {
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);
        ByteBuffer bytebuffer = null;

        try
        {
            bytebuffer = readToBuffer(inputStreamIn);
            int i = bytebuffer.position();
            ((Buffer)bytebuffer).rewind();
            return MemoryUtil.memASCII(bytebuffer, i);
        }
        catch (IOException ioexception)
        {
        }
        finally
        {
            if (bytebuffer != null)
            {
                MemoryUtil.memFree(bytebuffer);
            }
        }

        return null;
    }

    public static void initTexture(IntBuffer bufferIn, int width, int height)
    {
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);
        GL11.glPixelStorei(GL11.GL_UNPACK_SWAP_BYTES, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_LSB_FIRST, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 4);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, bufferIn);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
    }

    public static void allocateTexture(int textureId, int width, int height)
    {
        allocateTextureImpl(textureId, 0, width, height);
    }

    public static int uploadTextureImageAllocate(int textureId, BufferedImage texture, boolean blur, boolean clamp)
    {
        allocateTexture(textureId, texture.getWidth(), texture.getHeight());
        return uploadTextureImageSub(textureId, texture, 0, 0, blur, clamp);
    }

    public static int glGenTextures()
    {
        return GlStateManager.generateTexture();
    }

    public static void allocateTextureImpl(int glTextureId, int mipmapLevels, int width, int height)
    {
        Object object = TextureUtil.class;

        if (Reflector.SplashScreen.exists())
        {
            object = Reflector.SplashScreen.getTargetClass();
        }

        synchronized (object)
        {
            deleteTexture(glTextureId);
            bindTexture(glTextureId);
        }

        if (mipmapLevels >= 0)
        {
            GlStateManager.glTexParameteri(3553, 33085, mipmapLevels);
            GlStateManager.glTexParameteri(3553, 33082, 0);
            GlStateManager.glTexParameteri(3553, 33083, mipmapLevels);
            GlStateManager.glTexParameterf(3553, 34049, 0.0F);
        }

        for (int i = 0; i <= mipmapLevels; ++i)
        {
            GlStateManager.glTexImage2D(3553, i, 6408, width >> i, height >> i, 0, 32993, 33639, (IntBuffer)null);
        }
    }

    public static void deleteTexture(int textureId)
    {
        GlStateManager.deleteTexture(textureId);
    }

    public static int uploadTextureImageSub(int textureId, BufferedImage p_110995_1_, int p_110995_2_, int p_110995_3_, boolean p_110995_4_, boolean p_110995_5_)
    {
        bindTexture(textureId);
        uploadTextureImageSubImpl(p_110995_1_, p_110995_2_, p_110995_3_, p_110995_4_, p_110995_5_);
        return textureId;
    }

    private static void uploadTextureImageSubImpl(BufferedImage p_110993_0_, int p_110993_1_, int p_110993_2_, boolean p_110993_3_, boolean p_110993_4_)
    {
        int i = p_110993_0_.getWidth();
        int j = p_110993_0_.getHeight();
        int k = 4194304 / i;
        int[] aint = dataArray;
        setTextureBlurred(p_110993_3_);
        setTextureClamped(p_110993_4_);

        for (int l = 0; l < i * j; l += i * k)
        {
            int i1 = l / i;
            int j1 = Math.min(k, j - i1);
            int k1 = i * j1;
            p_110993_0_.getRGB(0, i1, i, j1, aint, 0, i);
            copyToBuffer(aint, k1);
            GlStateManager.glTexSubImage2D(3553, 0, p_110993_1_, p_110993_2_ + i1, i, j1, 32993, 33639, DATA_BUFFER);
        }
    }
    public static void setTextureClamped(boolean p_110997_0_)
    {
        if (p_110997_0_)
        {
            GlStateManager.glTexParameteri(3553, 10242, 33071);
            GlStateManager.glTexParameteri(3553, 10243, 33071);
        }
        else
        {
            GlStateManager.glTexParameteri(3553, 10242, 10497);
            GlStateManager.glTexParameteri(3553, 10243, 10497);
        }
    }
    private static void setTextureBlurred(boolean p_147951_0_)
    {
        setTextureBlurMipmap(p_147951_0_, false);
    }

    public static void setTextureBlurMipmap(boolean p_147954_0_, boolean p_147954_1_)
    {
        if (p_147954_0_)
        {
            GlStateManager.glTexParameteri(3553, 10241, p_147954_1_ ? 9987 : 9729);
            GlStateManager.glTexParameteri(3553, 10240, 9729);
        }
        else
        {
            int i = Config.getMipmapType();
            GlStateManager.glTexParameteri(3553, 10241, p_147954_1_ ? i : 9728);
            GlStateManager.glTexParameteri(3553, 10240, 9728);
        }
    }

    private static void copyToBuffer(int[] p_110990_0_, int p_110990_1_)
    {
        copyToBufferPos(p_110990_0_, 0, p_110990_1_);
    }

    private static void copyToBufferPos(int[] p_110994_0_, int p_110994_1_, int p_110994_2_)
    {
        int[] aint = p_110994_0_;
        DATA_BUFFER.clear();
        DATA_BUFFER.put(aint, p_110994_1_, p_110994_2_);
        DATA_BUFFER.position(0).limit(p_110994_2_);
    }
}
