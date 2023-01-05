package org.firstinspires.ftc.teamcode.robots.catbot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.robots.taubot.vision.pipeline.AprilTagDetectionPipeline;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;


import java.util.ArrayList;

@Config("IronGiantGameVariables")
@TeleOp(name="Iron Giant OpMode", group="Challenge")
public class IronGiantOpMode extends OpMode {
    //autonomous variables
    public static boolean auton = true; // controls if auton will run set to true to run with auton
    private boolean autonInitialized = false;
    public static  boolean testing = false;// turns off normal robot motion
    public static boolean red = true; // team boolean variable red true is red team
    //miscellaneous variables
    public static boolean calibrateOn = true;// turns off automatic elevator calibration
    private boolean calibrate = false;
    public static float DEADZONE = .1f;
    //vision variables
    OpenCvCamera camera;
    AprilTagDetectionPipeline aprilTagDetectionPipeline;
    static final double FEET_PER_METER = 3.28084;
    int tagDetected = 0;
    // UNITS ARE PIXELS
    double fx = 578.272;
    double fy = 578.272;
    double cx = 402.145;
    double cy = 221.506;
    // UNITS ARE METERS
    double tagsize = 0.045; //tag size on iron reign signal sleeve
    int ID_TAG_OF_INTEREST = 1; // Tag ID 1 from the 36h11 family
    int tagCount = 0;
    boolean tagFound = false;
    AprilTagDetection tagOfInterest = null;
    //Robot variable storage system
    Robot robot;
    //autonomous program
    Autonomous autonomous;
    @Override
    public void init() {
        robot = new Robot(telemetry, hardwareMap);
        autonomous = new Autonomous(robot);
        telemetry.addData("Status", "Initializing " + this.getClass() + "...");
//        telemetry.addData("Status", "Hold right_trigger to enable debug mode");
        telemetry.update();
        robot.motorInit();
        visionInit();
    }

    @Override
    public void init_loop() {
        if (!calibrate && calibrateOn)
            calibrate = robot.elevatorNClaw.calib();
        else
            aprilTagInitLoop();
        telemetry.update();

    }

    @Override
    public void loop() {
        telemetryOutput();
        if (!autonInitialized && !testing && auton) {
            autonDrive();
        } else if(!testing){
            telemetry.addData("should auton be running? \t", autonomous.hasBehaviors());
            if(autonomous.hasBehaviors()) {
                autonomous.runBehaviors();
            }
            else {
                robot.driveTrain.mechanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
//                robot.driveTrain.tankDrive(gamepad1.left_stick_y, gamepad1.right_stick_y);
                robot.elevatorNClaw.clawMove(gamepad1.left_bumper ? 1 : gamepad1.right_bumper ? -1 : 0);
                if (!calibrate && calibrateOn)
                    calibrate = robot.elevatorNClaw.calib();
                else
                    robot.elevatorNClaw.elevatorMove(gamepad1.left_trigger > DEADZONE ? -gamepad1.left_trigger : gamepad1.right_trigger > DEADZONE ? gamepad1.right_trigger : 0);
                if (gamepad1.dpad_down) {
                    calibrate = false;
                }
                if (gamepad1.y)
                    robot.elevatorNClaw.elevatorMove('y');
                if (gamepad1.b)
                    robot.elevatorNClaw.elevatorMove('b');
                if (gamepad1.a)
                    robot.elevatorNClaw.elevatorMove('a');
                if (gamepad1.x)
                    robot.elevatorNClaw.elevatorMove('x');
                if(gamepad1.dpad_up)
                {
                    autonomous.add(new Turn(robot, 180));
                }
                if(gamepad1.dpad_left)
                {
                    autonomous.add(new Turn(robot, 90));
                }
                if(gamepad1.dpad_right)
                {
                    autonomous.add(new Turn(robot, -90));
                }
            }
        }
    }
    public void telemetryOutput() {
        telemetry.addData("is in auton \t", auton);
        robot.elevatorNClaw.telemetryOutput();
        robot.driveTrain.telemetryOutput();
    }
    // auton
    public void autonDrive() {
        autonVisionTelemetry();
        if (!autonInitialized)
            autonInit(tagDetected, red);
        autonInitialized = true;
    }
    public void autonInit(int tagValue, boolean isRed)
    {
        if(isRed)
        {
            autonomous.add(new Drive(robot, 1.5));
            autonomous.add(new ClawMove(robot, true));
            autonomous.add(new Drive(robot, -1));
            switch(tagValue)
            {
                case 1: {
                    autonomous.add(new Turn(robot, 90));
                    autonomous.add(new Drive(robot, 1));
                    break;
                }
                case 2: {
                    autonomous.add(new Turn(robot, 90));
                    autonomous.add(new Drive(robot, 1));
                    autonomous.add(new Turn(robot, 90));
                    autonomous.add(new Drive(robot, 1));
                    break;
                }
                case 3: {
                    autonomous.add(new Turn(robot, 90));
                    autonomous.add(new Drive(robot, 1));
                    autonomous.add(new Turn(robot, 90));
                    autonomous.add(new Drive(robot, 1));
                    autonomous.add(new Drive(robot, 1));
                    break;
                }
                default : {

                }
            }
        }
        else
        {
            autonomous.add(new Turn(robot, -180));
            autonomous.add(new Drive(robot, 1.5));
            autonomous.add(new ClawMove(robot, true));
            autonomous.add(new Drive(robot, -1));
            switch(tagValue)
            {
                case 1: {
                    autonomous.add(new Turn(robot, -90));
                    autonomous.add(new Drive(robot, 1));
                    break;
                }
                case 2: {
                    autonomous.add(new Turn(robot, -90));
                    autonomous.add(new Drive(robot, 1));
                    autonomous.add(new Turn(robot, -90));
                    autonomous.add(new Drive(robot, 1));
                    break;
                }
                case 3: {
                    autonomous.add(new Turn(robot, -90));
                    autonomous.add(new Drive(robot, 1));
                    autonomous.add(new Turn(robot, -90));
                    autonomous.add(new Drive(robot, 1));
                    autonomous.add(new Drive(robot, 1));
                    break;
                }
                default : {

                }
            }
        }
        autonInitialized = true;
    }
    // vision setup
    public void visionInit(){
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);

