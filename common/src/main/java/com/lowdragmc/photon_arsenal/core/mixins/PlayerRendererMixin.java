package com.lowdragmc.photon_arsenal.core.mixins;

import com.lowdragmc.photon_arsenal.item.WeaponItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "getArmPose", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/UseAnim;"),
            cancellable = true)
    private static void injectGetArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        if (player.getItemInHand(hand).getItem() instanceof WeaponItem) {
            cir.setReturnValue(HumanoidModel.ArmPose.BOW_AND_ARROW);
        }
    }
}
