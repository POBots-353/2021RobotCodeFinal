/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.RobotContainer;

public class BallTransitSubsystem extends SubsystemBase {
  /**
   * Creates a new BallTransitSystem.
   */
  public CANSparkMax intakeMotor = new CANSparkMax(Constants.intakeMotorDeviceID,MotorType.kBrushless);
  public CANSparkMax conveyorMotor = new CANSparkMax(Constants.conveyorMotorDeviceID,MotorType.kBrushless);
  public CANSparkMax shooterMotor = new CANSparkMax(Constants.shooterMotorDeviceID,MotorType.kBrushless);
  /**What happens if we take out preshooter?**/
  //public CANSparkMax preShooterMotor = new CANSparkMax(Constants.preShooterDeviceID, MotorType.kBrushless);
  
  public DigitalInput shooterSensor = new DigitalInput(Constants.shooterLimitSwitch);
  public DigitalInput intakeSensor = new DigitalInput(Constants.intakeSensorNumber);
  //public DigitalInput conveyorSensor = new DigitalInput(Constants.conveyorSensorNumber); removed conveyorSensor, not sure if it will work

  public boolean intakeIn;
  public boolean intakeOut;
  public boolean shooterRunning;
  public int countTime;
  public boolean shooterReverse;

  public BallTransitSubsystem() {

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
     boolean intakeBtn = RobotContainer.operatorStick.getRawButtonPressed(Constants.intakeButtonNumber); // was driverStick
     boolean outtakeBtn = RobotContainer.operatorStick.getRawButtonPressed(Constants.outtakeButtonNumber); // was driverStick
     boolean conveyorUpBtn = RobotContainer.operatorStick.getRawButton(Constants.conveyorUpButtonNumber); //getRawButton
     boolean conveyorDownBtn = RobotContainer.operatorStick.getRawButton(Constants.conveyorUpButtonNumber); //getRawButton
     boolean shootBtn = RobotContainer.operatorStick.getRawButtonPressed(Constants.shootButtonNumber);

    if(intakeBtn||outtakeBtn){
      runIntake(intakeBtn,outtakeBtn);
    }

    //if(shootBtn){
      runShooter(shootBtn);
    //}

    if(conveyorUpBtn){ // these top 2 are simple conditional for if button for conveyor is pressed
      conveyorMotor.set(Constants.conveyorMotorSpeed);
    }
    else if(conveyorDownBtn){
      conveyorMotor.set(Constants.conveyorMotorSpeed*-1);
    }
    else if(intakeIn){ // these next three respond to global querries to run conveyor, could be ors but style
      conveyorMotor.set(Constants.conveyorMotorSpeed);
    }
    else if(intakeOut){
      conveyorMotor.set(Constants.conveyorMotorSpeed*-1);
    }
    else if(shooterReverse){
      conveyorMotor.set(Constants.conveyorMotorSpeed*-1);
    }
    else if(shooterRunning){
      conveyorMotor.set(Constants.conveyorMotorSpeed);
    }
    else{
        conveyorMotor.set(0);
    }

  }

  public void runIntake(boolean intakeBtn,  boolean outtakeBtn){
    if (intakeBtn == true){
      intakeMotor.set(Constants.intakeMotorSpeed);
      if(true){  //if(intakeSensor.get() == true){ // Removed for limit switch concerns on 2/29 ~CR
        intakeIn = true;
        intakeOut = false;
      }
    }
    else if (outtakeBtn == true){
      intakeMotor.set(-Constants.conveyorMotorSpeed);
      intakeIn = false;
      intakeOut = true;
    }
    else{
      intakeMotor.set(0);
      intakeIn = false;
      intakeOut = false;
    }
}

  public void runShooter (boolean shootBtn){
    if (shootBtn == true){
      countTime++;
      if (countTime > 50){
        shooterMotor.set(Constants.shooterMotorSpeed);
        //preShooterMotor.set(Constants.preShooterMotorSpeed); 
        //use redlight to see if code even gets here or limelight
        if (shooterSensor.get() == true){
          shooterRunning = false;
        }
        else{
          shooterRunning = true;
        }
      }
      
      else{
        shooterReverse = true;
      }
      
    }
    else{
      shooterRunning = false;
      shooterMotor.set(0);
      //preShooterMotor.set(0);
      countTime = 0;
      shooterReverse = false;
    }
  
}
}

