package de.solarpancake.tweaks.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;
import java.util.function.Function;

public class CatTreePipeBlock extends Block implements CatTreePart {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final EnumProperty<Elbow> ELBOW = EnumProperty.create("elbow", Elbow.class);
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;

    private static final Map<Direction.Axis, VoxelShape> CORE_SHAPES = Map.of(
            Direction.Axis.Y, Block.box(4, 0, 4, 12, 16, 12),
            Direction.Axis.X, Block.box(0, 4, 4, 16, 12, 12),
            Direction.Axis.Z, Block.box(4, 4, 0, 12, 12, 16));
    private static final VoxelShape CENTER_SHAPE = Block.box(4, 4, 4, 12, 12, 12);
    private static final Map<Direction, VoxelShape> ARM_SHAPES = Shapes.rotateAll(Block.box(4, 4, 0, 12, 12, 4));

    private final Function<BlockState, VoxelShape> shapes;

    public CatTreePipeBlock(Properties properties) {
        super(properties);
        BlockState state = this.stateDefinition.any()
                .setValue(AXIS, Direction.Axis.Y)
                .setValue(ELBOW, Elbow.NONE);
        for (BooleanProperty property : PROPERTY_BY_DIRECTION.values()) {
            state = state.setValue(property, false);
        }
        this.registerDefaultState(state);
        this.shapes = this.getShapeForEachState(CatTreePipeBlock::calculateShape);
    }

    /** The pipe end that is kept when bent into an elbow, or null for a straight pipe. */
    public static Direction keptEnd(BlockState state) {
        Elbow elbow = state.getValue(ELBOW);
        return elbow == Elbow.NONE ? null : Direction.fromAxisAndDirection(state.getValue(AXIS), elbow.axisDirection);
    }

    private static VoxelShape calculateShape(BlockState state) {
        Direction keptEnd = keptEnd(state);
        VoxelShape shape = keptEnd == null
                ? CORE_SHAPES.get(state.getValue(AXIS))
                : Shapes.or(CENTER_SHAPE, ARM_SHAPES.get(keptEnd));
        for (Direction direction : Direction.values()) {
            if (state.getValue(PROPERTY_BY_DIRECTION.get(direction))) {
                shape = Shapes.or(shape, ARM_SHAPES.get(direction));
            }
        }
        return shape;
    }

    @Override
    public boolean hasOpening(BlockState state, Direction side) {
        Direction keptEnd = keptEnd(state);
        return keptEnd == null ? side.getAxis() == state.getValue(AXIS) : side == keptEnd;
    }

    private static boolean shouldConnect(Direction.Axis axis, Direction direction, BlockState neighbour) {
        return direction.getAxis() != axis
                && neighbour.getBlock() instanceof CatTreePart part
                && part.hasOpening(neighbour, direction.getOpposite());
    }

    /** A pipe end counts as resting on something if it touches another cat tree piece or a solid face. */
    private static boolean isEndSupported(LevelReader level, BlockPos pos, Direction end) {
        BlockPos neighbourPos = pos.relative(end);
        BlockState neighbour = level.getBlockState(neighbourPos);
        return neighbour.getBlock() instanceof CatTreePart || neighbour.isFaceSturdy(level, neighbourPos, end.getOpposite());
    }

    private static BlockState withConnections(BlockState state, LevelReader level, BlockPos pos) {
        Direction.Axis axis = state.getValue(AXIS);
        int sideConnections = 0;
        for (Direction direction : Direction.values()) {
            boolean connected = shouldConnect(axis, direction, level.getBlockState(pos.relative(direction)));
            state = state.setValue(PROPERTY_BY_DIRECTION.get(direction), connected);
            if (connected) sideConnections++;
        }

        Elbow elbow = Elbow.NONE;
        if (sideConnections == 1) {
            boolean positiveSupported = isEndSupported(level, pos, Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE));
            boolean negativeSupported = isEndSupported(level, pos, Direction.fromAxisAndDirection(axis, Direction.AxisDirection.NEGATIVE));
            if (positiveSupported != negativeSupported) {
                elbow = positiveSupported ? Elbow.POSITIVE : Elbow.NEGATIVE;
            }
        }
        return state.setValue(ELBOW, elbow);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
        return withConnections(state, context.getLevel(), context.getClickedPos());
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState,
                                     RandomSource random) {
        return withConnections(state, level, pos);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shapes.apply(state);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        BlockState rotated = RotatedPillarBlock.rotatePillar(state, rotation);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            rotated = rotated.setValue(PROPERTY_BY_DIRECTION.get(rotation.rotate(direction)),
                    state.getValue(PROPERTY_BY_DIRECTION.get(direction)));
        }
        Direction keptEnd = keptEnd(state);
        return keptEnd == null ? rotated : rotated.setValue(ELBOW, Elbow.of(rotation.rotate(keptEnd)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        BlockState mirrored = state;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            mirrored = mirrored.setValue(PROPERTY_BY_DIRECTION.get(mirror.mirror(direction)),
                    state.getValue(PROPERTY_BY_DIRECTION.get(direction)));
        }
        Direction keptEnd = keptEnd(state);
        return keptEnd == null ? mirrored : mirrored.setValue(ELBOW, Elbow.of(mirror.mirror(keptEnd)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, ELBOW);
        PROPERTY_BY_DIRECTION.values().forEach(builder::add);
    }

    /** Which end of the pipe's axis is kept when it bends into an elbow. */
    public enum Elbow implements StringRepresentable {
        NONE("none", null),
        POSITIVE("positive", Direction.AxisDirection.POSITIVE),
        NEGATIVE("negative", Direction.AxisDirection.NEGATIVE);

        private final String name;
        private final Direction.AxisDirection axisDirection;

        Elbow(String name, Direction.AxisDirection axisDirection) {
            this.name = name;
            this.axisDirection = axisDirection;
        }

        public static Elbow of(Direction keptEnd) {
            return keptEnd.getAxisDirection() == Direction.AxisDirection.POSITIVE ? POSITIVE : NEGATIVE;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
