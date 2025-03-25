package ru.levelup.client.api.module.impl.visuals;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import ru.levelup.client.Client;
import ru.levelup.client.api.event.EventHendler;
import ru.levelup.client.api.event.impl.EventScreen;
import ru.levelup.client.api.module.Module;
import ru.levelup.client.api.module.ModuleHendler;
import ru.levelup.client.api.module.setting.impl.BooleanSetting;
import ru.levelup.client.api.utils.font.Fonts;
import ru.levelup.client.api.utils.render.ColorUtil;
import ru.levelup.client.api.utils.render.DrawHelper;
import ru.levelup.client.api.utils.render.animation.AnimationUtil;
import ru.levelup.client.protect.UserData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@ModuleHendler(name = "Interface", description = "", c = Module.Category.VISUALS)
public class Intarface extends Module{

    private BooleanSetting watermark = new BooleanSetting("WaterMark", BooleanSetting.ToggleState.ON);
    private BooleanSetting arraylist = new BooleanSetting("ModuleList", BooleanSetting.ToggleState.ON);
    private BooleanSetting potionInfo = new BooleanSetting("Potion Info", BooleanSetting.ToggleState.ON);
    private AnimationUtil enable = new AnimationUtil(0, 1, 0.03f);
    private AnimationUtil enable1 = new AnimationUtil(0, 1, 0.03f);
    private AnimationUtil enable2 = new AnimationUtil(0, 1, 0.03f);

    public Intarface(){
        addSetting(watermark, arraylist, potionInfo);
    }

    public void render(){
        enable.to = potionInfo.get() == BooleanSetting.ToggleState.ON ? 1 : 0;
        DrawHelper.scale(5, mc.getMainWindow().getScaledHeight() / 2, enable.getAnim(), ()-> renderPotions());
        enable2.to = arraylist.get() == BooleanSetting.ToggleState.ON ? 1 : 0;
        DrawHelper.scale(mc.getMainWindow().getScaledWidth() - 25, 6 + (14 / 2),
                enable2.getAnim(), ()-> renderArrayList());
        enable1.to = watermark.get() == BooleanSetting.ToggleState.ON ? 1 : 0;
        DrawHelper.scale(5 + (Fonts.font20.getWidth("XimeraClient / "
                + UserData.getGet().name + " / " + UserData.getGet().role) / 2), 5 + (15 / 2),
                enable1.getAnim(), ()-> renderWaterMark());
    }

    public void renderArrayList(){
        var font16 = Fonts.font16;
        List<Module> modules = Client.getInstance().getManager().getModules();
        modules.sort((module1, module2) -> {
            return font16.getWidth(module2.getName()) - font16.getWidth(module1.getName());
        });
        float y = 6;
        float index = 0.0F;
        int alpha = 150;
        int color = ColorUtil.getColor(18, alpha);
        ArrayList<Module> enableModule = new ArrayList();
        Iterator var9 = Client.getInstance().getManager().getModules().iterator();
        while(var9.hasNext()) {
            Module m = (Module)var9.next();
            if (m.getToggled()) {
                ++index;
                enableModule.add(m);
            }
        }
        int size = enableModule.size();
        float next = 0.0F;
        if (index < (float)(size - 1) && enableModule.get((int)index) != null) {
            next = (float)font16.getWidth(((Module)enableModule.get((int)index)).getName()) + 4.0F;
        } else {
            next = 0.0F;
        }

        enableModule.sort((m1, m2) -> {
            return font16.getWidth(m2.getName().toLowerCase()) - font16.getWidth(m1.getName().toLowerCase());
        });
        int lastIndex = enableModule.size() - 1;

        for(int i = 0; i < enableModule.size(); ++i) {
            Module m = (Module)enableModule.get(i);
            float x = mc.getMainWindow().getScaledWidth() - (float)font16.getWidth(m.getName().toLowerCase()) - 8;
            float wm = (float)font16.getWidth(m.getName().toLowerCase());
            float different = next - wm;
            float radius = different > 2.0F ? 2.0F : 2.0F;
            float rightFactor = i == 0 ? radius : 0.0F;
            if (i == 0) {
                DrawHelper.roundedRectangle(x, y, (wm + 4.0F), 14.0, rightFactor, rightFactor, 0, 3, color);
            } else if (i < lastIndex) {
                DrawHelper.roundedRectangle(x, y, (wm + 4.0F), 14.0, radius, 0, 0, 0.0, color);
            } else {
                DrawHelper.roundedRectangle(x, y, (wm + 4.0F), 14.0, radius, 0, radius, 0, color);
            }
            font16.drawString(m.getName().toLowerCase(), x + 1.0F, y + 1.0F, Client.getInstance().getTheme().getColor((int) y * 2));
            y += 14.0F;
        }
    }

    public void renderWaterMark(){
        float x = 0, y = 0;
        int color = ColorUtil.getColor(18, 150);
        DrawHelper.drawRound(x + 5, y + 5, 20 + Fonts.font20.getWidth("XimeraClient / "
                + UserData.getGet().name + " / " + UserData.getGet().role) - 2, 15, 4, color);
        Fonts.icon25.drawString("A", x + 6.0f, y + 6.5f, ColorUtil.getColor(180, 255));
        Fonts.font20.drawString("XimeraClient / " + UserData.getGet().name + " / " + UserData.getGet().role,
                x + 8 + Fonts.icon25.getWidth("A"), y + 4.5f, ColorUtil.getColor(180, 255));
    }

    public void renderPotions(){
        float x = 5, y = 100;
        ArrayList<EffectInstance> effects = new ArrayList();
        for (EffectInstance potionEffect : mc.player.getActivePotionEffects()) {
            if (potionEffect.getDuration() != 0
                    && !potionEffect.getPotion().getName().contains("effect.nightVision")) {
                effects.add(potionEffect);
            }
        }
        y = mc.getMainWindow().getScaledHeight() / 2 - (effects.size() * 24) / 2;
        for (EffectInstance effectInstance : effects) {
            if (effectInstance.getDuration() == 0) continue;
            String string = String.valueOf(effectInstance.getAmplifier() + 1);
            String string2 = I18n.format(effectInstance.getPotion().getName(), new Object[0]) + " ";
            String string3 = Potion.getDurationString(effectInstance);
            int color = ColorUtil.getColor(18, 150);
            float stringwidth = Fonts.font20.getWidth(string2 + string) + 18;
            DrawHelper.drawRound(x,y + 14, stringwidth, 20, 2, color);
            Fonts.font18.drawString(string2 + string, x + 18, y + 14, ColorUtil.getColor(180, 255));
            Fonts.font14.drawString(string3, x + 17.5f, y + 23, ColorUtil.getColor(180, 255));
            TextureAtlasSprite textureAtlasSprite = mc.getPotionSpriteUploader().getSprite(effectInstance.getPotion());
            mc.getTextureManager().bindTexture(textureAtlasSprite.getAtlasTexture().getTextureLocation());
            AbstractGui.blit(new MatrixStack(), (int) (x + 0), (int) (y + 16), 0, 16, 16, textureAtlasSprite);
            y+=24;
        }
    }

    @Override
    public void renderShader() {
        super.renderShader();
        render();
    }

    @EventHendler
    public void on2D(EventScreen evt){
        render();
    }
}
