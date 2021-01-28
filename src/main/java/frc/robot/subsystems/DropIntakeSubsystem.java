
/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/


/*


package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.RobotContainer;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;





public class DropIntakeSubsystem extends SubsystemBase {
  public int timeTurner = 0;



  //* Creates a Relay varible intakeSpike *
  //*** This is a constructor of the Realy class and takes one parameter Relay(int) being the port number ***
  Relay intakeSpike = new Relay(Constants.intakeSpikeNumber);
  


  //* Creates a constructor of the class DropIntakeSubsystem() *
  //*** This allows us to create instances of the class DropIntakeSubsystem() and can be called in other classes ***
  public DropIntakeSubsystem() {

  }



  //* Creates a method periodic() that will be called once per scheduler run *
  //*** This allows us to repeate sections of code and acts similar in nature to a loop ***
  @Override
  public void periodic() {

    //* Creates one boolean value (true or false) that indicates whether or not button release has been pressed or not *
    //*** True represents the button was pressed and false represents that the button was not pressed ***
    boolean release = RobotContainer.operatorStick.getRawButtonPressed(Constants.dropIntakeButtonNumber);
    
    //* "if" tests if release is true (pressed) which will give power to the electric solenoid *
    //*** Giving power to the elctric solenoid will pull the pin in and cause whatever it was holding to be released ***
    timeTurner++;

    if(timeTurner>100||release == true){
      intakeSpike.set(Relay.Value.kOff);
    }
    else{
      intakeSpike.set(Relay.Value.kOn);
    }
    }

  }

*/


