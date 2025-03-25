package ru.levelup.client.api.clickgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.MathHelper;
import ru.levelup.client.api.module.setting.Setting;
import ru.levelup.client.api.module.setting.impl.FloatSetting;
import ru.levelup.client.api.utils.render.ColorUtil;
import ru.levelup.client.api.utils.font.Fonts;
import ru.levelup.client.api.utils.misc.HoverUtils;
import ru.levelup.client.api.utils.misc.MathUtils;
import ru.levelup.client.api.utils.render.DrawHelper;

public class FloatComp extends Component{

    private FloatSetting setting;
    private float x = 0, y = 0, wedth = 146;

    public FloatComp(Setting settings){
        super(settings);
        setting = (FloatSetting) settings;
    }

    @Override
    public void renderSetting(MatrixStack matrix, float x1, float y1, float mouseX, float mouseY) {
        super.renderSetting(matrix, x1, y1, mouseX, mouseY);
        height = 23;
        x = x1;
        y = y1;
        int colorBackGround = ColorUtil.getColor(22, 255);
        int colorText = ColorUtil.getColor(105, 255);
        int colorSliderBackGround = ColorUtil.getColor(35, 255);
        int colorSlider = ColorUtil.getColor(105, 255);
        DrawHelper.drawRound(x, y, wedth, 23, 4, colorBackGround);
        Fonts.font14.drawString(setting.getName(), x + 2, y + 1, colorText);
        Fonts.font14.drawString(String.valueOf(setting.current), x + wedth - Fonts.font14.getWidth(String.valueOf(setting.current)) - 3, y + 1, colorText);
        DrawHelper.drawRound(x + 3, y + 15, 140, 3, 1, colorSliderBackGround);
        float sliderWidth = (float) ((setting.get() - setting.minimum)
                / (setting.maximum - setting.minimum) * 140);
        if (setting.sliding){
            float sliderValue = (float) MathHelper.clamp(
                    MathUtils.round((mouseX - x - 3) / 140 * (setting.maximum - setting.minimum) +
                            setting.minimum, setting.increment),
                    setting.minimum, setting.maximum);

            setting.current = sliderValue;
        }
        DrawHelper.drawRound(x + 3, y + 15, sliderWidth, 3, 2, colorSlider);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (HoverUtils.isHover(x, y, 146, 23, mouseX, mouseY)){
            setting.sliding = true;
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        setting.sliding = false;
    }
}
