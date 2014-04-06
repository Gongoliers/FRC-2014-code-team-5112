/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    
    static final double THRESHOLD = 0.13d;
    static final double ARM_THRESH = 0.07d;
    
    Joystick stick;
    Joystick util; 
    Jaguar leftDrive;
    Jaguar rightDrive;
    Jaguar collector;
    Jaguar passThrough;
    Victor hugo;
    
    public RobotTemplate(){
        stick = new Joystick(1);
        util = new Joystick(2);
        leftDrive = new Jaguar(1);
        rightDrive = new Jaguar(2);
        collector = new Jaguar(3);
        passThrough = new Jaguar(4);
        hugo = new Victor(5);
    }
    
    public void autonomous() {
        
        //drives forward and stops
        leftDrive.set(55);
        rightDrive.set(-50);
        Timer.delay(2.9);
        leftDrive.set(0);
        rightDrive.set(0);
        passThrough.set(1.0);
        Timer.delay(2.5);
        
        
        //This code can be used to test the autonomous mode
        /*leftDrive.set(0.25);
        rightDrive.set(0.25);
        Timer.delay(1.0);
        leftDrive.set(0.0);
        rightDrive.set(0.0);
        //waits half a second before shooting
        Timer.delay(0.05);
        //allows for the ball to be dumped into the goal
        collector.set(1.0);
        */
        
     /*//Test autonomous mode
      leftDrive.set(-0.5);
      rightDrive.set(-0.5);
      Timer.delay(3.0);
      leftDrive.set(0.75);
      rightDrive.set(0.75);
      Timer.delay(3.0);
      leftDrive.set(0.0);
      rightDrive.set(0.0);  
      Timer.delay(0.0005);
      collector.set(1.0):
        */
    

    /**
     * This function is called once each time the robot enters operator control.
     */
    
    /**
     * 
     * @param value the joystick value
     * @return the power the motor should run at
     */
    }
  
    public double getPower(double value) {
        double throttle = (-stick.getThrottle() + 1.0d) / 2.0d;
        return value * throttle;
    }
    
    //fifths the power so as to make driving a wee bit smoother
    public double getSquarePower(double value) {
        return value * value * value * value * value;
    }
    
    /*
    THE CONTROLS:
    
    Driver: Extreme 3D Pro
    Controller: Attack 3
    
    Move robot: Driver joystick
    Precision movements: Driver top-hat
    Move arm: Controller joystick (away from you for up)
    Suck the ball in: Controller trigger
    Position ball in rollers: Controller buttons 2 and 3 (forward and back)
    Shoot ball: Driver trigger
    Pass ball: Driver side button
    */
    
    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
            double x = getSquarePower(getPower(stick.getX())); //raw side-side value
            double y = -(getSquarePower(getPower(stick.getY()))); //raw forward-back power
            double arm = -getSquarePower(util.getY()); //raw inner wheels power
            
            //takes care of all the drive train stuff, choo choo
            if (y < -THRESHOLD || y > THRESHOLD) {
                if (x < -THRESHOLD) {
                    leftDrive.set(y + x);
                    rightDrive.set(-y);
                } else if (x > THRESHOLD) {
                    leftDrive.set(y);
                    rightDrive.set(-(y - x));
                } else {
                    leftDrive.set(y);
                    rightDrive.set(-y);
                }
            } else if (x < -THRESHOLD || x > THRESHOLD) {
                leftDrive.set(x);
                rightDrive.set(x);
            } else if (stick.getRawAxis(6) == -1) {
                leftDrive.set(0.25);
                rightDrive.set(0.25);
            } else if (stick.getRawAxis(6) == 1) {
                leftDrive.set(-0.25);
                rightDrive.set(-0.25);
            } else if (stick.getRawAxis(5) == -1) {
                leftDrive.set(-0.25);
                rightDrive.set(0.25);
            } else if (stick.getRawAxis(5) == 1) {
                leftDrive.set(0.25);
                rightDrive.set(-0.25);
            } else {
                leftDrive.set(0.0);
                rightDrive.set(0.0);
            } 

            if (arm > ARM_THRESH || arm < -ARM_THRESH) {
                collector.set(-arm);
            } else {
                collector.set(0.0);
            }
            
            if (stick.getRawButton(1)) {
                passThrough.set(1.0);
            } else if (stick.getRawButton(2)) {
                passThrough.set(-1.0);
            } else if (util.getRawButton(3)) {
                passThrough.set(0.25);
            } else if (util.getRawButton(2)) {
                passThrough.set(-0.25);
            } else {
                passThrough.set(0.0);
            }
            
            //the wheel on the arm
            if (util.getRawButton(1)) {
                hugo.set(-1.0);
            } else {
                hugo.set(0.0);
            }
            
        }
    }
    
    
    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
        operatorControl();
    }
}
