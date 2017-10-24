package com.cmdengineer.extra;

import com.cmdengineer.extra.apps.PasteFile;
import com.mrcrayfish.device.Reference;
import com.mrcrayfish.device.api.ApplicationManager;
import com.mrcrayfish.device.programs.ApplicationBoatRacers;
import com.mrcrayfish.device.programs.ApplicationNoteStash;
import com.mrcrayfish.device.programs.ApplicationPixelPainter;
import com.mrcrayfish.device.programs.auction.ApplicationMineBay;
import com.mrcrayfish.device.programs.email.ApplicationEmail;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(name = Info.MODNAME, modid = Info.MODID, version = Info.VERSION, acceptedMinecraftVersions = Info.MCACCEPTED, dependencies = Info.DEPENDS)
public class Main
{
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	ApplicationManager.registerApplication(new ResourceLocation(Info.MODID, "pastefile"), PasteFile.class);
    }
	
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	
    }
    
}
