from Adafruit_MotorHAT import Adafruit_MotorHAT, Adafruit_DCMotor, Adafruit_StepperMotor
import RPi.GPIO as GPIO
import time
import logging
import atexit
import threading
from collections import namedtuple

logger = logging.getLogger('dispenser.mechanism')
logger.setLevel(logging.DEBUG)



class Mechanism:
    '''
    Control for the pill dispensing mechanism
    '''
    NUM_CONTAINERS = 7
    STEPS_PER_REV = 200
    STEPS_PER_CONTAINER = STEPS_PER_REV / NUM_CONTAINERS
    FEED_ALIGN = STEPS_PER_CONTAINER / 2

    PillSlot = namedtuple('PillSlot', 'name step_location')
    LINACT_PIN = 5
    SOLENOID_PIN = 6

    def __init__(self, *args, **kwargs):
        # Call base constructor
        self.hat = Adafruit_MotorHAT(addr=0x60)
        # Release motors on exit
        atexit.register(self._turn_off_motors)
        # Assign motors
        self.stepper = self.hat.getStepper(200, 1)
        self.stepper.setSpeed(30)
        # Create threads for each stepper port
        self.st1 = threading.Thread()
        self.dc2 = threading.Thread()
        # dispenser data structures
        self.pill_dict = dict()
        self.current_step = 0
        atexit.register(self._turn_off_motors)
        # setup GPIO
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(self.LINACT_PIN, GPIO.OUT)
        GPIO.setup(self.SOLENOID_PIN, GPIO.OUT)


    @property
    def current_step(self):
        """
        0 steps - bottom dispense slot
        100 steps - top feed slot
        :return: int
        """
        return self.__current_step % self.STEPS_PER_REV

    @current_step.setter
    def current_step(self, val):
        if val > self.STEPS_PER_REV or val < 0:
            self.__current_step = val % self.STEPS_PER_REV
        else:
            self.__current_step = val

    @property
    def top_slot(self):
        return (self.current_step + 100) % self.STEPS_PER_REV

    @staticmethod
    def _stepper_worker(stepper, numsteps, direction, style):
        logging.info("Stepper starting...")
        stepper.step(numsteps, direction, style)
        logging.info("Stepper stopping...")

    def _turn_off_motors(self):
        logging.info("Shutting down motors../")
        self.hat.getMotor(1).run(Adafruit_MotorHAT.RELEASE)
        self.hat.getMotor(2).run(Adafruit_MotorHAT.RELEASE)
        self.hat.getMotor(3).run(Adafruit_MotorHAT.RELEASE)
        self.hat.getMotor(4).run(Adafruit_MotorHAT.RELEASE)
        logging.info("All motors released")

    def _execute_motor_action(self, numsteps, direction, style):
        self.st1 = threading.Thread(target=self._stepper_worker, args=(self.stepper, numsteps, direction, style))
        self.st1.start()
        # update position
        if direction == self.FORWARD:
            self.current_step += numsteps
        elif direction == self.BACKWARD:
            self.current_step += numsteps

    def get_current_pill(self):
        return self.pill_dict.get(self.current_step)

    def add_pill(self, pill_to_add):
        name = pill_to_add.name
        self.pill_dict[self.top_slot] = self.PillSlot(name, self.top_slot)
        self.pill_dict[self.top_slot] = self.PillSlot(name, self.top_slot)

    def dispense_pill(self, pill_disp):
        while self.get_current_pill() != pill_disp:
            self._execute_motor_action(self.STEPS_PER_CONTAINER, Adafruit_MotorHAT.FORWARD, Adafruit_MotorHAT.DOUBLE)
