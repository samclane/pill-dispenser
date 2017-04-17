import pymysql
from time import localtime, strptime
import logging
import re
from collections import namedtuple

logger = logging.getLogger('schedule_logging')
logger.setLevel(logging.DEBUG)

# convert weekday letter to python datetime weekday code
WEEKDAY_CODES = {
    'U': 6,
    'M': 0,
    'T': 1,
    'W': 2,
    'R': 3,
    'F': 4,
    'S': 5
}


class Scheduler:
    def __init__(self):
        self.last_time_updated = None
        self.pilldb = {}
        # sql info
        self.sql_host = 'localhost'
        self.sql_port = 3306
        self.sql_user = 'root'
        self.sql_password = 'password'  # should probably store this differently but whatever
        self.sql_db = 'pillvendatabase'

    def check_schedule(self):
        current_time = localtime()
        if current_time.tm_yday > self.last_time_updated.tm_yday or self.last_time_updated is None:
            self.update_schedules()

    def update_schedules(self):
        try:
            con = pymysql.connect(host=self.sql_host, port=self.sql_port, user=self.sql_user, passwd=self.sql_password,
                                  db=self.sql_db)
            cursor = con.cursor()
            cursor.execute("SELECT * FROM med_sched")
            for row in cursor:
                pillname, schedule = row
                logger.debug("Name: {}   Schedule: {}".format(pillname, schedule))
                self.pilldb[pillname] = self._extract_datetime(schedule)
        except pymysql.Error as e:
            logger.error("Error %d: %s" % (e.args[0], e.args[1]))
        finally:
            try:
                con.close()
                cursor.close()
            except NameError:
                pass  # if they are referenced before assignment whatever
        self.last_time_updated = localtime()

    def get_schedule(self):
        pass

    def get_pills(self, curr_time: tuple) -> list:
        '''
        Returns all pills that are set to be dispensed at a certain time
        '''
        return [pill for pill in self.pilldb.keys() if curr_time in self.pilldb[pill]]

    @staticmethod
    def _extract_datetime(schedule: str):
        '''
        :param schedule: str
        :return: list
        '''
        schedule_regex = r"([UMTWRFS]+)([\d{2}:\d{2}]+)"
        time_regex = r"(\d{2}:\d{2})+"
        datetime = namedtuple('datetime', 'day hour')
        datetime_schedule = []
        for dt in re.findall(schedule_regex, schedule):
            # for each match group
            days = dt[0]
            hours = dt[1]
            for d in days:
                dcode = WEEKDAY_CODES[d]
                for time in re.findall(time_regex, hours):
                    datetime_schedule.append(datetime(dcode, time))
        return datetime_schedule


if __name__ == "__main__":
    pass
