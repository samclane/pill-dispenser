from Adafruit_MotorHAT import Adafruit_MotorHAT, Adafruit_DCMotor, Adafruit_StepperMotor
import time
import logging
import atexit
import threading
from collections import namedtuple

NUM_CONTAINERS = 7
DEG_PER_CONTAINER = 360 / NUM_CONTAINERS
STEPS_PER_REV = 200
STEPS_PER_DEG = STEPS_PER_REV / 360
STEPS_PER_CONTAINER = STEPS_PER_DEG * DEG_PER_CONTAINER
FEED_ALIGN = STEPS_PER_CONTAINER / 2

PillSlot = namedtuple('PillSlot', 'name step_location')


class Mechanism(Adafruit_MotorHAT):
    '''
    Control for the pill dispensing mechanism
    '''
    def __init__(self, *args, **kwargs):
        # Call base constructor
        super(Adafruit_MotorHAT, self).__init__()
        # Release motors on exit
        atexit.register(self.__turn_off_motors)
        # Assign motors
        self._motors = {}
        self.__assign_motor('shaft_motor', 200, 1, 30)
        # Create threads for each stepper port
        self.st1 = threading.Thread()
        self.dc2 = threading.Thread()
        # dispenser data structures
        self.pill_dict = dict()
        self.current_step = 0

    @property
    def current_step(self):
        """
        0 steps - bottom dispense slot
        100 steps - top feed slot
        :return: int
        """
        return self.__current_step % STEPS_PER_REV

    @current_step.setter
    def current_step(self, val):
        if val > STEPS_PER_REV or val < 0:
            self.__current_step = val % STEPS_PER_REV
        else:
            self.__current_step = val

    @property
    def top_slot(self):
        return (self.current_step + 100) % STEPS_PER_REV

    @staticmethod
    def __stepper_worker(stepper, numsteps, direction, style):
        logging.info("Stepper starting...")
        stepper.step(numsteps, direction, style)
        logging.info("Stepper stopping...")

    def __turn_off_motors(self):
        logging.info("Shutting down motors../")
        self.getMotor(1).run(Adafruit_MotorHAT.RELEASE)
        self.getMotor(2).run(Adafruit_MotorHAT.RELEASE)
        self.getMotor(3).run(Adafruit_MotorHAT.RELEASE)
        self.getMotor(4).run(Adafruit_MotorHAT.RELEASE)
        logging.info("All motors released")

    def __assign_motor(self, name: str, steps_per_rev: int, port: int, speed: int = 30):
        self._motors[name] = self.getStepper(steps_per_rev, port)
        self._motors[name].setSpeed(speed)

    def __execute_motor_action(self, port: int, stepper, numsteps, direction, style):
        if port == 1:
            self.st1 = threading.Thread(target=self.__stepper_worker, args=(stepper, numsteps, direction, style))
        elif port == 2:
            self.dc2 = threading.Thread(target=self.__stepper_worker, args=(stepper, numsteps, direction, style))
        # update position
        if direction == self.FORWARD:
            self.current_step += numsteps
        elif direction == self.BACKWARD:
            self.current_step += numsteps

    def get_current_pill(self):
        return self.pill_dict.get(self.current_step)

    def add_pill(self, pill_to_add):
        name = pill_to_add.name
        self.pill_dict[self.top_slot] = PillSlot(name, self.top_slot)

