package org.firstinspires.ftc.teamcode.Audibles;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp
public class TeleOp_Mode extends LinearOpMode {
    private DcMotorEx FrontLeftMotor;
    private DcMotorEx BackLeftMotor;
    private DcMotorEx FrontRightMotor;
    private DcMotorEx BackRightMotor;
    private DcMotorEx IntakeMotor;
    private DcMotorEx FrontTransferMotor;
    private DcMotorEx BackTransferMotor;
    private DcMotorEx OuttakeMotor;
    private Servo LeftIntakeServo;
    private Servo RightIntakeServo;
    boolean transfer = false;
    boolean outtake = false;
    boolean ServoPos = false;

    @Override
    public void runOpMode() {
        //Declare Motors
        FrontLeftMotor = hardwareMap.get(DcMotorEx.class, "FrontLeftMotor");
        FrontRightMotor = hardwareMap.get(DcMotorEx.class, "FrontRightMotor");
        BackLeftMotor = hardwareMap.get(DcMotorEx.class, "BackLeftMotor");
        BackRightMotor =hardwareMap.get(DcMotorEx.class, "BackRightMotor");

        IntakeMotor = hardwareMap.get(DcMotorEx.class, "IntakeMotor");
        FrontTransferMotor = hardwareMap.get(DcMotorEx.class, "FrontTransferMotor");
        BackTransferMotor = hardwareMap.get(DcMotorEx.class, "BackTransferMotor");
        OuttakeMotor = hardwareMap.get(DcMotorEx.class, "OuttakeMotor");

        LeftIntakeServo = hardwareMap.get(Servo.class, "LeftIntakeServo");
        RightIntakeServo = hardwareMap.get(Servo.class, "RightIntakeServo");


        //Account for reversing Motors
        FrontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BackRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        FrontTransferMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        OuttakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        OuttakeMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        LeftIntakeServo.setPosition(-0.5);
        RightIntakeServo.setPosition(0.5);

        PIDFCoefficients outtakepidfCoefficients = new PIDFCoefficients(109, 0, 0, 16);
        OuttakeMotor.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, outtakepidfCoefficients);

        OuttakeMotor.setVelocity(0);
        FrontTransferMotor.setPower(0);
        BackTransferMotor.setPower(0);

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
            if (!ServoPos) {
                LeftIntakeServo.setPosition(0.5);
                RightIntakeServo.setPosition(-0.5);
                telemetry.addLine("Servos Powered Up!");
                ServoPos = true;
            }
            else {
                LeftIntakeServo.setPosition(-0.5);
                RightIntakeServo.setPosition(0.5);
                telemetry.addLine("Servos Powered Down!");
                ServoPos = false;
            }
        }
    }
    private void outtakeControls() {
        if (gamepad2.circleWasPressed()) {
            if (!outtake) {
                OuttakeMotor.setVelocity(1600);
                telemetry.addLine("Outtake On");
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
            FrontTransferMotor.setPower(1);
            BackTransferMotor.setPower(1);
        }
        else if (gamepad2.left_bumper) {
            FrontTransferMotor.setPower(-1);
            BackTransferMotor.setPower(-1);
        }
        else  {
            FrontTransferMotor.setPower(0);
            BackTransferMotor.setPower(0);
        }
    }
    private void arcadeDrive() {
        double yTar = -gamepad1.right_stick_y * 0.8; //Keep Negative
        double xTar = gamepad1.right_stick_x * 0.8 * 1.1; //Keep Positive
        double rxTar = gamepad1.left_stick_x * 0.8;

        double y = yTar;
        double x = xTar;
        double rx = rxTar;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx) / denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        FrontLeftMotor.setPower(frontLeftPower);
        FrontRightMotor.setPower(frontRightPower);
        BackLeftMotor.setPower(backLeftPower);
        BackRightMotor.setPower(backRightPower);
    }
}