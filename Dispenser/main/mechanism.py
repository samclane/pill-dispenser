from Adafruit_MotorHAT import Adafruit_MotorHAT, Adafruit_DCMotor, Adafruit_StepperMotor
import time
import logging
import atexit
import threading


class Mechanism(Adafruit_MotorHAT):
    # recommended for auto-disabling motors on shutdown!
    def __init__(self, *args, **kwargs):
        # Call base constructor
        super(Adafruit_MotorHAT, self).__init__()
        # Release motors on exit
        atexit.register(self.turn_off_motors)
        # Assign motors
        self._motors = {}
        self.assign_motor('shaft_motor', 200, 1, 30)
        # Create threads for each stepper port
        self.st1 = threading.Thread()
        self.st2 = threading.Thread()

    @staticmethod
    def __stepper_worker(stepper, numsteps, direction, style):
        logging.info("Stepper starting...")
        stepper.step(numsteps, direction, style)
        logging.info("Stepper stopping...")

    def turn_off_motors(self):
        logging.info("Shutting down motors../")
        self.getMotor(1).run(Adafruit_MotorHAT.RELEASE)
        self.getMotor(2).run(Adafruit_MotorHAT.RELEASE)
        self.getMotor(3).run(Adafruit_MotorHAT.RELEASE)
        self.getMotor(4).run(Adafruit_MotorHAT.RELEASE)
        logging.info("All motors released")

    def assign_motor(self, name: str, steps_per_rev: int, port: int, speed: int = 30):
        self._motors[name] = self.getStepper(steps_per_rev, port)
        self._motors[name].setSpeed(speed)

    def execute_motor_action(self, port: int, stepper, numsteps, direction, style):
        if port == 1:
            self.st1 = threading.Thread(target=self.__stepper_worker, args=(stepper, numsteps, direction, style))
        elif port == 2:
            self.st2 = threading.Thread(target=self.__stepper_worker, args=(stepper, numsteps, direction, style))
