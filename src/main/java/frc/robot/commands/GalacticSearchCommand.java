package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.GalacticSearchSubsystem;
import edu.wpi.first.wpilibj.AnalogInput;
import com.revrobotics.CANEncoder;
import frc.robot.GalacticConstants;


public class GalacticSearchCommand extends CommandBase{
    private final GalacticSearchSubsystem galacticSubsystem;
    public int t = 0;
    public boolean pathAColor;
    public boolean pathBColor; 
    
    public GalacticSearchCommand(GalacticSearchSubsystem subsystem){
        galacticSubsystem = subsystem;    
        //Use addRequirements() here to declare subsystem dependencies.
        addRequirements(galacticSubsystem);
      }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    pathAColor = galacticSubsystem.deciderA(); //disable during path B
    pathBColor = galacticSubsystem.deciderB(); //disable during path A

  }

  //in execute(): get information from ultrasonic sensor every t amount of milliseconds, 
  //use this info to guide direction that robot is facing, then drive
  @Override
  public void execute() {
    
  //disable during path B
    if (pathAColor){ //red A
      //Sends parameters to path
      galacticSubsystem.paths(GalacticConstants.encoderClicksToRedA, GalacticConstants.encoderClicksTurnRedA1, GalacticConstants.encoderClicksToRedA2); 
    }
    else if (!pathAColor){ //blue A
      galacticSubsystem.paths(0,0,0); 
    }

  //disable during path A
    if (pathBColor){ //red B
      galacticSubsystem.paths(0, 0, 0); 
    }
    else if (!pathBColor){ //blue B
      galacticSubsystem.paths(0, 0, 0); 

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