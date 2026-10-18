package de.solarpancake.tweaks.datagen;

import de.solarpancake.tweaks.Solarpancakestweaks;
import de.solarpancake.tweaks.block.ModBlocks;
import de.solarpancake.tweaks.block.custom.CatTreePipeBlock;
import de.solarpancake.tweaks.block.custom.DillCropBlock;
import de.solarpancake.tweaks.block.custom.IcedCrystalLampBlock;
import de.solarpancake.tweaks.block.custom.RaspberryBushBlock;
import de.solarpancake.tweaks.item.ModArmorMaterials;
import de.solarpancake.tweaks.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, Solarpancakestweaks.MODID);
    }


    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        /*  FlatItem  */
        itemModels.generateFlatItem(ModItems.ICED_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.CRUSHED_ICED_CRYSTAL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.GREEN_APPLE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.GREEN_APPLE_SLICED.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.GREEN_APPLE_CAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.RASPBERRY_CAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.ORIGINAL_CAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BLACK_CAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.EMPTY_CAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.LEMON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.DILL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.ICE_DETECTOR.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.ICED_CRYSTAL_ROD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.ICED_CRYSTAL_HORSE_ARMOR.get(), ModelTemplates.FLAT_ITEM);

        /*  FlatHandheldItem  */
        itemModels.generateFlatItem(ModItems.ICED_CRYSTAL_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ICED_CRYSTAL_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ICED_CRYSTAL_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ICED_CRYSTAL_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ICED_CRYSTAL_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.SHUFFLE_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);


        /*  TrimmableItem  */
        itemModels.generateTrimmableItem(ModItems.ICED_CRYSTAL_HELMET.get(), ModArmorMaterials.ICED_CRYSTAL_KEY, ItemModelGenerators.TRIM_PREFIX_HELMET, false);
        itemModels.generateTrimmableItem(ModItems.ICED_CRYSTAL_CHESTPLATE.get(), ModArmorMaterials.ICED_CRYSTAL_KEY, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, false);
        itemModels.generateTrimmableItem(ModItems.ICED_CRYSTAL_LEGGINGS.get(), ModArmorMaterials.ICED_CRYSTAL_KEY, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, false);
        itemModels.generateTrimmableItem(ModItems.ICED_CRYSTAL_BOOTS.get(), ModArmorMaterials.ICED_CRYSTAL_KEY, ItemModelGenerators.TRIM_PREFIX_BOOTS, false);


        /*  SpearItem  */
        itemModels.generateSpear(ModItems.ICED_CRYSTAL_SPEAR.get());

        /*  BowItem  */
        itemModels.createFlatItemModel(ModItems.ICED_CRYSTAL_BOW.get(), ModelTemplates.BOW);
        itemModels.generateBow(ModItems.ICED_CRYSTAL_BOW.get());


        /*  TrivialCube  */
        blockModels.createTrivialCube(ModBlocks.ICED_CRYSTAL_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.FLOWERING_LEMON_TREE_LEAVES.get());
        blockModels.createTrivialCube(ModBlocks.BUDDING_ICED_CRYSTAL.get());
        blockModels.createTrivialCube(ModBlocks.STEEL_GRATE.get());

        /*  AmethystClusterBlock  */
        blockModels.createAmethystCluster(ModBlocks.ICED_CRYSTAL_CLUSTER.get());
        blockModels.createAmethystCluster(ModBlocks.SMALL_ICED_CRYSTAL_BUD.get());
        blockModels.createAmethystCluster(ModBlocks.MEDIUM_ICED_CRYSTAL_BUD.get());
        blockModels.createAmethystCluster(ModBlocks.LARGE_ICED_CRYSTAL_BUD.get());


        /*  BlockFamily  */
        blockModels.family(ModBlocks.ICED_CRYSTAL_BRICKS.get())
                .stairs(ModBlocks.ICED_CRYSTAL_BRICKS_STAIRS.get())
                .slab(ModBlocks.ICED_CRYSTAL_BRICKS_SLAB.get());

        /*  PillarBlocks  */
        blockModels.createRotatedPillarWithHorizontalVariant(ModBlocks.ICED_CRYSTAL_PILLAR.get(),
                TexturedModel.COLUMN,
                TexturedModel.COLUMN_HORIZONTAL
        );

        /*  LampBlocks  */
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(ModBlocks.ICED_CRYSTAL_LAMP.get()).with(BlockModelGenerators.createBooleanModelDispatch(IcedCrystalLampBlock.CLICKED,
                        BlockModelGenerators.plainVariant(blockModels.createSuffixedVariant(ModBlocks.ICED_CRYSTAL_LAMP.get(), "_on", ModelTemplates.CUBE_ALL, TextureMapping::cube)),
                        BlockModelGenerators.plainVariant(TexturedModel.CUBE.create(ModBlocks.ICED_CRYSTAL_LAMP.get(), blockModels.modelOutput)))));

        /*  CropBlocks  */
        blockModels.createCropBlock(ModBlocks.RASPBERRY_BUSH.get(), RaspberryBushBlock.AGE, 0, 1, 2, 3);
        blockModels.createCropBlock(ModBlocks.DILL_CROP.get(), DillCropBlock.AGE, 0, 1, 2, 3);

        /*  CustomBlocks  */
        blockModels.createNonTemplateModelBlock(ModBlocks.BLENDER_BLOCK.get());

        /*  Cat tree  */
        createCatTreePost(blockModels, ModBlocks.CAT_TREE_1.get());
        createCatTreePost(blockModels, ModBlocks.CAT_TREE_3.get());
        createCatTreePost(blockModels, ModBlocks.CAT_TREE_4.get());
        createCatTreePipe(blockModels, ModBlocks.CAT_TREE_2.get());
    }

    /** Models face north; rotated so the front faces the player who placed it. */
    private static void createCatTreePost(BlockModelGenerators blockModels, Block post) {
        MultiVariant model = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(post));
        blockModels.blockStateOutput.accept(MultiVariantGenerator.dispatch(post, model)
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
    }

    /**
     * Straight pipe: the vertical core model rotated like a log, plus side arms. Side arms come in two
     * variants because the pipe surface is stepped (octagon-ish): "_side" fits when the steps run along
     * the arm's local X, "_side_alt" when they run along local Y.
     * Elbow pipe: one of the corner models, oriented to connect the kept end with the side connection.
     */
    private static void createCatTreePipe(BlockModelGenerators blockModels, Block pipe) {
        MultiVariant core = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(pipe));
        MultiVariant side = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(pipe, "_side"));
        MultiVariant sideAlt = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(pipe, "_side_alt"));
        MultiVariant corner = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(pipe, "_corner"));
        MultiVariant cornerFlat = BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(pipe, "_corner_flat"));

        MultiPartGenerator generator = MultiPartGenerator.multiPart(pipe)
                .with(straightPipe(Direction.Axis.Y), core)
                .with(straightPipe(Direction.Axis.Z), core.with(BlockModelGenerators.X_ROT_90))
                .with(straightPipe(Direction.Axis.X), core.with(BlockModelGenerators.X_ROT_90).with(BlockModelGenerators.Y_ROT_90))
                .with(pipeArm(Direction.NORTH, Direction.Axis.Y), side)
                .with(pipeArm(Direction.NORTH, Direction.Axis.X), sideAlt)
                .with(pipeArm(Direction.SOUTH, Direction.Axis.Y), side.with(BlockModelGenerators.Y_ROT_180))
                .with(pipeArm(Direction.SOUTH, Direction.Axis.X), sideAlt.with(BlockModelGenerators.Y_ROT_180))
                .with(pipeArm(Direction.EAST, Direction.Axis.Y), side.with(BlockModelGenerators.Y_ROT_90))
                .with(pipeArm(Direction.EAST, Direction.Axis.Z), sideAlt.with(BlockModelGenerators.Y_ROT_90))
                .with(pipeArm(Direction.WEST, Direction.Axis.Y), side.with(BlockModelGenerators.Y_ROT_270))
                .with(pipeArm(Direction.WEST, Direction.Axis.Z), sideAlt.with(BlockModelGenerators.Y_ROT_270))
                .with(pipeArm(Direction.UP, Direction.Axis.Z), side.with(BlockModelGenerators.X_ROT_270))
                .with(pipeArm(Direction.UP, Direction.Axis.X), sideAlt.with(BlockModelGenerators.X_ROT_270))
                .with(pipeArm(Direction.DOWN, Direction.Axis.Z), side.with(BlockModelGenerators.X_ROT_90))
                .with(pipeArm(Direction.DOWN, Direction.Axis.X), sideAlt.with(BlockModelGenerators.X_ROT_90));

        for (Direction.Axis axis : Direction.Axis.values()) {
            for (CatTreePipeBlock.Elbow elbow : new CatTreePipeBlock.Elbow[]{CatTreePipeBlock.Elbow.POSITIVE, CatTreePipeBlock.Elbow.NEGATIVE}) {
                Direction keptEnd = Direction.fromAxisAndDirection(axis, elbow == CatTreePipeBlock.Elbow.POSITIVE
                        ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE);
                for (Direction sideDirection : Direction.values()) {
                    if (sideDirection.getAxis() == axis) continue;
                    generator.with(BlockModelGenerators.condition()
                                    .term(CatTreePipeBlock.AXIS, axis)
                                    .term(CatTreePipeBlock.ELBOW, elbow)
                                    .term(CatTreePipeBlock.PROPERTY_BY_DIRECTION.get(sideDirection), true),
                            elbowVariant(corner, cornerFlat, keptEnd, sideDirection));
                }
            }
        }
        blockModels.blockStateOutput.accept(generator);
    }

    private static ConditionBuilder straightPipe(Direction.Axis axis) {
        return BlockModelGenerators.condition()
                .term(CatTreePipeBlock.AXIS, axis)
                .term(CatTreePipeBlock.ELBOW, CatTreePipeBlock.Elbow.NONE);
    }

    private static ConditionBuilder pipeArm(Direction direction, Direction.Axis pipeAxis) {
        return straightPipe(pipeAxis)
                .term(CatTreePipeBlock.PROPERTY_BY_DIRECTION.get(direction), true);
    }

    /** "_corner" connects UP + SOUTH (x=180 flips it to DOWN + NORTH), "_corner_flat" connects SOUTH + EAST. */
    private static MultiVariant elbowVariant(MultiVariant corner, MultiVariant cornerFlat, Direction a, Direction b) {
        if (a.getAxis() == Direction.Axis.Y || b.getAxis() == Direction.Axis.Y) {
            Direction vertical = a.getAxis() == Direction.Axis.Y ? a : b;
            Direction horizontal = vertical == a ? b : a;
            return vertical == Direction.UP
                    ? corner.with(rotationFromSouth(horizontal))
                    : corner.with(BlockModelGenerators.X_ROT_180).with(rotationFromSouth(horizontal.getOpposite()));
        }
        Direction facing = b == a.getCounterClockWise() ? a : b;
        return cornerFlat.with(rotationFromSouth(facing));
    }

    private static VariantMutator rotationFromSouth(Direction direction) {
        return switch (direction) {
            case WEST -> BlockModelGenerators.Y_ROT_90;
            case NORTH -> BlockModelGenerators.Y_ROT_180;
            case EAST -> BlockModelGenerators.Y_ROT_270;
            default -> BlockModelGenerators.NOP;
        };
    }
}
