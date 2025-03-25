package ru.levelup.client.api.utils.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import ru.levelup.client.Client;
import ru.levelup.client.api.utils.misc.MathUtils;
import ru.levelup.client.api.utils.misc.TimerUtil;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public class Gif {
    static int num = 0;
    private HashMap<Integer, ResourceLocation> frames;
    private int framesCount = 0;
    private int currentFrame = 0;
    private TimerUtil util;
    private ImageReader imageReader;
    private int FrameCooldown;

    public Gif(String name, int framesCooldown) {
        try {
            util = new TimerUtil();
            frames = new HashMap<>();

            ImageInputStream imageInputStream = ImageIO.createImageInputStream(Client.class.getResourceAsStream("/assets/minecraft/gif/" + name));
//            ImageInputStream imageInputStream = ImageIO.createImageInputStream(WebUtils.visitSiteInput(name));


            if (imageInputStream != null) {
                imageReader = ImageIO.getImageReaders(imageInputStream).next();
                imageReader.setInput(imageInputStream);
                framesCount = imageReader.getNumImages(true) - 1;
            }
            for (int i = 0; i < framesCount; i++) {
                frames.put(i, Minecraft.getInstance().getTextureManager().getDynamicTextureLocation(num + "texture", new DynamicTexture(getFrame(i))));
                num++;
            }
            this.FrameCooldown = framesCooldown;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Gif(String s) {
        this(s, 100);
    }

//    public void reCreate(String url) throws IOException {
//        util = new TimerUtil();
//        frames = new HashMap<>();
//        ImageInputStream imageInputStream = ImageIO.createImageInputStream(WebUtils.visitSiteInput(url));
//
//        if (imageInputStream != null) {
//            imageReader = ImageIO.getImageReaders(imageInputStream).next();
//            imageReader.setInput(imageInputStream);
//            framesCount = imageReader.getNumImages(true) - 1;
//        }
//        for (int i = 0; i < framesCount; i++) {
//            frames.put(i, Minecraft.getInstance().getTextureManager().getDynamicTextureLocation(num + "texture", new DynamicTexture(getFrame(i))));
//            num++;
//        }
//    }

    public NativeImage getFrame(int num) {
        try {
            BufferedImage bufferedImage = imageReader.read(num);
            return NativeImage.read(Objects.requireNonNull(convertImageToPngInputStream(bufferedImage)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public NativeImage getCurrentFrame() {
        return getFrame(getCurrentCount());
    }

    public int getCurrentCount() {
        if (util.hasReached(FrameCooldown)) {
            currentFrame++;
            if (currentFrame == framesCount + 1) {
                currentFrame = 0;
            }

            util.reset();
        }
        return currentFrame;
    }

    private static InputStream convertImageToPngInputStream(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public ResourceLocation getResource() {
        Gif gif = this;
        if (gif.frames.containsKey(gif.getCurrentCount())) {
            return gif.frames.get(gif.getCurrentCount());
        } else {
            gif.frames.put(gif.getCurrentCount(), Minecraft.getInstance().getTextureManager().getDynamicTextureLocation(MathUtils.getRandomInRange(0, 10000) + "texture", new DynamicTexture(gif.getCurrentFrame())));
            return gif.frames.get(gif.getCurrentCount());
        }
    }

    public ResourceLocation getResource(int procent) {
        Gif gif = this;
        int count = (int) MathUtils.calculateValue(procent, 0, framesCount);
        if (gif.frames.containsKey(count)) {
            return gif.frames.get(count);
        } else {
            gif.frames.put(count, Minecraft.getInstance().getTextureManager().getDynamicTextureLocation(MathUtils.getRandomInRange(0, 10000) + "texture", new DynamicTexture(gif.getFrame(count))));
            return gif.frames.get(count);
        }
    }

    public void setFrameCooldown(int frameCooldown) {
        FrameCooldown = frameCooldown;
    }

    public int getFrameCooldown() {
        return FrameCooldown;
    }
}
