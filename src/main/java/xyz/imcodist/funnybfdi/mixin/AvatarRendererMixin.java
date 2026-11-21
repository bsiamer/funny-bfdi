package xyz.imcodist.funnybfdi.mixin;

import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.imcodist.funnybfdi.accessor.BFDIRenderState;
import xyz.imcodist.funnybfdi.features.BFDIHeadFeature;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin {
    @Shadow
    protected abstract boolean addLayer(RenderLayer<?, ?> layer);

    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(EntityRendererProvider.Context ctx, boolean slim, CallbackInfo ci) {
        this.addLayer(new BFDIHeadFeature((RenderLayerParent<AvatarRenderState, PlayerModel>) (Object) this));
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    private void onExtractRenderState(Avatar avatar, AvatarRenderState state, float partialTick, CallbackInfo ci) {
        if (state instanceof BFDIRenderState bfdiState) {
            bfdiState.bfdi$setUuid(avatar.getUUID());
            bfdiState.bfdi$setHealth(avatar.getHealth());
            bfdiState.bfdi$setAbsorption(avatar.hasEffect(MobEffects.ABSORPTION));
            bfdiState.bfdi$setPoison(avatar.hasEffect(MobEffects.POISON));
            bfdiState.bfdi$setSubmergedInWater(avatar.isUnderWater());
            bfdiState.bfdi$setHurtTime(avatar.hurtTime);
        }
    }
}
