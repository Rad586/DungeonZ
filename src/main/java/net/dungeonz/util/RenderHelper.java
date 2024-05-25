package net.dungeonz.util;

import com.mojang.blaze3d.systems.RenderSystem;

import net.dungeonz.access.InGameHudAccess;
import net.dungeonz.init.ConfigInit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class RenderHelper {

    public static void renderDungeonCountdown(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (((InGameHudAccess) client.inGameHud).getDungeonCountdownRemainingTicks() > 0) {
            RenderSystem.enableBlend();

            Text text = Text.translatable("hud.dungeonz.dungeon_countdown", ((InGameHudAccess) client.inGameHud).getDungeonCountdownTicks() / 20);
            context.getMatrices().push();
            context.getMatrices().translate(context.getScaledWindowWidth() / 2 - client.textRenderer.getWidth(text) / 2 + ConfigInit.CONFIG.countdownX,
                    context.getScaledWindowHeight() / 2 + ConfigInit.CONFIG.countdownY, 0.0f);
            context.getMatrices().scale(ConfigInit.CONFIG.countdownSize, ConfigInit.CONFIG.countdownSize, ConfigInit.CONFIG.countdownSize);

            context.setShaderColor(1.0f, 1.0f, 1.0f, (float) ((InGameHudAccess) client.inGameHud).getDungeonCountdownRemainingTicks() / 18.0f);
            context.drawTextWithShadow(client.textRenderer, text, 0, 0, 0xFFFFFF);
            context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            context.getMatrices().pop();
            RenderSystem.disableBlend();
        }
    }

}
