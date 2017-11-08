package items;

import core.TutMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import network.tutmodMessage;
import network.tutmodPacketHandler;
import utils.ChestCoordStorage;
import utils.ModItems;

import java.util.ArrayList;

public class HuskItem extends Item
{

    private ChestCoordStorage[] chestList = new ChestCoordStorage[3];
    private ArrayList<ItemStack> itemList = new ArrayList<>();
    private ArrayList<Integer> airList = new ArrayList<>();
    private int totalItemUses = 0;
    private boolean itemisUsed = false;

    public HuskItem()
    {
        setRegistryName("huskitem");
        setUnlocalizedName(TutMod.MODID + ".huskitem");

    }

    @SideOnly(Side.CLIENT)
    public void initModel()
    {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        if (!worldIn.isRemote)
        {
            if (player.getHeldItem(player.getActiveHand()).getItem() == ModItems.huskItem)
            {

                TileEntity locatedChest = worldIn.getTileEntity(pos);

                if (locatedChest instanceof TileEntityChest)
                {
                    System.out.println("Chest located at: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());


                    if (!locatedChest.getTileData().hasKey("hasBeenAccessed") && totalItemUses <= 1)
                    {

                        chestList[totalItemUses] = new ChestCoordStorage(pos.getX(), pos.getY(), pos.getZ());
                        totalItemUses++;

                        locatedChest.getTileData().setBoolean("hasBeenAccessed", true);

                        for (int i = 0; i < ((TileEntityChest) locatedChest).getSizeInventory(); i++)
                        {

                            ItemStack itemHolder = (((TileEntityChest) locatedChest).getStackInSlot(i));

                            if (itemHolder.getItem() != Items.AIR)
                            {
                                player.sendMessage(new TextComponentString("Getting Called!"));
                                itemList.add(itemHolder);
                            }
                        }
                    }

                    if (!locatedChest.getTileData().getBoolean("hasBeenAccessed") && itemisUsed)
                    {
                        chestList[2] = new ChestCoordStorage(pos.getX(), pos.getY(), pos.getZ());
                        System.out.println("Using item!");

                        for (int i = 0; i < ((TileEntityChest) locatedChest).getSizeInventory(); i++)
                        {
                            if (((TileEntityChest) locatedChest).getStackInSlot(i).getItem() == Items.AIR)
                            {
                                airList.add(i);
                            }
                        }

                        if (airList.size() > itemList.size())
                        {
                            System.out.println(itemList);
                            System.out.println(itemList.size());

                            tutmodPacketHandler.INSTANCE.sendToServer(new tutmodMessage(chestList, itemList, airList));

                            totalItemUses = 0;

                        } else
                        {
                            System.out.println("Not enough space!");
                        }
                    }

                    if (totalItemUses == 2)
                        itemisUsed = true;
                }

            }
        }
        return super.onItemUseFirst(player, worldIn, pos, side, hitX, hitY, hitZ, hand);
    }
}