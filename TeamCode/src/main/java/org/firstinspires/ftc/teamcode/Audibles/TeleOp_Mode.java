package org.firstinspires.ftc.teamcode.Audibles;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

//TeleOp Annotation
@TeleOp
public class TeleOp_Mode extends LinearOpMode {
    private DcMotorEx FrontLeftMotor;
    private DcMotorEx BackLeftMotor;
    private DcMotorEx FrontRightMotor;
    private DcMotorEx BackRightMotor;
    private DcMotorEx IntakeMotor;

    boolean motorsPowered = false;

    @Override
    public void runOpMode() {
        //Declare Motors
        FrontLeftMotor = hardwareMap.get(DcMotorEx.class, "FrontLeftMotor");
        FrontRightMotor = hardwareMap.get(DcMotorEx.class, "FrontRightMotor");
        BackLeftMotor = hardwareMap.get(DcMotorEx.class, "BackLeftMotor");
        BackRightMotor =hardwareMap.get(DcMotorEx.class, "BackRightMotor");

        IntakeMotor = hardwareMap.get(DcMotorEx.class, "IntakeMotor");


        //Account for reversing Motors
        FrontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BackRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        //DO NOT TOUCH
        waitForStart();

        //Stop Button
        if (isStopRequested()) return;
        while (opModeIsActive()) {
            intakeControls();
            arcadeDrive();

            telemetry.update();
        }
    }

    private void intakeControls() {
        if (gamepad2.right_bumper && !motorsPowered) {
            IntakeMotor.setPower(1);
            telemetry.addLine("Intake Going Forward");
            motorsPowered = true;
        }
        else if (gamepad2.left_bumper && !motorsPowered) {
            IntakeMotor.setPower(-1);
            telemetry.addLine("Intake Going Backward");
            motorsPowered = true;
        }
        else if ((!gamepad2.left_bumper && !gamepad2.right_bumper) || (gamepad2.left_bumper && gamepad2.right_bumper)){
            IntakeMotor.setPower(0);
            motorsPowered = false;
        }
    }

    private void arcadeDrive() {
        double y = -gamepad1.left_stick_y; //Keep Negative
        double x = gamepad1.left_stick_x * 1.1; //Keep Positive
        double rx = gamepad1.right_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x + rx) / denominator;
        double backLeftPower = (y - x + rx)/denominator;
        double frontRightPower = (y - x - rx) / denominator;
        double backRightPower = (y + x - rx) / denominator;

        FrontLeftMotor.setPower(frontLeftPower);
        FrontRightMotor.setPower(frontRightPower);

        BackLeftMotor.setPower(backLeftPower);
        BackRightMotor.setPower(backRightPower);
    }
}