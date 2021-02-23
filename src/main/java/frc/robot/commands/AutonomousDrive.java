/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
//import edu.wpi.first.wpilibj2.command.CommandBase;
//import frc.robot.*;
 
public class AutonomousDrive extends CommandBase {
  
  private final DriveSubsystem driveSubsystem;
  public int t = 0;
  /**
   * Creates a new AutonomousDrive.
   */
  public AutonomousDrive(DriveSubsystem subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
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
    /*t++;
    if (t <100){
      driveSubsystem.manualDrive(1, 0, -.60); //sends commands to manualDrive repeatedly
    }
  */
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
