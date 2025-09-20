package com.netroaki.chex.client.toasts;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Custom toast for launch denial notifications
 */
public class LaunchDeniedToast implements Toast {
    
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/advancements/backgrounds/stone.png");
    
    private final Component title;
    private final Component message;
    private final long displayTime;
    private final ToastType type;
    
    public LaunchDeniedToast(Component title, Component message, ToastType type) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.displayTime = System.currentTimeMillis() + 5000; // 5 seconds
    }
    
    @Override
    public Toast.Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long time) {
        if (time > displayTime) {
            return Toast.Visibility.HIDE;
        }
        
        // Set up rendering
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        
        // Draw background
        guiGraphics.blit(TEXTURE, 0, 0, 0, 0, this.width(), this.height());
        
        // Draw border based on type
        int borderColor = getBorderColor();
        guiGraphics.fill(0, 0, this.width(), 1, borderColor);
        guiGraphics.fill(0, this.height() - 1, this.width(), this.height(), borderColor);
        guiGraphics.fill(0, 0, 1, this.height(), borderColor);
        guiGraphics.fill(this.width() - 1, 0, this.width(), this.height(), borderColor);
        
        // Draw title
        guiGraphics.drawString(toastComponent.getMinecraft().font, title, 8, 8, 0xFFFFFF);
        
        // Draw message
        guiGraphics.drawString(toastComponent.getMinecraft().font, message, 8, 20, 0xCCCCCC);
        
        return Toast.Visibility.SHOW;
    }
    
    private int getBorderColor() {
        return switch (type) {
            case ERROR -> 0xFF0000; // Red
            case WARNING -> 0xFFAA00; // Orange
            case INFO -> 0x00AAFF; // Blue
            case SUCCESS -> 0x00FF00; // Green
        };
    }
    
    public enum ToastType {
        ERROR,
        WARNING,
        INFO,
        SUCCESS
    }
    
    public static void show(ToastComponent toastComponent, Component title, Component message, ToastType type) {
        toastComponent.addToast(new LaunchDeniedToast(title, message, type));
    }
}
