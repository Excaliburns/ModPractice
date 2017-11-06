package utils;

import items.HuskItem;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    @GameRegistry.ObjectHolder("tutmod:huskitem")
    public static HuskItem huskItem = new HuskItem();

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        huskItem.initModel();
    }
}
