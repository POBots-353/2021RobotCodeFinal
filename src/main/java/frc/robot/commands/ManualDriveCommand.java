/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/





package frc.robot.commands;

import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.*;
 




public class ManualDriveCommand extends CommandBase {
  @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
  private final DriveSubsystem driveSubsystem;

  /**
   * Creates a new ExampleCommand.
   *
   * @param subsystem The subsystem used by this command.
   */
  public ManualDriveCommand(DriveSubsystem subsystem) {
    driveSubsystem = subsystem;
    addRequirements(driveSubsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

    double move = RobotContainer.driverStick.getY();
    double turn = RobotContainer.driverStick.getX();
    
    driveSubsystem.manualDrive(move, turn, scaleConstant());    
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if(interrupted){
      driveSubsystem.manualDrive(0, 0, 0);
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  //From CR's yellow sheet "turbo" method by CR 1/18/2020
  //Creates a method scaleConstant()
  public double scaleConstant(){
    double scale = 0.0;
    boolean turbo = RobotContainer.driverStick.getRawButton(Constants.turboButtonNumber);
    boolean slow = RobotContainer.driverStick.getRawButton(Constants.slowButtonNumber);
    if(turbo == true){ //Turbo no cap on throttle
      scale = Constants.turboScale;
    }
    else if(slow==true){ //slow drive
      scale = Constants.slowScale;
    }
    else{
      scale = Constants.driverScale; //drivers constant
    }
    return scale;
  }
}
