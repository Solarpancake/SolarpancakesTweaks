package de.solarpancake.tweaks.cattree;

import de.solarpancake.tweaks.Solarpancakestweaks;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.animal.feline.Cat;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = Solarpancakestweaks.MODID)
public class CatTreeEvents {
    @SubscribeEvent
    static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof Cat cat)) {
            return;
        }

        cat.goalSelector.addGoal(5, new CatLieOnCatTreeGoal(cat));

        cat.goalSelector.addGoal(6, new MoveTowardsRestrictionGoal(cat, 1.0) {
            @Override
            public boolean canUse() {
                return CatTreeBinding.isBound(cat) && super.canUse();
            }
        });
    }
}
