/*HI ----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import frc.robot.commands.AlignRobotCommand;
import frc.robot.commands.AutonomousDrive;
import frc.robot.commands.HoodCommand;
//import frc.robot.commands.DropIntakeCommand;

//Natalia is trying to add a command for the new autonomous challanges here:
//import frc.robot.commands.GalacticSearch;

import frc.robot.commands.ManualDriveCommand;
import frc.robot.subsystems.BallTransitSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.GalacticSearchSubsystem;
//import frc.robot.subsystems.OperatorIntakeSystem;
//import frc.robot.subsystems.DropIntakeSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.Joystick;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  // The robot's subsystems are defined here
  
  public final static DriveSubsystem driveSubsystem = new DriveSubsystem();
  public final static BallTransitSubsystem transitSubsystem = new BallTransitSubsystem();
  public final static HoodSubsystem hood = new HoodSubsystem();
  public final static GalacticSearchSubsystem galactic = new GalacticSearchSubsystem();
  //public final static ColorWheelSubsytem colorWheelSubsystem =  new ColorWheelSubsytem();
 
  // The robot's joysticks are defined here
  public final static Joystick driverStick = new Joystick(Constants.driverStickPort);
  public final static Joystick operatorStick = new Joystick(Constants.operatorStickPort);
 
  

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    driveSubsystem.setDefaultCommand(new ManualDriveCommand(driveSubsystem));
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    JoystickButton magicButton = new JoystickButton(driverStick, Constants.AutoAlignButtonNumber);
    magicButton.whileHeld(new AlignRobotCommand(driveSubsystem, hood))
    .whenReleased(new ManualDriveCommand(driveSubsystem));

    JoystickButton hoodToggleButton = new JoystickButton(operatorStick, Constants.hoodToggleBtnNum);
    hoodToggleButton.whenPressed(new HoodCommand(hood));

    //JoystickButton autoColorWheelButton = new JoystickButton(operatorStick, Constants.AutoColorButtonNumber);
    //autoColorWheelButton.whenPressed(new AutoColorWheelCommand(colorWheelSubsystem));

  }



/**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    //pathweaver
    // Create a voltage constraint to ensure we don't accelerate too fast
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
    //Change the path to the actual path in the computer
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
}
