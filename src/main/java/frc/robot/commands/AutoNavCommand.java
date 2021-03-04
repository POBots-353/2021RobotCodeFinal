package frc.robot.commands;

import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
//import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import frc.robot.Constants;
import frc.robot.RobotContainer;

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

    //resets encoders
    rightMotorEncoder.setPosition(0);
    leftMotorEncoder.setPosition(0);
    slalom();
  }

  

  @Override
  public void execute() {
    //slalom();
  }

  public void slalom(){
    
    //declares encoder objects to be passed into methods
    double encoderClicksLeft = leftMotorEncoder.getPosition();
    double encoderClicksRight = rightMotorEncoder.getPosition();

    //calls each method passing in the encoders where needed
    slalomEnterExit(encoderClicksRight);
    slalomLongSection(encoderClicksLeft);
    slalomCircle(encoderClicksRight);
    slalomLongSection(encoderClicksLeft);
    slalomEnterExit(encoderClicksRight);
  }

  //slalomEnterExit is the first and last curve (they are equal) going out and in the start and finish
  public void slalomEnterExit(double rightClicks){

    //physics math for turn in radians, radius of turn (in.), get length outer wheel travels (in.) and converts to amount of encoder clicks
    double theta = Math.PI/2;
    double radius = 30;
    double outerLength = theta * radius;
    double clicks = outerLength * 25;

    //turns until outer wheel travels the whole turn (speed will have to be adjusted for exact turn when we test)
    if (rightClicks < clicks){
      drive.tankDrive(0.3, 0.6);
    }
    else{
    //resets encoder click counts for next method
    rightMotorEncoder.setPosition(0);
    leftMotorEncoder.setPosition(0);
    }
  }

  //slalomLongSection is the second and second-to-last curve going around the long section of markers
  public void slalomLongSection(double leftClicks){

    //physics math for turn in radians, radius of turn (in.), get length outer wheel travels (in.) and converts to amount of encoder clicks
    double theta = Math.PI;
    double radius = 60;
    double outerLength = theta * radius;
    double clicks = outerLength * 25;

    //turns until outer wheel travels the whole turn (speed will have to be adjusted for exact turn when we test)
    if(leftClicks < clicks){
      drive.tankDrive(0.6, 0.3);
    }
    else{
    //resets encoder click counts for next method
    rightMotorEncoder.setPosition(0);
    leftMotorEncoder.setPosition(0);      
    }
  }

  //slalomCircle is the third turn, a circle around the marker farthest to the right
  public void slalomCircle(double rightClicks){

    //physics math for turn in radians, radius of turn (in.), get length outer wheel travels (in.) and converts to amount of encoder clicks
    double theta = 2 * Math.PI;
    double radius = 30;
    double outerLength = theta * radius;
    double clicks = outerLength * 25;

    //turns until outer wheel travels the whole turn (speed will have to be adjusted for exact turn when we test)
    if(rightClicks < clicks){
      drive.tankDrive(0.3, 0.6);
    }
    else{
    //resets encoder click counts for next method
    rightMotorEncoder.setPosition(0);
    leftMotorEncoder.setPosition(0);      
    }
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}