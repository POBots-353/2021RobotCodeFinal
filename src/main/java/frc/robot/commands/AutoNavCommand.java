package frc.robot.commands;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class AutoNavCommand extends CommandBase{

  private final DriveSubsystem driveSubsystem;
  Gyro gyro = new ADXRS450_Gyro(SPI.Port.kMXP);

  public AutoNavCommand(DriveSubsystem subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    driveSubsystem = subsystem;
    addRequirements(driveSubsystem);
  }

  public void slalom(){

  }
}
