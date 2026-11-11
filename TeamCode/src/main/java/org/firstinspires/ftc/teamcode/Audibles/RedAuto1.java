package org.firstinspires.ftc.teamcode.Audibles;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
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
        Drive_StartPos_ShootPos,
        ShootPreload,
        Drive_ShootPos_FlowerPos,
        IntakeFlower,
        Drive_FlowerPos_ParkPos
    }
    PathState pathState;
    private final Pose startPose = new Pose(57.93,7.7, Math.toRadians(90));
    private final Pose shootPose = new Pose(57.93,33.5, Math.toRadians(90));
    private final Pose flowerPose = new Pose(9.75, 45.5, Math.toRadians(180));
    private final Pose parkPose = new Pose(11.64, 95.61, Math.toRadians(180));
    private PathChain driveStartPosShootPos;
    private PathChain driveShootPosFlowerPos;
    private PathChain driveFlowerPosParkPose;

    @Override
    public void runOpMode() {
        pathState = PathState.Drive_StartPos_ShootPos;
        pathTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);

        buildPaths();
        follower.setPose(startPose);

        waitForStart();

        opModeTimer.resetTimer();
        SetPathState(pathState);

        if (isStopRequested()) return;

        if (opModeIsActive()) {
            follower.update();
            StatePathUpdate();

            telemetry.addData("Path State:", pathState.toString());
            telemetry.addData("X:", follower.getPose().getX());
            telemetry.addData("Y:", follower.getPose().getY());
            telemetry.addData("Heading:", follower.getPose().getHeading());
            telemetry.addData("Path Time:", pathTimer.getElapsedTimeSeconds());
        }
    }
    public void buildPaths() {
        driveStartPosShootPos = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();

        driveShootPosFlowerPos = follower.pathBuilder()
                .addPath(new BezierLine(shootPose, flowerPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), flowerPose.getHeading())
                .build();

        driveFlowerPosParkPose = follower.pathBuilder()
                .addPath(new BezierLine(flowerPose, parkPose))
                .setLinearHeadingInterpolation(flowerPose.getHeading(), parkPose.getHeading())
                .build();
    }
    public void StatePathUpdate() {
        switch(pathState) {
            case Drive_StartPos_ShootPos:
                follower.followPath(driveStartPosShootPos, true);
                SetPathState(PathState.ShootPreload);
                break;

            case ShootPreload:
                if (!follower.isBusy()) {
                    telemetry.addLine("Done State 1");
                    SetPathState(PathState.Drive_ShootPos_FlowerPos);
                }
                break;

            case Drive_ShootPos_FlowerPos:
                if (!follower.isBusy()) {
                    telemetry.addLine("Done State 2");
                    follower.followPath(driveShootPosFlowerPos);
                    SetPathState(PathState.IntakeFlower);
                }
                break;

            case IntakeFlower:
                if (!follower.isBusy()) {
                    telemetry.addLine("Done State 3");
                    SetPathState(PathState.Drive_FlowerPos_ParkPos);
                }
                break;

            case Drive_FlowerPos_ParkPos:
                if (!follower.isBusy()){
                    telemetry.addLine("Done State 4");
                    follower.followPath(driveFlowerPosParkPose);
                }
                break;

            default:
                telemetry.addLine("No State Commanded");
                break;
        }
    }
    public void SetPathState(PathState newState) {
        pathState = newState;
        pathTimer.resetTimer();
    }
}