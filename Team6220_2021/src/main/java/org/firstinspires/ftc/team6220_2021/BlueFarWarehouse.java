package org.firstinspires.ftc.team6220_2021;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "Blue Far Warehouse", group = "Autonomous")
public class BlueFarWarehouse extends LinearOpMode {

    DcMotor motorBackLeft;
    DcMotor motorBackRight;
    DcMotor motorFrontLeft;
    DcMotor motorFrontRight;

    @Override
    public void runOpMode() throws InterruptedException {
        motorBackLeft = hardwareMap.dcMotor.get("motorBackLeft");
        motorBackRight = hardwareMap.dcMotor.get("motorBackRight");
        motorFrontLeft = hardwareMap.dcMotor.get("motorFrontLeft");
        motorFrontRight = hardwareMap.dcMotor.get("motorFrontRight");
        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

            motorBackLeft.setPower(0.8);
            motorFrontRight.setPower(0.8);

            pauseMillis(200);

            motorBackLeft.setPower(-0.8);
            motorBackRight.setPower(-0.8);
            motorFrontLeft.setPower(-0.8);
            motorFrontRight.setPower(-0.8);

            pauseMillis(2550);

        }
        public void pauseMillis ( double time){
            double startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < time && opModeIsActive()) {
                idle();
            }
        }
    }