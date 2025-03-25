package ru.levelup.client.api.clickgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import ru.levelup.client.api.module.setting.Setting;
import ru.levelup.client.api.module.setting.impl.BooleanSetting;
import ru.levelup.client.api.utils.render.animation.AnimationUtil;
import ru.levelup.client.api.utils.render.ColorUtil;
import ru.levelup.client.api.utils.font.Fonts;
import ru.levelup.client.api.utils.misc.HoverUtils;
import ru.levelup.client.api.utils.render.DrawHelper;

public class BoolCopm extends Component{

    public BooleanSetting setting;
    private AnimationUtil anim1 = new AnimationUtil(0, 1, 0.15f);
    private AnimationUtil anim2 = new AnimationUtil(0, 1, 0.15f);
    private float x = 0, y = 0, width = 146;

    public BoolCopm(Setting settings) {
        super(settings);
        setting = (BooleanSetting) settings;
    }

    @Override
    public void renderSetting(MatrixStack matrix, float x1, float y1, float mouseX, float mouseY) {
        super.renderSetting(matrix, x1, y1, mouseX, mouseY);
        x = x1;
        y = y1;
        height = 14;
        int colorBackGround = ColorUtil.getColor(22, 255);
        int colorText = ColorUtil.getColor(105, 255);
        int colorEnable1 = ColorUtil.getColor(35, 255);
        int colorEnable2 = ColorUtil.getColor(99, 255);
        DrawHelper.drawRound(x, y, width, 14, 4, colorBackGround);
        Fonts.font14.drawString(setting.getName(), x + 2, y + 1, colorText);
        DrawHelper.drawRound(x + (width - 20), y + 4, 15, 6, 2,colorEnable1);
        anim1.speed = 0.05f;
        anim1.to = (setting.get() == BooleanSetting.ToggleState.ON) ? 7 : 0;
        DrawHelper.drawRound(x + (width - 20) + anim1.getAnim(), y + 3, 8, 8, 4, colorEnable2);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (HoverUtils.isHover(x, y, width, 20, mouseX, mouseY)){
            setting.toggle();
        }
    }
}
