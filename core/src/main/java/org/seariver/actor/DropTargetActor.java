package org.seariver.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import org.seariver.BaseActor;

public class DropTargetActor extends BaseActor {

    private boolean targetable;

    public DropTargetActor(float x, float y, Stage stage) {
        super(x, y, stage);
        targetable = true;
    }

    /**
     * Set whether this actor can be targeted by a DragAndDrop actor.
     *
     * @param targetable can this actor be targeted
     */
    public void setTargetable(boolean targetable) {
        this.targetable = targetable;
    }

    /**
     * Check if this actor can be targeted by a DragAndDrop actor.
     *
     * @return can this actor be targeted
     */
    public boolean isTargetable() {
        return targetable;
    }
}
