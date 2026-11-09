package org.firstinspires.ftc.teamcode.Audibles;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp
public class MotorTuner extends LinearOpMode {
    public double hRPM = 1600;
    public double lRPM = 900;
    double cRPM = hRPM;
    double F = 0;
    double P = 0;
    double[] stepSizes = {10.0, 1.0, 0.1, 0.001, 0.0001};
    int stepIdx = 1;
    private DcMotorEx OuttakeMotor;
    @Override
    public void runOpMode() {
        OuttakeMotor = hardwareMap.get(DcMotorEx.class, "OuttakeMotor");
        OuttakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        OuttakeMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(109, 0, 0, 16);
        OuttakeMotor.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        telemetry.addLine("Init complete");

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            if (gamepad1.dpad_up) {
                if (cRPM == hRPM) {
                    cRPM = lRPM;
                }
                else {
                    cRPM = hRPM;
                }
            }
            if (gamepad1.dpad_down) {
                stepIdx = (stepIdx + 1) % stepSizes.length;
            }

            if (gamepad1.square) {
                F += stepSizes[stepIdx];
            }
            if (gamepad1.circle) {
                F -= stepSizes[stepIdx];
            }

            if (gamepad1.triangle) {
                P += stepSizes[stepIdx];
            }
            if (gamepad1.cross) {
                P -= stepSizes[stepIdx];
            }

            //set new PIDF coeffecients

            pidfCoefficients.p = P;
            pidfCoefficients.f = F;

            OuttakeMotor.setPIDFCoefficients(DcMotorEx.RunMode.RUN_USING_ENCODER, pidfCoefficients);
            OuttakeMotor.setVelocity(cRPM);

            double cVelo = OuttakeMotor.getVelocity();
            double error = cRPM - cVelo;

            telemetry.addData("Target Velo", cRPM);
            telemetry.addData("Current Velo", "%.2f", cVelo);
            telemetry.addData("Error", "%.2f", error);
            telemetry.addLine("______________________________");
            telemetry.addData("Tuning P", "%.4f (D-Pad U/D)", P);
            telemetry.addData("Tuning F", "%.4f (D-Pad R/L)", F);
            telemetry.addData("Step Size", "%.4f (B Button)", stepSizes[stepIdx]);
            telemetry.update();
        }
    }
}