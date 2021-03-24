/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard; 
//import edu.wpi.first.wpilibj.SPI;
import frc.robot.Constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;

//import org.graalvm.compiler.asm.amd64.AMD64Address.Scale; //the shop ghost is in my house i don't know what this means


public class GalacticSearchSubsystem extends SubsystemBase {
  


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
  // The left-side drive encoder
 // private final Encoder m_leftEncoder = new Encoder(Constants.leftFrontMotorDeviceID, Constants.leftRearMotorDeviceID, false);

  // The right-side drive encoder
  //private final Encoder m_rightEncoder = new Encoder(Constants.rightFrontMotorDeviceID, Constants.rightRearMotorDeviceID, false);
  
  // The gyro sensor
<<<<<<< HEAD
  //Fix this
  Gyro m_gyro = new ADXRS450_Gyro();

=======
  private final Gyro m_gyro = new ADXRS450_Gyro(/*SPI.Port.kMXP*/);
>>>>>>> bfd9749f2362bc8aa3cb35535c350b0402a3ff5c
  // Odometry class for tracking robot pose
  private final DifferentialDriveOdometry m_odometry;
  public double leftDistance;
  public double rightDistance;
  public double conversionToMeters = 0.004267;
  /**
   * Creates a new DriveSubsystem.
   */
  public GalacticSearchSubsystem() {
    // Sets the distance per pulse for the encoders 
    //0.004267m
    leftMotorEncoder.setVelocityConversionFactor(conversionToMeters);
    rightMotorEncoder.setVelocityConversionFactor(conversionToMeters);
    leftMotorEncoder.setPosition(0);
    rightMotorEncoder.setPosition(0);
    resetEncoders();
    m_odometry = new DifferentialDriveOdometry(m_gyro.getRotation2d());
  }
  /**Returns position * meters**/
  public double getConversionToMeters(CANEncoder thing) {
    return thing.getPosition() * conversionToMeters;
  }
  @Override
  public void periodic() {
    // Update the odometry in the periodic block
    m_odometry.update(m_gyro.getRotation2d(), getConversionToMeters(leftMotorEncoder), 
    getConversionToMeters(rightMotorEncoder));
    //test smartdashboard 
  }

  /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }
  /**
   * Returns the current wheel speeds of the robot.
   *
   * @return The current wheel speeds.
   */
  public DifferentialDriveWheelSpeeds getWheelSpeeds() {
    return new DifferentialDriveWheelSpeeds(
      leftMotorEncoder.getVelocity() *conversionToMeters, 
      rightMotorEncoder.getVelocity() * conversionToMeters);
  }

  /**
   * Resets the odometry to the specified pose.
   *
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    resetEncoders();
    leftMotorEncoder.setPosition(0);
    rightMotorEncoder.setPosition(0);
    m_odometry.resetPosition(pose, m_gyro.getRotation2d());
  }

  /**
   * Drives the robot using arcade controls.
   *
   * @param fwd the commanded forward movement
   * @param rot the commanded rotation
   */
  public void arcadeDrive(double fwd, double rot) {
    drive.arcadeDrive(fwd, rot);
  }

  /**
   * Controls the left and right sides of the drive directly with voltages.
   *
   * @param leftVolts  the commanded left output
   * @param rightVolts the commanded right output
   */
  public void tankDriveVolts(double leftVolts, double rightVolts) {
    leftMotorGroup.setVoltage(leftVolts);
    rightMotorGroup.setVoltage(-rightVolts);
    drive.feed();
  }

  /**
   * Resets the drive encoders to currently read a position of 0.
   */
  public void resetEncoders() {
    leftMotorEncoder.setPosition(0);
    rightMotorEncoder.setPosition(0);
  }

  /**
   * Gets the average distance of the two encoders.
   *
   * @return the average of the two encoder readings
   */
  public double getAverageEncoderDistance() {
    return (leftMotorEncoder.getPosition() * conversionToMeters + rightMotorEncoder.getPosition() * conversionToMeters) / 2.0;
  }

  /**
   * Gets the left drive encoder.
   *
   * @return the left drive encoder
   */
  public CANEncoder getLeftEncoder() {
    return leftMotorEncoder;
  }

  /**
   * Gets the right drive encoder.
   *
   * @return the right drive encoder
   */
  public CANEncoder getRightEncoder() {
    return rightMotorEncoder;
  }
  /**
   * Sets the max output of the drive.  Useful for scaling the drive to drive more slowly.
   * @param maxOutput the maximum output to which the drive will be constrained
   */
  public void setMaxOutput(double maxOutput) {
    drive.setMaxOutput(maxOutput);
  }

  /**
   * Zeroes the heading of the robot.
   */
  public void zeroHeading() {
    m_gyro.reset();
  }
  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from -180 to 180
   */
  public double getHeading() {
    return m_gyro.getRotation2d().getDegrees();
  }

  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    return -m_gyro.getRate();
  }
}
