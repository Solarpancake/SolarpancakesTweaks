package de.solarpancake.tweaks.mixin;

import de.solarpancake.tweaks.cattree.CatTreeBinding;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.feline.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin {
    @Inject(method = "unableToMoveToOwner", at = @At("HEAD"), cancellable = true)
    private void solarpancakestweaks$stayAtCatTree(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof Cat cat && CatTreeBinding.isBound(cat)) {
            cir.setReturnValue(true);
        }
    }
}
