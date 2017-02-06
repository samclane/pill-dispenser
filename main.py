import RPi.GPIO as GPIO
import picamera
import logging
import pandas as pd


GPIO.setmode(GPIO.BOARD)


class PillDispenser:
    def __init__(self, patient_data: pd.Series):
        self.patient_data = patient_data

