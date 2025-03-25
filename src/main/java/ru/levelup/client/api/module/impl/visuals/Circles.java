package ru.levelup.client.api.module.impl.visuals;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.opengl.GL11;
import ru.levelup.client.Client;
import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.api.event.impl.EventUpdate;
import ru.levelup.client.api.event.impl.EventWorldRender;
import ru.levelup.client.api.event.impl.JumpEvent;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.ModuleHendler;
import ru.levelup.client.api.module.setting.impl.FloatSetting;
import ru.levelup.client.api.utils.misc.MathUtils;
import ru.levelup.client.api.utils.misc.TimerUtil;
import ru.levelup.client.api.utils.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

import static com.mojang.blaze3d.platform.GlStateManager.enableBlend;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX_COLOR;
import static org.lwjgl.opengl.GL11.GL_QUADS;

//Client.getInstance().getTheme().getColor((int) y * 2)
@ModuleHendler(name = "Circles", description = "", c = Module.Category.VISUALS)
public class Circles extends Module {
    public FloatSetting speedS = new FloatSetting("Speed", 10,5,15,1);
    public static float speed = 1f;
    public float steps = 8f;
    public float radius = 10f;
    public float time = 2f;
    public float size = 15f;
    public float sides = 90f;

    public ArrayList<Circle> circles = new ArrayList();
    public Circles(){
        addSetting(speedS);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        circles.clear();
    }

    @EventHendler
    public void onRender(JumpEvent event) {

        ClientPlayerEntity t = mc.player;
        if (mc.player.isOnGround()) {
            if (circles.size() > 20) circles.remove(0);
            this.circles.add(new Circle(new Vector3d(t.getPosX(), t.getPosY(), t.getPosZ()), (long) (time * 5000.0f), this.circles.size()));
        }
    }
    @EventHendler
    public void onRender(EventUpdate event) {

        circles.removeIf(c -> c.timer.hasReached((int)c.time));
    }

    public static MatrixStack matrixFrom(MatrixStack matrices, ActiveRenderInfo camera) {

        matrices.rotate(Vector3f.XP.rotationDegrees(camera.getPitch()));
        matrices.rotate(Vector3f.YP.rotationDegrees(camera.getYaw() + 180.0F));

        return matrices;
    }
    TimerUtil timerHelper = new TimerUtil();

    @EventHendler
    public void onRender(EventWorldRender event) {
        time = 1f;
        sides = 90;
        size = 10;
        radius = 10;
        steps = 8;
        MatrixStack ms = new MatrixStack();
        double ix = -((mc.getRenderManager().info.getProjectedView().getX()));
        double iy = -((mc.getRenderManager().info.getProjectedView().getY())) + 0.1;
        double iz = -((mc.getRenderManager().info.getProjectedView().getZ()));

        Collections.reverse(circles);
        {
            for (Circle c : circles) {
                double x = c.positionVector.x - c.factor / 2;
                double y = c.positionVector.y;
                double z = c.positionVector.z - c.factor / 2;

                c.factor = MathUtils.lerp(c.factor, 2, speedS.get() * 0.001f);

                c.alpha = MathUtils.lerp(c.alpha, 0, 0.015f);

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();

                ms.push();
                ms.translate(ix, iy, iz);
                ms.translate(x, y, z);
                ms.rotate(new Quaternion(new Vector3f(1, 0, 0), 90, true));

                enableBlend();
                GlStateManager.disableAlphaTest();
                GlStateManager.depthMask(false);
                //RenderUtil.setupRender();


                float rsp = (float) (c.timer.getTime()) / (float) (c.time);

                GL11.glAlphaFunc(GL11.GL_GREATER, 0.02f);
                GL11.glDisable(GL11.GL_POINT_SMOOTH);

                float apc = 1.0f - rsp;
                apc = (double) apc >= 0.85 ? (1.0f - apc) * 6 : apc;
                apc = (float) MathUtils.easeInOutQuad(apc, 1);
                apc = MathUtils.clamp(apc, 0.0f, 1.0f);
                int a = (int) MathUtils.clamp(apc * 255.0f, 0.0f, 255.0f);
                Color pink = new Color(251, 131, 255, 221);
                Color colorI = colorI = pink;
                Color colorB = colorB = pink;


                colorI = Color.RED;
                colorB = pink;


                String path = "ksenon/rr/omg.jpg";
                //String path = "ksenon/rr/kryg5.gif";
                boolean renderColor = false;


                if (renderColor) {
                    colorB = pink;
                    colorI = pink;
                }
                Color color = RenderUtil.setAlpha(colorB, (a));
                Color color2 = RenderUtil.setAlpha(colorI, a);

                enableBlend();
                RenderSystem.blendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
                RenderSystem.enableDepthTest();


//                Runnable rotateComponent = () -> mc.getTextureManager().bindTexture(new ResourceLocation("", path));
                mc.getTextureManager().bindTexture(Client.getInstance().getGifManager().jumpCircle1.getResource());

                bufferbuilder.begin(GL_QUADS, POSITION_TEX_COLOR);

                bufferbuilder.pos(ms.getLast().getMatrix(), 0, 0, 0).tex(0, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                bufferbuilder.pos(ms.getLast().getMatrix(), 0, c.factor, 0).tex(0, 1).color(color2.getRed(), color2.getGreen(), color2.getBlue(), color2.getAlpha()).endVertex();
                bufferbuilder.pos(ms.getLast().getMatrix(), c.factor, c.factor, 0).tex(1, 1).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
                bufferbuilder.pos(ms.getLast().getMatrix(), c.factor, 0, 0).tex(1, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();

                tessellator.draw();


                GlStateManager.enableAlphaTest();
                GlStateManager.disableBlend();
                GlStateManager.depthMask(true);
                ms.pop();
            }
        }
        Collections.reverse(circles);
    }

    public class Circle {
        public Vector3d positionVector;
        double x, y, z;

        double age;
        public long time;
        public TimerUtil timer;
        public int index;
        public float alpha = 255, factor = 0;
        public boolean reached = false;


        public Circle(Vector3d pos, long time, int index) {
            this.positionVector = pos;

            this.time = time;
            this.index = index;
            this.timer = new TimerUtil();
        }
    }
}
