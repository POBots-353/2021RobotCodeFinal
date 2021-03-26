package frc.robot.commands;

//import edu.wpi.first.wpilibj.interfaces.Gyro;
//import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveSubsystem;
//import edu.wpi.first.wpilibj.GyroBase;
//import edu.wpi.first.wpilibj.ADXRS450_Gyro;
//import com.revrobotics.CANSparkMax;
//import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;
//import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.SpeedControllerGroup;
//import frc.robot.Constants;
//import frc.robot.RobotContainer;

public class AutoNavCommand extends CommandBase{

  /*//***** --------------- BEGIN CANSPARKMAX DRIVE CODE --------------- *****
 
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
 */
  
  //***** --------------- END CANSPARKMAX DRIVE CODE --------------- *****


  private final DriveSubsystem driveSubsystem;
  //Gyro gyro = new ADXRS450_Gyro(SPI.Port.kMXP);
  
  public CANEncoder leftMotorEncoder; 
  public CANEncoder rightMotorEncoder;

  public AutoNavCommand(DriveSubsystem subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    driveSubsystem = subsystem;
    addRequirements(driveSubsystem);

    leftMotorEncoder = driveSubsystem.leftFrontMotor.getEncoder();
    rightMotorEncoder = driveSubsystem.rightFrontMotor.getEncoder();
    
    //resets encoders
    rightMotorEncoder.setPosition(0);
    leftMotorEncoder.setPosition(0);
    //slalom();
  }

  //fields to switch which methods run when
  public boolean enterGo = true;
  public boolean exitGo = false;
  public boolean longUpGo = false;
  public boolean longDownGo = false;
  public boolean circleGo = false;


  @Override
  public void execute() {
    slalom(); //THIS WASN'T HERE IT WASN'T UPDATING
  }

  public void slalom(){
    
    //declares encoder objects to be passed into methods
    double encoderClicksLeft = leftMotorEncoder.getPosition();
    double encoderClicksRight = rightMotorEncoder.getPosition();

    //calls each method passing in the encoders where needed
    
    if(circleGo == true){
      slalomCircle(encoderClicksRight);
    }
    else if(longUpGo == true){
      slalomLongSectionFirst(encoderClicksLeft);
    }
    else if(longDownGo == true){
      slalomLongSectionSecond(encoderClicksLeft);
    }
    else if(enterGo == true){
      slalomEnter(encoderClicksRight);
    }
    else if(exitGo == true){
      slalomExit(encoderClicksRight);
    }
  }

  //slalomEnterExit is the first and last curve (they are equal) going out and in the start and finish
  public void slalomEnter(double rightClicks){

    //physics math for turn in radians, radius of turn (in.), get length outer wheel travels (in.) and converts to amount of encoder clicks
    double theta = Math.PI/2;
    double radius = 57; //inner radius 30 in, outer 57 in (robot width 27 in)
    double outerLength = theta * radius;
    double clicks = outerLength * 25;

    //turns until outer wheel travels the whole turn
    if (rightClicks < clicks){
      driveSubsystem.drive.tankDrive(0.3, 0.57);
    }
    else{
    //resets encoder click counts for next method
      rightMotorEncoder.setPosition(0);
      leftMotorEncoder.setPosition(0);
      enterGo = false;
      longUpGo = true;
    }
  }

  //slalomLongSection is the second and second-to-last curve going around the long section of markers
  public void slalomLongSectionFirst(double leftClicks){

    //physics math for turn in radians, radius of turn (in.), get length outer wheel travels (in.) and converts to amount of encoder clicks
    double theta = Math.PI;
    double radius = 87; //inner radius 60 in, outer 87 in
    double outerLength = theta * radius;
    double clicks = outerLength * 25;

    //turns until outer wheel travels the whole turn (speed will have to be adjusted for exact turn when we test)
    if(leftClicks < clicks){
      driveSubsystem.drive.tankDrive(0.435, 0.3);
    }
    else{
    //resets encoder click counts for next method
      rightMotorEncoder.setPosition(0);
      leftMotorEncoder.setPosition(0);
      longUpGo = false;
      circleGo = true;
    }
  }

  //slalomCircle is the third turn, a circle around the marker farthest to the right
  public void slalomCircle(double rightClicks){

    //physics math for turn in radians, radius of turn (in.), get length outer wheel travels (in.) and converts to amount of encoder clicks
    double theta = 2 * Math.PI;
    double radius = 57; //inner radius 30 in, outer radius 57 in
    double outerLength = theta * radius;
    double clicks = outerLength * 25;

    //turns until outer wheel travels the whole turn
    if(rightClicks < clicks){
      driveSubsystem.drive.tankDrive(0.3, 0.57);
    }
    else{
    //resets encoder click counts for next method
      rightMotorEncoder.setPosition(0);
      leftMotorEncoder.setPosition(0);
      circleGo = false;
      longDownGo = true;
    }
  }

  //slalomLongSection is the second and second-to-last curve going around the long section of markers
  public void slalomLongSectionSecond(double leftClicks){

    //physics math for turn in radians, radius of turn (in.), get length outer wheel travels (in.) and converts to amount of encoder clicks
    double theta = Math.PI;
    double radius = 87; //inner radius 60 in, outer 87 in
    double outerLength = theta * radius;
    double clicks = outerLength * 25;

    //turns until outer wheel travels the whole turn (speed will have to be adjusted for exact turn when we test)
    if(leftClicks < clicks){
      driveSubsystem.drive.tankDrive(0.435, 0.3);
    }
    else{
    //resets encoder click counts for next method
      rightMotorEncoder.setPosition(0);
      leftMotorEncoder.setPosition(0);
      longDownGo = false;
      exitGo = true;
    }
  }

  public void slalomExit(double rightClicks){

    //physics math for turn in radians, radius of turn (in.), get length outer wheel travels (in.) and converts to amount of encoder clicks
    double theta = Math.PI/2;
    double radius = 57; //inner radius 30 in, outer 57 in (robot width 27 in)
    double outerLength = theta * radius;
    double clicks = outerLength * 25;

    //turns until outer wheel travels the whole turn
    if (rightClicks < clicks){
      driveSubsystem.drive.tankDrive(0.3, 0.57);
    }
    else{
    //resets encoder click counts for next method
      rightMotorEncoder.setPosition(0);
      leftMotorEncoder.setPosition(0);
      exitGo = false;
    }
  }

  @Override
  public boolean isFinished() {
    return false;
  }

}