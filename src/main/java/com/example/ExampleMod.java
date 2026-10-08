package com.example;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class ExampleMod implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Подключаем отрисовку поверх слотов инвентаря
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof HandledScreen<?> handledScreen) {
                ScreenEvents.afterRender(screen).register((scr, context, mouseX, mouseY, tickDelta) -> {
                    for (Slot slot : handledScreen.getScreenHandler().slots) {
                        ItemStack stack = slot.getStack();
                        if (!stack.isEmpty()) {
                            ItemEnchantmentsComponent enchantments = stack.getOrDefault(
                                DataComponentTypes.ENCHANTMENTS, 
                                ItemEnchantmentsComponent.DEFAULT
                            );

                            // Проверяем наличие Шипов (Thorns)
                            boolean hasThorns = enchantments.getEnchantments().stream().anyMatch(entry -> 
                                entry.getKey().map(key -> key.getValue().getPath().contains("thorns")).orElse(false)
                            );

                            if (hasThorns) {
                                int x = handledScreen.x + slot.x;
                                int y = handledScreen.y + slot.y;
                                // Зелёная подсветка точно поверх 16x16 ячейки
                                context.fill(x, y, x + 16, y + 16, 0x6600FF00);
                            }
                        }
                    }
                });
            }
        });
    }
}
