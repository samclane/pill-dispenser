import pymysql
from time import localtime, strftime
import logging

schedule_logger = logging.getLogger('schedule_logging')
schedule_logger.setLevel(logging.DEBUG)


class Scheduler:
    def __init__(self):
        self.last_time_updated = None
        # sql info
        self.sql_host = 'localhost'
        self.sql_port = 3306
        self.sql_user = 'root'
        self.sql_password = 'password'  # should probably store this differently but whatever
        self.sql_db = 'pillven_db'

    def check_schedule(self):
        try:
            con = pymysql.connect(host=self.sql_host, port=self.sql_port, user=self.sql_user, passwd=self.sql_password,
                                  db=self.sql_db)
            cursor = con.cursor()
            cursor.execute("SELECT * FROM med_sched")
            result = cursor.description
            print(result)
            print()
            for row in cursor:
                print(row)
        except pymysql.Error as e:
            schedule_logger.error("Error %d: %s" % (e.args[0], e.args[1]))
        finally:
            try:
                con.close()
                cursor.close()
            except NameError:
                pass  # if they are referenced before assignment whatever

    def update_schedules(self):
        # do some stuff
        self.last_time_updated = strftime("%Y-%m-%d %H:%M:%S", localtime())
        pass

    def get_schedule(self):
        pass

    def get_pill(curr_time: str):
        pass


if __name__ == "__main__":
    pass
