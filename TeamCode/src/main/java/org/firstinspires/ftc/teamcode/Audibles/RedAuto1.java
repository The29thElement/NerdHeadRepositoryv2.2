package org.firstinspires.ftc.teamcode.Audibles;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class RedAuto1 extends LinearOpMode {
    private DcMotorEx FrontLeftMotor;
    private DcMotorEx FrontRightMotor;
    private DcMotorEx BackLeftMotor;
    private DcMotorEx BackRightMotor;

    @Override
    public void runOpMode() {

        //declare motors
        FrontLeftMotor = hardwareMap.get(DcMotorEx.class, "FrontLeftMotor");
        FrontRightMotor = hardwareMap.get(DcMotorEx.class, "FrontRightMotor");
        BackLeftMotor = hardwareMap.get(DcMotorEx.class, "BackLeftMotor");
        BackRightMotor =hardwareMap.get(DcMotorEx.class, "BackRightMotor");

        FrontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        BackRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        if (opModeIsActive()) {

            sleep(1000);
            FrontLeftMotor.setPower(0.5);
            FrontRightMotor.setPower(0.5);
            BackLeftMotor.setPower(0.5);
            BackRightMotor.setPower(0.5);

            sleep(1000);
            FrontLeftMotor.setPower(0);
            FrontRightMotor.setPower(0);
            BackLeftMotor.setPower(0);
            BackRightMotor.setPower(0);

        }
    }

}
