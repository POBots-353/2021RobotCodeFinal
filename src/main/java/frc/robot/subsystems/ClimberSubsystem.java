/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/





package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;





public class ClimberSubsystem extends SubsystemBase {



  //* Creates a CANSparkMax varible climberMotor that is a MotorType kBrushless *
  //*** This is a constructor of the CANSparkMax class and takes two parameters CANSparkMax(int, int) being the port number and the motor type ***
  public CANSparkMax climberMotor = new CANSparkMax(Constants.climberMotorDeviceID,MotorType.kBrushless);



  //* Creates a constructor of the class ClimberSubsystem() *
  //*** This allows us to create instances of the class ClimberSubsystem() and can be called in other classes ***
  public ClimberSubsystem() {
    
  }


  //* Creates a method periodic() that will be called once per scheduler run *
  //*** This allows us to repeate sections of code and acts similar in nature to a loop ***
  @Override
  public void periodic() {

    //* Creates two boolean values (true or false) that indicates whether or not buttons climberUp and climberDown have been pressed or not *
    //*** True represents the button was pressed and false represents that the button was not pressed ***
    boolean climberUp = RobotContainer.operatorStick.getRawButtonPressed(Constants.climberUpButtonNumber);

    //* "if" tests if climberUp is true (pressed) and climberDown is false (not pressed) which will start the climberMotor to raise the climber *
    //* "else if" tests if climberDown is true (pressed) and climberUp is false (not pressed) which will start the climberMotor in the opposite direction to lower the climber *
    //* "else" makes it so any other combination of climberUp and climberDown occurs it stops the climberMotor *
    if(climberUp == true){
      climberMotor.set(-1.0* Constants.climberMotorSpeed);
    }
    else{
      climberMotor.set(0);
    }

  }



}