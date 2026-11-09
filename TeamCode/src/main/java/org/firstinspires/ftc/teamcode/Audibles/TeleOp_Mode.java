package org.firstinspires.ftc.teamcode.Audibles;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.IMU;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

@TeleOp
public class TeleOp_Mode extends LinearOpMode {
    private DcMotorEx FrontLeftMotor;
    private DcMotorEx BackLeftMotor;
    private DcMotorEx FrontRightMotor;
    private DcMotorEx BackRightMotor;
    private DcMotorEx IntakeMotor;
    private DcMotorEx TransferMotor;
    private DcMotorEx OuttakeMotor;
    private Servo LeftIntakeServo;
    private Servo RightIntakeServo;
    private Servo GateServo;
    private IMU imu;
    boolean transfer = false;
    boolean outtake = false;
    boolean IntakeServoPos = false;
    @Override
    public void runOpMode() throws InterruptedException {
        //Declare everything
        FrontLeftMotor = hardwareMap.get(DcMotorEx.class, "FrontLeftMotor");
        FrontRightMotor = hardwareMap.get(DcMotorEx.class, "FrontRightMotor");
        BackLeftMotor = hardwareMap.get(DcMotorEx.class, "BackLeftMotor");
        BackRightMotor =hardwareMap.get(DcMotorEx.class, "BackRightMotor");

        IntakeMotor = hardwareMap.get(DcMotorEx.class, "IntakeMotor");
        TransferMotor = hardwareMap.get(DcMotorEx.class, "TransferMotor");
        OuttakeMotor = hardwareMap.get(DcMotorEx.class, "OuttakeMotor");

        LeftIntakeServo = hardwareMap.get(Servo.class, "LeftIntakeServo");
        RightIntakeServo = hardwareMap.get(Servo.class, "RightIntakeServo");
        GateServo = hardwareMap.get(Servo.class, "FlapServo");

        imu = hardwareMap.get(IMU.class, "imu");

        //Account for reversing Motors
        FrontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BackRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        TransferMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        OuttakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        OuttakeMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        //IMU orientation
        IMU.Parameters parameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT));

        //Initialized Position
        LeftIntakeServo.setPosition(-0.5);
        RightIntakeServo.setPosition(0.5);
        GateServo.setPosition(0);

        PIDFCoefficients outtakepidfCoefficients = new PIDFCoefficients(109, 0, 0, 16);
        OuttakeMotor.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, outtakepidfCoefficients);

        OuttakeMotor.setVelocity(0);
        TransferMotor.setPower(0);

        imu.initialize(parameters);

        //DO NOT TOUCH
        waitForStart();

        //Stop Button
        if (isStopRequested()) return;
        while (opModeIsActive()) {
            intakeControls();
            transferControls();
            outtakeControls();
            arcadeDrive();
            telemetry.update();
        }
    }
    private void intakeControls() {
        if (gamepad2.crossWasPressed()) {
            if (!transfer) {
                IntakeMotor.setPower(1);
                telemetry.addLine("Intake On");
                transfer = true;
            }
            else {
                IntakeMotor.setPower(0);
                telemetry.addLine("Intake Off");
                transfer = false;
            }
        }
        if (gamepad2.squareWasPressed()) {
            if (!transfer) {
                IntakeMotor.setPower(-1);
                telemetry.addLine("Intake On");
                transfer = true;
            }
            else {
                IntakeMotor.setPower(0);
                telemetry.addLine("Intake Off");
                transfer = false;
            }
        }
        if (gamepad2.triangleWasPressed()) {
            if (!IntakeServoPos) {
                LeftIntakeServo.setPosition(0.5);
                RightIntakeServo.setPosition(-0.5);
                telemetry.addLine("Servos Powered Up!");
                IntakeServoPos = true;
            }
            else {
                LeftIntakeServo.setPosition(-0.5);
                RightIntakeServo.setPosition(0.5);
                telemetry.addLine("Servos Powered Down!");
                IntakeServoPos = false;
            }
        }
    }
    private void outtakeControls() {
        if (gamepad2.circleWasPressed()) {
            if (!outtake) {
                OuttakeMotor.setVelocity(1600);
                telemetry.addLine("Outtake On");
                if(gamepad2.optionsWasPressed()) {
                    GateServo.setPosition(1.0);
                }
                else {
                   GateServo.setPosition(0);
                }
                outtake = true;
            }
            else {
                OuttakeMotor.setVelocity(0);
                telemetry.addLine("Outtake off");
                outtake = false;
            }
        }
    }
    private void transferControls() {
        if (gamepad2.right_bumper) {
            TransferMotor.setPower(1);
        }
        else if (gamepad2.left_bumper) {
            TransferMotor.setPower(-1);
        }
        else  {
            TransferMotor.setPower(0);
        }
    }
    private void arcadeDrive() {
        double y = -gamepad1.left_stick_y; //Keep Negative
        double x = gamepad1.left_stick_x; //Keep Positive
        double rx = gamepad1.right_stick_x;

        if (gamepad1.options){
            imu.resetYaw();
        }

        double BotHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double rotX = x * Math.cos(-BotHeading) - y * Math.sin(-BotHeading);
        double rotY = x * Math.sin(-BotHeading) + y * Math.cos(-BotHeading);

        rotX = rotX * 1.1;  // Counteract imperfect strafing

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        FrontLeftMotor.setPower(frontLeftPower);
        FrontRightMotor.setPower(frontRightPower);
        BackLeftMotor.setPower(backLeftPower);
        BackRightMotor.setPower(backRightPower);
    }
}