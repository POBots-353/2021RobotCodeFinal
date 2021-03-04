/*HI ----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import java.io.IOException;
import java.nio.file.Path;
//import java.util.List;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import frc.robot.commands.AlignRobotCommand;
import frc.robot.commands.AutoNavCommand;
//import frc.robot.commands.AutonomousDrive;
import frc.robot.commands.HoodCommand;
//import frc.robot.commands.DropIntakeCommand;

import frc.robot.commands.ManualDriveCommand;
import frc.robot.subsystems.BallTransitSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.GalacticSearchSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  // The robot's subsystems are defined here

  public final static DriveSubsystem driveSubsystem = new DriveSubsystem();
  public final static BallTransitSubsystem transitSubsystem = new BallTransitSubsystem();
  public final static HoodSubsystem hood = new HoodSubsystem();
  public final static GalacticSearchSubsystem galactic = new GalacticSearchSubsystem();
  // public final static ColorWheelSubsytem colorWheelSubsystem = new
  // ColorWheelSubsytem();

  // The robot's joysticks are defined here
  public final static Joystick driverStick = new Joystick(Constants.driverStickPort);
  public final static Joystick operatorStick = new Joystick(Constants.operatorStickPort);

  // The robot's ultrasonic sensor is defined here
  // Initializes an nalogInput on port 0, and enables 2-bit averaging
  AnalogInput input = new AnalogInput(0);

  // Initializes an AnalogPotentiometer with the given AnalogInput
  // The full range of motion (in meaningful external units) is 0-180 (this could
  // be degrees, for instance)
  // The "starting point" of the motion, i.e. where the mechanism is located when
  // the potentiometer reads 0v, is 30.

  AnalogPotentiometer pot = new AnalogPotentiometer(input, 180, 30); // hehe pot ~ NS

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    driveSubsystem.setDefaultCommand(new ManualDriveCommand(driveSubsystem));
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */

  private void configureButtonBindings() {
    JoystickButton autoNavStart = new JoystickButton(driverStick, Constants.autoNavButton);
    autoNavStart.whenPressed(new AutoNavCommand(driveSubsystem));

    JoystickButton magicButton = new JoystickButton(driverStick, Constants.AutoAlignButtonNumber);
    magicButton.whileHeld(new AlignRobotCommand(driveSubsystem, hood))
        .whenReleased(new ManualDriveCommand(driveSubsystem));

    JoystickButton hoodToggleButton = new JoystickButton(operatorStick, Constants.hoodToggleBtnNum);
    hoodToggleButton.whenPressed(new HoodCommand(hood));

    // JoystickButton autoColorWheelButton = new JoystickButton(operatorStick,
    // Constants.AutoColorButtonNumber);
    // autoColorWheelButton.whenPressed(new
    // AutoColorWheelCommand(colorWheelSubsystem));

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   * 
   * @return
   *
   * @return the command to run in autonomous
   */
//When recording, change the trajectoryJSON string from A to B when doing B paths, and vice versa. 
  public Command trajectoryRed() {
    var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(new SimpleMotorFeedforward(Constants.ksVolts
    ,Constants.kvVoltSecondsPerMeter
    ,Constants.kaVoltSecondsSquaredPerMeter)
    ,Constants.kDriveKinematics,10);

    // Create config for trajectory
    TrajectoryConfig config = new TrajectoryConfig(Constants.kMaxSpeedMetersPerSecond,Constants.kMaxAccelerationMetersPerSecondSquared)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.kDriveKinematics)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint);

    // An example trajectory to follow.  All units in meters.
    //Change the path to the actual path in the computer?
    String trajectoryJSON = "output/RedA.wpilib.json";
    Trajectory trajectory = new Trajectory();
    try {
      Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
      trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
    } catch (IOException ex) {
      DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
    }

    RamseteCommand ramseteCommand = new RamseteCommand(
        trajectory,
        galactic::getPose,
        new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta),
        new SimpleMotorFeedforward(Constants.ksVolts,
        Constants.kvVoltSecondsPerMeter,
        Constants.kaVoltSecondsSquaredPerMeter),
        Constants.kDriveKinematics,
        galactic::getWheelSpeeds,
        new PIDController(Constants.kPDriveVel, 0, 0),
        new PIDController(Constants.kPDriveVel, 0, 0),
        // RamseteCommand passes volts to the callback
        galactic::tankDriveVolts,
        galactic
    );

    // Reset odometry to the starting pose of the trajectory.
    galactic.resetOdometry(trajectory.getInitialPose());

    // Run path following command, then stop at the end.
    return ramseteCommand.andThen(() -> galactic.tankDriveVolts(0, 0));
  }

  public Command trajectoryBlue() {
    var autoVoltageConstraint = new DifferentialDriveVoltageConstraint(new SimpleMotorFeedforward(Constants.ksVolts
    ,Constants.kvVoltSecondsPerMeter
    ,Constants.kaVoltSecondsSquaredPerMeter)
    ,Constants.kDriveKinematics,10);

    // Create config for trajectory
    TrajectoryConfig config = new TrajectoryConfig(Constants.kMaxSpeedMetersPerSecond,Constants.kMaxAccelerationMetersPerSecondSquared)
            // Add kinematics to ensure max speed is actually obeyed
            .setKinematics(Constants.kDriveKinematics)
            // Apply the voltage constraint
            .addConstraint(autoVoltageConstraint);

    // An example trajectory to follow.  All units in meters.
    //Change the path to the actual path in the computer?
    String trajectoryJSON = "output/BlueA.wpilib.json";
    Trajectory trajectory = new Trajectory();
    try {
      Path trajectoryPath = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
      trajectory = TrajectoryUtil.fromPathweaverJson(trajectoryPath);
    } catch (IOException ex) {
      DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
    }

    RamseteCommand ramseteCommand = new RamseteCommand(
        trajectory,
        galactic::getPose,
        new RamseteController(Constants.kRamseteB, Constants.kRamseteZeta),
        new SimpleMotorFeedforward(Constants.ksVolts,
        Constants.kvVoltSecondsPerMeter,
        Constants.kaVoltSecondsSquaredPerMeter),
        Constants.kDriveKinematics,
        galactic::getWheelSpeeds,
        new PIDController(Constants.kPDriveVel, 0, 0),
        new PIDController(Constants.kPDriveVel, 0, 0),
        // RamseteCommand passes volts to the callback
        galactic::tankDriveVolts,
        galactic
    );

    // Reset odometry to the starting pose of the trajectory.
    galactic.resetOdometry(trajectory.getInitialPose());

    // Run path following command, then stop at the end.
    return ramseteCommand.andThen(() -> galactic.tankDriveVolts(0, 0));
  }

public CANSparkMax intakeMotor = new CANSparkMax(Constants.intakeMotorDeviceID,MotorType.kBrushless);
public CANSparkMax conveyorMotor = new CANSparkMax(Constants.conveyorMotorDeviceID,MotorType.kBrushless);
  public Command getAutonomousCommand() {
    
    input.setAverageBits(2); 

    double ultrasonicValue = pot.get();
	  if (ultrasonicValue <= 830.0){//Red
      intakeMotor.set(Constants.intakeMotorSpeed);
      conveyorMotor.set(Constants.conveyorMotorSpeed);
      return trajectoryRed();
    }
    else{//Blue
      intakeMotor.set(Constants.intakeMotorSpeed);
      conveyorMotor.set(Constants.conveyorMotorSpeed);
      return trajectoryBlue();
    }
    
    //pathweaver
    // Create a voltage constraint to ensure we don't accelerate too fast

  }
}
