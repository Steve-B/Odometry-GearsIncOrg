package org.firstinspires.ftc.teamcode.src.robotAttachments.navigation;

/**
 * A interface that standardizes methods to get the current robot location.
 * Location must be 0,0,0 on initialization
 * Location must be returned in inches
 * Location methods must be thread safe to call
 */
public interface LocalizationAlgorithm {
    /**
     * Gets the x position of the robot in inches
     * (0,0) is relative to the red warehouse side of the field
     *
     * @return The x position of the robot in inches
     */
    double getX();

    /**
     * Gets the y position of the robot in inches
     * (0,0) is relative to the red warehouse side of the field
     *
     * @return The y position of the robot in inches
     */
    double getY();

    /**
     * Gets the orientation of the robot in degrees
     * north is pointing towards center and is perpendicular to the warehouse wall
     *
     * @return The heading of the robot in degrees
     */
    double getRot();

    /**
     * Sets the position of the robot
     *
     * @param X   The x coordinate the robot is at
     * @param Y   The Y coordinate the robot is at
     * @param rot The heading the robot is at
     */
    void setPos(double X, double Y, double rot);
}
