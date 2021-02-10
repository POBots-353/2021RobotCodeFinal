package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GalacticSearchSubsystem;
import edu.wpi.first.wpilibj.AnalogInput;


public class GalacticSearchCommand extends CommandBase{
    private final GalacticSearchSubsystem galacticSubsystem;
    public int t = 0;

    public GalacticSearchCommand(GalacticSearchSubsystem subsystem){
        galacticSubsystem = subsystem;    
        //Use addRequirements() here to declare subsystem dependencies.
        addRequirements(galacticSubsystem);
      }
  public boolean pathAorB;
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    pathAorB = galacticSubsystem.deciderA();
  }

  //in execute(): get information from ultrasonic sensor every t amount of milliseconds, 
  //use this info to guide direction that robot is facing, then drive
  @Override
  public void execute() {
    if (pathAorB){
      //Sends parameters to path
      galacticSubsystem.paths(0,0,0);
    }else if (pathAorB){
      galacticSubsystem.paths(0,0,0);
    }
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