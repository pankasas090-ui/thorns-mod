package com.example.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class ExampleMixin {

    @Inject(method = "drawSlot", at = @At("TAIL"))
    private void highlightThornsSlot(DrawContext context, Slot slot, CallbackInfo ci) {
        ItemStack stack = slot.getStack();
        if (stack.isEmpty()) return;

        ItemEnchantmentsComponent enchantments = stack.getOrDefault(
            DataComponentTypes.ENCHANTMENTS, 
            ItemEnchantmentsComponent.DEFAULT
        );

        boolean hasThorns = enchantments.getEnchantments().stream().anyMatch(entry -> 
            entry.getKey().map(key -> key.getValue().getPath().contains("thorns")).orElse(false)
        );

        if (hasThorns) {
            int x = slot.x;
            int y = slot.y;
            context.fill(x, y, x + 16, y + 16, 0x6600FF00);
        }
    }
}
