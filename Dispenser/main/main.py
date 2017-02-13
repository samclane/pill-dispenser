#!/usr/bin/python35
import sys
import os

sys.path.append(os.path.dirname(os.path.abspath(__file__)) + "/..")  # hack to allow sibling dir imports
import RPi.GPIO as GPIO
import picamera
import logging
# import schedule  # scheduling module, considering using
import pill_recog.pill_recog


class Medication:
    def __init__(self, i_name: str, i_dose: str, i_img_filename: str):
        self.name = i_name  # name of medication
        self.dose = i_dose  # dosage of medication
        self.img_filename = i_img_filename  # link to ref image of pill


class PatientData:
    def __init__(self, i_name: str, i_room_number: int, i_medication_list: list):
        self.name = i_name  # patient name
        self.room_number = i_room_number  # room number of patient
        self.medication_list = i_medication_list  # list of medications patient is on


class PillDispenser:
    def __init__(self, patient_data: dict):
        self.patient_data = patient_data


if __name__ == "__main__":
    GPIO.setmode(GPIO.BOARD)
    print("Completed Successfully")
