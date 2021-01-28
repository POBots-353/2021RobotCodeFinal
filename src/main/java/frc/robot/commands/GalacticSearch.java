package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.AnalogInput;


public class GalacticSearch extends CommandBase{
    private final DriveSubsystem driveSubsystem;
    public int t = 0;

    public GalacticSearch(DriveSubsystem subsystem){
        driveSubsystem = subsystem;    
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(driveSubsystem);
      }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    
  }

  //in execute(): get information from ultrasonic sensor every t amount of milliseconds, 
  //use this info to guide direction that robot is facing, then drive




}