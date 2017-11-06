package blocks;

import core.TutMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class Husk extends Block
{
    public Husk()
    {
        super(Material.ROCK);
        setRegistryName("husk");
        setUnlocalizedName(TutMod.MODID + ".husk");
    }

}
