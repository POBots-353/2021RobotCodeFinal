/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/





package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import frc.robot.Constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;






public class DriveSubsystem extends SubsystemBase {
  


  //***** --------------- BEGIN CANSPARKMAX DRIVE CODE --------------- *****

  //* Creates a CANSparkMax varible leftFrontMotor, leftRearMotor, rightFrontMotor, and rightRearMotor that are all MotorType kBrushless *
  //*** These are all constructors of the CANSparkMax class and take two parameters CANSparkMax(int, int)  being the port number and the motor type***
  public CANSparkMax leftFrontMotor = new CANSparkMax(Constants.leftFrontMotorDeviceID,MotorType.kBrushless);
  public CANSparkMax leftRearMotor = new CANSparkMax(Constants.leftRearMotorDeviceID, MotorType.kBrushless);
  public CANSparkMax rightFrontMotor = new CANSparkMax(Constants.rightFrontMotorDeviceID, MotorType.kBrushless);
  public CANSparkMax rightRearMotor = new CANSparkMax(Constants.rightRearMotorDeviceID, MotorType.kBrushless);
  
  //* Creates a SpeedControllerGroup with the (leftFrontMotor and leftRearMotor) and another SpeedControllerGroup with the (rightFrontMotor and rightRearMotor) *
  //*** This allows us to command both the right side motors or the left side motors at the same time ***
  //*** This is comparable to Talon motors controllers where there is a master motor controller and a slave motor controller that follows the master motor controller ***
  SpeedControllerGroup leftMotorGroup = new SpeedControllerGroup(leftFrontMotor, leftRearMotor);
  SpeedControllerGroup rightMotorGroup = new SpeedControllerGroup(rightFrontMotor, rightRearMotor);
  
  //* Creates a DifferentialDrive variable drive that pairs the leftMotorGroup and rightMotorGroup to take two parameters to drive (move and turn) *
  //*** This creates an instance of the class DifferentialDrive(motor, motor) and allows us to access any methods within that class ***
  public DifferentialDrive drive = new DifferentialDrive(leftMotorGroup,rightMotorGroup);

  //* Creates a manualDrive(double, double, double) method that takes in the x and y values from the joystick as well as the scale and sends them to the function arcadeDrive(int, int) *
  //*** This allows us to call a manualDrive(double, double, double) method that lets us use a joystick or controller to control the speed and direction of the robot ***
  public void manualDrive(double move, double turn, double scale){
    drive.arcadeDrive(-move * Math.abs(move) * scale, turn * Math.abs(turn) * scale); //Riley came up with the absolute value idea--> very smart
  }

  //* Creates a autoAlignDrive(double, double) method that takes in the x and y values from the Constants() class variables kPAim for move and kPDistance for turn and sends them to the function arcadeDrive(int, int) *
  //*** This allows us to call a autoAlignDrive(double, double) method that lets us use the limelight to autonomously drive ***
  public void autoAlignDrive(double move, double turn){
    drive.arcadeDrive(move, -turn);
  }

  //***** --------------- END CANSPARKMAX DRIVE CODE --------------- *****



  //* Creates a constructor of the class DriveSubsystem() and sets the DifferentialDrive() variable drive to true *
  //*** This allows us to use the arcadeDrive() method and actually turn any of the motors in unison ***
  public DriveSubsystem() {
    drive.setSafetyEnabled(true);
  }



  //* Creates a method periodic() that will be called once per scheduler run *
  //*** This allows us to repeate sections of code and acts similar in nature to a loop ***
  @Override
  public void periodic() {
    
  }



}
