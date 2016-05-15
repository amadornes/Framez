package com.amadornes.trajectory.api;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Base class for Trajectory's events. All events are fired on {@link MinecraftForge#EVENT_BUS}.
 */
public class TrajectoryEvent extends Event {

    private TrajectoryEvent() {

    }

    /**
     * Fired on {@link MinecraftForge#EVENT_BUS} before/after a block/structure starts/finishes moving.
     */
    public static class TrajectoryEventMove extends TrajectoryEvent {

        /**
         * Fired on {@link MinecraftForge#EVENT_BUS} before/after a block starts/finishes moving.
         */
        public static class Block extends TrajectoryEventMove {

            public final IMovingBlock block;
            public final ITrajectory trajectory;

            private Block(IMovingBlock block, ITrajectory trajectory) {

                this.block = block;
                this.trajectory = trajectory;
            }

            /**
             * Fired on {@link MinecraftForge#EVENT_BUS} before/after a block starts moving.
             */
            public static class Start extends Block {

                public Start(IMovingBlock block, ITrajectory trajectory) {

                    super(block, trajectory);
                }

                /**
                 * Fired on {@link MinecraftForge#EVENT_BUS} before a block starts moving.
                 */
                public static class Pre extends TrajectoryEventMove.Block.Finish {

                    public Pre(IMovingBlock block, ITrajectory trajectory) {

                        super(block, trajectory);
                    }
                }

                /**
                 * Fired on {@link MinecraftForge#EVENT_BUS} after a block starts moving.
                 */
                public static class Post extends TrajectoryEventMove.Block.Finish {

                    public Post(IMovingBlock block, ITrajectory trajectory) {

                        super(block, trajectory);
                    }
                }
            }

            /**
             * Fired on {@link MinecraftForge#EVENT_BUS} before/after a block finishes moving.
             */
            public static class Finish extends Block {

                public Finish(IMovingBlock block, ITrajectory trajectory) {

                    super(block, trajectory);
                }

                /**
                 * Fired on {@link MinecraftForge#EVENT_BUS} before a block finishes moving.
                 */
                public static class Pre extends TrajectoryEventMove.Block.Finish {

                    public boolean shouldApplyTransformation = true;

                    public Pre(IMovingBlock block, ITrajectory trajectory) {

                        super(block, trajectory);
                    }
                }

                /**
                 * Fired on {@link MinecraftForge#EVENT_BUS} after a block finishes moving.
                 */
                public static class Post extends TrajectoryEventMove.Block.Finish {

                    public Post(IMovingBlock block, ITrajectory trajectory) {

                        super(block, trajectory);
                    }
                }
            }
        }

        /**
         * Fired on {@link MinecraftForge#EVENT_BUS} before/after a structure starts/finishes moving.
         */
        public static class Structure extends TrajectoryEventMove {

            public final IMovingStructure structure;
            public final ITrajectory trajectory;

            private Structure(IMovingStructure structure, ITrajectory trajectory) {

                this.structure = structure;
                this.trajectory = trajectory;
            }

            /**
             * Fired on {@link MinecraftForge#EVENT_BUS} before/after a structure starts moving.
             */
            public static class Start extends Structure {

                public Start(IMovingStructure structure, ITrajectory trajectory) {

                    super(structure, trajectory);
                }

                /**
                 * Fired on {@link MinecraftForge#EVENT_BUS} before a structure starts moving.
                 */
                public static class Pre extends TrajectoryEventMove.Structure.Finish {

                    public Pre(IMovingStructure structure, ITrajectory trajectory) {

                        super(structure, trajectory);
                    }
                }

                /**
                 * Fired on {@link MinecraftForge#EVENT_BUS} after a structure starts moving.
                 */
                public static class Post extends TrajectoryEventMove.Structure.Finish {

                    public Post(IMovingStructure structure, ITrajectory trajectory) {

                        super(structure, trajectory);
                    }
                }
            }

            /**
             * Fired on {@link MinecraftForge#EVENT_BUS} before/after a structure finishes moving.
             */
            public static class Finish extends Structure {

                public Finish(IMovingStructure structure, ITrajectory trajectory) {

                    super(structure, trajectory);
                }

                /**
                 * Fired on {@link MinecraftForge#EVENT_BUS} before a structure finishes moving.
                 */
                public static class Pre extends TrajectoryEventMove.Structure.Finish {

                    public Pre(IMovingStructure structure, ITrajectory trajectory) {

                        super(structure, trajectory);
                    }
                }

                /**
                 * Fired on {@link MinecraftForge#EVENT_BUS} before block updates start being called.
                 */
                public static class PreUpdate extends TrajectoryEventMove.Structure.Finish {

                    public PreUpdate(IMovingStructure structure, ITrajectory trajectory) {

                        super(structure, trajectory);
                    }
                }

                /**
                 * Fired on {@link MinecraftForge#EVENT_BUS} after a structure finishes moving.
                 */
                public static class Post extends TrajectoryEventMove.Structure.Finish {

                    public Post(IMovingStructure structure, ITrajectory trajectory) {

                        super(structure, trajectory);
                    }
                }
            }
        }
    }

    /**
     * Fired on {@link MinecraftForge#EVENT_BUS} when rendering a block. Cancellable.<br/>
     * Cancel to prevent rendering the original block.
     */
    @SideOnly(Side.CLIENT)
    @Cancelable
    public static class TrajectoryEventRender extends TrajectoryEvent {

        /**
         * Fired on {@link MinecraftForge#EVENT_BUS} when a block is rendered statically. Cancellable.<br/>
         * Cancel to prevent rendering the original block.
         */
        @SideOnly(Side.CLIENT)
        public static class Static extends TrajectoryEventRender {

            public final IMovingBlock block;
            public final ITrajectory trajectory;
            public final int pass;

            public Static(IMovingBlock block, ITrajectory trajectory, int pass) {

                this.block = block;
                this.trajectory = trajectory;
                this.pass = pass;
            }
        }

        /**
         * Fired on {@link MinecraftForge#EVENT_BUS} when a block is rendered dynamically. Cancellable.<br/>
         * Cancel to prevent rendering the original block.
         */
        @SideOnly(Side.CLIENT)
        public static class Dynamic extends TrajectoryEventRender {

            public final IMovingBlock block;
            public final ITrajectory trajectory;
            public final int pass;
            public final float frame;

            public Dynamic(IMovingBlock block, ITrajectory trajectory, int pass, float frame) {

                this.block = block;
                this.trajectory = trajectory;
                this.pass = pass;
                this.frame = frame;
            }
        }
    }
}
