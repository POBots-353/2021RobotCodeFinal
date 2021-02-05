package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.GyroBase;

public class AutoNavCommand extends CommandBase{

  private final DriveSubsystem driveSubsystem;

  public AutoNavCommand(DriveSubsystem subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    driveSubsystem = subsystem;
    addRequirements(driveSubsystem);
  }

  public void slalom(){
    
  }
}
