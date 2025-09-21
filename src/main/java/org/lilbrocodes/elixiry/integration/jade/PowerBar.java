package org.lilbrocodes.elixiry.integration.jade;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.lilbrocodes.elixiry.common.block.entity.WitchCauldronBlockEntity;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IProgressStyle;

public class PowerBar implements IProgressStyle {
    private static final int BORDER_COLOR = 0xFFAAAAAA;
    private static final int BACKGROUND_COLOR = 0xFF000000;
    private final float charge;

    public PowerBar(float charge) {
        this.charge = charge;
    }

    @Override
    public IProgressStyle color(int i, int i1) {
        return this;
    }

    @Override
    public IProgressStyle textColor(int i) {
        return this;
    }

    @Override
    public IProgressStyle vertical(boolean b) {
        return this;
    }

    @Override
    public IProgressStyle overlay(IElement iElement) {
        return this;
    }

    private void drawBackground(DrawContext drawContext, float x, float y, float width, float height, int borderThickness) {
        drawContext.fill(
                (int) x,
                (int) y,
                (int) (x + width),
                (int) (y + height),
                BORDER_COLOR
        );

        drawContext.fill(
                (int) (x + borderThickness),
                (int) (y + borderThickness),
                (int) (x + width - borderThickness),
                (int) (y + height - borderThickness),
                BACKGROUND_COLOR
        );
    }

    @Override
    public void render(DrawContext drawContext, float x, float y, float width, float height, float progress, Text text) {
        int borderThickness = 1;
        int padding = 1;

        drawBackground(drawContext, x, y, width, height, borderThickness);

        float clampedProgress = Math.min(Math.max(progress, 0f), 1f);

        float innerWidth = width - 2 * (borderThickness + padding);
        float innerHeight = height - 2 * (borderThickness + padding);

        float filledWidth = innerWidth * clampedProgress;

        float startX = x + borderThickness + padding;
        float startY = y + borderThickness + padding;

        drawContext.fill(
                (int) startX,
                (int) startY,
                (int) (startX + filledWidth),
                (int) (startY + innerHeight),
                0xFFFFFFFF
        );

        TextRenderer font = MinecraftClient.getInstance().textRenderer;
        Text chargeText = Text.literal(String.valueOf((int) charge))
                .append(Text.literal(String.format("/%d", WitchCauldronBlockEntity.MAX_POWER)));

        int textWidth = font.getWidth(chargeText);
        int textX = (int) (x + width - textWidth);
        int textY = (int) (y + height + 2);

        drawContext.drawTextWithShadow(font, chargeText, textX, textY, 0xFFFFFF);
    }

}
