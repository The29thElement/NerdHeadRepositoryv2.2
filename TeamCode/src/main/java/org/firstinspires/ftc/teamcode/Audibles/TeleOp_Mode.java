package org.firstinspires.ftc.teamcode.Audibles;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
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
        DcMotor LeftMotor = hardwareMap.dcMotor.get("LeftMotor");
        DcMotor RightMotor = hardwareMap.dcMotor.get("RightMotor");
        DcMotor IntakeMotor = hardwareMap.dcMotor.get("IntakeMotor");

        //Account for reversing Motors
        RightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        //Intake Direction
        IntakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

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

            double denominator = Math.max(Math.abs(y) + Math.abs(x), 1);
            double LeftPower = (y + x) / denominator;
            double RightPower = (y - x) / denominator;

            LeftMotor.setPower(LeftPower);
            RightMotor.setPower(RightPower);

            if (gamepad2.circleWasPressed() ) {
                RightMotor.setPower(1.0);
            }
        }
    }
}