        camera.setPipeline(aprilTagDetectionPipeline);
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(800,448, OpenCvCameraRotation.UPSIDE_DOWN);
            }

            @Override
            public void onError(int errorCode)
            {

            }
        });

        telemetry.setMsTransmissionInterval(50);
    }
    public void tagToTelemetry(AprilTagDetection detection)
    {
        telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id));
        telemetry.addLine(String.format("\nTag Count ID=%d", tagCount));
    }

    public void aprilTagInitLoop() {
        tagCount = 0;

        ArrayList<AprilTagDetection> currentDetections = aprilTagDetectionPipeline.getLatestDetections();

        if (currentDetections.size() != 0) {
            tagFound = false;

            for (AprilTagDetection tag : currentDetections) {
                if (tag.id == 1 || tag.id == 2 || tag.id == 3) {
                    tagOfInterest = tag;
                    tagFound = true;
                    tagCount++;
                    break;
                }
            }

            if (tagFound) {
                telemetry.addLine("Tag of interest is in sight!\n\nLocation data:");
                tagDetected = tagOfInterest.id;
                tagToTelemetry(tagOfInterest);

            } else {
                tagCount = 0;
                telemetry.addLine("Don't see tag of interest :(");

                if (tagOfInterest == null) {
                    telemetry.addLine("(The tag has never been seen)");
                } else {
                    telemetry.addLine("\nBut we HAVE seen a tag before; last seen at:");
                    tagToTelemetry(tagOfInterest);
                }
            }

        } else {
            tagCount = 0;
            telemetry.addLine("Don't see tag of interest :(");

            if (tagOfInterest == null) {
                telemetry.addLine("(A tag has never been seen)");
            } else {
                telemetry.addLine("\nBut we HAVE seen a tag before; last seen at:");
                tagToTelemetry(tagOfInterest);
            }

        }

    }

    public void autonVisionTelemetry() {
        if (tagOfInterest != null) {
            telemetry.addLine("Tag snapshot:\n");
            tagToTelemetry(tagOfInterest);
            telemetry.update();
        } else {
            telemetry.addLine("Tag was not detected, only dropping off the cone");
            telemetry.update();
        }
    }
}