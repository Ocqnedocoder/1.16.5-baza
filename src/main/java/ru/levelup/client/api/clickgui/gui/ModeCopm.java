package ru.levelup.client.api.clickgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import ru.levelup.client.api.module.setting.Setting;
import ru.levelup.client.api.module.setting.impl.ModeSetting;
import ru.levelup.client.api.utils.render.ColorUtil;
import ru.levelup.client.api.utils.font.Fonts;
import ru.levelup.client.api.utils.misc.HoverUtils;
import ru.levelup.client.api.utils.render.DrawHelper;

public class ModeCopm extends Component{

    private ModeSetting setting;
    private float x = 0, y = 0, width = 146;

    public ModeCopm(Setting settings){
        super(settings);
        setting = (ModeSetting) settings;
    }

    @Override
    public void renderSetting(MatrixStack matrix, float x1, float y1, float mouseX, float mouseY) {
        super.renderSetting(matrix, x1, y1, mouseX, mouseY);
        x = x1;
        y = y1;
        if (setting.getModeType() == ModeSetting.MODESS.CYCLE) {
            height = 14;
            int colorBackGround = ColorUtil.getColor(22, 255);
            int colorText = ColorUtil.getColor(105, 255);
            DrawHelper.drawRound(x, y, width, 14, 4, colorBackGround);
            Fonts.font14.drawString(setting.getName() + ":" + setting.get(), x + 2, y + 1, colorText);
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (setting.getModeType() == ModeSetting.MODESS.CYCLE) {
            if (HoverUtils.isHover(x, y, width, 20, mouseX, mouseY)) {
                setting.cycle();
            }
        }
    }
}
