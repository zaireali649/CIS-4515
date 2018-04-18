package com.templecis.escaperoute.maze.game.events;

import com.templecis.escaperoute.maze.debug.D;
import com.templecis.escaperoute.maze.game.data.World;
import com.templecis.escaperoute.maze.game.stats.Stats;
import com.templecis.escaperoute.maze.utils.Timer;

public abstract class TimerEvent extends AbstractEvent {
    protected Timer timer;
    private boolean first;

    public TimerEvent(long duration) {
        timer = new Timer(duration);
        this.first = true;
    }

    public void startTimer() {
        this.timer.start();
    }

    public void fire() {
        super.fire();
        this.startTimer();
    }

    /**
     * Code that should be exectued once.
     */
    protected abstract void before(World world);

    /**
     * Code that should be executed when timer is finished
     */
    protected abstract void finished(World world);

    public boolean action(World world) {
        if(first) {
            this.before(world);
            this.first = false;
        }
        else {
            if(this.timer.isFinished()) {
                this.finished(world);
                return true;
            }
        }
        return false;
    }

}
