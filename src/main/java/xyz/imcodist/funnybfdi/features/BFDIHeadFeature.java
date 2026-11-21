package xyz.imcodist.funnybfdi.features;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import xyz.imcodist.funnybfdi.FunnyBFDI;
import xyz.imcodist.funnybfdi.accessor.BFDIRenderState;
import xyz.imcodist.funnybfdi.other.Config;
import xyz.imcodist.funnybfdi.other.MouthManager;

import java.util.UUID;

public class BFDIHeadFeature extends RenderLayer<AvatarRenderState, PlayerModel> {
    private final ModelPart base;
    private final Model.Simple mouthModel;

    public BFDIHeadFeature(RenderLayerParent<AvatarRenderState, PlayerModel> context) {
        super(context);

        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        root.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, 4.51F, 8.0F, 16.0F, 0.0F), PartPose.ZERO);
        LayerDefinition layerDefinition = LayerDefinition.create(meshDefinition, 16, 16);

        this.base = layerDefinition.bakeRoot().getChild("mouth");
        this.mouthModel = new Model.Simple(this.base, RenderTypes::entityTranslucent);
    }

    @Override
    public void submit(PoseStack matrices, SubmitNodeCollector submitNodeCollector, int light, AvatarRenderState state, float limbAngle, float limbDistance) {
        if (!Config.enabled) return;
        if (Config.mouthSize <= 0.0f) return;

        ModelPart head = getParentModel().getHead();
        if (state.isInvisible) return;
        if (!head.visible) return;

        BFDIRenderState bfdiState = (BFDIRenderState) state;
        UUID playerUuid = bfdiState.bfdi$getUuid();
        if (playerUuid == null) return;

        MouthManager.MouthState mouthState = MouthManager.getPlayerMouthState(playerUuid);

        float health = bfdiState.bfdi$getHealth();
        String mouthExpression = "normal";
        if (health <= 10.0f) {
            mouthExpression = "sad";
        }

        String mouth = "idle";

        boolean hasAbsorb = bfdiState.bfdi$hasAbsorption();
        boolean hasPoison = bfdiState.bfdi$hasPoison();

        if (mouthExpression.equals("normal") && hasAbsorb) {
            mouthExpression = "normal";
            mouth = "absorption";
        }

        if (health <= 4.0f) {
            mouthExpression = "sad";
            mouth = "critical";
        }

        if (bfdiState.bfdi$isSubmergedInWater() || state.isInWater) {
            mouth = "water";
        }

        if (mouthState != null && mouthState.talking) {
            String shape = mouthState.transitionMouthShape;
            if (!shape.equals("0")) {
                if (!mouth.equals("absorption") && !mouth.equals("critical") && !mouth.equals("water")) {
                    mouth = "talk" + shape;
                } else {
                    mouth = mouth + "talk";
                }
            } else {
                if (mouth.equals("water")) mouth = mouth + "talkclosed";
            }
        } else {
            if (hasPoison) {
                mouthExpression = "special";
                mouth = "poison";
            }
        }

        if (bfdiState.bfdi$getHurtTime() > 0) {
            mouthExpression = "special";
            mouth = "hurt";
        }

        Identifier textureId = Identifier.fromNamespaceAndPath(FunnyBFDI.MOD_ID, "textures/mouths/" + mouthExpression + "/" + mouth + ".png");
        RenderType renderType = RenderTypes.entityTranslucent(textureId);

        float offsetScale = 250.0f;

        matrices.pushPose();

        matrices.translate(head.x / 8.0, head.y / 16.0, head.z / 8.0);
        matrices.rotateDegrees(Axis.YP, 180.0F);

        matrices.rotate(Axis.YP, head.yRot);
        matrices.rotate(Axis.XN, head.xRot);
        matrices.rotate(Axis.ZN, head.zRot);

        matrices.translate(Config.mouthOffsetX / -offsetScale, Config.mouthOffsetY / offsetScale, Config.mouthOffsetZ / offsetScale);
        matrices.scale(Config.mouthSize, Config.mouthSize, 1.0f);

        submitNodeCollector.submitModel(this.mouthModel, Unit.INSTANCE, matrices, renderType, light, OverlayTexture.NO_OVERLAY, -1);

        matrices.popPose();
    }
}
