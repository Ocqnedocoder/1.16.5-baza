package ru.levelup.client.api.module.impl.visuals;

import com.google.common.collect.Queues;
import com.mojang.blaze3d.systems.IRenderCall;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.joml.Vector2d;
import ru.levelup.client.api.event.AnimationMath;
import ru.levelup.client.api.event.Event;
import ru.levelup.client.api.event.EventRender;
import ru.levelup.client.api.event.impl.EventMotion;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.ModuleHendler;
import ru.levelup.client.api.utils.render.ColorUtil;
import ru.levelup.client.api.utils.render.ImageUtils;

import java.awt.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static net.minecraft.client.renderer.WorldRenderer.frustum;
import static ru.levelup.client.api.utils.player.ProjectionUtils.project;
import static ru.levelup.client.api.utils.world.WorldUtil.TotemUtil.getSphere;

@ModuleHendler(name = "Particles", description = "Custom color hit enity", c = Module.Category.VISUALS)
public class Particles extends Module{
    CopyOnWriteArrayList<Point> points = new CopyOnWriteArrayList();
    private static final ConcurrentLinkedQueue<IRenderCall> renderQueue = Queues.newConcurrentLinkedQueue();

    public static void registerRenderCall(IRenderCall rc) {
        renderQueue.add(rc);
    }
    public static boolean isInView(Vector3d ent) {
        assert mc.getRenderViewEntity() != null;
        frustum.setCameraPosition(mc.getRenderManager().info.getProjectedView().x, mc.getRenderManager().info.getProjectedView().y,mc.getRenderManager().info.getProjectedView().z);
        return frustum.isBoundingBoxInFrustum(new AxisAlignedBB(ent.add(-0.5,-0.5, -0.5), ent.add(0.5,0.5, 0.5)));
    }
    public Particles() {
    }
    public void onEvent(Event event) {
        if (event instanceof EventMotion e) {
            for (Entity entity : mc.world.getAllEntities()) {
                if (entity instanceof LivingEntity l) {
                    if (l.hurtTime == 9) {
                        createPoints(l.getPositionVec().add(0,1,0));
                    }
                }
                if (entity instanceof EnderPearlEntity p) {
                    points.add(new Point(p.getPositionVec()));
                }
            }
        }
        if (event instanceof EventRender e) {
            if (e.isRender2D()) {
                if (points.size() > 100) {
                    points.remove(0);
                }
                for (Point point : points) {
                    long alive = (System.currentTimeMillis() - point.createdTime);
                    if (alive > point.aliveTime || !mc.player.canVectorBeSeenFixed(point.position) || !isInView(point.position)) {
                        points.remove(point);
                        continue;
                    }

                    Vector2d pos = project(point.position.x, point.position.y, point.position.z);

                    if (pos != null) {
                        float sizeDefault = point.size;

                        point.update();

                        float size = 1 - (float) alive / point.aliveTime;

                        registerRenderCall(() -> {
                            ImageUtils.drawImage(new ResourceLocation("gif/star.png"),(float) pos.x, (float) pos.y, (sizeDefault + 1) * size,(sizeDefault + 1) * size, Color.BLACK.getRGB());
                            ImageUtils.drawImage(new ResourceLocation("gif/star.png"),(float) pos.x, (float) pos.y, sizeDefault * size,sizeDefault * size,ColorUtil.getColor(points.indexOf(point),255));
                        });
                        ImageUtils.drawImage(new ResourceLocation("gif/star.png"),(float) pos.x, (float) pos.y, (sizeDefault + 1) * size,(sizeDefault + 1) * size, Color.BLACK.getRGB());
                        ImageUtils.drawImage(new ResourceLocation("gif/star.png"),(float) pos.x, (float) pos.y, sizeDefault * size,sizeDefault * size, ColorUtil.getColor(points.indexOf(point), 255));
                    }
                }
            }
        }
    }

    private void createPoints(Vector3d position) {
        for (int i = 0; i < ThreadLocalRandom.current().nextInt(5, 20); i++) {
            points.add(new Point(position));
        }
    }
    private final class Point {
        public Vector3d position;
        public Vector3d motion;
        public Vector3d animatedMotion;

        public long aliveTime;
        public float size;

        public long createdTime = System.currentTimeMillis();

        public Point(Vector3d position) {
            this.position = new Vector3d(position.x,position.y,position.z);
            this.motion = new Vector3d(java.util.concurrent.ThreadLocalRandom.current().nextFloat(-0.01f, 0.01f), 0, java.util.concurrent.ThreadLocalRandom.current().nextFloat(-0.01f, 0.01f));
            this.animatedMotion = new Vector3d(0,0,0);
            size = java.util.concurrent.ThreadLocalRandom.current().nextFloat(4, 7);
            aliveTime = java.util.concurrent.ThreadLocalRandom.current().nextLong(3000, 10000);
        }


        public void update() {
            if (isGround()) {
                motion.y = 1;
                motion.x *= 1.05;
                motion.z *= 1.05;
            } else {
                motion.y = -0.01;
                motion.y *= 2;
            }


            animatedMotion.x = AnimationMath.fast((float) animatedMotion.x, (float) (motion.x), 3);
            animatedMotion.y = AnimationMath.fast((float) animatedMotion.y, (float) (motion.y), 3);
            animatedMotion.z = AnimationMath.fast((float) animatedMotion.z, (float) (motion.z), 3);

            position = position.add(animatedMotion);


        }

        boolean isGround() {
            Vector3d position = this.position.add(animatedMotion);
            AxisAlignedBB bb = new AxisAlignedBB(position.x - 0.1, position.y - 0.1, position.z - 0.1, position.x + 0.1, position.y + 0.1, position.z + 0.1);
            return getSphere(new BlockPos(position), 2, 4, false, true, 0)
                    .stream()
                    .anyMatch(blockPos -> !mc.world.getBlockState(blockPos).isAir() &&
                            bb.intersects(new AxisAlignedBB(blockPos)) &&
                            AxisAlignedBB.calcSideHit(new AxisAlignedBB(blockPos.add(0, 1, 0)), position, new double[]{
                                    2D
                            },null, 0.1f, 0.1f,0.1f) == Direction.DOWN);
        }


    }
}
