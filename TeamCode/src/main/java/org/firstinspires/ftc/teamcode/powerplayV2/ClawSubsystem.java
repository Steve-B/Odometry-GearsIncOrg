package org.firstinspires.ftc.teamcode.powerplayV2;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ClawSubsystem extends SubsystemBase {
    private final Servo servo;

    public enum State {
        GRAB, RELEASE
    };

    State state;

    public ClawSubsystem(HardwareMap hardwareMap) {
        this.servo = hardwareMap.get(Servo.class, "claw");
        this.release();
    }

    public void grab() {
        servo.setPosition(0.75);
        state = State.GRAB;
    }

    public void release() {
        servo.setPosition(0.18);
        state = State.RELEASE;
    }

    public State getState() {
        return state;
    }
}