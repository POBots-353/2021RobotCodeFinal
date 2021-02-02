/*HI ----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
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
    // An ExampleCommand will run in autonomous
    
    return new AutonomousDrive(driveSubsystem);
  }
}
