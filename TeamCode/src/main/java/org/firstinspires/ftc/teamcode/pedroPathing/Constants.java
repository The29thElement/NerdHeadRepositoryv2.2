package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(10)
            .forwardZeroPowerAcceleration(-25)
            .lateralZeroPowerAcceleration(-25)
            .translationalPIDFCoefficients(new PIDFCoefficients(0, 0, 0, 0))
            .headingPIDFCoefficients(new PIDFCoefficients(0,0,0,0))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0, 0, 0,0, 0))
            .centripetalScaling(0); //All are Placeholders
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);
    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .leftFrontMotorName("FrontLeftMotor")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorName("FrontRightMotor")
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorName("BackLeftMotor")
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorName("BackRightMotor")
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .xVelocity(48)//placeholders
            .yVelocity(48);
    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(4*25.4) //placeholder, change to more accurate result(keep *25.4)
            .strafePodX(7*25.4) //placeholder, change to more accurate result
            .distanceUnit(DistanceUnit.MM)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);
    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .pinpointLocalizer(localizerConstants)
                .build();
    }
}