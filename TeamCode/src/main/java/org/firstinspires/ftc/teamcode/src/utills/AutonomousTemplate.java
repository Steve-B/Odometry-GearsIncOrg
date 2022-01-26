package org.firstinspires.ftc.teamcode.src.utills;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.src.robotAttachments.driveTrains.NavigationalDrivetrain;
import org.firstinspires.ftc.teamcode.src.robotAttachments.navigation.odometry.OdometryGlobalCoordinatePosition;
import org.firstinspires.ftc.teamcode.src.robotAttachments.sensors.IMU;
import org.firstinspires.ftc.teamcode.src.robotAttachments.subsystems.LinearSlide;

/**
 * A template for Autonomous OpModes, allows for easy initialization
 */
@Disabled
public abstract class AutonomousTemplate extends GenericOpModeTemplate {


    /**
     * Provides methods to use Odometry and associated drive methods
     */
    protected NavigationalDrivetrain driveSystem;


    /**
     * Provides Odometry Methods, Also held by NavigationalDrivetrain driveSystem
     */
    protected OdometryGlobalCoordinatePosition odometry;


    /**
     * Initializes all fields provided by this class
     *
     * @throws InterruptedException Throws if the OpMode is stopped during function execution
     */
    protected void initAll() throws InterruptedException {
        initOdometryServos();

        initDriveSystem();

        initSpinner();

        initSlide();

        initIntake();

        initLEDS();

        telemetry.addData("Default Initialization: ", "Finished");
        telemetry.update();
        checkStop();

    }

    /**
     * Initializes the Drive System
     *
     * @throws InterruptedException Throws if stop is requested during start
     */
    protected void initDriveSystem() throws InterruptedException {
        DcMotor front_right = hardwareMap.dcMotor.get(frontRightName);
        DcMotor front_left = hardwareMap.dcMotor.get(frontLeftName);
        DcMotor back_left = hardwareMap.dcMotor.get(backLeftName);
        DcMotor back_right = hardwareMap.dcMotor.get(backRightName);
        checkStop();

        front_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        front_right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        front_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        front_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        back_right.setDirection(DcMotorSimple.Direction.REVERSE);
        front_right.setDirection(DcMotorSimple.Direction.REVERSE);

        // DcMotor verticalEncoderLeft, DcMotor verticalEncoderRight, DcMotor horizontalEncoder
        //verticalEncoderLeft, verticalEncoderRight, horizontalEncoder
        odometry = new OdometryGlobalCoordinatePosition(front_left, front_right, back_right, 25, this::opModeIsActive, this::isStopRequested);
        checkStop();
        IMU imu = new IMU(hardwareMap, IMUName);
        checkStop();
        odometry.setImu(imu.getImu());


        odometry.reverseRightEncoder();

        odometry.start();

        if (voltageSensor == null) {
            this.initVoltageSensor();
        }

        driveSystem = new NavigationalDrivetrain(front_right, front_left, back_right, back_left, telemetry, odometry, this::isStopRequested, this::opModeIsActive, voltageSensor);
    }


    /**
     * Initializes the Linear Slide
     *
     * @throws InterruptedException Throws if the OpMode is stopped during execution
     */
    protected void initSlide() throws InterruptedException {
        super.initLinearSlide();
        slide.setTargetLevel(LinearSlide.HeightLevel.Down);
    }

    protected void initDriveSystemWithWheelDrift() throws InterruptedException {
        DcMotor front_right = hardwareMap.dcMotor.get(frontRightName);
        DcMotor front_left = hardwareMap.dcMotor.get(frontLeftName);
        DcMotor back_left = hardwareMap.dcMotor.get(backLeftName);
        DcMotor back_right = hardwareMap.dcMotor.get(backRightName);
        checkStop();

        front_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        front_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        back_right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        front_right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        front_left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        back_left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        front_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        front_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        back_right.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        back_left.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);


        back_right.setDirection(DcMotorSimple.Direction.REVERSE);
        front_right.setDirection(DcMotorSimple.Direction.REVERSE);

        // DcMotor verticalEncoderLeft, DcMotor verticalEncoderRight, DcMotor horizontalEncoder
        //verticalEncoderLeft, verticalEncoderRight, horizontalEncoder
        odometry = new OdometryGlobalCoordinatePosition(front_left, front_right, back_right, 25, this::opModeIsActive, this::isStopRequested);
        checkStop();
        IMU imu = new IMU(hardwareMap, IMUName);
        checkStop();
        odometry.setImu(imu.getImu());


        odometry.reverseRightEncoder();

        odometry.start();

        if (voltageSensor == null) {
            this.initVoltageSensor();
        }

        driveSystem = new NavigationalDrivetrain(front_right, front_left, back_right, back_left, telemetry, odometry, this::isStopRequested, this::opModeIsActive, voltageSensor);
    }

    /**
     * Initializes the Odometry Servos
     */
    @Override
    protected void initOdometryServos() {
        super.initOdometryServos();
        podServos.lower();
    }
}
