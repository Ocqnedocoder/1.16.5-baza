//package ru.levelup.client.api.clickgui;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import net.minecraft.client.Minecraft;
//import net.minecraft.util.math.MathHelper;
//import ru.levelup.client.Client;
//import ru.levelup.client.api.module.Module;
//import ru.levelup.client.api.module.setting.Setting;
//import ru.levelup.client.api.module.setting.impl.AimCenterSetting;
//import ru.levelup.client.api.module.setting.impl.BooleanSetting;
//import ru.levelup.client.api.module.setting.impl.FloatSetting;
//import ru.levelup.client.api.module.setting.impl.ModeSetting;
//import ru.levelup.client.api.uyils.*;
//import ru.levelup.client.api.uyils.rect.DrawHelper;
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class Panel {
//
//    private boolean openSetting = false;
//    private AnimationUtil anim1[],
//            anim2 = new AnimationUtil(0, 1, 0.05f),anim3 = new AnimationUtil(0, 1, 0.05f),anim4[],
//            anim5 = new AnimationUtil(0, 1, 0.05f),anim6 = new AnimationUtil(0, 1, 0.05f),anim7 = new AnimationUtil(0, 1, 0.05f),
//            anim8 = new AnimationUtil(0, 1, 0.05f),anim9 = new AnimationUtil(0, 1, 0.05f),anim10 = new AnimationUtil(0, 1, 0.05f);
//
//    private float x =(Minecraft.getInstance().getMainWindow().getScaledWidth() / 2) - (551 / 2),
//            y = (Minecraft.getInstance().getMainWindow().getHeight() / 2) - (440 ), width = 551, height = 380;
//    private Module.Category currentCategory;
//    private Module selectedModule;
//
//    public Panel(){
//        int numCategories = Module.Category.values().length;
//        anim1 = new AnimationUtil[numCategories];
//        for (int i = 0; i < numCategories; i++) {
//            anim1[i] = new AnimationUtil(0, 1, 0.05f);
//        }
//        int numModules = Client.getInstance().getManager().getModules().size();
//        anim4 = new AnimationUtil[numModules];
//        for (int i = 0; i < numModules; i++) {
//            anim4[i] = new AnimationUtil(0, 1, 0.05f);
//        }
//    }
//
//    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
//        int colorBackGround = ColorUtil.getColor(18, 210);
//        int colorLine = ColorUtil.getColor(37, 37, 37, 153);
//        x = 0;
//        anim2.speed = 0.05f;
//        anim2.to = (Minecraft.getInstance().getMainWindow().getScaledWidth() / 2) - (551 / 2);
//        x = anim2.getAnim();
//        DrawHelper.drawBlur(25, ()-> DrawHelper.drawRound(x, y, width, height, 6, colorBackGround));
//        DrawHelper.drawRound(x, y, width, height, 6, colorBackGround);
//        DrawHelper.drawRound(x + 51, y, 1, height , 0, colorLine);
//        DrawHelper.drawRound(x + 52, y + 35, width - 52, 1, 0, colorLine);
//        anim3.speed = 0.01f;
//        anim3.to = 160;
//        Fonts.icon75.drawString("A", x + 7, y + 10, ColorUtil.getColor(159, (int) (anim2.getAnim())));
//        float offset = 90;
//        for (int i = 0; i < Module.Category.values().length; i++) {
//            Module.Category category = Module.Category.values()[i];
//            anim1[i].speed = 0.02f;
//            anim1[i].to = (category == currentCategory) ? 255 : 160;
//            int alpha = (int) anim1[i].getAnim();
//            int colorIcon = ColorUtil.getColor(159,  alpha);
//            float x1 = x;
//            if (category == Module.Category.VISUALS) {
//                x1 -= 2;
//            }
//            Fonts.icon65.drawString(icon(category), x1 + 12, y + offset, colorIcon);
//            offset += Fonts.icon65.getHeight() + 10;
//        }
//        {
//            float xOffset = 70;
//            float y1 = y + 89;
//            float x1 = x + 71;
//            float height = 0;
//            float width1 = 0;
//            for (int i = 0; i < Client.getInstance().getManager().getModules().size(); i++) {
//                Module module = Client.getInstance().getManager().getModules().get(i);
//                anim4[i].speed = 0.03f;
//                anim4[i].to = module.getToggled() ? 255 : 160;
//                int alpha = (int) anim4[i].getAnim();
//                if (module.getCategory() == currentCategory) {
//                    Fonts.font25.drawString(module.getName(), x + xOffset,
//                            y + 8, ColorUtil.getColor(200, alpha));
//                    xOffset += Fonts.font25.getWidth(module.getName()) + 15;
//                    Module module1 = selectedModule;
//                    if (selectedModule != null && module1 == module) {
//                        anim5.speed = 0.02f;
//                        anim5.to = openSetting ? 60 : 1;
//                        int colors = ColorUtil.getColor(24, (int) anim5.getAnim());
//                        DrawHelper.drawRound(x + 61, y + 42, 479, 326, 6, colors);
//                        anim6.to = openSetting ? 255 : 1;
//                        int colors2 = ColorUtil.getColor(141, (int) (anim6.getAnim()));
//                        Fonts.font25.drawString("Settings " + selectedModule.getName(),
//                                x + 67, y + 42, colors2);
//                        int colors3 = ColorUtil.getColor(95, (int) (anim6.getAnim()));
//                        Fonts.font25.drawString(selectedModule.getDescription(), x + 67,
//                                y + 40 + Fonts.font18.getHeight(), colors3);
//                        List<Setting> sortedSettings = selectedModule.getSettings().stream()
//                                .sorted(Comparator.comparingInt(setting -> {
//                                    if (setting instanceof BooleanSetting) {
//                                        return 0;
//                                    } else if (setting instanceof ModeSetting) {
//                                        return 1;
//                                    } else if (setting instanceof FloatSetting) {
//                                        return 2;
//                                    } else if (setting instanceof AimCenterSetting) {
//                                        return 3;
//                                    } else {
//                                        return 4; // Other types
//                                    }
//                                }))
//                                .collect(Collectors.toList());
//                        int columnIndex = 0;
//                        for (Setting setting : sortedSettings) {
//                            if (setting instanceof FloatSetting set4){
//                                width1 = 210;
//                                height = 42;
//                                int colors4 = ColorUtil.getColor(24, (int) (anim6.getAnim()));
//                                {
//                                    DrawHelper.drawRound(x1, y1, 210, 42, 4, colors4);
//                                    Fonts.font25.drawString(set4.getName(), x1 + 3, y1 + 1, colors3);
//                                    Fonts.font25.drawString(String.valueOf(set4.current),
//                                            x1 + 203 - Fonts.font25.getWidth(String.valueOf(set4.current)),
//                                            y1 + 1, colors3);
//                                    {
//                                        int colors5 = ColorUtil.getColor(63, (int) (anim6.getAnim()));
//                                        DrawHelper.drawRound(x1 + 3, y1 + 28, 199, 7, 2, colors5);
//                                        {
//                                            int colors6 = ColorUtil.getColor(104, (int) (anim6.getAnim()));
//                                            float sliderWidth = (float) ((set4.current - set4.minimum)
//                                                    / (set4.maximum - set4.minimum) * 199);
//                                            DrawHelper.drawRound(x1 + 3, y1 + 28, sliderWidth, 7, 2, colors6);
//                                        }
//                                    }
//                                }
//                            }
//                            if (setting instanceof ModeSetting set2) {
//                                int colors4 = ColorUtil.getColor(24, (int) (anim6.getAnim()));
//                                width1 = 210;
//                                DrawHelper.drawRound(x1, y1, width1, 20, 4, colors4);
//                                Fonts.font25.drawString(set2.getName() + ":"+set2.currentMode
//                                        , x1 + 3, y1 + 1, colors3);
//                                height = 20;
//                            }
//                            if (!(setting instanceof ModeSetting)) {
//                                if (setting instanceof AimCenterSetting set1) {
//                                    height = 220;
//                                    width1 = 210;
//                                    int colors4 = ColorUtil.getColor(24, (int) (anim6.getAnim()));
//                                    {
//                                        DrawHelper.drawRound(x1, y1, 210, 210, 6, colors4);
//                                        Fonts.font25.drawString(set1.getName(), x1 + 5, y1 + 3, colors3);
//                                        Fonts.font25.drawString(set1.current1 + ":" + set1.current2,
//                                                x1 + 203 - Fonts.font25.getWidth(set1.current1 + ":"
//                                                        + set1.current2), y1 + 3, colors3);
//                                        int colorBacks = ColorUtil.getColor(35, (int) (anim6.getAnim()));
//                                        int colorBacks2 = ColorUtil.getColor(48, (int) (anim6.getAnim()));
//                                        DrawHelper.drawRound(x1 + 5, y1 + 25, 200, 181, 4, colorBacks);
//                                        {
//                                            int numLines = 13;
//                                            int interval = 15;
//                                            for (int is = 1; is <= numLines; is++) {
//                                                float xOfs = is * interval;
//                                                DrawHelper.drawRound(x1 + xOfs - 1, y1 + 26, 1, 180, 0, colorBacks2);
//                                            }
//                                            int numLines2 = 12;
//                                            int interval2 = 15;
//                                            for (int is = 1; is <= numLines2; is++) {
//                                                float yOfs = is * interval2;
//                                                DrawHelper.drawRound(x1 + 5, y1 + yOfs + 20, 200, 1, 0, colorBacks2);
//                                            }
//                                        }
//                                        {
//                                            float animx1 = (float) ((set1.get1() - set1.minimum1) /
//                                                    (set1.maximum1 - set1.minimum1) * 102);
//                                            float animx2 = (float) ((set1.get2() - set1.minimum2) /
//                                                    (set1.maximum2 - set1.minimum2) * 102);
//                                            StencilUtil.initStencilToWrite();
//                                            DrawHelper.drawRound(x1, y1, 210, 210, 6, colors4);
//                                            StencilUtil.readStencilBuffer(1);
//                                            int colors5 = ColorUtil.getColor(88, (int) (anim6.getAnim()));
//                                            float clippedAnimX1 = MathHelper.clamp(x1 + 5 + animx1 - 1,
//                                                    x1 + 5, x1 + 5 + 200);
//                                            float clippedAnimX2 = MathHelper.clamp(y1 + 26,
//                                                    y1 + 26, y1 + 26 + 180);
//                                            DrawHelper.drawRound(clippedAnimX1, clippedAnimX2, 1, 180, 0, colors5);
//                                            DrawHelper.drawRound(x1 + 5, y1 + animx2 + 25, 200, 1, 0, colors5);
//                                            StencilUtil.uninitStencilBuffer();
//                                        }
//                                    }
//                                }
//                            }
//                            if (setting instanceof BooleanSetting set3){
//                                int colors4 = ColorUtil.getColor(24, (int) (anim6.getAnim()));
//                                int colors5 = ColorUtil.getColor(30, (int) (anim6.getAnim()));
//                                DrawHelper.drawRound(x1, y1, 210, 20, 4, colors4);
//                                anim7.to = set3.get() ? 10 : 0;
//                                Fonts.font25.drawString(set3.getName(), x1 + 3, y1 + 1, colors3);
//                                {
//                                    DrawHelper.drawRound(x1 + 185,y1 + 5, 20, 10, 4, colors5);
//                                    DrawHelper.drawRound(x1 + 185 + anim7.getAnim(),y1 + 5, 10, 10, 4, colors3);
//                                }
//                                width1 = 210;
//                                height = 20;
//                            }
//                            columnIndex++;
//                            if (columnIndex % 2 == 0) {
//                                x1 = x + 71;
//                                y1 += height + 10;
//                            } else {
//                                x1 += width1 + 10;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    public void mouseClicked(double mouseX, double mouseY, int button) {
//        float xOffset = 60;
//        for (Module module : Client.getInstance().getManager().getModules()) {
//            if (module.getCategory() == currentCategory) {
//                if (HoverUtils.isHover(x + xOffset, y + 8, Fonts.font25.getWidth(module.getName())
//                        , Fonts.font25.getHeight(), mouseX, mouseY)) {
//                    if (button == 0) {
//                        module.toggled();
//                    } else if (button == 1) {
//                        openSetting = !openSetting;
//                        if (openSetting) {
//                            selectedModule = module;
//                        }
//                    }
//                }
//                xOffset += Fonts.font25.getWidth(module.getName()) + 15;
//            }
//        }
//        float offset = 90;
//        for (Module.Category category : Module.Category.values()){
//            if (HoverUtils.isHover(x + 12, y + offset, 25, 25, mouseX, mouseY) && button == 0){
//                currentCategory = category;
//            }
//            offset += Fonts.icon65.getHeight() + 10;
//        }
//
//        Module module1 = selectedModule;
//
//        if (module1 != null) {
//            float y1 = y + 89;
//            float x1 = x + 71;
//            float height = 0;
//            float height2 = 0;
//            float width1 = 0;
//            int columnIndex = 0;
//            boolean aimCenterSettingFound = false;
//            List<Setting> sortedSettings = selectedModule.getSettings().stream()
//                    .sorted(Comparator.comparingInt(setting -> {
//                        if (setting instanceof BooleanSetting) {
//                            return 0;
//                        } else if (setting instanceof ModeSetting) {
//                            return 1;
//                        } else if (setting instanceof FloatSetting) {
//                            return 2;
//                        } else if (setting instanceof AimCenterSetting) {
//                            return 3;
//                        } else {
//                            return 4; // Other types
//                        }
//                    }))
//                    .collect(Collectors.toList());
//            if (module1 == Client.getInstance().getManager().getModule("SwingAnimation")) {
//                for (Setting setting : module1.getSettings()) {
//                    width1 = 210;
//                    height = (setting instanceof BooleanSetting) ? 20 : ((setting instanceof FloatSetting) ? 42 : 220);
//
//                    if (setting instanceof ModeSetting set2 && HoverUtils.isHover(x1, y1 + 7, width1, 20, mouseX, mouseY)) {
//                        height = 20;
//                        set2.cycle();
//                    }
//
//                    if (setting instanceof AimCenterSetting set1 && HoverUtils.isHover(x1, y1, width1, 210, mouseX, mouseY)) {
//                        set1.dragging = true;
//                        aimCenterSettingFound = true;
//                    }
//
//                    if (setting instanceof BooleanSetting set3 && HoverUtils.isHover(x1, y1, width1, 20, mouseX, mouseY)) {
//                        height = 20;
//                        set3.setToggled(!set3.get());
//                    }
//
//                    if (setting instanceof FloatSetting set4) {
//                        if (HoverUtils.isHover(x1, y1, width1, height, mouseX, mouseY)) {
//                            set4.sliding = true;
//                        }
//                    }
//
//                    columnIndex++;
//                    if (columnIndex % 2 == 0) {
//                        x1 = x + 71;
//                        y1 += height + 10;
//                    } else {
//                        x1 += width1 + 10;
//                    }
//                }
//            } else{
//                for (Setting setting : sortedSettings) {
//                    width1 = 210;
//                    height = (setting instanceof BooleanSetting) ? 20 : 42;
//                    if (setting instanceof ModeSetting set2 && HoverUtils.isHover(x1, y1 + 7, width1, 20, mouseX, mouseY)) {
//                        height = 20;
//                        set2.cycle();
//                    }
//                    if (setting instanceof BooleanSetting set3 && HoverUtils.isHover(x1, y1, width1, 20, mouseX, mouseY)) {
//                        height = 20;
//                        set3.setToggled(!set3.get());
//                    }
//                    if (setting instanceof FloatSetting set4) {
//                        if (HoverUtils.isHover(x1, y1, width1, height, mouseX, mouseY)) {
//                            set4.sliding = true;
//                        }
//                    }
//                    columnIndex++;
//                    if (columnIndex % 2 == 0) {
//                        x1 = x + 71;
//                        y1 += height + 10;
//                    } else {
//                        x1 += width1 + 10;
//                    }
//                }
//            }
//        }
//    }
//
//    public void mouseReleased(double mouseX, double mouseY, int button) {
//        if (selectedModule != null) {
//            for (Setting setting : selectedModule.getSettings()) {
//                if (setting instanceof AimCenterSetting set1) {
//                    set1.dragging = false;
//                }
//                if (setting instanceof FloatSetting set4) {
//                    set4.sliding = false;
//                }
//            }
//        }
//    }
//
//    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
//        if (selectedModule != null) {
//            float x1 = x + 71;
//            float y1 = y + 89;
//            float height = 0;
//            int columnIndex = 0;
//            for (Setting setting : selectedModule.getSettings()) {
//                if (setting instanceof AimCenterSetting set1 && set1.dragging) {
//                    height = 210;
//                    float sliderValueX = (float) MathHelper.clamp(
//                            MathUtils.round((mouseX - x1 - 5) / 102 * (set1.maximum1 - set1.minimum1)
//                                    + set1.minimum1, set1.increment1), set1.minimum1, set1.maximum1);
//                    float sliderValueY = (float) MathHelper.clamp(
//                            MathUtils.round((mouseY - y1 - 5) / 102 * (set1.maximum2 - set1.minimum2)
//                                    + set1.minimum2, set1.increment2), set1.minimum2, set1.maximum2);
//                    set1.current1 = sliderValueX;
//                    set1.current2 = sliderValueY;
//                }
//                if (setting instanceof FloatSetting set4 && set4.sliding) {
//                    height = 42;
//                    float sliderValue = (float) MathHelper.clamp(
//                            MathUtils.round((mouseX - x1 - 5) / 199 * (set4.maximum - set4.minimum)
//                                    + set4.minimum, set4.increment), set4.minimum, set4.maximum);
//                    set4.current = sliderValue;
//                }
//                columnIndex++;
//                if (columnIndex % 2 == 0) {
//                    x1 = x + 71;
//                    y1 += height + 10;
//                } else {
//                    x1 += 220;
//                }
//            }
//        }
//    }
//
//    public void keyPressed(int keyCode, int scanCode, int modifiers) {
//
//    }
//
//    public String icon(Module.Category c) {
//        switch (c) {
//            case COMBAT: return "b";
//            case MOVEMENT: return "d";
//            case VISUALS: return "c";
//            case PLAYER: return "e";
//            case MISC: return "f";
//            default: return "";
//        }
//    }
//
//}
