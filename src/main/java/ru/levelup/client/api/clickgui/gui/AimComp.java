package ru.levelup.client.api.clickgui.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.MathHelper;
import ru.levelup.client.api.module.setting.Setting;
import ru.levelup.client.api.module.setting.impl.AimCenterSetting;
import ru.levelup.client.api.utils.render.ColorUtil;
import ru.levelup.client.api.utils.font.Fonts;
import ru.levelup.client.api.utils.misc.HoverUtils;
import ru.levelup.client.api.utils.misc.MathUtils;
import ru.levelup.client.api.utils.render.DrawHelper;

public class AimComp extends Component{

    private AimCenterSetting setting;
    private float x = 0, y = 0, width = 147;

    public AimComp(Setting setting1) {
        super(setting1);
        setting = (AimCenterSetting) setting1;
    }

    @Override
    public void renderSetting(MatrixStack matrix, float x1, float y1, float mouseX, float mouseY) {
        super.renderSetting(matrix, x1, y1, mouseX, mouseY);
        x = x1;
        y = y1;
        height = 87;
        int colorBackGround = ColorUtil.getColor(22, 255);
        DrawHelper.drawRound(x, y, width + 2, 87, 4, colorBackGround);
        int colorBackground2 = ColorUtil.getColor(33, 255);
        DrawHelper.drawRound(x + 3, y + 14, 70, 70, 2, colorBackground2);
        DrawHelper.drawRound(x + 76, y + 14, 70, 70, 2, colorBackground2);
        int colorText = ColorUtil.getColor(105, 255);
        Fonts.font16.drawString(setting.getName(), x + 3, y, colorText);
        int colorBackground3 = ColorUtil.getColor(47, 255);
        int numLines = 11;
        int interval = 6;
        for (int is = 1; is <= numLines; is++) {
            float xOfs = is * interval;
            DrawHelper.drawRound(x + xOfs + 1.5f, y + 14, 1, 70, 0, colorBackground3);
        }
        int numLines2 = 11;
        int interval2 = 6;
        for (int is = 1; is <= numLines2; is++) {
            float yOfs = is * interval2;
            DrawHelper.drawRound(x + 3, y + yOfs + 12.5f, 70, 1, 0, colorBackground3);
        }
        for (int is = 1; is <= numLines; is++) {
            float xOfs = is * interval;
            DrawHelper.drawRound(x + xOfs + 74, y + 14, 1, 70, 0, colorBackground3);
        }
        for (int is = 1; is <= numLines2; is++) {
            float yOfs = is * interval2;
            DrawHelper.drawRound(x + 76, y + yOfs + 12.5f, 70, 1, 0, colorBackground3);
        }
        int colorCrosshair = ColorUtil.getColor(80, 255);
        float crosshairSize = 6;
        float animx1 = (float) ((setting.current1 - setting.minimum1) /
                (setting.maximum1 - setting.minimum1) * (70 - crosshairSize));
        float animxy = (float) ((setting.current2 - setting.minimum2) /
                (setting.maximum2 - setting.minimum2) * (70 - crosshairSize));
        DrawHelper.drawRound(x + animx1, y + animxy + 14, crosshairSize, 1, 0, colorCrosshair);
        DrawHelper.drawRound(x + animx1 + (crosshairSize / 2f) - 0.5f, y + animxy
                - (crosshairSize / 2f) + 14.5f, 1, crosshairSize, 0, colorCrosshair);
        float animx2 = (float) ((setting.current3 - setting.minimum3) /
                (setting.maximum3 - setting.minimum3) * (70 - crosshairSize));
        float animxy2 = (float) ((setting.current4 - setting.minimum4) /
                (setting.maximum4 - setting.minimum4) * (70 - crosshairSize));
        DrawHelper.drawRound(x + animx2 + 76, y + animxy2 + 14, crosshairSize, 1, 0, colorCrosshair);
        DrawHelper.drawRound(x + animx2 + (crosshairSize / 2f) + 76, y + animxy2
                - (crosshairSize / 2f) + 14.5f, 1, crosshairSize, 0, colorCrosshair);
        Fonts.font14.drawCenteredString(new MatrixStack(),
                    setting.current3 + ":" + setting.current4, x + 76 + (70 / 2), y + (45), -1);
        Fonts.font14.drawCenteredString(new MatrixStack(),
                    setting.current1 + ":" + setting.current2, x + 3 + (70 / 2), y + (45), -1);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (HoverUtils.isHover(x, y + 14, 70, 70, mouseX, mouseY)){
            setting.sliding1 = true;
        }
        if (HoverUtils.isHover(x + 76, y + 14, 70, 70, mouseX, mouseY)){
            setting.sliding2 = true;
        }
    }

    @Override
    public void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        if (setting.sliding1) {
            float sliderValueX = (float) MathHelper.clamp(
                    MathUtils.round((mouseX - x - 5) / 70 * (setting.maximum1 - setting.minimum1)
                            + setting.minimum1, setting.increment1), setting.minimum1, setting.maximum1);
            float sliderValueY = (float) MathHelper.clamp(
                    MathUtils.round((mouseY - y - 14) / 70 * (setting.maximum2 - setting.minimum2)
                            + setting.minimum2, setting.increment2), setting.minimum2, setting.maximum2);
            setting.current1 = sliderValueX;
            setting.current2 = sliderValueY;
        }
        if (setting.sliding2) {
            float sliderValueX = (float) MathHelper.clamp(
                    MathUtils.round((mouseX - x - 81) / 70 * (setting.maximum3 - setting.minimum3)
                            + setting.minimum3, setting.increment3), setting.minimum3, setting.maximum3);
            float sliderValueY = (float) MathHelper.clamp(
                    MathUtils.round((mouseY - y - 14) / 70 * (setting.maximum4 - setting.minimum4)
                            + setting.minimum4, setting.increment4), setting.minimum4, setting.maximum4);
            setting.current3 = sliderValueX;
            setting.current4 = sliderValueY;
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        super.mouseReleased(mouseX, mouseY, button);
        setting.sliding1 = false;
        setting.sliding2 = false;
    }
}
