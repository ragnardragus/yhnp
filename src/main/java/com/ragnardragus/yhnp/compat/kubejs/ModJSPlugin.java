package com.ragnardragus.yhnp.compat.kubejs;

import com.ragnardragus.yhnp.compat.kubejs.ModJSEvents;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import net.minecraftforge.fml.ModList;

public class ModJSPlugin extends KubeJSPlugin {

    @Override
    public void registerEvents() {
        ModJSEvents.GROUP.register();
    }
}
