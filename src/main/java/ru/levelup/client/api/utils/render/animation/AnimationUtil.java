package ru.levelup.client.api.utils.render.animation;

import ru.levelup.client.api.utils.misc.MathUtils;

public class AnimationUtil {

    long mc;
    float anim;
    float anim2;
    public float to;
    public float speed;
    public AnimationUtil(float anim, float to, float speed){
        this.anim = anim;
        this.to = to;
        this.speed = speed;
        mc = System.currentTimeMillis();
    }
    public float getAnim() {
        int count = (int) ((System.currentTimeMillis() - mc) / 5);
        if (count > 0){
            mc = System.currentTimeMillis();
        }
        for (int i = 0; i < count; i++) {
            anim = MathUtils.lerp(anim, to, speed);
        }
        return anim;

    }


    public float getAnimHarp() {
        int count = (int) ((System.currentTimeMillis() - mc) / 5);
        if (count > 0){
            mc = System.currentTimeMillis();
        }
        for (int i = 0; i < count; i++) {
            anim = MathUtils.harp(anim, to, speed);
        }
        return anim;

    }
    public void reset(){
        anim = 0;
        to = 0;
        anim2 = 0;
        mc = System.currentTimeMillis();
    }


    public void setAnim(float anim) {
        this.anim = anim;
        this.anim2 = anim;
        mc = System.currentTimeMillis();
    }
}