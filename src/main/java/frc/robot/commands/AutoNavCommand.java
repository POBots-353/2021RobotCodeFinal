package frc.robot.commands;

import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import frc.robot.Constants;

public class AutoNavCommand extends CommandBase{

  //***** --------------- BEGIN CANSPARKMAX DRIVE CODE --------------- *****
 
  //* Creates a CANSparkMax varible leftFrontMotor, leftRearMotor, rightFrontMotor, and rightRearMotor that are all MotorType kBrushless *
  //*** These are all constructors of the CANSparkMax class and take two parameters CANSparkMax(int, int)  being the port number and the motor type***
  public CANSparkMax leftFrontMotor = new CANSparkMax(Constants.leftFrontMotorDeviceID,MotorType.kBrushless);
  public CANSparkMax leftRearMotor = new CANSparkMax(Constants.leftRearMotorDeviceID, MotorType.kBrushless);
  public CANSparkMax rightFrontMotor = new CANSparkMax(Constants.rightFrontMotorDeviceID, MotorType.kBrushless);
  public CANSparkMax rightRearMotor = new CANSparkMax(Constants.rightRearMotorDeviceID, MotorType.kBrushless);

  //* Creates a SpeedControllerGroup with the (leftFrontMotor and leftRearMotor) and another SpeedControllerGroup with the (rightFrontMotor and rightRearMotor) *
  //*** This allows us to command both the right side motors or the left side motors at the same time ***
  //*** This is comparable to Talon motors controllers where there is a master motor controller and a slave motor controller that follows the master motor controller ***
  SpeedControllerGroup leftMotorGroup = new SpeedControllerGroup(leftFrontMotor, leftRearMotor);
  SpeedControllerGroup rightMotorGroup = new SpeedControllerGroup(rightFrontMotor, rightRearMotor);
  
  //* Creates a DifferentialDrive variable drive that pairs the leftMotorGroup and rightMotorGroup to take two parameters to drive (move and turn) *
  //*** This creates an instance of the class DifferentialDrive(motor, motor) and allows us to access any methods within that class ***
  public DifferentialDrive drive = new DifferentialDrive(leftMotorGroup,rightMotorGroup);

  public CANEncoder leftMotorEncoder = leftFrontMotor.getEncoder();
  public CANEncoder rightMotorEncoder = rightFrontMotor.getEncoder();
  
  //***** --------------- END CANSPARKMAX DRIVE CODE --------------- *****


  private final DriveSubsystem driveSubsystem;
  Gyro gyro = new ADXRS450_Gyro(SPI.Port.kMXP);

  public AutoNavCommand(DriveSubsystem subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    driveSubsystem = subsystem;
    addRequirements(driveSubsystem);
  }



  public void slalom(){
    rightMotorEncoder.setPosition(0);
    leftMotorEncoder.setPosition(0);
    double encoderClicksLeft = leftMotorEncoder.getPosition();
    double encoderClicksRight = rightMotorEncoder.getPosition();

    enterExit(encoderClicksRight);
    longSection(encoderClicksLeft);
    circle(encoderClicksRight);
    longSection(encoderClicksLeft);
    enterExit(encoderClicksRight);
  }

  public void enterExit(double rightClicks){
    double theta = Math.PI/2;
    double radius = 30;
    double outerLength = theta * radius;
    double clicks = outerLength * 25;

    while(rightClicks < clicks){
      drive.tankDrive(0.3, 0.6);
    }
    rightMotorEncoder.setPosition(0);
    leftMotorEncoder.setPosition(0);
  }

  public void longSection(double leftClicks){
    double theta = Math.PI;
    double radius = 60;
    double outerLength = theta * radius;
    double clicks = outerLength * 25;

    while(leftClicks < clicks){
      drive.tankDrive(0.6, 0.3);
    }
    rightMotorEncoder.setPosition(0);
    leftMotorEncoder.setPosition(0);
  }

  public void circle(double rightClicks){
    double theta = 2 * Math.PI;
    double radius = 30;
    double outerLength = theta * radius;
    double clicks = outerLength * 25;

    while(rightClicks < clicks){
      drive.tankDrive(0.3, 0.6);
    }
    rightMotorEncoder.setPosition(0);
    leftMotorEncoder.setPosition(0);
  }
}