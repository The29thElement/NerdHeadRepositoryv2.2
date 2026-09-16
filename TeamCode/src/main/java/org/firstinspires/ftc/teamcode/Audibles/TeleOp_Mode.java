package org.firstinspires.ftc.teamcode.Audibles;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

//TeleOp Annotation
@TeleOp
public class TeleOp_Mode extends LinearOpMode {

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
            double y = -gamepad1.left_stick_y; //Keep Negative
            double x = gamepad1.left_stick_x; //Keep Positive

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