package de.solarpancake.tweaks.mixin;

import de.solarpancake.tweaks.cattree.CatTreeBinding;
import net.minecraft.world.entity.animal.feline.Cat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.world.entity.animal.feline.Cat$CatRelaxOnOwnerGoal")
public abstract class CatRelaxOnOwnerGoalMixin {
    @Shadow
    @Final
    private Cat cat;

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void solarpancakestweaks$stayAtCatTree(CallbackInfoReturnable<Boolean> cir) {
        if (CatTreeBinding.isBound(cat)) {
            cir.setReturnValue(false);
        }
    }
}
