package com.ragnardragus.yhnp.compat;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

import java.util.HashMap;
import java.util.Map;

public class CompatHandler {

    private static final Map<String, Runnable> MOD_MAP = new HashMap<>();

    public static void init() {
        MOD_MAP.put("curios", () -> MinecraftForge.EVENT_BUS.register(new CuriosCompat()));
        MOD_MAP.put("irons_spellbooks", () -> MinecraftForge.EVENT_BUS.register(new IronsSpellsbooksCompat()));

        MOD_MAP.forEach((modid, supplier) -> {
            if (ModList.get().isLoaded(modid)) {
                supplier.run();
            }
        });
    }
}
