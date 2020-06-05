/* 
 *      NoItemDespawn - 1.15.2 <> Idea and codedesign by PT400C - Eventhandling class
 *      � Jomcraft Network 2020
 */
package de.pt400c.noitemdespawn;

import java.util.List;

import de.pt400c.noitemdespawn.config.NIDConfig;
import net.minecraft.entity.item.ItemEntity;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class EventHooksFML {
	
	@SubscribeEvent
	public void despawnEvent(ItemExpireEvent event) {
		if (!event.getEntity().world.isRemote) {

			int number = 0;
			ItemEntity e = (ItemEntity) event.getEntity();

			List<ItemEntity> neighbours = e.world.getEntitiesWithinAABB(ItemEntity.class, e.getBoundingBox().grow(NIDConfig.COMMON.clumpRadius.get()));

			for (ItemEntity entities : neighbours) {
				if (!entities.equals(e) && entities.getItem().getItem().equals(e.getItem().getItem()))
					number++;
			}

			if (NIDConfig.COMMON.despawnWhitelist.get().get(0).equals("*") ? (number < NIDConfig.COMMON.maxClumpSize.get()) :(number < NIDConfig.COMMON.maxClumpSize.get() || !NIDConfig.COMMON.despawnWhitelist.get().contains(event.getEntityItem().getItem().getItem().getRegistryName().toString()))) {

				event.getEntityItem().lifespan = 2000000000;
				if (event.getEntityItem().age > 1999999997)
					event.getEntityItem().age = 0;
				event.setCanceled(true);
			}
		}
	}
	
	/*
	@SubscribeEvent
	public void tickEvent(RenderWorldLastEvent event) {
		if(Minecraft.getInstance().player.ticksExisted % 20 == 0) {
		for(Entity e : Minecraft.getInstance().world.getAllEntities()) {
			if(e instanceof ItemEntity) {
				
		
				e.move(MoverType.PISTON, new Vec3d(0, 1, 0));
			}
		}
		}
	}*/
	
	@SubscribeEvent
	public void serverStarting(FMLServerStartingEvent event) {
		CommandNID.register(event);
	}

}