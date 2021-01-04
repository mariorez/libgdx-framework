package org.seariver.actor;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.seariver.BaseActor;

/**
 * Enables drag and drop functionality for actors.
 */
public class DragAndDropActor extends BaseActor {

    private DragAndDropActor self;
    private DropTargetActor dropTarget;

    private float grabOffsetX;
    private float grabOffsetY;

    private float startPositionX;
    private float startPositionY;

    private boolean draggable;

    public DragAndDropActor(float x, float y, Stage stage) {
        super(x, y, stage);
        self = this;
        draggable = true;

        addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float eventOffsetX, float eventOffsetY, int pointer, int button) {
                if (!self.isDraggable()) return false;

                self.grabOffsetX = eventOffsetX;
                self.grabOffsetY = eventOffsetY;

                // store original position in case actor needs to return to it later
                self.startPositionX = self.getX();
                self.startPositionY = self.getY();

                self.toFront();

                // increase size; object appears larger when lifted by player
                self.addAction(Actions.scaleTo(1.1f, 1.1f, 0.25f));

                self.onDragStart();

                return true; // returning true indicates other touch methods are called
            }

            public void touchDragged(InputEvent event, float eventOffsetX, float eventOffsetY, int pointer) {
                float deltaX = eventOffsetX - self.grabOffsetX;
                float deltaY = eventOffsetY - self.grabOffsetY;
                self.moveBy(deltaX, deltaY);
            }

            public void touchUp(InputEvent event, float eventOffsetX, float eventOffsetY, int pointer, int button) {
                self.setDropTarget(null);

                // keep track of distance to closest object
                float closestDistance = Float.MAX_VALUE;

                for (BaseActor actor : BaseActor.getList(self.getStage(), "org.seariver.actor.DropTargetActor")) {
                    DropTargetActor target = (DropTargetActor) actor;

                    if (target.isTargetable() && self.overlaps(target)) {
                        float currentDistance = Vector2.dst(self.getX(), self.getY(), target.getX(), target.getY());

                        // check if this target is even closer
                        if (currentDistance < closestDistance) {
                            self.setDropTarget(target);
                            closestDistance = currentDistance;
                        }
                    }
                }

                // return object to original size when dropped by player
                self.addAction(Actions.scaleTo(1.00f, 1.00f, 0.25f));

                self.onDrop();
            }
        });
    }

    /**
     * Set whether this actor can be dragged.
     */
    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    /**
     * Check if this actor can be dragged.
     */
    public boolean isDraggable() {
        return draggable;
    }

    /**
     * Check if a drop target currently exists.
     */
    public boolean hasDropTarget() {
        return (dropTarget != null);
    }

    /**
     * Automatically set when actor is dropped on dropTargetActor target.
     */
    public void setDropTarget(DropTargetActor dropTargetActor) {
        dropTarget = dropTargetActor;
    }

    /**
     * If this actor is dropped on a "targetable" actor, that actor can be obtained from this method.
     */
    public DropTargetActor getDropTarget() {
        return dropTarget;
    }

    /**
     * Called when drag begins; extending classes may override this method.
     */
    public void onDragStart() {
    }

    /**
     * Called when drop occurs; extending classes may override this method.
     */
    public void onDrop() {
    }

    /**
     * Slide this actor to the center of another actor.
     */
    public void moveToActor(BaseActor other) {
        float x = other.getX() + (other.getWidth() - this.getWidth()) / 2;
        float y = other.getY() + (other.getHeight() - this.getHeight()) / 2;
        addAction(Actions.moveTo(x, y, 0.50f, Interpolation.pow3));
    }

    /**
     * Slide this actor back to its original position before it was dragged.
     */
    public void moveToStart() {
        addAction(Actions.moveTo(startPositionX, startPositionY, 0.50f, Interpolation.pow3));
    }

    public void act(float deltaTime) {
        super.act(deltaTime);
    }
}
