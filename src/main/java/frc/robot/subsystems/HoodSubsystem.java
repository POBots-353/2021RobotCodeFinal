/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class HoodSubsystem extends SubsystemBase {
  /**
   * Creates a new HoodSubsystem.
   */
  public CANSparkMax hoodMotor = new CANSparkMax(Constants.hoodMotorDeviceID, MotorType.kBrushless);
  public CANPIDController hoodMotorController = hoodMotor.getPIDController();
  public CANEncoder hoodMotorEncoder = hoodMotor.getEncoder();

  public int hoodToggleState;


  double kP = 0.294; 
  double kI = 0;
  double kD = 0; 
  double kIz = 0;
  double kFF = 0.000156; 
  double kMaxOutput = 1; 
  double kMinOutput = -1;
  double maxRPM = 570;
  double allowedErr = 0;
  double minVel = 0;
  // Smart Motion Coefficients
  double maxVel = 200; // rpm
  double maxAcc = 150;
  double setPoint, encoderPosition;

  public HoodSubsystem() {
    hoodMotor.restoreFactoryDefaults();
    hoodMotorEncoder.setPosition(0);
    hoodToggleState = 1;
    // set PID coefficients
    hoodMotorController.setP(kP);
    hoodMotorController.setI(kI);
    hoodMotorController.setD(kD);
    hoodMotorController.setIZone(kIz);
    hoodMotorController.setFF(kFF);
    hoodMotorController.setOutputRange(kMinOutput, kMaxOutput);

    int smartMotionSlot = 0;
    hoodMotorController.setSmartMotionMaxVelocity(maxVel, smartMotionSlot);
    hoodMotorController.setSmartMotionMinOutputVelocity(minVel, smartMotionSlot);
    hoodMotorController.setSmartMotionMaxAccel(maxAcc, smartMotionSlot);
    hoodMotorController.setSmartMotionAllowedClosedLoopError(allowedErr, smartMotionSlot);
    //SmartDashboard.putNumber("Set Position", 0);
    //SmartDashboard.putNumber("Set Velocity", 0);
  }
@Override
  public void periodic() {
    if(hoodToggleState == 0 && RobotContainer.operatorStick.getRawButton(Constants.hoodRunBtnNum)){
      double ty = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ty").getDouble(0);
 
    
    
    setPoint = ty; //regression to convert limelight to encoder value
    // anticipate arctan here 
    }
    else if(hoodToggleState == 1 && RobotContainer.operatorStick.getRawButton(Constants.hoodRunBtnNum)){
      setPoint = Constants.hoodAngle1;
    }
    else if(hoodToggleState == 2 && RobotContainer.operatorStick.getRawButton(Constants.hoodRunBtnNum)){
      setPoint = Constants.hoodAngle2;
    }
    else if(hoodToggleState == 3 && RobotContainer.operatorStick.getRawButton(Constants.hoodRunBtnNum)){
      setPoint = Constants.hoodAngle3;
    }
    hoodMotorController.setReference(setPoint, ControlType.kPosition);
    

    encoderPosition = hoodMotorEncoder.getPosition();

    SmartDashboard.putNumber("Hood toggle position: ", hoodToggleState);
    SmartDashboard.putNumber("Encoder Position", encoderPosition);
    SmartDashboard.putNumber("Setpoint ", setPoint);
}

}
