package org.firstinspires.ftc.teamcode.Audibles;

import com.pedropathing.follower.Follower;
import com.pedropathing.util.Timer;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous
public class RedAuto1 extends LinearOpMode {

    private Follower follower;
    private Timer pathTimer;
    private Timer opModeTimer;
    public enum PathState {
        /*Start Position -> End Position
        Drive -> Movement State
        Shoot -> Attempt to Score
        Flower -> Attempt to remove pollen*/
    }

    @Override
    public void runOpMode() {

        waitForStart();

        if (isStopRequested()) return;

        if (opModeIsActive()) {

        }
    }

}
