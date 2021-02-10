/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/





package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import frc.robot.Constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;

//import org.graalvm.compiler.asm.amd64.AMD64Address.Scale; //the shop ghost is in my house i don't know what this means






public class GalacticSearchSubsystem extends SubsystemBase {
  


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

  private final AnalogInput ultrasonicWide = new AnalogInput(Constants.ultrasonicWideSensorNumber);
  private final AnalogInput ultrasonicNarrow = new AnalogInput(Constants.ultrasonicNarrowSensorNumber);

  public CANEncoder leftMotorEncoder = leftFrontMotor.getEncoder();
  public CANEncoder rightMotorEncoder = rightFrontMotor.getEncoder();
  
  //***** --------------- END CANSPARKMAX DRIVE CODE --------------- *****

  public double rawValue;
  public double currentDistance;
  public double pathADistance = 156;
  public double pathBDistance = 120;
  public double scale = 0; // 0 is a placehoder for now
  public double encoderClicksToRedA = 4166 / 42; //amount of rotations for path Red A
  public double encoderClicksTurn = 212 / 42; //Change name and placeholder
  public double encoderClicksToRedA2 = 212 / 42; //placeholder
  public double radiusOfTurn = 0; //placeholder
  public boolean stop = false;
  public double leftSidePower = 0.4;
  public double rightSidePower = 0.4;
  public double encoderClicksLeft = 0;
  public double encoderClicksRight = 0;
  public int number = 0;
  public GalacticSearchSubsystem() {
    drive.setSafetyEnabled(true);
  }
  public void paths(double encoderClicksFoward1, double turn1,double encoderClicksFoward2){
    encoderClicksLeft = leftMotorEncoder.getPosition();
    encoderClicksRight = rightMotorEncoder.getPosition();
    number = 0;
    /* ***Sets the motor to a certain speed*** */
    switch (number) {
      case 0:
      drive.tankDrive(leftSidePower, rightSidePower);
      if (encoderClicksLeft > encoderClicksToRedA){
        rightMotorEncoder.setPosition(0);
        leftMotorEncoder.setPosition(0);
        number += 1;
      }
      break;
      case 1:
      drive.curvatureDrive(0.3, radiusOfTurn, false);//not using curvature, need to use tank for turning
      if (encoderClicksRight > encoderClicksTurn){
        rightMotorEncoder.setPosition(0);
        leftMotorEncoder.setPosition(0);
        number += 1;
      }
      break;
      case 2:
      drive.tankDrive(leftSidePower, rightSidePower);
      if (encoderClicksLeft > encoderClicksToRedA2){
        rightMotorEncoder.setPosition(0);
        leftMotorEncoder.setPosition(0);
        number += 1;
      }
      break;
    }
  }
  public boolean deciderA(/*AnalogInput distance*/){ //disable during path B
    rawValue = ultrasonicWide.getValue();
    currentDistance = rawValue * 0.125 * 2.54; //this is going to convert the raw value to centimeters and then centimeters to inches
    if (currentDistance <= pathADistance){
      return true;
    }
    else if (currentDistance > pathADistance){
      return false;
    }
    return false;
  }
  public boolean deciderB(AnalogInput distance){ //disable during path A
    rawValue = ultrasonicWide.getValue();
    currentDistance = rawValue * 0.125 * 2.54; //this is going to convert the raw value to centimeters and then centimeters to inches
    if (currentDistance <= pathBDistance){
      return true;
    }
    else if (currentDistance > pathADistance){
      return false;
    }
    return false;
  }

  //* Creates a method periodic() that will be called once per scheduler run *
  //*** This allows us to repeate sections of code and acts similar in nature to a loop ***
  @Override
  public void periodic() {
    
  }



}
